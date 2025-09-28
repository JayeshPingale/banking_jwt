package com.testlab.services;

import com.techlabs.security.dto.LoginDto;
import com.techlabs.security.dto.RegistrationDto;
import com.techlabs.security.dto.UserResponseDto;

public interface AuthService {
	
	UserResponseDto register(RegistrationDto registration);
    String login(LoginDto loginDto);

}
