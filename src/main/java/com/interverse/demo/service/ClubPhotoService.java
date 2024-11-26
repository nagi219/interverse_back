package com.interverse.demo.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.interverse.demo.model.Club;
import com.interverse.demo.model.ClubPhoto;
import com.interverse.demo.model.ClubPhotoRepository;
import com.interverse.demo.model.ClubRepository;
import com.interverse.demo.model.Product;
import com.interverse.demo.model.ProductPhotos;
import com.interverse.demo.model.User;
import com.interverse.demo.model.UserRepository;

@Service
public class ClubPhotoService {

	@Value("${upload.clubphoto.dir}")
	private String uploadDir;

	@Autowired
	private ClubPhotoRepository cpRepo;

	@Autowired
	private ClubRepository cRepo;

	@Autowired
	private UserRepository uRepo;

	public ClubPhoto saveClubPhoto(ClubPhoto clubPhoto) {
		Club club = clubPhoto.getClub();
		User uploaderId = clubPhoto.getUploaderId();
		if (cRepo.existsById(club.getId()) && uRepo.existsById(uploaderId.getId())) {
			return cpRepo.save(clubPhoto);
		}
		throw new IllegalStateException("Club or User is not exists.");
	}

	// 尋找club中所有照片
	public List<ClubPhoto> getAllPhotosByClubId(Integer clubId) {
		return cpRepo.findByClubId(clubId);
	}

	// user從club中刪除自己上傳的照片
	public void deletePhotoIfOwner(Integer id, Integer uploaderId) throws IOException {
		ClubPhoto photo = cpRepo.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Photo not found with ID: " + id));

		// 檢查上傳者ID是否與要求刪除的使用者ID相同
		if (!photo.getUploaderId().getId().equals(uploaderId)) {
			throw new SecurityException("You do not have permission to delete this photo.");
		}
		Path filePath = Paths.get(photo.getPhoto());
		Files.deleteIfExists(filePath);

		cpRepo.deleteById(id);
	}

	@Transactional
	public ClubPhoto createClubPhoto(MultipartFile file, Integer clubId, Integer uploaderId) throws IOException {
		// 獲得 Club 對象
		Club club = cRepo.findById(clubId).orElseThrow(() -> new RuntimeException("Club not found with id: " + clubId));

		// 獲得上傳者user
		User uploader = uRepo.findById(uploaderId)
				.orElseThrow(() -> new RuntimeException("Uploader not found with id: " + uploaderId));

		// 獲得上傳文件的名字並去除路徑中的不安全字符
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());

		// 生成唯一文件名，防止名字衝突
		String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;

		// 將路徑轉換為Path類的對象
		Path uploadPath = Paths.get(uploadDir);

		// 目錄不存在則創建
		if (!Files.exists(uploadPath)) {
			Files.createDirectories(uploadPath);
		}

		// 生成完整路徑
		Path filePath = uploadPath.resolve(uniqueFileName);

		// 將文件寫入指定位置
		file.transferTo(filePath.toFile());

		// 存入 ClubPhoto 實體
		ClubPhoto clubPhoto = new ClubPhoto();
		clubPhoto.setPhoto(filePath.toString());
		clubPhoto.setClub(club);
		clubPhoto.setUploaderId(uploader); // 設置上傳者ID

		return cpRepo.save(clubPhoto);
	}

	public ClubPhoto getClubPhoto(Integer clubId, Integer photoId) {
		Club club = cRepo.findById(clubId).orElseThrow(() -> new RuntimeException("Club not found with id: " + clubId));

		return cpRepo.findByClubAndId(club, photoId).orElseThrow(
				() -> new RuntimeException("ClubPhoto not found with clubId: " + clubId + " and photoId: " + photoId));
	}
}
