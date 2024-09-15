package com.tickethandler.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.extern.log4j.Log4j2;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Log4j2
@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userService;

    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider
    		,UserDetailsService userService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService=userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
    		HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
    	
      
    	try {
			//this is the header that contains the token
			final String authHeader = request.getHeader("Authorization");
			final String jwt;
			final String username;
			if(authHeader == null ||! authHeader.startsWith("Bearer ")) {
				filterChain.doFilter(request, response);
				return;
			}
			jwt = authHeader.substring(7);
			username = jwtTokenProvider.extractUsername(jwt);
			if(username!= null && SecurityContextHolder.getContext().getAuthentication() ==null) {
				UserDetails userDetails = this.userService.loadUserByUsername(username);
				if(jwtTokenProvider.isTokenValid(jwt, userDetails)) {
					UsernamePasswordAuthenticationToken authToken = new 
							UsernamePasswordAuthenticationToken(userDetails,null ,userDetails.getAuthorities());
					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					
					SecurityContextHolder.getContext().setAuthentication(authToken);
				}
			}
			
			
		} catch (UsernameNotFoundException e) {
			log.error("Username or email cannot be found ",e);
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		} catch (ServletException e) {
			e.printStackTrace();
		}
    	filterChain.doFilter(request, response);
    }

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
