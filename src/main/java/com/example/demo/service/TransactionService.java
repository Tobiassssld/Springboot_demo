package com.example.demo.service;

import com.example.demo.entity.Transaction;
import com.example.demo.enums.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface TransactionService {
    Transaction createTransaction(Long fromAccountId, Long toAccountId,
                                  BigDecimal amount, TransactionType type, String description);
    Transaction getTransactionById(String transactionId);
    Page<Transaction> getTransactionsByAccountId(Long accountId, Pageable pageable);
    List<Transaction> getTransactionsByAccountAndType(Long accountId, TransactionType type);
    List<Transaction> getTransactionsByDateRange(Long accountId, LocalDateTime startDate, LocalDateTime endDate);
    String generateTransactionId();
}
