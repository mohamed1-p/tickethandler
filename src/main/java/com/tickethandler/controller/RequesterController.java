package com.tickethandler.controller;

import java.util.Collections;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tickethandler.dto.LoginDto;
import com.tickethandler.dto.RegisterDto;
import com.tickethandler.exception.ResourceNotFoundException;
import com.tickethandler.model.ReqeusterRole;
import com.tickethandler.model.Requester;
import com.tickethandler.repo.CompanyRepository;
import com.tickethandler.repo.RequesterRepository;
import com.tickethandler.repo.RequesterRoleRepository;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("api/requester")
public class RequesterController {

	
	private AuthenticationManager authenticationManager;
	private RequesterRepository requesterRepository;
	private RequesterRoleRepository roleRepository;
	private PasswordEncoder passwordEncoder;
	private CompanyRepository companyRepository;
	
	@Autowired
	public RequesterController(AuthenticationManager authenticationManager, RequesterRepository requesterRepository,
			RequesterRoleRepository roleRepository, PasswordEncoder passwordEncoder
			,CompanyRepository companyRepository) {
		super();
		this.companyRepository=companyRepository;
		this.authenticationManager = authenticationManager;
		this.requesterRepository = requesterRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	
	@PostMapping("/register")
	public ResponseEntity<String> registerNewRequester(@RequestBody RegisterDto registerDto) {
		
		if(requesterRepository.existsByrequesterEmail(registerDto.getEmail()))
		{
			return new ResponseEntity<>("Email already Exist",HttpStatus.BAD_REQUEST);
		}
		
		
		Requester requester = new Requester();
		requester.setRequesterEmail(registerDto.getEmail());
		requester.setCompany(companyRepository.findById(registerDto.getCompanyId()).
				orElseThrow(()->new ResourceNotFoundException("company Doesn't exist")));
		requester.setRequesterName(registerDto.getRequesterName());
		requester.setRequesterMobileNo(registerDto.getMobileNo());
		requester.setRequesterPassword(passwordEncoder.encode(registerDto.getPassword()));
		
		ReqeusterRole roles = roleRepository.findByRole("USER").get();
		requester.setRoles(Collections.singletonList(roles));
		requesterRepository.save(requester);
		return new ResponseEntity<>("register success!",HttpStatus.OK);
	}
	
	
	
	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody LoginDto loginDto) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						loginDto.getEmail(), loginDto.getPassword()));
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		return new ResponseEntity<>("welcome",HttpStatus.OK);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
