package com.testlab.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data

public class AddressResponseDTO {
	private Long addressId;
	private String city;
	private String state;
	private String pincode;
}
