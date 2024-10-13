package com.tickethandler.security;



import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.tickethandler.exception.AuthEntryPoint;
import com.tickethandler.exception.CustomAccessDeniedHandler;





@Configuration
@EnableWebSecurity
public class SecurityConfig{

	
	private  JwtTokenFilter jwtTokenFilter;
	private  AuthenticationProvider authenticationProvider;
	private AuthEntryPoint authEntryPoint;
	private CustomAccessDeniedHandler accessDeniedHandler;
	
	
	public SecurityConfig(JwtTokenFilter jwtTokenFilter, 
			AuthenticationProvider authenticationProvider,
			AuthEntryPoint authEntryPoint,CustomAccessDeniedHandler accessDeniedHandler) {
		this.jwtTokenFilter = jwtTokenFilter;
		this.authenticationProvider = authenticationProvider;
		this.authEntryPoint=authEntryPoint;
		this.accessDeniedHandler=accessDeniedHandler;
	}


	@Bean
	SecurityFilterChain filterChain(HttpSecurity http)throws Exception{

        http.csrf(csrf -> csrf.disable())
                .cors(cors -> cors.disable())
                .exceptionHandling(handling -> handling.authenticationEntryPoint(authEntryPoint)
                				.accessDeniedHandler(accessDeniedHandler))
                .authorizeRequests(requests -> requests
                        .antMatchers(HttpMethod.POST, "/api/tickets/create").hasAnyAuthority("REQUESTER", "ADMIN")
                        .antMatchers(HttpMethod.GET, "/api/tickets/filter").hasAnyAuthority("ENGINEER", "ADMIN","REQUESTER")
                        .antMatchers(HttpMethod.GET, "/api/tickets/**").hasAnyAuthority("ENGINEER", "ADMIN")
                        .antMatchers(HttpMethod.PUT, "/api/tickets/**").hasAnyAuthority("ENGINEER", "ADMIN")
                        .antMatchers(HttpMethod.DELETE, "/api/tickets/**").hasAuthority("ADMIN")
                        .antMatchers(HttpMethod.PUT, "/api/admin/**").hasAuthority("ADMIN")
                        .antMatchers(HttpMethod.POST, "/api/admin/**").hasAuthority("ADMIN")
                        .antMatchers(HttpMethod.POST, "/api/login").permitAll()
                        .antMatchers("/api/company/companies").hasAuthority("ADMIN")
                        .antMatchers("/api/product").hasAuthority("ADMIN")
                        .anyRequest().authenticated());
		
		
	
		http.addFilterBefore(jwtTokenFilter,UsernamePasswordAuthenticationFilter.class);
		http.sessionManagement(management -> management.
        		sessionCreationPolicy(SessionCreationPolicy.STATELESS)).
        authenticationProvider(authenticationProvider);
		
		
		
		return http.build();
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
