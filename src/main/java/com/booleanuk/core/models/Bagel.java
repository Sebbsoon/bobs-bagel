package com.booleanuk.core.models;

public class Bagel extends Item {

	public Bagel(String id, String name, double price) {
		super(id, name, price);
		this.type="Bagel";
	}
}