package com.tickethandler.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tickethandler.model.Attachment;
import com.tickethandler.model.Ticket;




@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {

	Page<Attachment> findByTicket(Ticket ticket,Pageable pageable);
}
