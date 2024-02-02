package com.booleanuk.core.models;

public class Coffee extends Item{

	public Coffee(String id, String name, double price) {
		super(id, name, price);
		this.type="Coffee";
	}

}
