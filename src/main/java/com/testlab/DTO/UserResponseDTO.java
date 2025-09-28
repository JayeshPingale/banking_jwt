package com.testlab.DTO;

import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class UserResponseDTO {

	private Long id;
	
	private String userName;
	
	private List<String> roles;
	
	private CustomerResponseDTO customer;

	

	
//	private AddressResponseDTO address;
	
//	private AccountResponseDTO account;
}
