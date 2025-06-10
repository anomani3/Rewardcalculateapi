package com.ashraf.rewards.rewardcalculate;

import com.ashraf.rewards.rewardcalculate.dto.RewardResponse;
import com.ashraf.rewards.rewardcalculate.dto.TransactionRequest;
import com.ashraf.rewards.rewardcalculate.service.RewardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class RewardServiceTest {

    private RewardService svc;

    @BeforeEach
    void setUp() {
        svc = new RewardService();
    }

    //  Positive: Add valid transaction
    @Test
    void testAddTransactionSuccess() {
        TransactionRequest t = new TransactionRequest();
        t.setCustomerId("CUST001");
        t.setAmount(120);
        t.setTransactionDate(LocalDate.now().minusDays(1));

        assertDoesNotThrow(() -> svc.add(t));
    }

    //  Negative: Add transaction with null date
    @Test
    void testAddTransactionWithNullDate() {
        TransactionRequest t = new TransactionRequest();
        t.setCustomerId("CUST002");
        t.setAmount(100);
        t.setTransactionDate(null);

        Exception ex = assertThrows(IllegalArgumentException.class, () -> svc.add(t));
        assertEquals("Transaction date cannot be null", ex.getMessage());
    }

    //  Negative: Add transaction with future date
    @Test
    void testAddTransactionWithFutureDate() {
        TransactionRequest t = new TransactionRequest();
        t.setCustomerId("CUST003");
        t.setAmount(100);
        t.setTransactionDate(LocalDate.now().plusDays(1));

        Exception ex = assertThrows(IllegalArgumentException.class, () -> svc.add(t));
        assertTrue(ex.getMessage().contains("Transaction date cannot be in the future"));
    }

    //  Positive: Calculate rewards with valid data
    @Test
    void testCalcRewardsWithValidData() {
        TransactionRequest t1 = new TransactionRequest("CUST004", 120, LocalDate.now().minusMonths(1));
        TransactionRequest t2 = new TransactionRequest("CUST004", 100, LocalDate.now().minusMonths(2));
        TransactionRequest t3 = new TransactionRequest("CUST004", 30, LocalDate.now().minusMonths(3)); // No reward

        svc.add(t1);
        svc.add(t2);
        svc.add(t3);

        RewardResponse response = svc.calc("CUST004");

        assertEquals("CUST004", response.getCustomerId());
        assertEquals(140, response.getTotalPoints()); // 90 + 50
        Map<String, Integer> monthly = response.getMonthlyPoints();
        assertEquals(2, monthly.values().stream().filter(p -> p > 0).count()); // Only 2 months should have points
    }

    //  Negative: Calculate rewards for non-existing customer
    @Test
    void testCalcRewardsForUnknownCustomer() {
        Exception ex = assertThrows(NoSuchElementException.class, () -> svc.calc("UNKNOWN"));
        assertEquals("Customer not found: UNKNOWN", ex.getMessage());
    }

    //  Positive: Get all transactions
    @Test
    void testGetAllTransactions() {
        TransactionRequest t = new TransactionRequest("CUST005", 150, LocalDate.now().minusMonths(1));
        svc.add(t);

        Map<String, java.util.List<TransactionRequest>> all = svc.getAllTransactions();

        assertTrue(all.containsKey("CUST005"));
        assertEquals(1, all.get("CUST005").size());
        assertEquals(150, all.get("CUST005").get(0).getAmount());
    }
}
