package com.ashraf.rewards.rewardcalculate.dto;

import java.util.Map;

/**
 * RewardResponse is a Data Transfer Object (DTO) used to send
 * calculated reward points for a customer, including monthly breakdown and total.
 */
public class RewardResponse {

	// Unique identifier for the customer
	private String customerId;

	// A map containing reward points per month (e.g., "April 2025" -> 120 points)
	private Map<String, Integer> monthlyPoints;

	// Total reward points accumulated across all months
	private int totalPoints;

	// Default constructor
	public RewardResponse() {}

	/**
	 * Parameterized constructor to create a RewardResponse object.
	 *
	 * @param customerId   the ID of the customer
	 * @param monthlyPoints a map containing reward points per month
	 * @param totalPoints   total reward points across all months
	 */
	public RewardResponse(String customerId, Map<String, Integer> monthlyPoints, int totalPoints) {
		this.customerId = customerId;
		this.monthlyPoints = monthlyPoints;
		this.totalPoints = totalPoints;
	}

	// Getter for customerId
	public String getCustomerId() {
		return customerId;
	}

	// Getter for monthlyPoints
	public Map<String, Integer> getMonthlyPoints() {
		return monthlyPoints;
	}

	// Getter for totalPoints
	public int getTotalPoints() {
		return totalPoints;
	}

	// Setter for customerId
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	// Setter for monthlyPoints
	public void setMonthlyPoints(Map<String, Integer> monthlyPoints) {
		this.monthlyPoints = monthlyPoints;
	}

	// Setter for totalPoints
	public void setTotalPoints(int totalPoints) {
		this.totalPoints = totalPoints;
	}
}
