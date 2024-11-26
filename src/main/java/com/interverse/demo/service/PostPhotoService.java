package com.interverse.demo.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.interverse.demo.model.PostPhoto;
import com.interverse.demo.model.PostPhotosRepository;
import com.interverse.demo.model.UserPost;
import com.interverse.demo.model.UserPostRepository;

@Service
public class PostPhotoService {
	
    @Value("${upload.userPost.dir}")
    private String uploadDir;
	
	@Autowired
	private PostPhotosRepository postPhotoRepo;
	
	@Autowired
	private UserPostRepository postRepo;
	
	public PostPhoto createPhoto(MultipartFile file, Integer postId) throws IOException {
		
		//如果找不到對應的 UserPost，則丟出一個 RuntimeException。
		UserPost post=postRepo.findById(postId).orElseThrow(() -> new RuntimeException("UserPost not found with id: " + postId));
		
		//file.getOriginalFilename() 取得上傳檔案的原始檔名。
		//StringUtils.cleanPath() 清理檔名（通常是移除任何不必要的路徑元素或安全問題）。
		//使用 UUID.randomUUID().toString() 生成一個唯一的檔名，並與原始檔名結合，避免檔名重複。
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		String uniqueFileName=UUID.randomUUID().toString() + "_" + fileName;
		
		//uploadDir 是存放上傳檔案的目錄路徑。如果目錄不存在，則創建。
		Path uploadPath = Paths.get(uploadDir);
		if(!Files.exists(uploadPath)) {
			Files.createDirectories(uploadPath);
		}
		
		//建立檔案的完整路徑 filePath。
		//使用 Files.copy() 方法將檔案內容從 InputStream 複製到指定的檔案路徑。如果檔案已經存在，則覆蓋它。
		Path filePath = uploadPath.resolve(uniqueFileName);
		Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
		
		PostPhoto postPhoto = new PostPhoto();
		postPhoto.setName(fileName);
		//設定照片的名稱和URL。URL 指向上傳目錄中的檔案（例如 /uploads + 檔名）。
        postPhoto.setUrl(uploadDir +"/"+ uniqueFileName);  // 使用相对URL
		//將照片與 UserPost 關聯起來。
		postPhoto.setUserPost(post);
		
		return postPhotoRepo.save(postPhoto);
	}
	
	public List<PostPhoto> findPhotoListByPostId(Integer postId){
		List<PostPhoto> list = postPhotoRepo.findPhotoListByPostId(postId);
		return list;
	}
	
	
}
