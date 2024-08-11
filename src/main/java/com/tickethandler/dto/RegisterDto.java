package com.tickethandler.dto;

import lombok.Data;

@Data
public class RegisterDto {

	private String requesterName;
	private int companyId;
	private String mobileNo;
	private String email;
	private String password;
}
