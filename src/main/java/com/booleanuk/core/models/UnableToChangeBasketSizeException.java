package com.booleanuk.core.models;

public class UnableToChangeBasketSizeException extends Throwable {
	public UnableToChangeBasketSizeException(int newSize, int items) {
		super("Unable to change basket size to "+newSize+", "+items+" items in basket.");
	}
}
