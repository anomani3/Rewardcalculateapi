package com.rewards.rewardcalculate.service;

import com.rewards.rewardcalculate.dto.RewardResponse;
import com.rewards.rewardcalculate.dto.TransactionRequest;
import com.rewards.rewardcalculate.model.Transaction;
import com.rewards.rewardcalculate.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Service class responsible for handling business logic related to rewards.
 */
@Service
public class RewardService {

  private final TransactionRepository repo;

  public RewardService(TransactionRepository repo) {
    this.repo = repo;
  }

  /**
   * Adds a new transaction after validating the input data.
   *
   * @param req the transaction request
   */
  public void add(TransactionRequest req) {
    if (req.getTransactionDate() == null) {
      throw new IllegalArgumentException("Transaction date cannot be null");
    }
    if (req.getTransactionDate().isAfter(LocalDate.now())) {
      throw new IllegalArgumentException("Transaction date cannot be in the future");
    }

    Transaction tx = new Transaction(req.getCustomerId(), req.getTransactionDate(), req.getAmount());
    repo.save(tx);
  }

  /**
   * Calculates reward points for a given customer based on the last 3 full months.
   *
   * @param customerId the customer ID
   * @return reward response object
   */
  public RewardResponse calculateRewardsWithIn3Month(String customerId) {
    List<Transaction> transactions = repo.findByCustomerId(customerId);

    if (transactions.isEmpty()) {
      throw new NoSuchElementException("Customer not found or has no transactions: " + customerId);
    }

    LocalDate now = LocalDate.now();
    LocalDate firstOfThisMonth = now.withDayOfMonth(1);
    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM");

    List<String> lastThreeMonths = new ArrayList<>();
    for (int i = 1; i <= 3; i++) {
      lastThreeMonths.add(firstOfThisMonth.minusMonths(i).format(fmt));
    }

    Map<String, Integer> monthly = new TreeMap<>();
    int total = 0;

    for (String month : lastThreeMonths) {
      monthly.put(month, 0);
    }

    for (Transaction tx : transactions) {
      String txMonth = tx.getTransactionDate().format(fmt);
      if (monthly.containsKey(txMonth)) {
        int points = calculatePoints(tx.getAmount());
        monthly.put(txMonth, monthly.get(txMonth) + points);
        total += points;
      }
    }

    return new RewardResponse(customerId, monthly, total);
  }

  /**
   * Returns all transactions in the system.
   *
   * @return list of transactions
   */
  public List<Transaction> getAll() {
    return repo.findAll();
  }

  /**
   * Calculates reward points based on transaction amount.
   *
   * @param amount the transaction amount
   * @return reward points earned
   */
  private int calculatePoints(double amount) {
    if (amount <= 50) return 0;
    else if (amount <= 100) return (int) (amount - 50);
    else return 50 + (int) ((amount - 100) * 2);
  }
}
