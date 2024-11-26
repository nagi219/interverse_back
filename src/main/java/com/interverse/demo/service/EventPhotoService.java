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
import com.interverse.demo.model.Event;
import com.interverse.demo.model.EventPhoto;
import com.interverse.demo.model.EventPhotoRepository;
import com.interverse.demo.model.EventRepository;
import com.interverse.demo.model.User;
import com.interverse.demo.model.UserRepository;

@Service
public class EventPhotoService {
	
	
	@Value("${upload.eventphoto.dir}")
	private String uploadDir;
	
	@Autowired
	private EventPhotoRepository epRepo;
	
	@Autowired
	private EventRepository eRepo;
	
	@Autowired
	private UserRepository uRepo;
	
	public EventPhoto saveEventPhoto(EventPhoto eventPhoto) {
		Event event = eventPhoto.getEvent();
		User uploaderId = eventPhoto.getUploaderId();
		if(eRepo.existsById(event.getId()) && uRepo.existsById(uploaderId.getId())) {
			return epRepo.save(eventPhoto);
		}
		throw new IllegalStateException("Event or User is not exists.");
	}

	// 尋找event中所有照片
	public List<EventPhoto> findAllByEventId(Integer eventId) {
		return epRepo.findByEventId(eventId);
	}


	// user從event中刪除自己上傳的照片
	public void deletePhotoIfOwner(Integer id, Integer uploaderId) throws IOException {
		EventPhoto photo = epRepo.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Photo not found with ID: " + id));

		// 檢查上傳者ID是否與要求刪除的使用者ID相同
		if (!photo.getUploaderId().getId().equals(uploaderId)) {
			throw new SecurityException("You do not have permission to delete this photo.");
		}
		Path filePath = Paths.get(photo.getPhoto());
		Files.deleteIfExists(filePath);
		epRepo.deleteById(id);
	}
	@Transactional
	public EventPhoto createEventPhoto(MultipartFile file, Integer eventId, Integer uploaderId) throws IOException {
		// 獲取event
		Event event = eRepo.findById(eventId).orElseThrow(() -> new RuntimeException("event not found with id: " + eventId));

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

		// 存入 EventPhoto 實體
		EventPhoto eventPhoto = new EventPhoto();
		eventPhoto.setPhoto(filePath.toString());
		eventPhoto.setEvent(event);
		eventPhoto.setUploaderId(uploader); // 设置上传者 ID

		return epRepo.save(eventPhoto);
	}

	public EventPhoto getEventPhoto(Integer eventId, Integer photoId) {
		Event event = eRepo.findById(eventId).orElseThrow(() -> new RuntimeException("Event not found with id: " + eventId));

		return epRepo.findByEventAndId(event, photoId).orElseThrow(
				() -> new RuntimeException("EventPhoto not found with eventId: " + eventId + " and photoId: " + photoId));
	}
}
