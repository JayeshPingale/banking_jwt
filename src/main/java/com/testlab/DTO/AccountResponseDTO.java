package com.testlab.DTO;

import lombok.Data;

@Data
public class AccountResponseDTO {

    private Long accountId;
    private String accountNumber;
    private String accountType;
    private Double balance;
    private Long customerId;  
}
