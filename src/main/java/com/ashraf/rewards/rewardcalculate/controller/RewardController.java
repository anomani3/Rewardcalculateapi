package com.ashraf.rewards.rewardcalculate.controller;

import com.ashraf.rewards.rewardcalculate.dto.RewardResponse;
import com.ashraf.rewards.rewardcalculate.dto.TransactionRequest;
import com.ashraf.rewards.rewardcalculate.service.RewardService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rewards")
public class RewardController {

  private final RewardService svc;

  public RewardController(RewardService svc) {
    this.svc = svc;
  }

  @PostMapping("/transaction")
  public ResponseEntity<String> add(@Valid @RequestBody TransactionRequest t) {
    svc.add(t);
    return ResponseEntity.ok("Transaction recorded successfully");
  }

  @GetMapping("/{id}")
  public ResponseEntity<RewardResponse> getCustomerRewards(@PathVariable("id") String id) {
    return ResponseEntity.ok(svc.calc(id));
  }

  @GetMapping("/transactions")
  public ResponseEntity<Map<String, List<TransactionRequest>>> getAllTransactions() {
    return ResponseEntity.ok(svc.getAllTransactions());
  }
}
