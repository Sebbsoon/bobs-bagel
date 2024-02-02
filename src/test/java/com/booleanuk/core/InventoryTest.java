package com.booleanuk.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class InventoryTest {
	private Inventory inventory;

	@Test
	public void inInventoryTest() {
		inventory = new Inventory();
		Assertions.assertTrue(inventory.inInventory("BGLO"));
		Assertions.assertFalse(inventory.inInventory("NONEXISTENT"));
	}

	@Test
	public void getFillingsTest() {
		inventory = new Inventory();

		String expected = "Bacon:\t0.12\n" +
				"Egg:\t0.12\n" +
				"Cheese:\t0.12\n" +
				"Cream Cheese:\t0.12\n" +
				"Smoked Salmon:\t0.12\n" +
				"Ham:\t0.12";
		Assertions.assertEquals(expected, inventory.getFillings());
	}

	@Test
	public void getPriceTest() {
		inventory = new Inventory();
		Assertions.assertEquals(0.49, inventory.getPrice("BGLO"), 0.001);
		Assertions.assertEquals(0.0, inventory.getPrice("NONEXISTENT"), 0.001);
	}

	@Test
	public void getNameTest() {
		inventory = new Inventory();

		try {
			Assertions.assertEquals("Onion", inventory.getName("BGLO"));
			inventory.getName("NONEXISTENT");
			Assertions.fail("Expected NotInInventoryException was not thrown");
		} catch (NotInInventoryException e) {

		}
	}

	@Test
	public void hasDiscountBulkTest() {
		inventory = new Inventory();
		Assertions.assertTrue(inventory.hasDiscountBulk("BGLO"));
		Assertions.assertFalse(inventory.hasDiscountBulk("NONEXISTENT"));
	}

	@Test
	public void getDiscountComboPairsTest() {
		inventory = new Inventory();
		Assertions.assertEquals(1, inventory.getDiscountComboPairs().size());
		Assertions.assertArrayEquals(new String[]{"BGL", "COF"}, inventory.getDiscountComboPairs().get(0));
	}

	@Test
	public void getDiscountComboAmountTest() {
		inventory = new Inventory();
		double expectedAmount = 1.25;
		Assertions.assertEquals(expectedAmount, inventory.getDiscountComboAmount(new String[]{"BGL", "COF"}));
	}
}
