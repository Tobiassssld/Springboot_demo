package com.example.demo.controller;

import com.example.demo.dto.DepositRequest;
import com.example.demo.dto.TransferRequest;
import com.example.demo.dto.WithdrawRequest;
import com.example.demo.entity.Account;
import com.example.demo.entity.User;
import com.example.demo.service.AccountService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<?> createAccount(@RequestParam String accountType,
                                           @RequestParam(defaultValue = "USD") String currency){
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            User user = userService.findByUsername(username);

            Account account = accountService.createAccount(user.getId(), accountType, currency);
            return ResponseEntity.ok(account);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(@RequestBody DepositRequest request){
        try {
            accountService.deposit(request.getAccountNumber(), request.getAmount());
            return ResponseEntity.ok("deposit successful");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/balance")
    public ResponseEntity<?> getBalance() {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            System.out.println("Getting balance for user: " + username); // 调试日志

            User user = userService.findByUsername(username);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found: " + username);
            }

            System.out.println("Found user with ID: " + user.getId()); // 调试日志

            Account account = accountService.getAccountByUserId(user.getId());
            if (account == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found for user: " + username);
            }

            return ResponseEntity.ok(account);
        } catch (Exception e) {
            System.out.println("Error in getBalance: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/withdraw")
    public ResponseEntity<String> withdraw(@RequestBody WithdrawRequest request){
        try{
            accountService.withdraw(request.getAccountNumber(), request.getAmount());
            return ResponseEntity.ok("withdrawal successful");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(@RequestBody TransferRequest request) {
        try {
            accountService.transfer(request.getFromAccount(), request.getToAccount(), request.getAmount());
            return ResponseEntity.ok("Transfer successful");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
