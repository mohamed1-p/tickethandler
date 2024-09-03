package com.tickethandler.security;



import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;





@Configuration
@EnableWebSecurity
public class SecurityConfig{

	
	private  JwtTokenFilter jwtTokenFilter;
	private  AuthenticationProvider authenticationProvider;
	
	
	public SecurityConfig(JwtTokenFilter jwtTokenFilter, 
			AuthenticationProvider authenticationProvider) {
		this.jwtTokenFilter = jwtTokenFilter;
		this.authenticationProvider = authenticationProvider;
	}


	@Bean
	SecurityFilterChain filterChain(HttpSecurity http)throws Exception{
	
		http.authorizeHttpRequests(configurer ->
		configurer
				.requestMatchers(HttpMethod.POST,"/api/tickets/create").
				hasAuthority("REQUESTER"));
		http.authorizeHttpRequests(configurer ->
		configurer
				.requestMatchers(HttpMethod.GET,"/api/tickets/**").
				authenticated());
		
		http.authorizeHttpRequests(configurer ->
		configurer
				.requestMatchers(HttpMethod.PUT,"/api/tickets/**").
				hasAuthority("ENGINEER"));
		
		http.authorizeHttpRequests(configurer ->
		configurer
				.requestMatchers(HttpMethod.DELETE,"/api/tickets/**").
				hasAuthority("ADMIN"));
		
		
		http.authorizeHttpRequests(configurer-> configurer.requestMatchers(
				HttpMethod.POST,"api/register/**").permitAll());
		
		http.authorizeHttpRequests(configurer-> configurer.requestMatchers(
				HttpMethod.POST,"api/login").permitAll());
		
		http.authorizeHttpRequests(configurer ->
		configurer
				.requestMatchers("/api/company/**")
				.authenticated());
		http.authorizeHttpRequests(configurer ->
		configurer
				.requestMatchers("/api/product/**")
				.authenticated());
		
		
	
		http.addFilterBefore(jwtTokenFilter,UsernamePasswordAuthenticationFilter.class);
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and().authenticationProvider(authenticationProvider);
		
		http.csrf(csrf-> csrf.disable());
		
		return http.build();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
