package com.example.demo.controller;

import com.example.demo.entity.Transaction;
import com.example.demo.entity.User;
import com.example.demo.entity.Account;
import com.example.demo.enums.TransactionType;
import com.example.demo.service.TransactionService;
import com.example.demo.service.UserService;
import com.example.demo.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@RestController
@RequestMapping("/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;

    @GetMapping("/history")
    public ResponseEntity<?> getTransactionHistory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userService.findByUsername(username);
            Account account = accountService.getAccountByUserId(user.getId());

            Page<Transaction> transactions = transactionService.getTransactionsByAccountId(
                    account.getId(), PageRequest.of(page, size));

            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/history/date-range")
    public ResponseEntity<?> getTransactionsByDateRange(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userService.findByUsername(username);
            Account account = accountService.getAccountByUserId(user.getId());


            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime start = LocalDate.parse(startDate, formatter).atStartOfDay();
            LocalDateTime end = LocalDate.parse(endDate, formatter).atTime(23, 59, 59);

            List<Transaction> transactions = transactionService.getTransactionsByDateRange(
                    account.getId(), start, end);

            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            // 添加详细错误日志
            System.out.println("Date range query error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Date format error. Use yyyy-MM-dd format. Error: " + e.getMessage());
        }
    }

    @GetMapping("/history/type/{type}")
    public ResponseEntity<?> getTransactionsByType(@PathVariable TransactionType type){
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userService.findByUsername(username);
            Account account = accountService.getAccountByUserId(user.getId());

            List<Transaction> transactions = transactionService.getTransactionsByAccountAndType(
                    account.getId(), type);

            return ResponseEntity.ok(transactions);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<?> getTransactionDetails(@PathVariable String transactionId) {
        try {
            System.out.println("Looking for transaction: " + transactionId);
            Transaction transaction = transactionService.getTransactionById(transactionId);
            return ResponseEntity.ok(transaction);
        } catch (Exception e) {
            System.out.println("Transaction not found: " + transactionId + ", Error: " + e.getMessage());
            return ResponseEntity.badRequest().body("Transaction not found: " + transactionId);
        }
    }
}
