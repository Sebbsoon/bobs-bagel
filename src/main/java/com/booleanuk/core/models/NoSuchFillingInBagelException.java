package com.booleanuk.core.models;

public class NoSuchFillingInBagelException extends Exception {
	public NoSuchFillingInBagelException(String id) {
		super("No filling with id" + id + " in bagel.");
	}
}
