package com.rewards.rewardcalculate.service;

import com.rewards.rewardcalculate.dto.RewardResponse;
import com.rewards.rewardcalculate.dto.TransactionRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * RewardService handles transaction recording and reward point calculation logic.
 */
@Service
public class RewardService {

  // In-memory store: customerId -> list of their transactions
  private final Map<String, List<TransactionRequest>> store = new HashMap<>();

  /**
   * Adds a new transaction for a customer after validating it.
   *
   * @param t the transaction request
   */
  public void add(TransactionRequest t) {
    if (t.getTransactionDate() == null) {
      throw new IllegalArgumentException("Transaction date cannot be null");
    }

    if (t.getTransactionDate().isAfter(LocalDate.now())) {
      throw new IllegalArgumentException("Transaction date cannot be in the future: " + t.getTransactionDate());
    }

    store.computeIfAbsent(t.getCustomerId(), x -> new ArrayList<>()).add(t);
  }

  /**
   * Calculates reward points for a given customer within the last 3 full months.
   * Returns only the months with transaction data.
   *
   * @param customerId the customer ID
   * @return RewardResponse containing calculated rewards
   */
  public RewardResponse calc(String customerId) {
    if (!store.containsKey(customerId)) {
      throw new NoSuchElementException("Customer not found: " + customerId);
    }

    Map<String, Integer> monthly = new TreeMap<>(); // Keep months in order
    int total = 0;
    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM");

    LocalDate now = LocalDate.now();
    LocalDate firstOfThisMonth = now.withDayOfMonth(1);

    // Collect the last 3 full months (excluding current)
    List<String> lastThreeMonths = new ArrayList<>();
    for (int i = 1; i <= 3; i++) {
      LocalDate month = firstOfThisMonth.minusMonths(i);
      lastThreeMonths.add(month.format(fmt));
    }

    List<TransactionRequest> transactions = store.get(customerId);

    for (TransactionRequest t : transactions) {
      String transactionMonth = t.getTransactionDate().format(fmt);
      if (lastThreeMonths.contains(transactionMonth)) {
        int points = calcPoints(t.getAmount());
        monthly.put(transactionMonth, monthly.getOrDefault(transactionMonth, 0) + points);
        total += points;
      }
    }

    if (monthly.isEmpty()) {
      throw new IllegalArgumentException("Customer does not have transactions in the last 3 months");
    }

    return new RewardResponse(customerId, monthly, total);
  }

  /**
   * Calculates reward points based on transaction amount.
   * Rules:
   * - $0-$50 → 0 points
   * - $51-$100 → 1 point per dollar over 50
   * - $101+ → 1 point per dollar over 50, plus 1 extra per dollar over 100
   */
  private int calcPoints(double amount) {
    if (amount <= 50) return 0;
    if (amount <= 100) return (int) (amount - 50);
    return 50 + (int) ((amount - 100) * 2);
  }

  /**
   * Returns all transactions grouped by customer.
   */
  public Map<String, List<TransactionRequest>> getAllTransactions() {
    return store;
  }
}
