package com.testlab.mapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.testlab.DTO.TransactionRequestDTO;
import com.testlab.DTO.TransactionResponseDTO;
import com.testlab.entities.Transaction;

public class TransactionMapper {

    public static Transaction toEntity(TransactionRequestDTO req) {
        if (req == null) return null;
        Transaction txn = new Transaction();
        txn.setAmount(req.getAmount());
        txn.setTransType(req.getTransType());
//        txn.setTimestamp(req.getTimestamp());
        
        return txn;
    }

//    public static TransactionResponseDTO toResponse(Transaction t) {
//        if (t == null) return null;
//        TransactionResponseDTO resp = new TransactionResponseDTO();
//        resp.setTransId(t.getTransId());
//        resp.setAmount(t.getAmount());
//        resp.setTransType(t.getTransType());
////        resp.setTimestamp(t.getTimestamp());
////        resp.setAccountId(t.getAccount().getId());
//        return resp;
//    }
    
    public static TransactionResponseDTO toResponse(Transaction txn) {
        TransactionResponseDTO resp = new TransactionResponseDTO();
        resp.setTransId(txn.getTransId());
        resp.setTransType(txn.getTransType());
        resp.setAmount(txn.getAmount());
        resp.setDate(txn.getDate());

        if (txn.getFromAccount() != null) {
            resp.setFromaccountId(txn.getFromAccount().getAccountId());
            resp.setBalance(txn.getFromAccount().getBalance()); // ✅ current balance of account
        } else {
            resp.setFromaccountId(null);
            resp.setBalance(null);
        }

        if (txn.getToAccount() != null) {
            resp.setToaccountId(txn.getToAccount().getAccountId());
        } else {
            resp.setToaccountId(null);
        }

        if (txn.getCustomer() != null) {
            resp.setCustomerId(txn.getCustomer().getCustomerId());
        } else {
            resp.setCustomerId(null);
        }

        return resp;
    }

}
