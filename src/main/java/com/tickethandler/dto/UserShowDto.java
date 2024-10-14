package com.tickethandler.dto;

import lombok.Data;

@Data
public class UserShowDto {

	private String email;
	private String phone_number;
	private String name;
	private String type;
	private String companyName;
}
