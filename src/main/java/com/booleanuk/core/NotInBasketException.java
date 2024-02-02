package com.booleanuk.core;

public class NotInBasketException extends Exception{
	public NotInBasketException(int id){
		super("No item at index: "+id+" in basket.");
	}
}
