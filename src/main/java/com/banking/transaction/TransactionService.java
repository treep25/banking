package com.banking.transaction;

import com.banking.TransactionType;
import com.banking.account.Account;
import com.banking.account.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final AccountService accountService;
    private final TransactionRepository transactionRepository;

    @Transactional
    public void deposit(String accountNumber, BigDecimal amount) {
        Account account = accountService.getAccount(accountNumber);
        account.setBalance(account.getBalance().add(amount));

        Account updatedAccount = accountService.updateAccount(account);

        transactionRepository.save(Transaction
                .builder()
                .type(TransactionType.DEPOSIT)
                .amount(amount)
                .accountNumber(accountNumber)
                .account(updatedAccount)
                .build()
        );
    }

    @Transactional
    public void withdraw(String accountNumber, BigDecimal amount) {
        Account account = accountService.getAccount(accountNumber);
        if (account.getBalance().compareTo(amount) >= 0) {
            account.setBalance(account.getBalance().subtract(amount));
            Account updatedAccount = accountService.updateAccount(account);

            transactionRepository.save(Transaction
                    .builder()
                    .type(TransactionType.WITHDRAWAL)
                    .amount(amount)
                    .accountNumber(accountNumber)
                    .account(updatedAccount)
                    .build()
            );
        } else {
            throw new IllegalArgumentException("Insufficient funds");
        }
    }

    @Transactional
    public void transfer(String fromAccountNumber, String toAccountNumber, BigDecimal amount) {
        withdraw(fromAccountNumber, amount);
        deposit(toAccountNumber, amount);

        transactionRepository.save(Transaction
                .builder()
                .type(TransactionType.WITHDRAWAL)
                .amount(amount)
                .accountNumber(fromAccountNumber)
                .targetAccountNumber(toAccountNumber)
                .account(accountService.getAccount(fromAccountNumber))
                .build()
        );
    }
}
