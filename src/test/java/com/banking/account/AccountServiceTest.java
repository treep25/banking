package com.banking.account;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepositoryMock;

    @InjectMocks
    private AccountService accountServiceMock;

    @Test
    void testCreateAccount_WhenAccountDoesNotExist_ShouldCreateNewAccount() {
        // given
        AccountCreateDto accountCreateDto = new AccountCreateDto("123456", BigDecimal.valueOf(1000));
        when(accountRepositoryMock.findByAccountNumber("123456")).thenReturn(null);
        when(accountRepositoryMock.save(any(Account.class))).thenReturn(Account.builder()
                .accountNumber("123456")
                .balance(BigDecimal.valueOf(1000))
                .build());

        // when
        Account newAccount = accountServiceMock.createAccount(accountCreateDto);

        // then
        assertEquals("123456", newAccount.getAccountNumber());
        assertEquals(BigDecimal.valueOf(1000), newAccount.getBalance());
        verify(accountRepositoryMock, times(1)).save(any(Account.class));
    }

    @Test
    void testCreateAccount_WhenAccountAlreadyExists_ShouldThrowException() {
        // given
        AccountCreateDto accountCreateDto = new AccountCreateDto("123456", BigDecimal.valueOf(1000));
        when(accountRepositoryMock.findByAccountNumber("123456")).thenReturn(Account.builder()
                .accountNumber("123456")
                .balance(BigDecimal.valueOf(1000))
                .build());

        // then
        assertThrows(RuntimeException.class, () -> accountServiceMock.createAccount(accountCreateDto));
        verify(accountRepositoryMock, never()).save(any(Account.class));
    }

    @Test
    void testGetAccountInfo_WhenAccountExists_ShouldReturnAccount() {
        // given
        when(accountRepositoryMock.findByAccountNumber("123456")).thenReturn(Account.builder()
                .accountNumber("123456")
                .balance(BigDecimal.valueOf(1000))
                .build());

        // when
        Account account = accountServiceMock.getAccountInfo("123456");

        // then
        assertEquals("123456", account.getAccountNumber());
        assertEquals(BigDecimal.valueOf(1000), account.getBalance());
        verify(accountRepositoryMock, times(1)).findByAccountNumber("123456");
    }

    @Test
    void testGetAccountInfo_WhenAccountDoesNotExist_ShouldThrowException() {
        // given
        when(accountRepositoryMock.findByAccountNumber("123456")).thenReturn(null);

        // then
        assertThrows(RuntimeException.class, () -> accountServiceMock.getAccountInfo("123456"));
        verify(accountRepositoryMock, times(1)).findByAccountNumber("123456");
    }

    @Test
    void testGetAllAccounts_ShouldReturnListOfAccounts() {
        // given
        List<Account> accounts = Arrays.asList(
                Account.builder().accountNumber("123456").balance(BigDecimal.valueOf(1000)).build(),
                Account.builder().accountNumber("654321").balance(BigDecimal.valueOf(2000)).build()
        );
        when(accountRepositoryMock.findAll()).thenReturn(accounts);

        // when
        List<Account> allAccounts = accountServiceMock.getAllAccounts();

        // then
        assertEquals(2, allAccounts.size());
        assertEquals("123456", allAccounts.get(0).getAccountNumber());
        assertEquals("654321", allAccounts.get(1).getAccountNumber());
        verify(accountRepositoryMock, times(1)).findAll();
    }

    @Test
    void testUpdateAccount_WhenAccountExists_ShouldUpdateAccount() {
        // given
        Account account = Account.builder()
                .accountNumber("123456")
                .balance(BigDecimal.valueOf(1000))
                .build();
        when(accountRepositoryMock.save(any(Account.class))).thenReturn(account);

        // when
        Account updatedAccount = accountServiceMock.updateAccount(account);

        // then
        assertEquals("123456", updatedAccount.getAccountNumber());
        assertEquals(BigDecimal.valueOf(1000), updatedAccount.getBalance());
        verify(accountRepositoryMock, times(1)).save(account);
    }
}