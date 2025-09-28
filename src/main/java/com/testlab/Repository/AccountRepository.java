package com.testlab.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.testlab.entities.Account;

public interface AccountRepository extends JpaRepository<Account, Long>{

//	 List<Account> findByCustomerCustomerid(Integer customerid);
//
//	   @Query("SELECT a FROM Account a WHERE a.customer.customerid = :customerid")
//	    List<Account> findAccountsByCustomerId(@Param("customerid") Integer customerid);
}
