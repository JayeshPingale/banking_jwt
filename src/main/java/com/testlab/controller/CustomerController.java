package com.testlab.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.testlab.DTO.AddressRequestDTO;
import com.testlab.DTO.CustomerRequestDTO;
import com.testlab.DTO.CustomerResponseDTO;
import com.testlab.DTO.CustomerUpdateDTO;
import com.testlab.services.CustomerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping("/customers")
    public ResponseEntity<CustomerResponseDTO> createCustomer(@Valid @RequestBody CustomerRequestDTO dto){
        CustomerResponseDTO resp = customerService.createCustomer(dto);
        return ResponseEntity.created(URI.create("/api/customers/" + resp.getCustomerId())).body(resp);
    }

    @GetMapping("/customers/{id}")
    public ResponseEntity<CustomerResponseDTO> getCustomerById(@PathVariable Long id){
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    @GetMapping("/customers")
    public ResponseEntity<List<CustomerResponseDTO>> getAllCustomers(){
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @PutMapping("/customers/{id}")
    public ResponseEntity<CustomerResponseDTO> updateCustomer(@PathVariable Long id,
                                                              @Valid @RequestBody CustomerUpdateDTO dto){
        return ResponseEntity.ok(customerService.updateCustomer(id, dto));
    }

    @PostMapping("/customers/{id}/address")
    public ResponseEntity<Object> addOrUpdateAddress(@PathVariable Long id,
                                                                  @Valid @RequestBody AddressRequestDTO dto){
        return ResponseEntity.ok(customerService.addOrUpdateAddress(id, dto));
    }

    @DeleteMapping("/customers/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id){
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
}