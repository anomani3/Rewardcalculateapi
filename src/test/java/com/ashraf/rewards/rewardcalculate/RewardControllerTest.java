//package com.ashraf.rewards.rewardcalculate;
//
//import com.ashraf.rewards.rewardcalculate.dto.TransactionRequest;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.time.LocalDate;
//import java.util.*;
//
//import com.ashraf.rewards.rewardcalculate.controller.RewardController;
//import com.ashraf.rewards.rewardcalculate.dto.RewardResponse;
//import com.ashraf.rewards.rewardcalculate.service.RewardService;
//
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(RewardController.class)
//public class RewardControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @MockBean
//    private RewardService rewardService;
//
//    private TransactionRequest transaction;
//
//    @BeforeEach
//    void setup() {
//        transaction = new TransactionRequest();
//        transaction.setCustomerId("CUST001");
//        transaction.setTransactionDate(LocalDate.now().minusMonths(1));
//        transaction.setAmount(120);
//    }
//
//    @Test
//    void testAddTransaction() throws Exception {
//        mockMvc.perform(post("/api/rewards/transaction")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(transaction)))
//                .andExpect(status().isOk())
//                .andExpect(content().string("Recorded"));
//
//        verify(rewardService, times(1)).add(any(TransactionRequest.class));
//    }
//
//    @Test
//    void testGetCustomerRewards() throws Exception {
//        Map<String, Integer> monthlyPoints = new HashMap<>();
//        monthlyPoints.put(LocalDate.now().minusMonths(1).format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM")), 90);
//
//        RewardResponse response = new RewardResponse("CUST001", monthlyPoints, 90);
//
//        when(rewardService.calc("CUST001")).thenReturn(response);
//
//        mockMvc.perform(get("/api/rewards/CUST001"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.customerId").value("CUST001"))
//                .andExpect(jsonPath("$.totalPoints").value(90))
//                .andExpect(jsonPath("$.monthlyPoints.*").isNotEmpty());
//    }
//
//    @Test
//    void testGetCustomerRewardsNotFound() throws Exception {
//        when(rewardService.calc("UNKNOWN")).thenThrow(new NoSuchElementException("Customer not found: UNKNOWN"));
//
//        mockMvc.perform(get("/api/rewards/UNKNOWN"))
//                .andExpect(status().isInternalServerError());
//    }
//
//    @Test
//    void testGetAllTransactions() throws Exception {
//        Map<String, List<TransactionRequest>> transactions = new HashMap<>();
//        transactions.put("CUST001", Collections.singletonList(transaction));
//
//        when(rewardService.getAllTransactions()).thenReturn(transactions);
//
//        mockMvc.perform(get("/api/rewards/transactions"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.CUST001").isArray());
//    }
//
//    @Test
//    void testInvalidTransaction() throws Exception {
//        TransactionRequest invalid = new TransactionRequest();
//
//        mockMvc.perform(post("/api/rewards/transaction")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(invalid)))
//                .andExpect(status().isBadRequest());
//    }
//}
