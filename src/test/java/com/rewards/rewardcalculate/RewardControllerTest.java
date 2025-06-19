//package com.rewards.rewardcalculate;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.rewards.rewardcalculate.controller.RewardController;
//import com.rewards.rewardcalculate.dto.RewardResponse;
//import com.rewards.rewardcalculate.dto.TransactionRequest;
//import com.rewards.rewardcalculate.model.Transaction;
//import com.rewards.rewardcalculate.service.RewardService;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Map;
//
//import static org.hamcrest.Matchers.containsString;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(RewardController.class)
//public class RewardControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private RewardService rewardService;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Test
//    void testAddTransaction_Success() throws Exception {
//        TransactionRequest req = new TransactionRequest("CUST123", LocalDate.now().minusDays(1), 120.0);
//
//        mockMvc.perform(post("/api/rewards/transaction")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(req)))
//                .andExpect(status().isOk())
//                .andExpect(content().string("Transaction recorded"));
//    }
//
//    @Test
//    void testAddTransaction_InvalidDate() throws Exception {
//        TransactionRequest req = new TransactionRequest("CUST123", LocalDate.now().plusDays(5), 80.0);
//
//        Mockito.doThrow(new IllegalArgumentException("Transaction date cannot be in the future"))
//                .when(rewardService).add(Mockito.any(TransactionRequest.class));
//
//        mockMvc.perform(post("/api/rewards/transaction")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(req)))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().string(containsString("Transaction date cannot be in the future")));
//    }
//
//    @Test
//    void testCalcCustomerRewards_Success() throws Exception {
//        Map<String, Integer> monthlyPoints = Map.of("2025-05", 90);
//        RewardResponse rewardResponse = new RewardResponse("CUST123", monthlyPoints, 90);
//
//        Mockito.when(rewardService.calculate("CUST123")).thenReturn(rewardResponse);
//
//        mockMvc.perform(get("/api/rewards/CUST123"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.customerId").value("CUST123"))
//                .andExpect(jsonPath("$.totalPoints").value(90));
//    }
//
//    @Test
//    void testCalcCustomerRewards_NotFound() throws Exception {
//        Mockito.when(rewardService.calculate("UNKNOWN"))
//                .thenThrow(new java.util.NoSuchElementException("Customer not found"));
//
//        mockMvc.perform(get("/api/rewards/UNKNOWN"))
//                .andExpect(status().isNotFound())
//                .andExpect(content().string(containsString("Customer not found")));
//    }
//
//    @Test
//    void testGetAllTransactions_Success() throws Exception {
//        Transaction t = new Transaction("CUST123", LocalDate.now().minusDays(2), 150);
//        List<Transaction> transactions = List.of(t);
//
//        Mockito.when(rewardService.getAll()).thenReturn(transactions);
//
//        mockMvc.perform(get("/api/rewards/transactions"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].customerId").value("CUST123"))
//                .andExpect(jsonPath("$[0].amount").value(150.0));
//    }
//}
