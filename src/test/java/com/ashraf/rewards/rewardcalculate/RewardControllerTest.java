package com.ashraf.rewards.rewardcalculate;

import com.ashraf.rewards.rewardcalculate.controller.RewardController;
import com.ashraf.rewards.rewardcalculate.dto.RewardResponse;
import com.ashraf.rewards.rewardcalculate.dto.TransactionRequest;
import com.ashraf.rewards.rewardcalculate.service.RewardService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

/**
 * Unit tests for the RewardController using Spring's MockMvc.
 * It validates the behavior of REST endpoints under various scenarios.
 */
@WebMvcTest(RewardController.class)
class RewardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RewardService svc;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Test case: Valid transaction is added successfully.
     * Expects HTTP 200 and success message in response.
     */
    @Test
    void testAddTransactionSuccess() throws Exception {
        TransactionRequest t = new TransactionRequest();
        t.setCustomerId("CUST001");
        t.setTransactionDate(LocalDate.now().minusDays(1));
        t.setAmount(120);

        mockMvc.perform(post("/api/rewards/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(t)))
                .andExpect(status().isOk())
                .andExpect(content().string("Transaction recorded successfully"));
    }

    /**
     * Test case: Adding a transaction with a future date.
     * Expects HTTP 400 and appropriate error message.
     */
    @Test
    void testAddTransactionFutureDate() throws Exception {
        TransactionRequest t = new TransactionRequest();
        t.setCustomerId("CUST001");
        t.setTransactionDate(LocalDate.now().plusDays(1));
        t.setAmount(100);

        doThrow(new IllegalArgumentException("Transaction date cannot be in the future"))
                .when(svc).add(any(TransactionRequest.class));

        mockMvc.perform(post("/api/rewards/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(t)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Transaction date cannot be in the future")));
    }

    /**
     * Test case: Fetch reward points for a known customer.
     * Expects HTTP 200 and JSON containing reward data.
     */
    @Test
    void testGetCustomerRewardsSuccess() throws Exception {
        Map<String, Integer> monthlyPoints = Map.of("2025-04", 90);
        RewardResponse response = new RewardResponse("CUST001", monthlyPoints, 90);

        Mockito.when(svc.calc("CUST001")).thenReturn(response);

        mockMvc.perform(get("/api/rewards/CUST001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value("CUST001"))
                .andExpect(jsonPath("$.totalPoints").value(90));
    }

    /**
     * Test case: Requesting rewards for a non-existent customer.
     * Expects HTTP 404 and error message.
     */
    @Test
    void testGetCustomerRewardsNotFound() throws Exception {
        Mockito.when(svc.calc("UNKNOWN"))
                .thenThrow(new NoSuchElementException("Customer not found"));

        mockMvc.perform(get("/api/rewards/UNKNOWN"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Customer not found")));
    }

    /**
     * Test case: Fetch all transactions.
     * Expects HTTP 200 and JSON containing customer transaction data.
     */
    @Test
    void testGetAllTransactions() throws Exception {
        TransactionRequest t = new TransactionRequest();
        t.setCustomerId("CUST001");
        t.setTransactionDate(LocalDate.now().minusDays(5));
        t.setAmount(100);

        Map<String, List<TransactionRequest>> map = Map.of("CUST001", List.of(t));

        Mockito.when(svc.getAllTransactions()).thenReturn(map);

        mockMvc.perform(get("/api/rewards/transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.CUST001[0].amount").value(100));
    }
}
