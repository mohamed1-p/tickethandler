package com.tickethandler.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tickethandler.dto.AuthResponse;
import com.tickethandler.dto.EngineerRegisterDto;
import com.tickethandler.dto.LoginDto;
import com.tickethandler.dto.RegisterDto;
import com.tickethandler.exception.ResourceNotFoundException;
import com.tickethandler.model.Requester;
import com.tickethandler.model.SupportEngineer;
import com.tickethandler.model.UserEntity;
import com.tickethandler.model.UserRole;
import com.tickethandler.repo.CompanyRepository;
import com.tickethandler.repo.UserRepository;
import com.tickethandler.repo.UserRoleRepository;
import com.tickethandler.security.JwtTokenProvider;



@Service
public class UserService {
	
	private PasswordEncoder passwordEncoder;
	private UserRoleRepository roleRepository;
	private UserRepository userRepository;
	private CompanyRepository companyRepository;
	private AuthenticationManager authenticationManager;
	private JwtTokenProvider jwtTokenProvider;

	
	
	@Autowired
	public UserService(PasswordEncoder passwordEncoder, UserRoleRepository roleRepository,
			UserRepository userRepository, CompanyRepository companyRepository,
			AuthenticationManager authenticationManager,JwtTokenProvider jwtTokenProvider) {
		this.passwordEncoder = passwordEncoder;
		this.roleRepository = roleRepository;
		this.userRepository = userRepository;
		this.companyRepository = companyRepository;
		this.authenticationManager=authenticationManager;
		this.jwtTokenProvider=jwtTokenProvider;
		
	}





	public boolean createEngineer(EngineerRegisterDto engineer) {
		
		if(userRepository.existsByemail(engineer.getEmail())) {
			return false;
		}
		UserEntity user = mapDtoToUser(engineer);
		
		UserRole roles =  roleRepository.findByRole("ENGINEER").get();
		user.setRoles(Collections.singletonList(roles));
		userRepository.save(user);
		return true;
	}
	
	
	
	
	
	public boolean createRequester(RegisterDto registerDto) {
		if(userRepository.existsByemail(registerDto.getEmail())) {
			return false;
		}
		UserEntity user = mapRequesterToUser(registerDto);
		UserRole roles =  roleRepository.findByRole("REQUESTER").orElseThrow();
		user.setRoles(Collections.singletonList(roles));
		userRepository.save(user);
		
		return true;
	}
	
	
	public AuthResponse authenticate(LoginDto userDto) {
    	authenticationManager.authenticate(
    			new UsernamePasswordAuthenticationToken(
    					userDto.getEmail(), userDto.getPassword()));
    	
    	var user = userRepository.findByemail(userDto.getEmail()).orElseThrow();
    	
    	var jwtToken = jwtTokenProvider.generateToken(user);
        
        return AuthResponse.builder()
        		.accessToken(jwtToken)
        		.build();
    }
	
	
	
	private UserEntity mapDtoToUser(EngineerRegisterDto registerDto) {
		
		SupportEngineer engineer = new SupportEngineer();
		
		engineer.setEmail(registerDto.getEmail());
		engineer.setMobileNo(registerDto.getMobileNo());
		engineer.setFullName(registerDto.getEngineerName());
		engineer.setPassword(passwordEncoder.encode(registerDto.getPassword()));
		
		return engineer;
		
	}
	
	private UserEntity mapRequesterToUser(RegisterDto registerDto) {
		
		Requester requester = new Requester();
		
		requester.setEmail(registerDto.getEmail());
		requester.setCompany(companyRepository.findById(registerDto.getCompanyId()).
				orElseThrow(()->new ResourceNotFoundException("company Doesn't exist")));
		requester.setFullName(registerDto.getRequesterName());
		requester.setMobileNo(registerDto.getMobileNo());
		requester.setPassword(passwordEncoder.encode(registerDto.getPassword()));
		
		return requester;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
