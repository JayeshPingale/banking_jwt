package com.testlab.services;

import java.util.List;

import com.testlab.DTO.AddressRequestDTO;
import com.testlab.DTO.CustomerRequestDTO;
import com.testlab.DTO.CustomerResponseDTO;
import com.testlab.DTO.CustomerUpdateDTO;

public interface CustomerService {

    CustomerResponseDTO createCustomer(CustomerRequestDTO dto);

    CustomerResponseDTO getCustomerById(Long id);

    List<CustomerResponseDTO> getAllCustomers();

    CustomerResponseDTO updateCustomer(Long id, CustomerUpdateDTO dto);

    void deleteCustomer(Long id);

    CustomerResponseDTO addOrUpdateAddress(Long customerId, AddressRequestDTO dto);
}
