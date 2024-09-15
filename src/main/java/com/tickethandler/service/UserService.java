package com.tickethandler.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

import lombok.extern.log4j.Log4j2;

@Log4j2
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
			AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
		this.passwordEncoder = passwordEncoder;
		this.roleRepository = roleRepository;
		this.userRepository = userRepository;
		this.companyRepository = companyRepository;
		this.authenticationManager = authenticationManager;
		this.jwtTokenProvider = jwtTokenProvider;

	}

	public AuthResponse createEngineer(EngineerRegisterDto engineer) {

		if (userRepository.existsByemail(engineer.getEmail())) {
			return null;
		}
		UserEntity user = mapDtoToUser(engineer);

		UserRole roles = roleRepository.findByRole("ENGINEER").get();
		user.getRoles().add(roles);
		userRepository.save(user);
		String jwtToken = jwtTokenProvider.generateToken(user);

		return AuthResponse.builder().accessToken(jwtToken).build();
	}

	public AuthResponse createRequester(RegisterDto registerDto) {
		if (userRepository.existsByemail(registerDto.getEmail())) {
			return null;
		}
		UserEntity user = mapRequesterToUser(registerDto);
		UserRole roles = roleRepository.findByRole("REQUESTER").
				orElseThrow(() -> new ResourceNotFoundException("Cannot create an account for requester, please contact Admin"));
		user.getRoles().add(roles);
		userRepository.save(user);

		String jwtToken = jwtTokenProvider.generateToken(user);

		return AuthResponse.builder().accessToken(jwtToken).build();
	}

	public AuthResponse authenticate(LoginDto userDto) {
			authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(userDto.getEmail(), userDto.getPassword()));

		UserEntity user = userRepository.findByemail(userDto.getEmail())
				.orElseThrow(() -> new ResourceNotFoundException("User don't exist"+userDto.getEmail()));

		String jwtToken = jwtTokenProvider.generateToken(user);

		return AuthResponse.builder().accessToken(jwtToken).build();
	}

	public void makeAdmin(String userEmail) {
		
		UserEntity newAdminUser = userRepository.findByemail(userEmail).
				orElseThrow(()->
				new ResourceNotFoundException("The user you want to make admin don't exist ("+ userEmail+")")); 
		boolean hasEngineerRole = newAdminUser.getRoles().stream()
		        .anyMatch(role -> role.getRole().equals("ENGINEER"));
		
		if(hasEngineerRole) {
			 UserRole adminRole = roleRepository.findByRole("ADMIN").
		 				orElseThrow(() -> new ResourceNotFoundException("The role you are searching for doesn't exist"));
				newAdminUser.getRoles().add(adminRole);
				userRepository.save(newAdminUser);
		}else {
			throw new RuntimeException("Only engineers are allowd to be admins");
		}
		
	}
	
	/*
	 * @PostConstruct public void initializeDefaultUser() { if
	 * (!userRepository.existsByemail("admin")) { SupportEngineer adminUser = new
	 * SupportEngineer(); adminUser.setEmail("admin");
	 * adminUser.setPassword(passwordEncoder.encode("SDT234896517"));
	 * adminUser.setFullName("admin engineer");
	 * adminUser.setMobileNo("admin number"); UserRole firstRole =
	 * roleRepository.findByRole("ENGINEER"). orElseThrow(() -> new
	 * RuntimeException()); adminUser.getRoles().add(firstRole); UserRole secondRole
	 * = roleRepository.findByRole("ADMIN"). orElseThrow(() -> new
	 * RuntimeException()); adminUser.getRoles().add(secondRole);
	 * userRepository.save(adminUser);
	 * 
	 * }
	 */
       
	//}
	
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
		requester.setCompany(companyRepository.findById(registerDto.getCompanyId())
				.orElseThrow(() -> new ResourceNotFoundException("company Doesn't exist")));
		requester.setFullName(registerDto.getRequesterName());
		requester.setMobileNo(registerDto.getMobileNo());
		requester.setPassword(passwordEncoder.encode(registerDto.getPassword()));

		return requester;
	}

}
