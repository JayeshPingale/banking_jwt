package com.testlab.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.testlab.entities.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
