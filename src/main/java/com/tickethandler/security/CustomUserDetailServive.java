package com.tickethandler.security;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tickethandler.model.ReqeusterRole;
import com.tickethandler.model.Requester;
import com.tickethandler.repo.RequesterRepository;

@Service
public class CustomUserDetailServive implements UserDetailsService {

	@Autowired
	private RequesterRepository requesterRepository;
	
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Requester requester = requesterRepository.findByrequesterEmail(email).get();
		
		return new User(requester.getRequesterEmail(),requester.getRequesterPassword(),
				mapRolesToAutority(requester.getRoles()));
		
	}
	

	private Collection<GrantedAuthority> mapRolesToAutority(List<ReqeusterRole> roles){
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getRole()))
				.collect(Collectors.toList());
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
