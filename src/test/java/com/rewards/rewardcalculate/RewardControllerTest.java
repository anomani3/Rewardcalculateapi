package com.rewards.rewardcalculate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rewards.rewardcalculate.controller.RewardController;
import com.rewards.rewardcalculate.dto.RewardResponse;
import com.rewards.rewardcalculate.dto.TransactionRequest;
import com.rewards.rewardcalculate.service.RewardService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RewardController.class)
public class RewardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RewardService rewardService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testAddTransactionSuccess() throws Exception {
        TransactionRequest request = new TransactionRequest("CUST100", 120, LocalDate.now().minusDays(2));

        mockMvc.perform(post("/api/rewards/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Transaction recorded successfully"));
    }

    @Test
    void testAddTransactionFutureDate() throws Exception {
        TransactionRequest request = new TransactionRequest("CUST100", 100, LocalDate.now().plusDays(2));

        Mockito.doThrow(new IllegalArgumentException("Transaction date cannot be in the future"))
                .when(rewardService).add(any(TransactionRequest.class));

        mockMvc.perform(post("/api/rewards/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Transaction date cannot be in the future")));
    }

    @Test
    void testGetCustomerRewardsSuccess() throws Exception {
        RewardResponse response = new RewardResponse("CUST100", Map.of("2025-04", 90), 90);

        Mockito.when(rewardService.calc("CUST100")).thenReturn(response);

        mockMvc.perform(get("/api/rewards/CUST100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerId").value("CUST100"))
                .andExpect(jsonPath("$.totalPoints").value(90))
                .andExpect(jsonPath("$.monthlyPoints.2025-04").value(90));
    }

    @Test
    void testGetCustomerRewardsNotFound() throws Exception {
        Mockito.when(rewardService.calc("UNKNOWN"))
                .thenThrow(new NoSuchElementException("Customer not found"));

        mockMvc.perform(get("/api/rewards/UNKNOWN"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Customer not found")));
    }

    @Test
    void testGetAllTransactionsSuccess() throws Exception {
        TransactionRequest tr = new TransactionRequest("CUST100", 150, LocalDate.now().minusDays(10));
        Map<String, List<TransactionRequest>> data = Map.of("CUST100", List.of(tr));

        Mockito.when(rewardService.getAll()).thenReturn(data);

        mockMvc.perform(get("/api/rewards/transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.CUST100[0].amount").value(150));
    }
}
