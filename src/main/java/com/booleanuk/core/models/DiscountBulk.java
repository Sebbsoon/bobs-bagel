package com.booleanuk.core.models;

public class DiscountBulk {
	private String id;
	private int bulk;
	double amount;
	public DiscountBulk(String id, int bulk,double amount) {
		this.id=id;
		this.bulk=bulk;
		this.amount=amount;
	}

	public String getId() {
		return id;
	}

	public int getBulk() {
		return bulk;
	}

	public double getAmount() {
		return amount;
	}
}
