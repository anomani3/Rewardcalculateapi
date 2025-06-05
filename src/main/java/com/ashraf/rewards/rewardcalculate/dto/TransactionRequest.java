package com.ashraf.rewards.rewardcalculate.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class TransactionRequest {

	@NotBlank
	private String customerId;

	@NotNull
	private LocalDate transactionDate;

	@Min(0)
	private double amount;

	public TransactionRequest(String cust001, double v, LocalDate now) {
	}

	public TransactionRequest() {

	}

	// Getters and Setters
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public LocalDate getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(LocalDate transactionDate) {
		this.transactionDate = transactionDate;
	}

	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
}
