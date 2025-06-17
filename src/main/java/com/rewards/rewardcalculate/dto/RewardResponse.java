package com.rewards.rewardcalculate.dto;

import java.util.Map;

/**
 * Data Transfer Object (DTO) for sending reward calculation results.
 * <p>
 * This class encapsulates the reward information for a specific customer,
 * including reward points earned per month and the total points.
 */
public class RewardResponse {

	/** The ID of the customer */
	private String customerId;

	/** A map representing reward points earned per month (e.g., "2025-05" -> 120 points) */
	private Map<String, Integer> monthlyPoints;

	/** Total reward points accumulated by the customer */
	private int totalPoints;

	/**
	 * Default no-args constructor required for deserialization.
	 */
	public RewardResponse() {}

	/**
	 * Constructs a RewardResponse with the specified customer ID, monthly reward breakdown,
	 * and total reward points.
	 *
	 * @param customerId     the ID of the customer
	 * @param monthlyPoints  map of month-to-reward points
	 * @param totalPoints    total reward points accumulated
	 */
	public RewardResponse(String customerId, Map<String, Integer> monthlyPoints, int totalPoints) {
		this.customerId = customerId;
		this.monthlyPoints = monthlyPoints;
		this.totalPoints = totalPoints;
	}

	/**
	 * Returns the customer ID.
	 *
	 * @return the customer ID
	 */
	public String getCustomerId() {
		return customerId;
	}

	/**
	 * Returns the map of monthly reward points.
	 *
	 * @return map of month-to-points
	 */
	public Map<String, Integer> getMonthlyPoints() {
		return monthlyPoints;
	}

	/**
	 * Returns the total reward points accumulated by the customer.
	 *
	 * @return total reward points
	 */
	public int getTotalPoints() {
		return totalPoints;
	}
}
