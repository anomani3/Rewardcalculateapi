package com.ashraf.rewards.rewardcalculate.service;

import com.ashraf.rewards.rewardcalculate.dto.RewardResponse;
import com.ashraf.rewards.rewardcalculate.dto.TransactionRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class RewardService {

  private final Map<String, List<TransactionRequest>> store = new HashMap<>();

  public void add(TransactionRequest t) {
    if (t.getTransactionDate() == null) {
      throw new IllegalArgumentException("Transaction date cannot be null");
    }

    if (t.getTransactionDate().isAfter(LocalDate.now())) {
      throw new IllegalArgumentException("Transaction date cannot be in the future: " + t.getTransactionDate());
    }

    store.computeIfAbsent(t.getCustomerId(), x -> new ArrayList<>()).add(t);
  }



  public RewardResponse calc(String customerId) {
    if (!store.containsKey(customerId)) {
      throw new NoSuchElementException("Customer not found: " + customerId);
    }

    Map<String, Integer> monthly = new TreeMap<>();
    int total = 0;
    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM");

    LocalDate now = LocalDate.now();
    // Get first day of the current month
    LocalDate firstOfThisMonth = now.withDayOfMonth(1);

    // Create the last 3 full months
    List<String> lastThreeMonths = new ArrayList<>();
    for (int i = 1; i <= 3; i++) {
      LocalDate month = firstOfThisMonth.minusMonths(i);
      lastThreeMonths.add(month.format(fmt));
      monthly.put(month.format(fmt), 0); // initialize with 0
    }

    List<TransactionRequest> transactions = store.get(customerId);

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

  private int calcPoints(double amount) {
    if (amount <= 50) return 0;
    if (amount <= 100) return (int)(amount - 50);
    return 50 + (int)((amount - 100) * 2);
  }

  public Map<String, List<TransactionRequest>> getAllTransactions() {
    return store;
  }

}
