package com.booleanuk.core;

import java.util.ArrayList;

public class DiscountCombo {
	String[] comboItems;
	double newPrice;

	public DiscountCombo(String[] comboItems, double newPrice) {
		this.comboItems=comboItems;
		this.newPrice=newPrice;

	}
	public String[] getComboItems() {
		return comboItems;
	}

	public double getNewPrice() {
		return newPrice;
	}
}
