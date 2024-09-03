package com.tickethandler.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {

	private String accessToken;
	
	
}
