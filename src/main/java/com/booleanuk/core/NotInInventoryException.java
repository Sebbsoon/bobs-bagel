package com.booleanuk.core;

public class NotInInventoryException extends Exception {
	public NotInInventoryException(String id) {
		super("No item with id:" + id + " in inventory.");
	}
}
