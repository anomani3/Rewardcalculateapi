package com.rewards.rewardcalculate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rewards.rewardcalculate.controller.RewardController;
import com.rewards.rewardcalculate.dto.RewardResponse;
import com.rewards.rewardcalculate.dto.TransactionRequest;
import com.rewards.rewardcalculate.model.Transaction;
import com.rewards.rewardcalculate.service.RewardService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RewardController.class)
class RewardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RewardService rewardService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testAddTransaction_Success() throws Exception {
        TransactionRequest request = new TransactionRequest();
        request.setCustomerId("CUST1001");
        request.setAmount(120);
        request.setTransactionDate(LocalDate.now().minusDays(1));

        mockMvc.perform(post("/api/rewards/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Transaction recorded successfully"));
    }

    @Test
    void testAddTransaction_FutureDate_BadRequest() throws Exception {
        TransactionRequest request = new TransactionRequest();
        request.setCustomerId("CUST1002");
        request.setAmount(150);
        request.setTransactionDate(LocalDate.now().plusDays(1));

        doThrow(new IllegalArgumentException("Transaction date cannot be in the future"))
                .when(rewardService).add(any(TransactionRequest.class));

        mockMvc.perform(post("/api/rewards/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Transaction date cannot be in the future"));
    }

    @Test
    void testGetCustomerRewards_Success() throws Exception {
        Map<String, Integer> monthly = new HashMap<>();
        monthly.put("2025-04", 90);
        RewardResponse response = new RewardResponse("CUST1003", monthly, 90);

        Mockito.when(rewardService.calc("CUST1003")).thenReturn(response);

        mockMvc.perform(get("/api/rewards/CUST1003"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value("CUST1003"))
                .andExpect(jsonPath("$.totalPoints").value(90));
    }

    @Test
    void testGetCustomerRewards_NotFound() throws Exception {
        Mockito.when(rewardService.calc("UNKNOWN"))
                .thenThrow(new NoSuchElementException("Customer not found"));

        mockMvc.perform(get("/api/rewards/UNKNOWN"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Customer not found"));
    }

    @Test
    void testGetAllTransactions_Success() throws Exception {
        TransactionRequest tx = new TransactionRequest();
        tx.setCustomerId("CUST1004");
        tx.setAmount(100);
        tx.setTransactionDate(LocalDate.now().minusDays(3));

        Map<String, List<TransactionRequest>> transactions = Map.of("CUST1004", List.of(tx));

        Mockito.when(rewardService.getAll()).thenReturn((List<Transaction>) transactions);

        mockMvc.perform(get("/api/rewards/transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.CUST1004[0].amount").value(100));
    }
}
