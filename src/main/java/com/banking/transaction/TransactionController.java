package com.banking.transaction;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(@RequestBody TransactionDto transactionDto) {
        if (StringUtils.isNotBlank(transactionDto.getFromAccount())) {
            if (!transactionDto.getAmount().equals(BigDecimal.ZERO)
                    && transactionDto.getAmount().compareTo(BigDecimal.ZERO) > 0) {

                transactionService.deposit(transactionDto.getFromAccount(), transactionDto.getAmount());
                return ResponseEntity.ok("Deposit successful");
            }
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid deposit amount");
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid account number");
    }

    @PostMapping("/withdraw")
    public ResponseEntity<String> withdraw(@RequestBody TransactionDto transactionDto) {
        if (StringUtils.isNotBlank(transactionDto.getFromAccount())) {
            if (!transactionDto.getAmount().equals(BigDecimal.ZERO)
                    && transactionDto.getAmount().compareTo(BigDecimal.ZERO) > 0) {

                transactionService.withdraw(transactionDto.getFromAccount(), transactionDto.getAmount());
                return ResponseEntity.ok("Withdrawal successful");
            }
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid deposit amount");
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid account number");
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(@RequestBody TransactionDto transactionDto) {
        if (StringUtils.isNotBlank(transactionDto.getFromAccount())
                && StringUtils.isNotBlank(transactionDto.getToAccount())) {
            if (!transactionDto.getAmount().equals(BigDecimal.ZERO)
                    && transactionDto.getAmount().compareTo(BigDecimal.ZERO) > 0) {

                transactionService.transfer(transactionDto.getFromAccount(), transactionDto.getToAccount(), transactionDto.getAmount());
                return ResponseEntity.ok("Transfer successful");
            }
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid deposit amount");
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid transfer params");
    }
}
