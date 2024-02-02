package com.booleanuk.core;

import java.util.ArrayList;

public class Item {
	String id;
	String name;
	double price;
	String type="Unknown";
	public Item (String id,String name, double price){
		this.id=id;
		this.name=name;
		this.price=price;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public double getPrice() {
		return price;
	}

	public String getType() {
		return type;
	}
}
