package com.rewards.rewardcalculate.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

/**
 * TransactionRequest is a DTO used for capturing incoming transaction data.
 * This data is used to calculate reward points for a customer.
 */
public class TransactionRequest {

	// The unique identifier for the customer who made the transaction
	@NotBlank(message = "Customer ID must not be blank")
	private String customerId;

	// The date on which the transaction occurred
	@NotNull(message = "Transaction date must not be null")
	private LocalDate transactionDate;

	// The amount spent in the transaction; must be 0 or greater
	@Min(value = 0, message = "Amount must be zero or positive")
	private double amount;

	/**
	 * Parameterized constructor (likely unused — doesn't initialize fields).
	 * You may update this if you plan to use it to set values directly.
	 */
	public TransactionRequest(String customerId, double amount, LocalDate transactionDate) {
		this.customerId = customerId;
		this.amount = amount;
		this.transactionDate = transactionDate;
	}

	// Default no-arg constructor required for deserialization (e.g., from JSON)
	public TransactionRequest() {}

	// Getter for customerId
	public String getCustomerId() {
		return customerId;
	}

	// Setter for customerId
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	// Getter for transactionDate
	public LocalDate getTransactionDate() {
		return transactionDate;
	}

	// Setter for transactionDate
	public void setTransactionDate(LocalDate transactionDate) {
		this.transactionDate = transactionDate;
	}

	// Getter for amount
	public double getAmount() {
		return amount;
	}

	// Setter for amount
	public void setAmount(double amount) {
		this.amount = amount;
	}
}
