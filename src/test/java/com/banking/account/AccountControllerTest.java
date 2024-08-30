package com.banking.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AccountService accountServiceMock;

    @InjectMocks
    private AccountController accountControllerMock;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(accountControllerMock).build();
    }

    @Test
    void testCreateAccount_WhenValidInput_ShouldReturnCreatedAccount() throws Exception {
        // given
        AccountCreateDto accountCreateDto = new AccountCreateDto("123456", BigDecimal.valueOf(1000));
        Account account = Account.builder()
                .accountNumber("123456")
                .balance(BigDecimal.valueOf(1000))
                .build();

        when(accountServiceMock.createAccount(any(AccountCreateDto.class))).thenReturn(account);

        // when & then
        mockMvc.perform(post("/api/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountCreateDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accountNumber").value("123456"))
                .andExpect(jsonPath("$.balance").value(1000));

        verify(accountServiceMock, times(1)).createAccount(any(AccountCreateDto.class));
    }

    @Test
    void testCreateAccount_WhenInvalidAccountNumber_ShouldReturnBadRequest() throws Exception {
        // given
        AccountCreateDto accountCreateDto = new AccountCreateDto("", BigDecimal.valueOf(1000));

        // when & then
        mockMvc.perform(post("/api/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountCreateDto)))
                .andExpect(status().isBadRequest());

        verify(accountServiceMock, never()).createAccount(any(AccountCreateDto.class));
    }

    @Test
    void testCreateAccount_WhenInvalidInitialBalance_ShouldReturnBadRequest() throws Exception {
        // given
        AccountCreateDto accountCreateDto = new AccountCreateDto("123456", BigDecimal.ZERO);

        // when & then
        mockMvc.perform(post("/api/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountCreateDto)))
                .andExpect(status().isBadRequest());

        verify(accountServiceMock, never()).createAccount(any(AccountCreateDto.class));
    }

    @Test
    void testGetAccountInfo_WhenAccountExists_ShouldReturnAccount() throws Exception {
        // given
        Account account = Account.builder()
                .accountNumber("123456")
                .balance(BigDecimal.valueOf(1000))
                .build();

        when(accountServiceMock.getAccountInfo("123456")).thenReturn(account);

        // when & then
        mockMvc.perform(get("/api/v1/accounts/123456")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber").value("123456"))
                .andExpect(jsonPath("$.balance").value(1000));

        verify(accountServiceMock, times(1)).getAccountInfo("123456");
    }

    @Test
    void testGetAccountInfo_WhenInvalidAccountNumber_ShouldReturnBadRequest() throws Exception {
        // when & then
        mockMvc.perform(get("/api/v1/accounts/ ")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(accountServiceMock, never()).getAccountInfo(anyString());
    }

    @Test
    void testGetAccountInfo_WhenAccountDoesNotExist_ShouldThrowException() {
        // given
        when(accountServiceMock.getAccountInfo("123456")).thenThrow(new RuntimeException("Account not found"));

        // then
        assertThrows(ServletException.class, () ->
                mockMvc.perform(get("/api/v1/accounts/123456")
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNotFound())
        );

        verify(accountServiceMock, times(1)).getAccountInfo("123456");
    }

    @Test
    void testGetAllAccounts_ShouldReturnListOfAccounts() throws Exception {
        // given
        List<Account> accounts = Arrays.asList(
                Account.builder().accountNumber("123456").balance(BigDecimal.valueOf(1000)).build(),
                Account.builder().accountNumber("654321").balance(BigDecimal.valueOf(2000)).build()
        );

        when(accountServiceMock.getAllAccounts()).thenReturn(accounts);

        // when & then
        mockMvc.perform(get("/api/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].accountNumber").value("123456"))
                .andExpect(jsonPath("$[1].accountNumber").value("654321"));

        verify(accountServiceMock, times(1)).getAllAccounts();
    }
}