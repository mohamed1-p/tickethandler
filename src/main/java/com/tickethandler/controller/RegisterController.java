package com.tickethandler.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tickethandler.dto.AuthResponse;
import com.tickethandler.dto.EngineerRegisterDto;

import com.tickethandler.dto.RegisterDto;

import com.tickethandler.service.UserService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;



@RestController
@RequestMapping("api/register")
public class RegisterController {

	private UserService userService;

	@Autowired
	public RegisterController( UserService userService) {

		this.userService = userService;



	}

	@PostMapping("")
	public ResponseEntity<AuthResponse> registerNewRequester(@RequestBody RegisterDto registerDto) {

		return ResponseEntity.ok(userService.createRequester(registerDto));
	}

	@PostMapping("/engineer")
	public ResponseEntity<AuthResponse> registerNewEngineer(@RequestBody EngineerRegisterDto registerDto) {

		return ResponseEntity.ok(userService.createEngineer(registerDto));
	}
	
	@PutMapping("make-admin")
	public ResponseEntity<?> makeAdmin(@RequestParam String email) {
	
		userService.makeAdmin(email);
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}

}
