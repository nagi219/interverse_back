package com.interverse.demo.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.interverse.demo.dto.ClubPhotoDTO;
import com.interverse.demo.model.ClubPhoto;
import com.interverse.demo.service.ClubPhotoService;

@RestController
@RequestMapping("/clubPhoto")
public class ClubPhotoController {

	@Autowired
	private ClubPhotoService cpService;

	// 建立照片
	@PostMapping("/new")
	public ResponseEntity<?> uploadClubPhoto(@RequestParam("file") MultipartFile file,
			@RequestParam("clubId") Integer clubId, @RequestParam("uploaderId") Integer uploaderId) {
		try {
			ClubPhoto savedPhoto = cpService.createClubPhoto(file, clubId, uploaderId);
			return ResponseEntity.status(HttpStatus.CREATED).body(savedPhoto);
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File upload failed.");
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	// 尋找club中所有照片
	@GetMapping("/club/{clubId}")
	public ResponseEntity<List<ClubPhotoDTO>> getAllClubPhoto(@PathVariable Integer clubId) {
		try {
			List<ClubPhoto> allPhotosByClubId = cpService.getAllPhotosByClubId(clubId);
			List<ClubPhotoDTO> photosDTO = allPhotosByClubId.stream().map(this::convertToDTO)
					.collect(Collectors.toList());
			return ResponseEntity.ok(photosDTO);
		} catch (Exception e) {
			System.out.println("錯誤 " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

		}
	}

	// user可以在club中刪除自己上傳的照片
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteClubPhoto(@PathVariable Integer id, @RequestParam Integer uploaderId) {
		if (uploaderId == null) {
			return ResponseEntity.badRequest().body("Uploader ID is required.");
		}
		try {
			cpService.deletePhotoIfOwner(id, uploaderId);
			return ResponseEntity.ok("Photo deleted successfully.");
		} catch (SecurityException | IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting the file.");
		}
	}

	// 轉換DTO
	private ClubPhotoDTO convertToDTO(ClubPhoto clubPhoto) {
		ClubPhotoDTO dto = new ClubPhotoDTO();

		dto.setId(clubPhoto.getId());
//		dto.setPhoto(clubPhoto.getPhoto());
		dto.setClubId(clubPhoto.getClub().getId());
		dto.setUploaderId(clubPhoto.getUploaderId().getId());
		dto.setUserName(clubPhoto.getUploaderId().getNickname());

		return dto;
	}

	@GetMapping("/{clubId}/{photoId}")
	public ResponseEntity<Resource> getSpecificClubPhoto(@PathVariable Integer clubId, @PathVariable Integer photoId)
			throws MalformedURLException {
		// 使用 clubId 和 photoId 來獲取特定的照片
		ClubPhoto photo = cpService.getClubPhoto(clubId, photoId);

		if (photo == null) {
			return ResponseEntity.notFound().build();
		}

		String photoPath = photo.getPhoto();

		Path path = Paths.get(photoPath);
		Resource resource = new UrlResource(path.toUri());

		if (resource.exists() || resource.isReadable()) {
			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + photo.getPhoto() + "\"")
					.contentType(MediaType.IMAGE_JPEG) // 或者根據實際情況設置
					.body(resource);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
}
