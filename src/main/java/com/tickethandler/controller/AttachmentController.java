package com.tickethandler.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tickethandler.dto.AttachmentDto;
import com.tickethandler.dto.ResponsePage;
import com.tickethandler.service.AttachmentService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("api/attachments")
public class AttachmentController {
	 	
	private final AttachmentService attachmentService;
	
	
	 @Autowired
	    public AttachmentController(AttachmentService attachmentService) {
	        this.attachmentService = attachmentService;
	      
	        
	    }
	 
	 
	 @GetMapping
	 public ResponseEntity<ResponsePage<AttachmentDto>> getAttachmentByTicket(
			 @RequestParam(value = "pageNo",defaultValue = "0")int pageNo,
			 @RequestParam(value = "pageSize",defaultValue = "10")int pageSize){
	 	ResponsePage<AttachmentDto> attachmentDto = attachmentService.getAll(pageNo,pageSize);
		 
		 return ResponseEntity.ok(attachmentDto);
	 }
	 
	 @PostMapping("/upload")
	    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file,
	                                             @RequestParam("ticketId") Long ticketId) {
	        try {
	        	attachmentService.saveFile(file, ticketId);
	            return ResponseEntity.ok("File uploaded successfully: ");
	        } catch (IOException e) {
	            return ResponseEntity.badRequest().body("Could not upload file: " + e.getMessage());
	        }
	    }
	 
	 
	 
	 @GetMapping("by-ticket")
	 public ResponseEntity<ResponsePage<AttachmentDto>> getAttachmentByRequest(@RequestParam("ticketId") Long ticketId,
			 @RequestParam(value = "pageNo",defaultValue = "0")int pageNo,
			 @RequestParam(value = "pageSize",defaultValue = "10")int pageSize){
	 	ResponsePage<AttachmentDto> attachmentDto = attachmentService.findByTicketId(ticketId,pageNo,pageSize);
		 
		 return ResponseEntity.ok(attachmentDto);
	 }
	 
	 
	 @GetMapping("/download/{attachmentId}")
	    public ResponseEntity<Resource> downloadFile(@PathVariable Long attachmentId) throws IOException {
	        
	        Resource resource = attachmentService.getFile(attachmentId);

	        
	        Path filePath = Paths.get(resource.getURI());
	        String contentType = Files.probeContentType(filePath);
	        if (contentType == null) {
	            contentType = "application/octet-stream";
	        }

	       
	        return ResponseEntity.ok()
	            .contentType(MediaType.parseMediaType(contentType))
	            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
	            .body(resource);
	    }
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 

}
