package com.tickethandler.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

	

	
//	@Bean
//	public UserDetailsManager userDetialsManager(DataSource dataSource) {
//		
//		JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager();
//		
//		jdbcUserDetailsManager.setUsersByUsernameQuery(
//				"select user_id,password from requesters where user_id=?");
//		
//		jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(
//				"select user_id, role from roles where user_id=?");
//		
//		return jdbcUserDetailsManager;
//	}
//	
//	
	
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http)throws Exception{
	
		http.authorizeHttpRequests(configurer ->
		configurer
				.requestMatchers(HttpMethod.POST,"/api/tickets/create").
				hasRole("REQUESTER"));
		http.authorizeHttpRequests(configurer ->
		configurer
				.requestMatchers(HttpMethod.GET,"/api/tickets/**").
				hasRole("REQUESTER"));
		
		http.authorizeHttpRequests(configurer ->
		configurer
				.requestMatchers(HttpMethod.PUT,"/api/tickets/**").
				hasRole("ENGINEER"));
		
		http.authorizeHttpRequests(configurer ->
		configurer
				.requestMatchers(HttpMethod.DELETE,"/api/tickets/**").
				hasRole("ADMIN"));
		
		
		http.authorizeHttpRequests(configurer-> configurer.requestMatchers(HttpMethod.POST,"api/requester/**").permitAll());
		
		
		//http.httpBasic(Customizer.withDefaults());
		
		http.csrf(csrf-> csrf.disable());
		
		return http.build();
	}
	
	
	@Bean
	AuthenticationManager authenticationManger(AuthenticationConfiguration authenticationConfiguration) 
			throws Exception{
		return authenticationConfiguration.getAuthenticationManager();
		
	}
	
	@Bean
	BCryptPasswordEncoder passwordEncoder() {
		 return new BCryptPasswordEncoder();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
