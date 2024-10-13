package com.tickethandler.service;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.tickethandler.dto.AttachmentDto;
import com.tickethandler.dto.ResponsePage;
import com.tickethandler.exception.ResourceNotFoundException;
import com.tickethandler.model.Attachment;
import com.tickethandler.model.AttachmentType;
import com.tickethandler.model.Ticket;
import com.tickethandler.repo.AttachmentRepository;
import com.tickethandler.repo.AttachmentTypeRepository;
import com.tickethandler.repo.TicketRepository;


@Service
public class AttachmentService {

	private final AttachmentRepository attachmentRepository;
	private final FileStorageService fileStorageService;
	private final AttachmentTypeRepository typeRepository;
	private final TicketRepository ticketRepository;

	
	@Autowired
	public AttachmentService(AttachmentRepository attachmentRepository,
			FileStorageService fileStorageService,AttachmentTypeRepository typeRepository,
			TicketRepository ticketRepository) {
		
		this.attachmentRepository=attachmentRepository;
		this.fileStorageService=fileStorageService;
		this.typeRepository=typeRepository;
		this.ticketRepository=ticketRepository;
	
	}
	
	
	
	
	
	
	public ResponsePage<AttachmentDto> getAll(int pageNo, int pageSize) {
		
		 Pageable pageable = PageRequest.of(pageNo, pageSize);
		 Page<Attachment> attachmentsPage = attachmentRepository.findAll(pageable);
		 List<Attachment> attachments = attachmentsPage.getContent();
		 
		 List<AttachmentDto> attachmentsDto = attachments.parallelStream()
				    .map(this::mapAttachmentToDto)
				    .collect(Collectors.toList());
	     
		 ResponsePage<AttachmentDto> content = mapAttachmentToPageObject(attachmentsPage, attachmentsDto);
	      return content;
	}
	
	@Transactional
	 public Attachment saveFile(MultipartFile file, Long ticketId)
			 throws IOException {
		 
		
	        String fileName = fileStorageService.storeFile(file);
	        Attachment attachment = new Attachment();
	        AttachmentType type = typeRepository.findByCode(fileStorageService.getFileExtension(fileName));
	
	        //Request request = requestRepository.findById(requestId).orElseThrow(() ->
	        	//	new RuntimeException("No request by this Id"));
	        Ticket ticket =  ticketRepository.findById(ticketId).orElseThrow(() ->
        			new ResourceNotFoundException("No ticket by this Id"));
	        
	        attachment.setFileName(fileName);
	        attachment.setAttachmentType(type);
	        attachment.setFilePath(fileStorageService.getFilePath(fileName).toString());
	        attachment.setUploadDate(LocalDateTime.now());
	        attachment.setTicket(ticket);
	        

	        return attachmentRepository.save(attachment);
	         
	}
	
	
	 public ResponsePage<AttachmentDto> findByTicketId(Long ticketId,int pageNo,int pageSize) {
		 
		 Pageable pageable = PageRequest.of(pageNo, pageSize);
		 Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() ->
			new ResourceNotFoundException("No ticket by this Id"));
		 Page<Attachment> attachmentsPage = attachmentRepository.findByTicket(ticket, pageable);
		 
		 List<Attachment> attachments = attachmentsPage.getContent();
		 
		 List<AttachmentDto> attachmentsDto = attachments.stream()
				 	.map(this::mapAttachmentToDto)
				    .collect(Collectors.toList());
	     
		ResponsePage<AttachmentDto> content = mapAttachmentToPageObject(attachmentsPage, attachmentsDto);
	      return content;
	 }
	 
	 
	 
	 public Resource getFile(Long attachmentId) throws IOException {
	       
	        Attachment attachment = attachmentRepository.findById(attachmentId)
	            .orElseThrow(() -> new RuntimeException("Attachment not found with id: " + attachmentId));

	       
	        Path filePath = Paths.get(attachment.getFilePath());
	        Resource resource = new UrlResource(filePath.toUri());

	        
	        if (!resource.exists()) {
	            throw new RuntimeException("File not found: " + attachment.getFileName());
	        }

	        return resource;
	    }
	 
	
	
	private AttachmentDto mapAttachmentToDto(Attachment attachment) {
	   AttachmentDto dto = new AttachmentDto();
  	   dto.setAttachmentId(attachment.getId());
  	   dto.setFileName(attachment.getFileName());
  	   dto.setAttachmentType(attachment.getAttachmentType().getName());
  	   dto.setTicketId(attachment.getTicket().getTicketNo());
  	  // dto.setRequestId(attachment.getRequest().getId());
  	
  	   
  	   return dto;
	}
	
	
	
	private ResponsePage<AttachmentDto> mapAttachmentToPageObject(Page<Attachment> attachmentsPage,
			 List<AttachmentDto> attachmentsDto){
		
		ResponsePage<AttachmentDto> content = new ResponsePage<>();
		
		content.setContent(attachmentsDto);
		content.setPage(attachmentsPage.getNumber());
      	content.setSize(attachmentsPage.getSize());
      	content.setTotalElements(attachmentsPage.getTotalElements());
      	content.setTotalpages(attachmentsPage.getTotalPages());
      	content.setLast(attachmentsPage.isLast());
	
		return content;
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
