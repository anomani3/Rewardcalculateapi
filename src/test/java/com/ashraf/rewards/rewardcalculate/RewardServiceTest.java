//package com.ashraf.rewards.rewardcalculate;
//
//import com.ashraf.rewards.rewardcalculate.dto.RewardResponse;
//import com.ashraf.rewards.rewardcalculate.dto.TransactionRequest;
//import com.ashraf.rewards.rewardcalculate.service.RewardService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.time.LocalDate;
//import java.util.NoSuchElementException;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class RewardServiceTest {
//
//    private RewardService rewardService;
//
//    @BeforeEach
//    void setup() {
//        rewardService = new RewardService();
//
//        // Add multiple transactions for multiple customers
//        // Transactions spread over last 3 months and outside
//
//        // Customer 1 - multiple valid transactions within 3 months
//        rewardService.add(new TransactionRequest("CUST001", 120, LocalDate.now().minusMonths(1))); // 90 pts
//        rewardService.add(new TransactionRequest("CUST001", 80, LocalDate.now().minusMonths(2)));  // 30 pts
//        rewardService.add(new TransactionRequest("CUST001", 200, LocalDate.now().minusMonths(4))); // Outside 3-month window, ignored
//
//        // Customer 2 - transactions within 3 months
//        rewardService.add(new TransactionRequest("CUST002", 150, LocalDate.now().minusMonths(1).minusDays(5))); // 150 pts
//        rewardService.add(new TransactionRequest("CUST002", 70, LocalDate.now().minusMonths(3).plusDays(1)));   // 20 pts
//
//        // Customer 3 - no transactions added (test empty scenario)
//    }
//
//    @Test
//    void testCustomer1RewardCalculation() {
//        RewardResponse response = rewardService.calc("CUST001");
//
//        assertEquals("CUST001", response.getCustomerId());
//        assertFalse(response.getMonthlyPoints().isEmpty());
//        assertTrue(response.getTotalPoints() > 0);
//
//        // Check total points: 90 + 30 = 120 (ignores outside 3 month transaction)
//        assertEquals(120, response.getTotalPoints());
//    }
//
//    @Test
//    void testCustomer2RewardCalculation() {
//        RewardResponse response = rewardService.calc("CUST002");
//
//        assertEquals("CUST002", response.getCustomerId());
//        assertFalse(response.getMonthlyPoints().isEmpty());
//
//        // Total points: 150 + 20 = 170
//        assertEquals(170, response.getTotalPoints());
//    }
//
//    @Test
//    void testCustomerWithoutTransactions() {
//        // This customer not added, should throw exception
//        assertThrows(NoSuchElementException.class, () -> rewardService.calc("CUST003"));
//    }
//}
