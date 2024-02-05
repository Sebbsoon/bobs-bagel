package com.booleanuk.core;

import com.booleanuk.core.models.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class BasketTest {
Inventory inventory;
	@AfterEach
	void tearDown() {
		inventory.closeConnection();
	}
	@Test
	public void addItemTest() {
		inventory = new Inventory();
		Basket basket = new Basket(inventory);
		Assertions.assertFalse(basket.inBasket("BGLP"));
		Assertions.assertFalse(basket.inBasket("BGLO"));
		try {
			basket.addItem("BGLP");
		} catch (NotInInventoryException e) {
			throw new RuntimeException(e);
		}
		Assertions.assertTrue(basket.inBasket("BGLP"));
		Assertions.assertFalse(basket.inBasket("BGLO"));
	}

	@Test
	public void removeItemTest() {
		 inventory = new Inventory();
		Basket basket = new Basket(inventory);
		try {
			basket.addItem("BGLP");
		} catch (NotInInventoryException e) {
			throw new RuntimeException(e);
		}
		Assertions.assertTrue(basket.inBasket("BGLP"));
		Assertions.assertFalse(basket.inBasket("BGLO"));
		try {
			basket.removeItem(0);
		} catch (NotInBasketException e) {
			throw new RuntimeException(e);
		}
		Assertions.assertFalse(basket.inBasket("BGLP"));
		Assertions.assertFalse(basket.inBasket("BGLO"));
	}

	@Test
	public void basketIsFullTest() {
		 inventory = new Inventory();
		Basket basket = new Basket(inventory);
		Assertions.assertFalse(basket.isFull());
		try {
			for (int i = 0; i < 30; i++) {

				basket.addItem("BGLP");
			}


		} catch (NotInInventoryException e) {
			throw new RuntimeException(e);
		}
		Assertions.assertTrue(basket.isFull());

		try {
			basket.removeItem(0);
		} catch (NotInBasketException e) {
			throw new RuntimeException(e);
		}
		Assertions.assertFalse(basket.isFull());

	}

	@Test
	public void inBasketTest() {
		 inventory = new Inventory();
		Basket basket = new Basket(inventory);
		Assertions.assertFalse(basket.inBasket("BGLP"));
		try {
			basket.addItem("BGLP");
		} catch (NotInInventoryException e) {
			throw new RuntimeException(e);
		}
		Assertions.assertTrue(basket.inBasket("BGLP"));
	}

	@Test
	public void showFillingsTest() {
		 inventory = new Inventory();
		Basket basket = new Basket(inventory);
		Assertions.assertEquals("Bacon:\t0.12\nEgg:\t0.12\nCheese:\t0.12\nCream Cheese:\t0.12\nSmoked Salmon:\t0.12\nHam:\t0.12", basket.showFillings());
	}

	@Test
	public void setSizeTest() {
		 inventory = new Inventory();
		Basket basket = new Basket(inventory);
		Assertions.assertEquals(30, basket.getSize());
		try {
			basket.setSize(2);
		} catch (UnableToChangeBasketSizeException e) {
			throw new RuntimeException(e);
		}
		try {
			basket.addItem("BGLO");
			basket.addItem("BGLP");
		} catch (NotInInventoryException e) {
			throw new RuntimeException(e);
		}
		Assertions.assertThrows(UnableToChangeBasketSizeException.class, () -> {
			basket.setSize(1);
		});
	}

	@Test
	public void getTotalCostTest() {
		 inventory = new Inventory();
		Basket basket = new Basket(inventory);
		try {
			basket.addItem("BGLP");
		} catch (NotInInventoryException e) {
			throw new RuntimeException(e);
		}
		Assertions.assertEquals(0.39, basket.getTotalCost());
	}

	@Test
	public void addFillingTest() {
		 inventory = new Inventory();
		Basket basket = new Basket(inventory);
		try {
			basket.addItem("BGLP");
			basket.addExtra(0, "FILS");
		} catch (NotInInventoryException e) {
			throw new RuntimeException(e);
		}


	}

	@Test
	public void removeFillingTest() {
		 inventory = new Inventory();
		Basket basket = new Basket(inventory);
		try {
			basket.addItem("BGLP");
			basket.addExtra(0, "FILS");
		} catch (NotInInventoryException e) {
			throw new RuntimeException(e);
		}
		basket.removeExtra(0, "FILS");
	}

	@Test
	public void getPriceTest() {
		 inventory = new Inventory();
		Basket basket = new Basket(inventory);
		try {
			Assertions.assertEquals(0.39, basket.getPrice("BGLP"));
			Assertions.assertEquals(0.49, basket.getPrice("BGLO"));
			Assertions.assertEquals(0.12, basket.getPrice("FILS"));
		} catch (NotInInventoryException e) {
			throw new RuntimeException(e);
		}

	}

	@Test
	public void buyBulkTest() {
		 inventory = new Inventory();
		Basket basket2 = new Basket(inventory);
		for (int i = 0; i < 12; i++) {
			try {
				basket2.addItem("BGLP");
			} catch (NotInInventoryException e) {
				throw new RuntimeException(e);
			}
		}
		Assertions.assertEquals(3.99, basket2.getTotalCost(), 0.01);

		for (int i = 0; i < 6; i++) {
			try {
				basket2.addItem("BGLO");
			} catch (NotInInventoryException e) {
				throw new RuntimeException(e);
			}
		}
		Assertions.assertEquals(3.99 + 2.49, basket2.getTotalCost(), 0.001);

		Basket basket3 = new Basket(inventory);
		try {
			basket3.addItem("BGLO");
			basket3.addItem("COFB");
		} catch (NotInInventoryException e) {
			throw new RuntimeException(e);
		}
		Assertions.assertEquals(1.25, basket3.getTotalCost(), 0.01);
		Basket basket = new Basket(inventory);
		try {
			for (int i = 0; i < 12; i++) {

				basket.addItem("BGLP");
			}
			Assertions.assertEquals(3.99, basket.getTotalCost(), 0.1);
			basket.addItem("BGLP");
			basket.addItem("BGLP");
			basket.addItem("BGLP");
			basket.addItem("BGLP");
			Assertions.assertEquals(5.55, basket.getTotalCost(), 0.1);
		} catch (NotInInventoryException e) {
			throw new RuntimeException(e);
		}
		Basket basket4 = new Basket(inventory);
		try {
			basket4.addItem("BGLO");
			basket4.addItem("BGLO");
			Assertions.assertEquals(0.49 + 0.49, basket4.getTotalCost(), 0.1);

			for (int i = 0; i < 12; i++) {

				basket4.addItem("BGLP");
			}
			Assertions.assertEquals(0.49 + 0.49 + 3.99, basket4.getTotalCost(), 0.1);

			for (int i = 0; i < 6; i++) {

				basket4.addItem("BGLE");
			}
			Assertions.assertEquals(0.49 + 0.49 + 3.99 + 2.49, basket4.getTotalCost(), 0.1);
			basket4.addItem("COFB");
			basket4.addItem("COFB");
			Assertions.assertEquals(1.25 + 1.25 + 3.99 + 2.49, basket4.getTotalCost(), 0.1);
			basket4.addItem("COFB");
			Assertions.assertEquals(1.25 + 1.25 + 3.99 + 2.49 + 0.99, basket4.getTotalCost(), 0.1);

		} catch (NotInInventoryException e) {
			throw new RuntimeException(e);
		}
		Basket basket5 = new Basket(inventory);

		try {
			basket5.addItem("BGLP");
			basket5.addItem("BGLP");
			basket5.addItem("BGLP");
			basket5.addItem("BGLP");
			basket5.addItem("BGLP");
			basket5.addItem("BGLP");
			Assertions.assertEquals(0.39 * 6, basket5.getTotalCost(), 0.1);


		} catch (NotInInventoryException e) {
			throw new RuntimeException(e);
		}
		try {
			basket5.addExtra(0, "FILX");
			basket5.addExtra(0, "FILS");
			basket5.addExtra(1, "FILH");


			Assertions.assertEquals(0.39 * 6 + 0.12 * 3, basket5.getTotalCost(), 0.1);


		} catch (NotInInventoryException e) {
			throw new RuntimeException(e);
		}

	}

	@Test
	public void printReceiptTest() {
		 inventory = new Inventory();
		Basket basket = new Basket(inventory);
		String receipt = basket.printReceipt();
		LocalDateTime currentTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String formattedTime = currentTime.format(formatter);
		String example = "\n          ~~~ Bob's Bagels ~~~\n" +
				"           " + formattedTime + "\n" +
				"----------------------------------------\n" +
				"----------------------------------------\n" +
				"Total\t\t\t\t\t\t\t\u00A30.00\n" +
				"\n" +
				"               Thank you\n" +
				"\t        for your order !\n";
		Assertions.assertEquals(example, receipt);

		try {
			basket.addItem("BGLP");
			formattedTime = currentTime.format(formatter);

			example = "\n          ~~~ Bob's Bagels ~~~\n" +
					"           " + formattedTime + "\n" +
					"----------------------------------------\n" +
					"Bagel Plain                 1   \u00A30.39\n" +
					"----------------------------------------\n" +
					"Total\t\t\t\t\t\t\t\u00A30.39\n" +
					"\n" +
					"               Thank you\n" +
					"\t        for your order !\n";
			receipt = basket.printReceipt();
			Assertions.assertEquals(example, receipt);
		} catch (NotInInventoryException e) {
			throw new RuntimeException(e);
		}

		try {
			basket.addItem("BGLP");
			formattedTime = currentTime.format(formatter);

			example = "\n          ~~~ Bob's Bagels ~~~\n" +
					"           " + formattedTime + "\n" +
					"----------------------------------------\n" +
					"Bagel Plain                 2   \u00A30.39\n" +
					"----------------------------------------\n" +
					"Total\t\t\t\t\t\t\t\u00A30.78\n" +
					"\n" +
					"               Thank you\n" +
					"\t        for your order !\n";
			receipt = basket.printReceipt();
			Assertions.assertEquals(example, receipt);
		} catch (NotInInventoryException e) {
			throw new RuntimeException(e);
		}
		try {
			basket.addItem("BGLP");
			basket.addItem("BGLP");
			basket.addItem("BGLP");
			basket.addItem("BGLP");
			formattedTime = currentTime.format(formatter);

			example = "\n          ~~~ Bob's Bagels ~~~\n" +
					"           " + formattedTime + "\n" +
					"----------------------------------------\n" +
					"Bagel Plain                 6   \u00A30.39\n" +
					"----------------------------------------\n" +
					"Total\t\t\t\t\t\t\t\u00A32.34\n" +
					"\n" +
					"               Thank you\n" +
					"\t        for your order !\n";
			receipt = basket.printReceipt();
			Assertions.assertEquals(example, receipt);
		} catch (NotInInventoryException e) {
			throw new RuntimeException(e);
		}
		try {
			for (int i = 0; i < 5; i++) {
				basket.addItem("BGLO");
			}
			formattedTime = currentTime.format(formatter);

			example = "\n          ~~~ Bob's Bagels ~~~\n" +
					"           " + formattedTime + "\n" +
					"----------------------------------------\n" +
					"Bagel Plain                 6   \u00A30.39\n" +
					"Bagel Onion                 5   \u00A30.49\n" +
					"----------------------------------------\n" +
					"Total\t\t\t\t\t\t\t\u00A34.79\n" +
					"\n" +
					"               Thank you\n" +
					"\t        for your order !\n";
			receipt = basket.printReceipt();
			Assertions.assertEquals(example, receipt);
		} catch (NotInInventoryException e) {
			throw new RuntimeException(e);
		}
		try {
			basket = new Basket(inventory);
			basket.addItem("COFB");
			formattedTime = currentTime.format(formatter);

			example = "\n          ~~~ Bob's Bagels ~~~\n" +
					"           " + formattedTime + "\n" +
					"----------------------------------------\n" +
					"Coffee Black                1   \u00A30.99\n" +
					"----------------------------------------\n" +
					"Total\t\t\t\t\t\t\t\u00A30.99\n" +
					"\n" +
					"               Thank you\n" +
					"\t        for your order !\n";
			receipt = basket.printReceipt();
			Assertions.assertEquals(example, receipt);
		} catch (NotInInventoryException e) {
			throw new RuntimeException(e);
		}
		try {
			basket = new Basket(inventory);
			basket.addItem("BGLP");
			basket.addExtra(0, "FILX");
			basket.addExtra(0, "FILS");
			formattedTime = currentTime.format(formatter);

			example = "\n          ~~~ Bob's Bagels ~~~\n" +
					"           " + formattedTime + "\n" +
					"----------------------------------------\n" +
					"Bagel Plain                 1   \u00A30.39\n" +
					"Filling Cream Cheese        1   \u00A30.12\n" +
					"Filling Smoked Salmon       1   \u00A30.12\n" +
					"----------------------------------------\n" +
					"Total\t\t\t\t\t\t\t\u00A30.63\n" +
					"\n" +

					"               Thank you\n" +
					"\t        for your order !\n";
			receipt = basket.printReceipt();
			Assertions.assertEquals(example, receipt);
		} catch (NotInInventoryException e) {
			throw new RuntimeException(e);
		}
		try {
			basket = new Basket(inventory);
			basket.addItem("BGLP");
			basket.addItem("BGLO");
			basket.addItem("BGLO");
			basket.addExtra(0, "FILX");
			basket.addExtra(0, "FILS");
			basket.addExtra(1, "FILH");
			formattedTime = currentTime.format(formatter);

			example = "\n          ~~~ Bob's Bagels ~~~\n" +
					"           " + formattedTime + "\n" +
					"----------------------------------------\n" +
					"Bagel Plain                 1   \u00A30.39\n" +
					"Bagel Onion                 2   \u00A30.49\n" +
					"Filling Cream Cheese        1   \u00A30.12\n" +
					"Filling Smoked Salmon       1   \u00A30.12\n" +
					"Filling Ham                 1   \u00A30.12\n" +
					"----------------------------------------\n" +
					"Total\t\t\t\t\t\t\t\u00A31.73\n" +
					"\n" +
					"               Thank you\n" +
					"\t        for your order !\n";
			receipt = basket.printReceipt();
			Assertions.assertEquals(example, receipt);
		} catch (NotInInventoryException e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	public void printReceiptDiscountTest() {
		LocalDateTime currentTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		 inventory = new Inventory();
		Basket basket = new Basket(inventory);
		String example;
		try {
			basket.addItem("BGLP");
			basket.addItem("COFB");
			String formattedTime = currentTime.format(formatter);
			example = "\n          ~~~ Bob's Bagels ~~~\n" +
					"           " + formattedTime + "\n" +
					"----------------------------------------\n" +
					"Bagel + Coffee Combo        1   \u00A31.25\n" +
					"----------------------------------------\n" +
					"Total\t\t\t\t\t\t\t\u00A31.25\n" +
					"\n" +
					"               Thank you\n" +
					"\t        for your order !\n";
			Assertions.assertEquals(example, basket.printReceipt());
		} catch (NotInInventoryException e) {
			throw new RuntimeException(e);
		}
		try {
			basket.addItem("BGLP");
			basket.addItem("COFB");
			String formattedTime = currentTime.format(formatter);
			example = "\n          ~~~ Bob's Bagels ~~~\n" +
					"           " + formattedTime + "\n" +
					"----------------------------------------\n" +
					"Bagel + Coffee Combo        2   \u00A31.25\n" +
					"----------------------------------------\n" +
					"Total\t\t\t\t\t\t\t\u00A32.50\n" +
					"\n" +
					"               Thank you\n" +
					"\t        for your order !\n";
			Assertions.assertEquals(example, basket.printReceipt());
		} catch (NotInInventoryException e) {
			throw new RuntimeException(e);
		}
		try {
			for (int i = 0; i < 6; i++) {
				basket.addItem("BGLO");

			}
			String formattedTime = currentTime.format(formatter);
			example = "\n          ~~~ Bob's Bagels ~~~\n" +
					"           " + formattedTime + "\n" +
					"----------------------------------------\n" +
					"Bagel Onion                 6   \u00A30.49\n" +
					"                              (-\u00A30.45)\n" +
					"Bagel + Coffee Combo        2   \u00A31.25\n" +
					"----------------------------------------\n" +
					"Total\t\t\t\t\t\t\t\u00A34.99\n" +
					"\n" +
					"               Thank you\n" +
					"\t        for your order !\n";
			Assertions.assertEquals(example, basket.printReceipt());
		} catch (NotInInventoryException e) {
			throw new RuntimeException(e);
		}
		try {
			for (int i = 0; i < 6; i++) {
				basket.addItem("BGLO");
			}
			for (int i = 0; i < 12; i++) {
				basket.addItem("BGLP");
			}
			basket.addItem("COFB");

			String formattedTime = currentTime.format(formatter);
			example = "\n          ~~~ Bob's Bagels ~~~\n" +
					"           " + formattedTime + "\n" +
					"----------------------------------------\n" +
					"Bagel Plain                12   \u00A30.39\n" +
					"                              (-\u00A30.69)\n" +
					"Bagel Onion                12   \u00A30.49\n" +
					"                              (-\u00A30.90)\n" +
					"Bagel + Coffee Combo        2   \u00A31.25\n" +
					"Coffee Black                1   \u00A30.99\n" +

					"----------------------------------------\n" +
					"Total\t\t\t\t\t\t\t\u00A312.46\n" +
					"\n" +
					"               Thank you\n" +
					"\t        for your order !\n";
			Assertions.assertEquals(example, basket.printReceipt());
		} catch (NotInInventoryException e) {
			throw new RuntimeException(e);
		}
		try {
			basket.addExtra(0, "FILH");
			basket.addExtra(0, "FILX");
			basket.addExtra(4, "FILS");
			String formattedTime = currentTime.format(formatter);
			example = "\n          ~~~ Bob's Bagels ~~~\n" +
					"           " + formattedTime + "\n" +
					"----------------------------------------\n" +
					"Bagel Plain                12   \u00A30.39\n" +
					"                              (-\u00A30.69)\n" +
					"Bagel Onion                12   \u00A30.49\n" +
					"                              (-\u00A30.90)\n" +
					"Bagel + Coffee Combo        2   \u00A31.25\n" +
					"Coffee Black                1   \u00A30.99\n" +
					"Filling Ham                 1   \u00A30.12\n" +
					"Filling Cream Cheese        1   \u00A30.12\n" +
					"Filling Smoked Salmon       1   \u00A30.12\n" +
					"----------------------------------------\n" +
					"Total\t\t\t\t\t\t\t\u00A312.82\n" +
					"\n" +
					"               Thank you\n" +
					"\t        for your order !\n";
			Assertions.assertEquals(example, basket.printReceipt());
		} catch (NotInInventoryException e) {
			throw new RuntimeException(e);
		}


	}
}
