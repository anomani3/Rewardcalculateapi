package com.ashraf.rewards.rewardcalculate.dto;

import java.util.Map;

public class RewardResponse {
	private String customerId;
	private Map<String, Integer> monthlyPoints;
	private int totalPoints;

	public RewardResponse() {}

	public RewardResponse(String customerId, Map<String, Integer> monthlyPoints, int totalPoints) {
		this.customerId = customerId;
		this.monthlyPoints = monthlyPoints;
		this.totalPoints = totalPoints;
	}

	public String getCustomerId() {
		return customerId;
	}

	public Map<String, Integer> getMonthlyPoints() {
		return monthlyPoints;
	}

	public int getTotalPoints() {
		return totalPoints;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public void setMonthlyPoints(Map<String, Integer> monthlyPoints) {
		this.monthlyPoints = monthlyPoints;
	}

	public void setTotalPoints(int totalPoints) {
		this.totalPoints = totalPoints;
	}
}
