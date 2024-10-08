package com.tickethandler.controller;




import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tickethandler.dto.AuthResponse;
import com.tickethandler.dto.ChangePasswordRequest;
import com.tickethandler.dto.LoginDto;
import com.tickethandler.service.UserService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("api/")
public class LoginController {

	
	private UserService userService;
	
	@Autowired
	public LoginController(UserService userService) {
	this.userService=userService;
	
		
	}
	
	
	
	
	
	@PostMapping("login")
    public ResponseEntity<AuthResponse> authenticateUser(@RequestBody LoginDto LoginDto) {
      
		return ResponseEntity.ok(userService.authenticate(LoginDto));
    }
	
	@PutMapping("change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request) {
        
		return userService.changePassword(request);
        
    }

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
