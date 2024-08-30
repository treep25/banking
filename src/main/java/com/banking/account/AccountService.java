package com.banking.account;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public Account createAccount(AccountCreateDto accountCreateDto) {
        if (getAccount(accountCreateDto.getAccountNumber()) == null) {
            return accountRepository.save(
                    Account.builder().accountNumber(accountCreateDto.getAccountNumber())
                            .balance(accountCreateDto.getInitialBalance()).build()
            );
        }
        throw new RuntimeException("Account has already been created");
    }

    public Account getAccountInfo(String accountNumber) {
        Account byAccountNumber = accountRepository.findByAccountNumber(accountNumber);

        if (byAccountNumber == null) {
            throw new RuntimeException("Account not found");
        }
        return byAccountNumber;
    }

    public Account getAccount(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber);
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Account updateAccount(Account accountToUpdate) {
        return accountRepository.save(accountToUpdate);
    }
}
