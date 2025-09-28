package com.testlab.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.techlabs.security.dto.LoginDto;
import com.techlabs.security.dto.RegistrationDto;
import com.techlabs.security.dto.UserResponseDto;
import com.techlabs.security.entity.Role;
import com.techlabs.security.entity.User;
import com.techlabs.security.exception.UserApiException;
import com.techlabs.security.repo.RoleRepo;
import com.techlabs.security.repo.UserRepo;
import com.techlabs.security.security.JwtTokenProvider;
import com.techlabs.security.service.AuthService;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@Service
@AllArgsConstructor
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private UserRepo userRepo;
	@Autowired
	private RoleRepo roleRepo;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private JwtTokenProvider tokenProvider;

	@Override
	public UserResponseDto register(RegistrationDto registerDto) {
		if (userRepo.existsByUsername(registerDto.getUsername()))
			throw new UserApiException(HttpStatus.BAD_REQUEST, "User already exists");

		User user = new User();
		user.setUsername(registerDto.getUsername());
		user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

		// List<Role> roles = new ArrayList<Role>();

//		registerDto.getRoles().stream().map((role)-> roles.add(roleRepo.findByRolename(role).get()));

		Role userRole = roleRepo.findByRolename(registerDto.getRole())
				.orElseThrow(() -> new UserApiException(HttpStatus.BAD_REQUEST, "Role does not exists"));
		userRole.getUsers().add(user);
		user.setRole(userRole);

		user = userRepo.save(user);

		UserResponseDto dto = new UserResponseDto();
		dto.setUserId(user.getUserId());
		dto.setUsername(user.getUsername());

		return dto;
	}

	@Override
	public String login(LoginDto loginDto) {
		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
			SecurityContextHolder.getContext().setAuthentication(authentication);
			String token = tokenProvider.generateToken(authentication);

			return token;
		} catch (BadCredentialsException e) {
			throw new UserApiException(HttpStatus.NOT_FOUND, "Username or Password is incorrect");
		}
	}

}
