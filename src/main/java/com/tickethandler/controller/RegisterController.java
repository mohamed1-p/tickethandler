package com.tickethandler.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tickethandler.dto.AuthResponse;
import com.tickethandler.dto.EngineerRegisterDto;
import com.tickethandler.dto.EngineerShowDto;
import com.tickethandler.dto.RegisterDto;
import com.tickethandler.dto.ResponsePage;
import com.tickethandler.service.UserService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;



@RestController
@RequestMapping("api/admin")
public class RegisterController {

	private UserService userService;

	@Autowired
	public RegisterController( UserService userService) {

		this.userService = userService;



	}

	
	@GetMapping("engineers")
	public ResponseEntity<?> listAllEngineers(){
		
		return null;
	}
	
	@PostMapping("/create-requester")
	public ResponseEntity<?> registerNewRequester(@RequestBody RegisterDto registerDto) {
		if(userService.createRequester(registerDto)==null) {
			return new  ResponseEntity<>("User Already exists!",HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>("Register Success!",HttpStatus.OK);
	}

	@PostMapping("/create-engineer")
	public ResponseEntity<?> registerNewEngineer(@RequestBody EngineerRegisterDto registerDto) {

		if(userService.createEngineer(registerDto)==null) {
			return new ResponseEntity<>("User Already exists!",HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>("Register Success!",HttpStatus.OK);
	}
	
	@GetMapping("/get-engineers")
	public ResponseEntity<ResponsePage<EngineerShowDto>> getAllEngineers(@RequestParam String name, 
			 @RequestParam(value = "pageNo",defaultValue = "0")int pageNo,
			 @RequestParam(value = "pageSize",defaultValue = "10")int pageSize){
		
		ResponsePage<EngineerShowDto> engineers = userService.findEngineersByName(name, pageNo, pageSize);
		return new ResponseEntity<>(engineers,HttpStatus.OK);
				
		
	}
	
	@PutMapping("make-admin")
	public ResponseEntity<?> makeAdmin(@RequestParam String email) {
	
		userService.makeAdmin(email);
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}
	
	

}
