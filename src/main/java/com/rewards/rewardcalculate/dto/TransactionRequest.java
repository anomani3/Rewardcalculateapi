package com.rewards.rewardcalculate.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * Data Transfer Object (DTO) representing a transaction request.
 * <p>
 * This class is used to receive transaction details from clients such as
 * customer ID, transaction date, and transaction amount.
 * It includes basic validation constraints to ensure data correctness.
 */
public class TransactionRequest {

    /**
     * ID of the customer who made the transaction.
     * This field must not be blank.
     */
    @NotBlank
    private String customerId;

    /**
     * Date when the transaction was made.
     * This field must not be null.
     */
    @NotNull
    private LocalDate transactionDate;

    /**
     * Amount spent in the transaction.
     * Must be zero or a positive number.
     */
    @Min(0)
    private double amount;

    /**
     * Default no-args constructor required for deserialization.
     */
    public TransactionRequest() {}

    /**
     * Constructs a TransactionRequest with the specified values.
     *
     * @param customerId       the ID of the customer
     * @param transactionDate  the date of the transaction
     * @param amount           the amount of the transaction
     */
    public TransactionRequest(String customerId, LocalDate transactionDate, double amount) {
        this.customerId = customerId;
        this.transactionDate = transactionDate;
        this.amount = amount;
    }

    /**
     * Returns the customer ID.
     *
     * @return customer ID
     */
    public String getCustomerId() {
        return customerId;
    }

    /**
     * Sets the customer ID.
     *
     * @param customerId the ID to set
     */
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    /**
     * Returns the transaction date.
     *
     * @return transaction date
     */
    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    /**
     * Sets the transaction date.
     *
     * @param transactionDate the date to set
     */
    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    /**
     * Returns the transaction amount.
     *
     * @return transaction amount
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Sets the transaction amount.
     *
     * @param amount the amount to set
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }
}
