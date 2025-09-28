package com.testlab.services.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.testlab.DTO.TransactionRequestDTO;
import com.testlab.DTO.TransactionResponseDTO;
import com.testlab.Exception.NotFoundException;
import com.testlab.Repository.AccountRepository;
import com.testlab.Repository.CustomerRepository;
import com.testlab.Repository.TransactionRepository;
import com.testlab.entities.Account;
import com.testlab.entities.Customer;
import com.testlab.entities.Transaction;
import com.testlab.mapper.TransactionMapper;
import com.testlab.services.TransactionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class TransactionServiceImpl implements TransactionService {

	private final TransactionRepository transactionRepo;
	private final AccountRepository accountRepo;
	private final CustomerRepository customerRepo;

	@Override
	public TransactionResponseDTO createTransaction(TransactionRequestDTO dto) {
		// Fetch accounts and customer
		Account fromAccount = accountRepo.findById(dto.getFromaccountId())
				.orElseThrow(() -> new NotFoundException("From Account not found: " + dto.getFromaccountId()));

		if ("TRANSFER".equals(dto.getTransType()) && dto.getToaccountId() == null) {
		    throw new IllegalArgumentException("toAccountId is required for TRANSFER");
		}
		Account toAccount = null;
		if ("TRANSFER".equals(dto.getTransType())) {
			if (dto.getToaccountId() == null) {
				throw new NotFoundException("to Account id is required for TRANSFER");
			}
			toAccount = accountRepo.findById(dto.getToaccountId())
					.orElseThrow(() -> new NotFoundException("To Account not found"));
		}
		Customer customer = customerRepo.findById(dto.getCustomerId())
				.orElseThrow(() -> new NotFoundException("Customer not found: " + dto.getCustomerId()));

		if (dto.getTransType().equals("DEBIT") || dto.getTransType().equals("TRANSFER")) {
			Double balance = fromAccount.getBalance() - dto.getAmount();
			if (balance < 500) {
				throw new IllegalArgumentException("Insuffient balance 500 is the minimum balance");
			}
		}

		switch (dto.getTransType()) {
		case "DEBIT" -> fromAccount.setBalance(fromAccount.getBalance() - dto.getAmount());
		case "CREDIT" -> fromAccount.setBalance(fromAccount.getBalance() + dto.getAmount());
		case "TRANSFER" -> {
			fromAccount.setBalance(fromAccount.getBalance() - dto.getAmount());
			toAccount.setBalance(fromAccount.getBalance() + dto.getAmount());
		}

		default -> {
			throw new IllegalArgumentException("Invalid transaction type: " + dto.getTransType());
		}
		}
		// Save accounts
		accountRepo.save(fromAccount);
		if (toAccount != null) {
			accountRepo.save(toAccount);
		}

		// Create Transaction entity
		Transaction txn = new Transaction();
		txn.setFromAccount(fromAccount);
//		txn.setToAccount(toAccount);  // null allowed for CREDIT/DEBIT
		txn.setToAccount(dto.getTransType().equals("TRANSFER") ? toAccount : null);
		txn.setCustomer(customer);
		txn.setTransType(dto.getTransType());
		txn.setAmount(dto.getAmount());
		txn.setDate(LocalDateTime.now());

		// Save transaction
		Transaction savedTxn = transactionRepo.save(txn);

		// Map to Response DTO using TransactionMapper
		return TransactionMapper.toResponse(savedTxn);
	}

	@Override
	@Transactional(readOnly = true)
	public TransactionResponseDTO getTransactionById(Long transId) {
		Transaction transaction = transactionRepo.findById(transId)
				.orElseThrow(() -> new NotFoundException("Transaction not found: " + transId));

		TransactionResponseDTO resp = new TransactionResponseDTO();
		resp.setTransId(transaction.getTransId());
		resp.setTransType(transaction.getTransType());
		resp.setAmount(transaction.getAmount());
		resp.setDate(transaction.getDate());
		resp.setFromaccountId(transaction.getFromAccount().getAccountId());
		resp.setToaccountId(transaction.getToAccount().getAccountId());
		resp.setCustomerId(transaction.getCustomer().getCustomerId());

		return resp;
	}

	@Override
	public List<TransactionResponseDTO> getAllTransactions() {
	    List<Transaction> transactions = transactionRepo.findAll();
	    return transactions.stream()
	            .map(TransactionMapper::toResponse) // null-safe mapper
	            .collect(Collectors.toList());
	}
	@Override
	@Transactional(readOnly = true)
	public List<TransactionResponseDTO> getTransactionsByCustomerId(Long customerId) {
		Customer customer = customerRepo.findById(customerId)
				.orElseThrow(() -> new NotFoundException("CustomerId not found"));

		List<Transaction> transactions = customer.getTransactions();
		if (transactions == null || transactions.isEmpty()) {
			throw new NotFoundException("No transactions assigned to this customer.");
		}

		// Map each Transaction to TransactionResponseDTO
		return transactions.stream().map(tx -> {
			TransactionResponseDTO dto = new TransactionResponseDTO();
			dto.setTransId(tx.getTransId());
			dto.setTransType(tx.getTransType());
			dto.setAmount(tx.getAmount());
			dto.setDate(tx.getDate());
			dto.setFromaccountId(tx.getFromAccount() != null ? tx.getFromAccount().getAccountId() : null);
			dto.setToaccountId(tx.getToAccount() != null ? tx.getToAccount().getAccountId() : null);
			dto.setCustomerId(tx.getCustomer() != null ? tx.getCustomer().getCustomerId() : null);
			return dto;
		}).toList();
	}

}
