package com.tickethandler.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {

	
	//get the path from the application properties
		@Value("${file.upload-dir}")
		private String uploadDir;

		public String storeFile(MultipartFile file) throws IOException {
		
			
			// Create the upload directory if it doesn't exist
	        Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
	       
	        Files.createDirectories(uploadPath);
	       
	        // Generate a unique file name
	        String fileName = UUID.randomUUID().toString() + getFileExtension(file.getOriginalFilename());
	       
	        // Resolve the file path
	        Path filePath = uploadPath.resolve(fileName);
	        
	        // Copy the file to the target location
	        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

	        return fileName;
	    }
		
		
		
		public String getFileExtension(String fileName) {
	        if (fileName == null) {
	            return "";
	        }
	        int dotIndex = fileName.lastIndexOf('.');
	        return (dotIndex == -1) ? "" : fileName.substring(dotIndex);
	    }
		

		public Path getFilePath(String fileName) {
	        return Paths.get(uploadDir).toAbsolutePath().normalize().resolve(fileName);
	    }

		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
}
