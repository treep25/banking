package com.banking.transaction;

import com.banking.account.Account;
import com.banking.account.AccountService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {
    @Mock
    private AccountService accountServiceMock;

    @Mock
    private TransactionRepository transactionRepositoryMock;

    @InjectMocks
    private TransactionService transactionServiceMock;

    @Test
    void testDeposit_WhenSuccess_Should() {
        //given
        Account account = Account.builder().accountNumber("123456").balance(BigDecimal.valueOf(1000)).build();

        when(accountServiceMock.getAccount("123456")).thenReturn(account);
        when(accountServiceMock.updateAccount(any(Account.class))).thenReturn(account);

        //when
        transactionServiceMock.deposit("123456", BigDecimal.valueOf(200));

        //then
        verify(accountServiceMock, times(1)).getAccount("123456");
        verify(accountServiceMock, times(1)).updateAccount(any(Account.class));
        verify(transactionRepositoryMock, times(1)).save(any(Transaction.class));
    }

    @Test
    void testWithdraw_WhenSuccess_Should() {
        //given
        Account account = Account.builder().accountNumber("123456").balance(BigDecimal.valueOf(1000)).build();

        when(accountServiceMock.getAccount("123456")).thenReturn(account);
        when(accountServiceMock.updateAccount(any(Account.class))).thenReturn(account);

        //when
        transactionServiceMock.withdraw("123456", BigDecimal.valueOf(500));

        //then
        verify(accountServiceMock, times(1)).getAccount("123456");
        verify(accountServiceMock, times(1)).updateAccount(any(Account.class));
        verify(transactionRepositoryMock, times(1)).save(any(Transaction.class));
    }

    @Test
    void testWithdraw_WhenInsufficientFunds() {
        //given
        Account account = Account.builder().accountNumber("123456").balance(BigDecimal.valueOf(1000)).build();

        when(accountServiceMock.getAccount("123456")).thenReturn(account);

        //then
        assertThrows(IllegalArgumentException.class, () ->
                transactionServiceMock.withdraw("123456", BigDecimal.valueOf(2000))
        );

        verify(accountServiceMock, times(1)).getAccount("123456");
        verify(accountServiceMock, never()).updateAccount(any(Account.class));
        verify(transactionRepositoryMock, never()).save(any(Transaction.class));
    }

    @Test
    void testTransfer_WhenSuccess() {
        //given
        Account accountFrom = Account.builder().accountNumber("123456").balance(BigDecimal.valueOf(1000)).build();
        Account accountTo = Account.builder().accountNumber("654321").balance(BigDecimal.valueOf(500)).build();

        when(accountServiceMock.getAccount("123456")).thenReturn(accountFrom);
        when(accountServiceMock.getAccount("654321")).thenReturn(accountTo);
        when(accountServiceMock.updateAccount(any(Account.class))).thenReturn(accountFrom).thenReturn(accountTo);

        //when
        transactionServiceMock.transfer("123456", "654321", BigDecimal.valueOf(300));

        //then
        verify(accountServiceMock, times(2)).getAccount("123456");
        verify(accountServiceMock, times(1)).getAccount("654321");
        verify(accountServiceMock, times(2)).updateAccount(any(Account.class));
        verify(transactionRepositoryMock, times(3)).save(any(Transaction.class));
    }
}