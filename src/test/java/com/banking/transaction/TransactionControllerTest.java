package com.banking.transaction;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();
    }

    @Test
    void testDeposit_WhenValidInput_ShouldReturnOk() throws Exception {
        // given
        TransactionDto transactionDto = new TransactionDto("123456", null, BigDecimal.valueOf(200));

        // when & then
        mockMvc.perform(post("/api/v1/transactions/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionDto)))
                .andExpect(status().isOk());

        verify(transactionService, times(1)).deposit("123456", BigDecimal.valueOf(200));
    }

    @Test
    void testDeposit_WhenInvalidAccount_ShouldReturnBadRequest() throws Exception {
        // given
        TransactionDto transactionDto = new TransactionDto("", null, BigDecimal.valueOf(200));

        // when & then
        mockMvc.perform(post("/api/v1/transactions/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionDto)))
                .andExpect(status().isBadRequest());

        verify(transactionService, never()).deposit(anyString(), any(BigDecimal.class));
    }

    @Test
    void testDeposit_WhenInvalidAmount_ShouldReturnBadRequest() throws Exception {
        // given
        TransactionDto transactionDto = new TransactionDto("123456", null, BigDecimal.ZERO);

        // when & then
        mockMvc.perform(post("/api/v1/transactions/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionDto)))
                .andExpect(status().isBadRequest());

        verify(transactionService, never()).deposit(anyString(), any(BigDecimal.class));
    }

    @Test
    void testWithdraw_WhenValidInput_ShouldReturnOk() throws Exception {
        // given
        TransactionDto transactionDto = new TransactionDto("123456", null, BigDecimal.valueOf(200));

        // when & then
        mockMvc.perform(post("/api/v1/transactions/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionDto)))
                .andExpect(status().isOk());

        verify(transactionService, times(1)).withdraw("123456", BigDecimal.valueOf(200));
    }

    @Test
    void testWithdraw_WhenInvalidAccount_ShouldReturnBadRequest() throws Exception {
        // given
        TransactionDto transactionDto = new TransactionDto("", null, BigDecimal.valueOf(200));

        // when & then
        mockMvc.perform(post("/api/v1/transactions/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionDto)))
                .andExpect(status().isBadRequest());

        verify(transactionService, never()).withdraw(anyString(), any(BigDecimal.class));
    }

    @Test
    void testWithdraw_WhenInvalidAmount_ShouldReturnBadRequest() throws Exception {
        // given
        TransactionDto transactionDto = new TransactionDto("123456", null, BigDecimal.ZERO);

        // when & then
        mockMvc.perform(post("/api/v1/transactions/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionDto)))
                .andExpect(status().isBadRequest());

        verify(transactionService, never()).withdraw(anyString(), any(BigDecimal.class));
    }

    @Test
    void testTransfer_WhenValidInput_ShouldReturnOk() throws Exception {
        // given
        TransactionDto transactionDto = new TransactionDto("123456", "654321", BigDecimal.valueOf(200));

        // when & then
        mockMvc.perform(post("/api/v1/transactions/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionDto)))
                .andExpect(status().isOk());

        verify(transactionService, times(1)).transfer("123456", "654321", BigDecimal.valueOf(200));
    }

    @Test
    void testTransfer_WhenInvalidAccount_ShouldReturnBadRequest() throws Exception {
        // given
        TransactionDto transactionDto = new TransactionDto("", "654321", BigDecimal.valueOf(200));

        // when & then
        mockMvc.perform(post("/api/v1/transactions/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionDto)))
                .andExpect(status().isBadRequest());

        verify(transactionService, never()).transfer(anyString(), anyString(), any(BigDecimal.class));
    }

    @Test
    void testTransfer_WhenInvalidAmount_ShouldReturnBadRequest() throws Exception {
        // given
        TransactionDto transactionDto = new TransactionDto("123456", "654321", BigDecimal.ZERO);

        // when & then
        mockMvc.perform(post("/api/v1/transactions/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionDto)))
                .andExpect(status().isBadRequest());

        verify(transactionService, never()).transfer(anyString(), anyString(), any(BigDecimal.class));
    }

    @Test
    void testTransfer_WhenMissingToAccount_ShouldReturnBadRequest() throws Exception {
        // given
        TransactionDto transactionDto = new TransactionDto("123456", "", BigDecimal.valueOf(200));

        // when & then
        mockMvc.perform(post("/api/v1/transactions/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionDto)))
                .andExpect(status().isBadRequest());

        verify(transactionService, never()).transfer(anyString(), anyString(), any(BigDecimal.class));
    }
}