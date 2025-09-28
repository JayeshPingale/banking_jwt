package com.testlab.services;

import java.util.List;

import com.testlab.DTO.UserRequestDTO;
import com.testlab.DTO.UserResponseDTO;
import com.testlab.DTO.UserUpdateDTO;

public interface UserService {
	 UserResponseDTO create(UserRequestDTO reqDTO);
	 
		
	 UserResponseDTO getUserByID(Long id);
		
		List<UserResponseDTO> getAllUsers();
		
		UserResponseDTO updateUser(Long id,UserUpdateDTO req);
		
		void deleteUserbyID(Long id);
		
		UserResponseDTO updatePassword(Long userId, String newPassword);
		
}
