package com.tickethandler.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "attachments")
public class Attachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "attachment_type_id", nullable = false)
    private AttachmentType attachmentType;

    @Column(name = "file_path", nullable = false)
    private String filePath;
    
    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "upload_date", nullable = false)
    private LocalDateTime uploadDate;

    @ManyToOne
    @JoinColumn(name = "ticket_id", nullable = false) // Connects attachment to a ticket
    private Ticket ticket;
    
}