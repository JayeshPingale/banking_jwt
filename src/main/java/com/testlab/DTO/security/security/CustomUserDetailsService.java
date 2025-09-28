package com.testlab.DTO.security.security;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.testlab.Repository.UserRepository;
import com.testlab.entities.User;

// Spring Security calls this method (loadUserByUsername) automatically 
// during the authentication process when a user tries to log in.

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepo;

	// It fetches user details and roles from the DB and prepares them 
	// for Spring Security’s login authentication.
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	    User user = userRepo.findByUsername(username)
	            .orElseThrow(() -> new UsernameNotFoundException("user not found"));

	    Set<GrantedAuthority> authorities = user.getRoles().stream()
	            .map(r -> new SimpleGrantedAuthority(r.getRoleName()))
	            .collect(Collectors.toSet());

	    return new org.springframework.security.core.userdetails.User(
	            user.getUserName(), 
	            user.getPassword(), 
	            authorities
	    );
	}
}
