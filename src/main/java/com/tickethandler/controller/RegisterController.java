package com.tickethandler.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tickethandler.dto.EngineerRegisterDto;
import com.tickethandler.dto.LoginDto;
import com.tickethandler.dto.RegisterDto;
import com.tickethandler.model.SupportEngineer;
import com.tickethandler.model.UserRole;

import com.tickethandler.repo.UserRoleRepository;
import com.tickethandler.service.UserService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("api/register")
public class RegisterController {
	
	
	private AuthenticationManager authenticationManager;
	private UserService userService;

	
	
	@Autowired
	public RegisterController(AuthenticationManager authenticationManager,
			UserService userService) {
	
		this.userService=userService;
		
		this.authenticationManager=authenticationManager;
		
	}
	
	
	@PostMapping("")
	public ResponseEntity<String> registerNewRequester(@RequestBody RegisterDto registerDto) {

		if(userService.createRequester(registerDto) == false) {
			
			return new ResponseEntity<>("something went wrong!",HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>("register success!",HttpStatus.OK);
	}

	@PostMapping("/engineer")
	public ResponseEntity<String> registerNewEngineer(@RequestBody EngineerRegisterDto registerDto) {

		if(userService.createEngineer(registerDto) == false) {
			return new ResponseEntity<>("something went wrong!",HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>("register success!",HttpStatus.OK);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
