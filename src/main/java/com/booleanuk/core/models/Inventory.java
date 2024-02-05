package com.booleanuk.core.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;


public class Inventory {
	//ArrayList<Item> inventory;
	ArrayList<DiscountBulk> discountsBulk;
	ArrayList<DiscountCombo> discountsCombo;
	private final DatabaseHandler databaseHandler;

	public Inventory() {
		this.databaseHandler = new DatabaseHandler();
		init();
	}

	public boolean inInventory(String id) {
		String sql = "SELECT COUNT(*) FROM products WHERE sku = ?";
		try (ResultSet resultSet = databaseHandler.executeQuery(sql, id)) {
			if (resultSet.next()) {
				int count = resultSet.getInt(1);
				return count > 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false; // or throw a custom exception based on your application needs
	}

	public String getFillings() {
		String sql = "SELECT variant, price FROM products WHERE type_id =(" +
				"SELECT id FROM product_types WHERE type = 'Filling')";
		try (ResultSet resultSet = databaseHandler.executeQuery(sql)) {
			StringBuilder sb = new StringBuilder();
			while (resultSet.next()) {
				String name = resultSet.getString("variant");
				double price = resultSet.getDouble("price");
				sb.append(name).append(":\t").append(price).append("\n");
			}
			if (!sb.isEmpty()) {
				sb.deleteCharAt(sb.length() - 1);
			}
			return sb.toString();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ""; // or throw a custom exception based on your application needs
	}

	public String getItems() {
		String sql = "SELECT sku FROM products";
		try (ResultSet resultSet = databaseHandler.executeQuery(sql)) {
			StringBuilder sb = new StringBuilder();
			while (resultSet.next()) {
				String sku = resultSet.getString("sku");
				sb.append(sku);
			}
			return sb.toString();
		} catch (SQLException e) {
			// Log the exception or throw a custom exception
			e.printStackTrace();
			return ""; // or throw new CustomException("Error retrieving items", e);
		} finally {
			// Ensure that the database connection is closed
			databaseHandler.closeConnection();
		}
	}


	public double getPrice(String id) {
		String sql = "SELECT price FROM products WHERE sku = ?";
		try (ResultSet resultSet = databaseHandler.executeQuery(sql, id)) {
			if (resultSet.next()) {
				return resultSet.getDouble("price");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public String getName(String id) throws NotInInventoryException {
		String sql = "SELECT variant FROM products WHERE sku = ?";
		try (ResultSet resultSet = databaseHandler.executeQuery(sql, id)) {
			if (resultSet.next()) {
				return resultSet.getString("variant");
			} else {
				throw new NotInInventoryException(id);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}


	public double getBulkAmount(String id) {
		for (DiscountBulk item : discountsBulk) {
			if (item.getId().equals(id)) {
				return item.getAmount();
			}
		}
		return 0;
	}

	public int getBulkBulk(String id) {
		for (DiscountBulk item : discountsBulk) {
			if (item.getId().equals(id)) {
				return item.getBulk();
			}
		}
		return 0;
	}

	public boolean hasDiscountBulk(String id) {
		for (DiscountBulk discount : discountsBulk) {
			if (discount.getId().equals(id)) return true;
		}


		return false;
	}

	public ArrayList<String[]> getDiscountComboPairs() {
		ArrayList<String[]> pairs = new ArrayList<>();
		for (DiscountCombo combo : discountsCombo) {
			pairs.add(combo.comboItems);
		}
		return pairs;
	}

	public double getDiscountComboAmount(String[] pair) {
		for (DiscountCombo combo : discountsCombo) {
			if (Arrays.equals(combo.getComboItems(), pair)) {
				return combo.getNewPrice();
			}
		}
		return 0;
	}

	public String getType(String id) {
		String sql = "SELECT name FROM products WHERE id = ?";
		try (ResultSet resultSet = databaseHandler.executeQuery(sql, id)) {
			if (resultSet.next()) {
				return resultSet.getString("name");
			} else {
				throw new NotInInventoryException(id);
			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		} catch (NotInInventoryException e) {
			throw new RuntimeException(e);
		}
	}

	private void init() {
		this.discountsBulk = new ArrayList<>();
		this.discountsCombo = new ArrayList<>();

		DiscountBulk d1 = new DiscountBulk("BGLO", 6, 0.415);
		DiscountBulk d2 = new DiscountBulk("BGLP", 12, 0.3325);
		DiscountBulk d3 = new DiscountBulk("BGLE", 6, 0.415);
		DiscountCombo d4 = new DiscountCombo(new String[]{"BGL", "COF"}, 1.25);
		discountsBulk.add(d1);
		discountsBulk.add(d2);
		discountsBulk.add(d3);
		discountsCombo.add(d4);

/*
		this.inventory = new ArrayList<>();
		Item item1 = new Bagel("BGLO", "Onion", 0.49);
		Item item2 = new Bagel("BGLP", "Plain", 0.39);
		Item item3 = new Bagel("BGLE", "Everything", 0.49);
		Item item4 = new Bagel("BGLS", "Sesame", 0.49);
		Item item5 = new Coffee("COFB", "Black", 0.99);
		Item item6 = new Coffee("COFW", "White", 1.19);
		Item item7 = new Coffee("COFC", "Cappuccino", 1.29);
		Item item8 = new Coffee("COFL", "Latte", 1.29);
		Item item9 = new Filling("FILB", "Bacon", 0.12);
		Item item10 = new Filling("FILE", "Egg", 0.12);
		Item item11 = new Filling("FILC", "Cheese", 0.12);
		Item item12 = new Filling("FILX", "Cream Cheese", 0.12);
		Item item13 = new Filling("FILS", "Smoked Salmon", 0.12);
		Item item14 = new Filling("FILH", "Ham", 0.12);

		inventory.add(item1);
		inventory.add(item2);
		inventory.add(item3);
		inventory.add(item4);
		inventory.add(item5);
		inventory.add(item6);
		inventory.add(item7);
		inventory.add(item8);
		inventory.add(item9);
		inventory.add(item10);
		inventory.add(item11);
		inventory.add(item12);
		inventory.add(item13);
		inventory.add(item14);

 */

	}



/*
	private Item getItem(String id) throws NotInInventoryException {
		for (Item item : inventory) {
			if (item.getId().equals(id)) {
				return item;
			}
		}
		throw new NotInInventoryException(id);
	}

 */

	public void closeConnection() {
		databaseHandler.closeConnection();
	}
}
