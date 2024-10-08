package com.tickethandler.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tickethandler.dto.AuthResponse;
import com.tickethandler.dto.ChangePasswordRequest;
import com.tickethandler.dto.EngineerRegisterDto;
import com.tickethandler.dto.EngineerShowDto;
import com.tickethandler.dto.LoginDto;
import com.tickethandler.dto.ProductDto;
import com.tickethandler.dto.RegisterDto;
import com.tickethandler.dto.ResponsePage;
import com.tickethandler.exception.ResourceNotFoundException;
import com.tickethandler.model.Product;
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

	public String createEngineer(EngineerRegisterDto engineer) {

		if (userRepository.existsByemail(engineer.getEmail())) {
			return null;
		}
		UserEntity user = mapDtoToUser(engineer);

		UserRole roles = roleRepository.findByRole("ENGINEER").get();
		user.getRoles().add(roles);
		userRepository.save(user);
		//String jwtToken = jwtTokenProvider.generateToken(user);

		return "Register Success!";
	}

	public String createRequester(RegisterDto registerDto) {
		if (userRepository.existsByemail(registerDto.getEmail())) {
			return null;
		}
		UserEntity user = mapRequesterToUser(registerDto);
		UserRole roles = roleRepository.findByRole("REQUESTER").
				orElseThrow(() -> new ResourceNotFoundException("Cannot create an account for requester, please contact Admin"));
		user.getRoles().add(roles);
		userRepository.save(user);

		//String jwtToken = jwtTokenProvider.generateToken(user);

		return "Register Success!";
	}

	public AuthResponse authenticate(LoginDto userDto) {
		
				authenticationManager
						.authenticate(new UsernamePasswordAuthenticationToken(userDto.getEmail(), userDto.getPassword()));


		UserEntity user = userRepository.findByemail(userDto.getEmail())
				.orElseThrow(() -> new ResourceNotFoundException("User don't exist"+userDto.getEmail()));

		
		String jwtToken = jwtTokenProvider.generateToken(user);

		return AuthResponse.builder().accessToken(jwtToken).build();
	}
	
	
	public ResponsePage<EngineerShowDto> findEngineersByName(String name,int pageNo, int pageSize){
	
		Pageable pageable = PageRequest.of(pageNo, pageSize);
		Page<SupportEngineer> usersPage = userRepository.findByFullNameContaining(name, pageable);
		List<SupportEngineer> users = usersPage.getContent();
		
		List<EngineerShowDto> engineerDto = users.stream()
				.map(this::maptoEngineerDto)
				.collect(Collectors.toList());
		
		return mapDtoToPage(usersPage,engineerDto);
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
	
	public ResponseEntity<?> changePassword(ChangePasswordRequest request) {
		UserDetails  userDetails =  (UserDetails ) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	        String userEmail = userDetails.getUsername();
	        
	        UserEntity user = userRepository.findByemail(userEmail).
					orElseThrow(()->
					new ResourceNotFoundException("The user you want to make admin don't exist ("+ userEmail+")")); 
	        
	        if (user == null || !passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
	            return ResponseEntity.badRequest().body("Current password is incorrect");
	        }
	        
	        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
	        userRepository.save(user);

	        return ResponseEntity.ok("Password changed successfully");
	
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
				.orElseThrow(() -> new ResourceNotFoundException("Company ID is not valid")));
		requester.setFullName(registerDto.getRequesterName());
		requester.setMobileNo(registerDto.getMobileNo());
		requester.setPassword(passwordEncoder.encode(registerDto.getPassword()));

		return requester;
	}
	
	
	private EngineerShowDto maptoEngineerDto(UserEntity user) {
		EngineerShowDto engineer = new EngineerShowDto();
		engineer.setEmail(user.getEmail());
		engineer.setName(user.getFullName());
		engineer.setPhone_number(user.getMobileNo());
		return engineer;
		
	}
	
	private ResponsePage<EngineerShowDto> mapDtoToPage(Page<SupportEngineer> engineer,
			List<EngineerShowDto> dto){
		ResponsePage<EngineerShowDto> content = new ResponsePage<>();
		
		content.setContent(dto);
		content.setPage(engineer.getNumber());
		content.setSize(engineer.getSize());
		content.setTotalElements(engineer.getTotalElements());
		content.setTotalpages(engineer.getTotalPages());
		content.setLast(engineer.isLast());
		
		return content;
	}

	

}
