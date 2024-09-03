package com.tickethandler.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;



import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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
        filterChain.doFilter(request, response);
    }

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
