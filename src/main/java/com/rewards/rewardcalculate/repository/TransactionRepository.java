package com.rewards.rewardcalculate.repository;

import com.rewards.rewardcalculate.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

/**
 * Repository interface for accessing and managing {@link Transaction} entities in the database.
 * <p>
 * This interface extends {@link JpaRepository} to provide standard CRUD operations
 * and defines custom query methods for retrieving transactions based on customer ID and date range.
 */
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    /**
     * Finds all transactions for a given customer within a specified date range.
     *
     * @param customerId the ID of the customer whose transactions are to be retrieved
     * @param start      the start date (inclusive) of the transaction date range
     * @param end        the end date (inclusive) of the transaction date range
     * @return a list of matching {@link Transaction} objects
     */
    List<Transaction> findByCustomerIdAndTransactionDateBetween(String customerId, LocalDate start, LocalDate end);

    /**
     * Finds all transactions for a given customer ID.
     */
    List<Transaction> findByCustomerId(String customerId);
}
