package com.banking.account;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<Account> createAccount(@RequestBody AccountCreateDto accountCreateDto) {
        if(StringUtils.isNotBlank(accountCreateDto.getAccountNumber())){
            if(!accountCreateDto.getInitialBalance().equals(BigDecimal.ZERO)){

                return new ResponseEntity<>(accountService.createAccount(accountCreateDto), HttpStatus.CREATED);
            }
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid initial balance");
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid account number");
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<Account> getAccountInfo(@PathVariable String accountNumber) {
        if(StringUtils.isNotBlank(accountNumber)){
            return ResponseEntity.ok(accountService.getAccountInfo(accountNumber));
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid account number");
    }

    @GetMapping
    public ResponseEntity<List<Account>> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAllAccounts());
    }
}

