package com.tickethandler.security;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.tickethandler.model.UserEntity;
import com.tickethandler.model.UserRole;
import com.tickethandler.repo.UserRepository;




@Configuration
public class ApplicationConfig {


	private final UserRepository userRepository;
	@Autowired
	public ApplicationConfig(UserRepository userRepository) {
		this.userRepository=userRepository;
	}
	
	
	  @Bean
		 UserDetailsService userDetailService() {
			return new UserDetailsService() {
				
				@Override
				public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
					UserEntity user = userRepository.findByemail(username).
							orElseThrow(() -> new UsernameNotFoundException("user not found"));
					
					return new User(user.getEmail(),user.getPassword(),
							mapRolesToAutority(user.getRoles()));
				}
			};
		}

		
		private Collection<GrantedAuthority> mapRolesToAutority(Set<UserRole> roles){
			return roles.stream().map(role -> new SimpleGrantedAuthority(role.getRole()))
					.collect(Collectors.toList());
		}
		
		
		//the data access object to fetch username and filers
		@Bean
		 AuthenticationProvider authenticationProvider() {
			DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
			authProvider.setUserDetailsService(userDetailService());
			authProvider.setPasswordEncoder(passwordEncoder());
			
			return authProvider;
		}
		
		
		@Bean
		AuthenticationManager authenticationManger(
				AuthenticationConfiguration authenticationConfiguration) 
				throws Exception{
			return authenticationConfiguration.getAuthenticationManager();
			
		}
		
		
		@Bean
		BCryptPasswordEncoder passwordEncoder() {
			 return new BCryptPasswordEncoder();
		}
		

	}





















