package com.interverse.demo.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.interverse.demo.dto.ClubDTO;
import com.interverse.demo.model.Club;
import com.interverse.demo.service.ClubService;

@RestController
@RequestMapping("/clubs")
public class ClubController {

	@Autowired
	private ClubService cService;

	@Value("${upload.clubphoto0.dir}")
	private String uploadDir;

	// 轉換DTO
	private ClubDTO convertToDTO(Club club) {
		ClubDTO dto = new ClubDTO();

		dto.setId(club.getId());
		dto.setClubName(club.getClubName());
		dto.setDescription(club.getDescription());
		dto.setIsPublic(club.getIsPublic());
		dto.setIsAllowed(club.getIsAllowed());
		dto.setAdded(club.getAdded());
		dto.setClubCreator(club.getClubCreator().getId());
		dto.setPhoto(club.getPhoto());
		dto.setUserName(club.getClubCreator().getNickname());
		// 如果需要，将 event 的名称列表填充到 DTO 中
//		dto.setEventNames(club.getEvent().stream().map(event -> event.getEventName()) // 假设 Event 类有 getEventName 方法
//				.collect(Collectors.toList()));
//		
		return dto;
	}

	@PostMapping("/new")
	public ResponseEntity<?> createClub(@RequestPart("club") Club club,
			@RequestPart(value = "photo", required = false) MultipartFile photo) {
		try {
			if (photo != null && !photo.isEmpty()) {
				String filename = UUID.randomUUID().toString() + "_" + photo.getOriginalFilename();
				Path filePath = Paths.get(uploadDir, filename);
				Files.copy(photo.getInputStream(), filePath);
				club.setPhoto(filename);
			}

			Club savedClub = cService.saveClub(club);
			return ResponseEntity.ok(convertToDTO(savedClub));
		} catch (IOException e) {
			 e.printStackTrace(); // 調試印出完整的
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error creating club: " + e.getMessage());
		}
	}

	@GetMapping("/all")
	public List<ClubDTO> getAllClub() {
		List<Club> allClub = cService.findAllClub();
		return allClub.stream().map(this::convertToDTO).collect(Collectors.toList());
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getClub(@PathVariable Integer id) {
		Club result = cService.findClubById(id);

		if (result != null) {
			ClubDTO clubDTO = convertToDTO(result);
			return ResponseEntity.ok(clubDTO);

		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("無此ID");
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteClub(@PathVariable Integer id) {

		if (cService.findClubById(id) == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("無此 ID");
		}
		// 刪除相關的照片文件
		if (cService.findClubById(id).getPhoto() != null) {
			try {
				Files.deleteIfExists(Paths.get(cService.findClubById(id).getPhoto()));
			} catch (IOException e) {
				// 記錄錯誤，但繼續刪除俱樂部
				System.err.println("Error deleting photo file: " + e.getMessage());
			}
		}
		cService.deleteClubById(id);
		return ResponseEntity.status(HttpStatus.OK).body("Delete Successful");
	}

	@PutMapping("/{id}/edit")
	public ResponseEntity<?> updateClub(@PathVariable Integer id, @RequestPart("club") Club club,
	                                    @RequestPart(value = "photo", required = false) MultipartFile photo) {
	    Club existingClub = cService.findClubById(id);
	    if (existingClub == null) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("無此 ID");
	    }

	    try {
	        if (photo != null && !photo.isEmpty()) {
	            // 處理新上傳的照片
	            String filename = UUID.randomUUID().toString() + "_" + photo.getOriginalFilename();
	            Path filePath = Paths.get(uploadDir, filename);
	            Files.copy(photo.getInputStream(), filePath);
	            club.setPhoto(filename); // 只存儲文件名
	        } else {
	            // 如果沒有新照片，保留原有的照片路徑
	            club.setPhoto(existingClub.getPhoto());
	        }

	        club.setId(id);
	        club.setAdded(existingClub.getAdded());

	        Club updatedClub = cService.saveClub(club);
	        return ResponseEntity.ok(convertToDTO(updatedClub));
	    } catch (IOException e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Error updating club: " + e.getMessage());
	    }
	}
	
	@GetMapping("/my-clubs")
    public ResponseEntity<List<ClubDTO>> getMyClubs(@RequestParam Integer userId) {
        List<Club> clubs = cService.findClubsByCreatorId(userId);
        List<ClubDTO> clubDTOs = clubs.stream()
                                      .map(this::convertToDTO)
                                      .collect(Collectors.toList());
        return ResponseEntity.ok(clubDTOs);
    }
	
	@GetMapping("/photo/{filename}")
    public ResponseEntity<Resource> getPhoto(@PathVariable String filename) {
        try {
            Path filePath = Paths.get(uploadDir).resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG) // 或根據實際情況設置
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}