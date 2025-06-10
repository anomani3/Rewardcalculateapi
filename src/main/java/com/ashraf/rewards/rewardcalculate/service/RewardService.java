package com.ashraf.rewards.rewardcalculate.service;

import com.ashraf.rewards.rewardcalculate.dto.RewardResponse;
import com.ashraf.rewards.rewardcalculate.dto.TransactionRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * RewardService is the core business logic layer that manages customer transactions
 * and calculates reward points based on recent transaction history.
 */
@Service
public class RewardService {

  // In-memory data store to hold transactions grouped by customerId
  private final Map<String, List<TransactionRequest>> store = new HashMap<>();

  /**
   * Adds a new transaction to the in-memory store after validating its date.
   *
   * @param t the transaction to be added
   * @throws IllegalArgumentException if transaction date is null or in the future
   */
  public void add(TransactionRequest t) {
    if (t.getTransactionDate() == null) {
      throw new IllegalArgumentException("Transaction date cannot be null");
    }

    if (t.getTransactionDate().isAfter(LocalDate.now())) {
      throw new IllegalArgumentException("Transaction date cannot be in the future: " + t.getTransactionDate());
    }

    // Append transaction to the customer's list or create one if it doesn't exist
    store.computeIfAbsent(t.getCustomerId(), x -> new ArrayList<>()).add(t);
  }

  /**
   * Calculates the reward points for a customer over the last 3 full months.
   *
   * @param customerId the ID of the customer
   * @return RewardResponse containing monthly and total reward points
   * @throws NoSuchElementException if the customer is not found in the store
   */
  public RewardResponse calc(String customerId) {
    if (!store.containsKey(customerId)) {
      throw new NoSuchElementException("Customer not found: " + customerId);
    }

    Map<String, Integer> monthly = new TreeMap<>(); // Sorted map for chronological order
    int total = 0;
    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM");

    LocalDate now = LocalDate.now();
    LocalDate firstOfThisMonth = now.withDayOfMonth(1); // Start of current month

    // Generate list of the last 3 full months (excluding current)
    List<String> lastThreeMonths = new ArrayList<>();
    for (int i = 1; i <= 3; i++) {
      LocalDate month = firstOfThisMonth.minusMonths(i);
      String formattedMonth = month.format(fmt);
      lastThreeMonths.add(formattedMonth);
      monthly.put(formattedMonth, 0); // Initialize with 0 points
    }

    List<TransactionRequest> transactions = store.get(customerId);

    // Calculate reward points for each transaction in the last 3 months
    for (TransactionRequest t : transactions) {
      String transactionMonth = t.getTransactionDate().format(fmt);
      if (lastThreeMonths.contains(transactionMonth)) {
        int points = calcPoints(t.getAmount());
        monthly.put(transactionMonth, monthly.get(transactionMonth) + points);
        total += points;
      }
    }

    return new RewardResponse(customerId, monthly, total);
  }

  /**
   * Calculates reward points based on transaction amount.
   * - No points for <= $50
   * - 1 point per $ over 50 up to 100
   * - 2 points per $ over 100
   *
   * @param amount the amount spent in the transaction
   * @return the calculated reward points
   */
  private int calcPoints(double amount) {
    if (amount <= 50) return 0;
    else if (amount <= 100) {
      return (int) (amount - 50);
    }
    else
    return 50 + (int)((amount - 100) * 2);
  }

  /**
   * Returns all stored transactions for all customers.
   *
   * @return map of customerId to list of their transactions
   */
  public Map<String, List<TransactionRequest>> getAllTransactions() {
    return store;
  }

}
