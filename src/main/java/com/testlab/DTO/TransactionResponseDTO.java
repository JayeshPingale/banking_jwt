package com.testlab.DTO;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class TransactionResponseDTO {

    private Long transId;
    private String transType;
    private Double amount;
    private LocalDateTime date;
    private Long fromaccountId;
    private Long toaccountId;
    private Long customerId;
    private Double balance;
}