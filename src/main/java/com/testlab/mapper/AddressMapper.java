package com.testlab.mapper;

import com.testlab.DTO.AddressRequestDTO;
import com.testlab.DTO.AddressResponseDTO;
import com.testlab.entities.Address;

public class AddressMapper {

    public static Address toEntity(AddressRequestDTO req) {
        if (req == null) return null;
        Address addr = new Address();
        addr.setCity(req.getCity());
        addr.setState(req.getState());
        addr.setPincode(req.getPincode());
        return addr;
    }

    public static AddressResponseDTO toResponse(Address addr) {
        if(addr == null) return null;
        AddressResponseDTO dto = new AddressResponseDTO();
        dto.setAddressId(addr.getAddressId());
        dto.setCity(addr.getCity());
        dto.setState(addr.getState());
        dto.setPincode(addr.getPincode());
        return dto;
    }
}
