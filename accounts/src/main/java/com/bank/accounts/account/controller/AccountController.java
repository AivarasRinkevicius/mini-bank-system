package com.bank.accounts.account.controller;

import com.bank.accounts.account.dto.AccountDto;
import com.bank.accounts.account.model.Account;
import com.bank.accounts.account.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    AccountService accountService;

    @PostMapping
    public ResponseEntity<Account> create(@RequestBody AccountDto accountDto) {
        Account createdAccount = accountService.create(accountDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAccount);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> findAccount(@PathVariable long id) {
        return ResponseEntity.status(HttpStatus.FOUND).body(accountService.getById(id));
    }
}
