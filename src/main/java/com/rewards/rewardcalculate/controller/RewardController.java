package com.rewards.rewardcalculate.controller;

import com.rewards.rewardcalculate.dto.RewardResponse;
import com.rewards.rewardcalculate.dto.TransactionRequest;
import com.rewards.rewardcalculate.model.Transaction;
import com.rewards.rewardcalculate.service.RewardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

/**
 * Controller class for handling reward-related HTTP requests.
 * Exposes endpoints for recording transactions, calculating customer rewards,
 * and retrieving all recorded transactions.
 */
@RestController
@RequestMapping("/api/rewards")
public class RewardController {

  private final RewardService svc;

  /**
   * Constructor-based dependency injection for RewardService.
   *
   * @param svc the RewardService instance to handle business logic
   */
  public RewardController(RewardService svc) {
    this.svc = svc;
  }

  /**
   * Endpoint to record a new customer transaction.
   *
   * @param req the transaction request payload (validated)
   * @return a success message if the transaction is recorded successfully
   */
  @PostMapping("/transaction")
  public ResponseEntity<String> add(@Valid @RequestBody TransactionRequest req) {
    svc.add(req);
    return ResponseEntity.ok("Transaction recorded");
  }

  /**
   * Endpoint to calculate and retrieve reward points for a specific customer.
   *
   * @param customerId the ID of the customer whose rewards are to be calculated
   * @return a RewardResponse containing monthly and total reward points
   */
  @GetMapping("/{customerId}")
  public ResponseEntity<RewardResponse> calc(@PathVariable String customerId) {
    return ResponseEntity.ok(svc.calc(customerId));
  }

  /**
   * Endpoint to retrieve all stored transactions across all customers.
   *
   * @return a list of all transactions in the system
   */
  @GetMapping("/transactions")
  public ResponseEntity<List<Transaction>> getAll() {
    return ResponseEntity.ok(svc.getAll());
  }
}
