package com.testlab.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.testlab.DTO.AddressRequestDTO;
import com.testlab.DTO.CustomerRequestDTO;
import com.testlab.DTO.CustomerResponseDTO;
import com.testlab.DTO.CustomerUpdateDTO;
import com.testlab.Repository.CustomerRepository;
import com.testlab.entities.Address;
import com.testlab.entities.Customer;
import com.testlab.mapper.CustomerMapper;
import com.testlab.services.CustomerService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerServiceImpl implements CustomerService{
	 private final CustomerRepository customerRepo;

	    @Override
	    public CustomerResponseDTO createCustomer(CustomerRequestDTO dto) {
	        Address address = new Address();
	        address.setCity(dto.getAddress().getCity());
	        address.setState(dto.getAddress().getState());
	        address.setPincode(dto.getAddress().getPincode());

	        Customer customer = new Customer();
	        customer.setEmailid(dto.getEmail());
	        customer.setContactNumber(dto.getContactNumber());
	        customer.setDob(dto.getDob());
	        
	        // link address
	        customer.setAddress(address);

	        Customer savedCustomer = customerRepo.save(customer);

	        return CustomerMapper.toResponse(savedCustomer);
	    }

	    @Override
	    public CustomerResponseDTO getCustomerById(Long id) {
	        Customer customer = customerRepo.findById(id)
	                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
	        return CustomerMapper.toResponse(customer);
	    }

	    @Override
	    public List<CustomerResponseDTO> getAllCustomers() {
	        return customerRepo.findAll().stream()
	                .map(CustomerMapper::toResponse)
	                .collect(Collectors.toList());
	    }

	    @Override
	    public CustomerResponseDTO updateCustomer(Long id, CustomerUpdateDTO dto) {
	        Customer customer = customerRepo.findById(id)
	                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
	        CustomerMapper.updateEntity(customer, dto);
	        return CustomerMapper.toResponse(customer);
	    }

	    @Override
	    public void deleteCustomer(Long id) {
	        Customer customer = customerRepo.findById(id)
	                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
	        customerRepo.delete(customer);
	    }

	    @Override
	    public CustomerResponseDTO addOrUpdateAddress(Long customerId, AddressRequestDTO dto) {
	        Customer customer = customerRepo.findById(customerId)
	                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + customerId));

	        Address address = customer.getAddress();
	        if (address == null) {
	            address = new Address();
	        }

	        address.setCity(dto.getCity());
	        address.setState(dto.getState());
	        address.setPincode(dto.getPincode());

	        customer.setAddress(address);
	        customerRepo.save(customer);

	        return CustomerMapper.toResponse(customer);
	    }

	
}
