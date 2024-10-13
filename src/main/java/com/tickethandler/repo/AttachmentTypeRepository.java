package com.tickethandler.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tickethandler.model.AttachmentType;



@Repository
public interface AttachmentTypeRepository extends JpaRepository<AttachmentType, Long> {
    AttachmentType findByCode(String code);
}