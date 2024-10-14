package com.tickethandler.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.security.auth.message.callback.PrivateKeyCallback.Request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tickethandler.dto.AuthResponse;
import com.tickethandler.dto.ChangePasswordRequest;
import com.tickethandler.dto.EngineerRegisterDto;
import com.tickethandler.dto.LoginDto;

import com.tickethandler.dto.RegisterDto;
import com.tickethandler.dto.ResponsePage;
import com.tickethandler.dto.UserShowDto;
import com.tickethandler.exception.ResourceNotFoundException;
import com.tickethandler.model.Product;
import com.tickethandler.model.Requester;
import com.tickethandler.model.SupportEngineer;
import com.tickethandler.model.Ticket;
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
			AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
		this.passwordEncoder = passwordEncoder;
		this.roleRepository = roleRepository;
		this.userRepository = userRepository;
		this.companyRepository = companyRepository;
		this.authenticationManager = authenticationManager;
		this.jwtTokenProvider = jwtTokenProvider;

	}

	@Transactional
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

	@Transactional
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
	
	
	

	 public ResponsePage<UserShowDto> getUsersFiltered(int pageNo,int pageSize,Integer companyId, Integer productId, String name) {
	        Specification<UserEntity> spec =filterByCompanyProductAndName(companyId, productId, name);
	        
	    	Pageable pageable = PageRequest.of(pageNo, pageSize);
	        Page<UserEntity> usersPage = userRepository.findAll(spec,pageable);
	        List<UserEntity> users = usersPage.getContent();
	        
	        List<UserShowDto> userDto = users.stream()
	                .map(user -> {
	                    if (user instanceof Requester) {
	                        
	                        return maptoRequesterDto((Requester) user);
	                    } else{
	                       
	                        return maptoEngineerDto(user);
	                    } 
	                })
	                .collect(Collectors.toList());
	        
	        
	        return mapDtoToPage(usersPage,userDto);
	    }
    
    
    

	public AuthResponse authenticate(LoginDto userDto) {
		
				authenticationManager
						.authenticate(new UsernamePasswordAuthenticationToken(userDto.getEmail(), userDto.getPassword()));


		UserEntity user = userRepository.findByemail(userDto.getEmail())
				.orElseThrow(() -> new ResourceNotFoundException("User don't exist"+userDto.getEmail()));

		
		String jwtToken = jwtTokenProvider.generateToken(user);

		return AuthResponse.builder().accessToken(jwtToken).build();
	}
	
	
	public ResponsePage<UserShowDto> findEngineersByName(String name,int pageNo, int pageSize){
	
		Pageable pageable = PageRequest.of(pageNo, pageSize);
		Page<SupportEngineer> usersPage = userRepository.findByFullNameContaining(name, pageable);
		List<SupportEngineer> users = usersPage.getContent();
		
		List<UserShowDto> engineerDto = users.stream()
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
	
	
	private UserShowDto maptoEngineerDto(UserEntity user) {
		
		UserShowDto userDto = new UserShowDto();
		userDto.setEmail(user.getEmail());
		userDto.setName(user.getFullName());
		userDto.setPhone_number(user.getMobileNo());
		userDto.setType("Engineer");
		userDto.setCompanyName(" ");
		
		return userDto;
		
	}
	private UserShowDto maptoRequesterDto(Requester user) {
		
		UserShowDto userDto = new UserShowDto();
		userDto.setEmail(user.getEmail());
		userDto.setName(user.getFullName());
		userDto.setPhone_number(user.getMobileNo());
		userDto.setType("Requester");
		userDto.setCompanyName(user.getCompany().getCompanyName());
		return userDto;
		
	}
	
	private ResponsePage<UserShowDto> mapDtoToPage(Page<?> engineer,
			List<UserShowDto> dto){
		ResponsePage<UserShowDto> content = new ResponsePage<>();
		
		content.setContent(dto);
		content.setPage(engineer.getNumber());
		content.setSize(engineer.getSize());
		content.setTotalElements(engineer.getTotalElements());
		content.setTotalpages(engineer.getTotalPages());
		content.setLast(engineer.isLast());
		
		return content;
	}

	 public static Specification<UserEntity> filterByCompanyProductAndName(Integer companyId, Integer productId, String name) {
	        return (root, query, criteriaBuilder) -> {
	            Predicate predicate = criteriaBuilder.conjunction();
	            
	           
	            if (companyId != null) {
	            	Root<Requester> requesterRoot = criteriaBuilder.treat(root, Requester.class);
	                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(requesterRoot.get("company").get("companyId"), companyId));
	            }
	            
	            
	            if (productId != null) {
	            	  //Join<Requester, Ticket> ticketJoin = requesterRoot.join("tickets"); // Assuming "tickets" is a collection in Requester
	                //  predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(ticketJoin.get("product").get("id"), productId));
	            	Root<Requester> requesterRoot = criteriaBuilder.treat(root, Requester.class); 
	            	Join<Requester, Ticket> ticketJoin = requesterRoot.join("tickets");

	                 
	                 Join<Ticket, Product> productJoin = ticketJoin.join("product");
	                 
	                
	                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(productJoin.get("productId"), productId));
	            }
	            
	            
	            if (name != null && !name.isEmpty()) {
	                predicate = criteriaBuilder.and(predicate, 
	                    criteriaBuilder.like(criteriaBuilder.lower(root.get("fullName")), "%" + name.toLowerCase() + "%"));
	            }
	            
	            return predicate;
	        };
	    }
	

}
