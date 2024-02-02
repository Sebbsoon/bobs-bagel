package com.booleanuk.core.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Basket {
	private int itemIndex;
	private int size = 30;
	private Inventory inventory;
	private HashMap<Integer, String> items;
	private HashMap<Integer, ArrayList<String>> extra;

	public Basket(Inventory inventory) {
		this.items = new HashMap<>();
		this.extra = new HashMap<>();
		this.inventory = inventory;
	}

	public boolean inBasket(String id) {
		return items.containsValue(id);
	}

	public void addItem(String id) throws NotInInventoryException {
		if (inventory.inInventory(id)) {
			if (!isFull()) {
				items.put(itemIndex++, id);
			}
		} else {
			throw new NotInInventoryException(id);
		}
	}

	public void removeItem(int index) throws NotInBasketException {
		if (items.containsKey(index)) {
			items.remove(index);
		} else {
			throw new NotInBasketException(index);
		}
	}

	public boolean isFull() {
		return items.size() >= size;
	}

	public void setSize(int size) throws UnableToChangeBasketSizeException {
		if (items.size() > size) {
			throw new UnableToChangeBasketSizeException(size, items.size());
		}
		this.size = size;
	}

	public String showFillings() {
		return inventory.getFillings();
	}

	public void addExtra(int index, String id) throws NotInInventoryException {
		if (inventory.inInventory(id)) {
			if (items.get(index).contains("BGL") && id.contains("FIL")) {
				if (extra.containsKey(index)) {
					extra.get(index).add(id);
				} else {
					ArrayList<String> itemsExtra = new ArrayList<>();
					itemsExtra.add(id);
					extra.put(index, itemsExtra);
				}
			}

		}else throw new NotInInventoryException(id);
	}

	public void removeExtra(int index, String id) {
		if (inventory.inInventory(id)) {
			if (items.get(index).contains("BGL") && id.contains("FIL")) {
				if (extra.containsKey(index)) {
					extra.get(index).remove(id);
				}
			}
		}
	}

	public int getSize() {
		return this.size;
	}

	public double getPrice(String id) throws NotInInventoryException {
		if (inventory.inInventory(id)) {
			return inventory.getPrice(id);
		} else {
			throw new NotInInventoryException(id);
		}
	}

	public double getTotalCost() {
		double total = 0;
		ArrayList<String> itemsLeft = new ArrayList<>(items.values());
		HashSet<String> uniqueItems = new HashSet<>(items.values());
		ArrayList<String> hasDiscount = fillHasDisciunt(uniqueItems);
		HashMap<String, Integer> bulkAmount = fillBulkAmount(hasDiscount);
		ArrayList<String> discountItems = moveDiscountItems(itemsLeft, bulkAmount);
		HashSet<String[]> discountPairs = fillDiscountPairs(itemsLeft);

		total += calcBulkTotal(discountItems);
		total += calcComboTotal(discountPairs, itemsLeft);
		total += calcItemsTotal(itemsLeft);
		total += calcExtraTotal(extra);
		return total;
	}

	public String printReceipt() {

		StringBuilder sb = new StringBuilder();
		receiptStart(sb);
		receiptItems(sb);
		receiptEnding(sb);
		System.out.println(sb);
		return sb.toString();
	}

	private void receiptItemsRegular(StringBuilder sb, ArrayList<String> itemsLeft) {
		HashSet<String> uniqueItems = new HashSet<>(itemsLeft);
		HashMap<String, Integer> itemOcurrance = countOccurrences(itemsLeft, uniqueItems.toArray(new String[0]));
		for (String item : uniqueItems) {
			try {
				sb.append(String.format("%-26s", inventory.getType(item) + " " + inventory.getName(item)));
			} catch (NotInInventoryException e) {
				throw new RuntimeException(e);
			}
			sb.append(String.format("%3s", itemOcurrance.get(item)));
			sb.append(String.format("%4s", "\u00A3"));
			sb.append(String.format("%.2f", inventory.getPrice(item)));
			sb.append("\n");
		}
		for (ArrayList<String> extras : extra.values()) {
			uniqueItems = new HashSet<>(extras);
			itemOcurrance = countOccurrences(new ArrayList<>(extras), uniqueItems.toArray(new String[0]));
			for (String item : extras) {
				try {
					sb.append(String.format("%-26s", inventory.getType(item) + " " + inventory.getName(item)));

				} catch (NotInInventoryException e) {
					throw new RuntimeException(e);
				}
				sb.append(String.format("%3s", itemOcurrance.get(item)));
				sb.append(String.format("%4s", "\u00A3"));
				sb.append(String.format("%.2f", inventory.getPrice(item)));
				sb.append("\n");
			}
		}
	}

	private void receiptItems(StringBuilder sb) {
		ArrayList<String> itemsLeft = new ArrayList<>(items.values());
		HashSet<String> uniqueItems = new HashSet<>(items.values());
		ArrayList<String> hasDiscount = fillHasDisciunt(uniqueItems);
		HashMap<String, Integer> bulkAmount = fillBulkAmount(hasDiscount);
		ArrayList<String> discountItems = moveDiscountItems(itemsLeft, bulkAmount);
		HashSet<String[]> discountPairs = fillDiscountPairs(itemsLeft);

		receiptItemsBulk(sb, discountItems);
		receiptItemsCombo(sb, discountPairs, itemsLeft);
		receiptItemsRegular(sb, itemsLeft);
	}

	private void receiptItemsCombo(StringBuilder sb, HashSet<String[]> discountPairs, ArrayList<String> itemsLeft) {

		for (String[] pair : discountPairs) {
			int smallest = calcSmallest(itemsLeft, pair);
			removeComboitems(itemsLeft, pair, smallest);
			sb.append(String.format("%-26s", "Bagel + Coffee Combo"));
			sb.append(String.format("%3s", smallest));
			sb.append(String.format("%4s", "\u00A3"));
			sb.append(String.format("%.2f", 1.25));
			sb.append("\n");
		}

	}

	private void receiptItemsBulk(StringBuilder sb, ArrayList<String> itemsLeft) {
		HashSet<String> uniqueItems = new HashSet<>(itemsLeft);
		HashMap<String, Integer> itemOccurrence = countOccurrences(itemsLeft, uniqueItems.toArray(new String[0]));
		for (String item : uniqueItems) {
			try {
				sb.append(String.format("%-26s", inventory.getType(item) + " " + inventory.getName(item)));
			} catch (NotInInventoryException e) {
				throw new RuntimeException(e);
			}
			sb.append(String.format("%3s", itemOccurrence.get(item)));
			sb.append(String.format("%4s", "\u00A3"));
			sb.append(String.format("%.2f", inventory.getPrice(item)));
			sb.append("\n");
			sb.append(String.format("%33s", "(-\u00A3"));
			sb.append(String.format("%.2f", (inventory.getPrice(item) - inventory.getBulkAmount(item)) * itemOccurrence.get(item)));
			sb.append(")");
			sb.append("\n");
		}

	}


	private void receiptEnding(StringBuilder sb) {
		sb.append("----------------------------------------\n");
		sb.append("Total\t\t\t\t\t\t\t\u00A3").append(String.format("%.2f", getTotalCost())).append("\n");
		sb.append("\n               Thank you\n	        for your order !\n");
	}

	private void receiptStart(StringBuilder sb) {
		sb.append("\n          ~~~ Bob's Bagels ~~~\n");
		LocalDateTime currentTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String formattedTime = currentTime.format(formatter);
		sb.append("           ").append(formattedTime).append("\n");
		sb.append("----------------------------------------\n");
	}

	private double calcBulkTotal(ArrayList<String> discountItems) {
		double total = 0;
		for (String item : discountItems) {
			double price = inventory.getBulkAmount(item);
			total += price;
		}
		return total;
	}

	private double calcComboTotal(HashSet<String[]> discountPairs, ArrayList<String> itemsLeft) {
		double total = 0;
		for (String[] pair : discountPairs) {
			int smallest = calcSmallest(itemsLeft, pair);
			removeComboitems(itemsLeft, pair, smallest);
			for (int j = 0; j < smallest; j++) {
				total += inventory.getDiscountComboAmount(pair);
			}
		}
		return total;
	}

	private double calcItemsTotal(ArrayList<String> itemsLeft) {
		double total = 0;
		for (String item : itemsLeft) {
			total += inventory.getPrice(item);
		}
		return total;
	}

	private double calcExtraTotal(HashMap<Integer, ArrayList<String>> extra) {
		double total = 0;
		for (ArrayList<String> extras : extra.values()) {
			for (String item : extras) {
				total += inventory.getPrice(item);
			}
		}
		return total;
	}

	private int calcSmallest(ArrayList<String> itemsLeft, String[] pair) {
		HashMap<String, Integer> occurrences = countOccurrences(itemsLeft, pair);
		int smallest = Integer.MAX_VALUE;
		for (Integer i : occurrences.values()) {
			if (i < smallest) {
				smallest = i;
			}
		}
		return smallest;
	}

	private int calcClosestMultiple(HashMap<String, Integer> bulkAmount, String bulkItem) {
		int input = bulkAmount.get(bulkItem);
		int target = inventory.getBulkBulk(bulkItem);
		if (Math.round((float) input / target) * target > input) {
			return 0;
		}
		return Math.round((float) input / target) * target;
	}

	private static boolean containsAllItems(ArrayList<String> items, String[] array) {
		for (String str : array) {
			boolean found = false;

			for (String item : items) {
				if (item.contains(str)) {
					found = true;
					break;
				}
			}

			if (!found) {
				return false;
			}
		}
		return true;
	}

	private static HashMap<String, Integer> countOccurrences(ArrayList<String> items, String[] stringsToCount) {
		HashMap<String, Integer> occurrences = new HashMap<>();
		for (String str : items) {
			if (containsString(stringsToCount, str)) {
				occurrences.put(str, occurrences.getOrDefault(str, 0) + 1);
			}
		}

		return occurrences;
	}

	public HashMap<Integer, String> getItems() {
		return items;
	}

	public void setItems(HashMap<Integer, String> items) {
		this.items = items;
	}

	public HashMap<Integer, ArrayList<String>> getExtra() {
		return extra;
	}

	public void setExtra(HashMap<Integer, ArrayList<String>> extra) {
		this.extra = extra;
	}

	private static boolean containsString(String[] array, String target) {
		for (String str : array) {
			if (target.contains(str)) {
				return true;
			}
		}
		return false;
	}

	private ArrayList<String> fillHasDisciunt(HashSet<String> uniqueItems) {
		ArrayList<String> hasDiscount = new ArrayList<>();

		for (String item : uniqueItems) {
			if (inventory.hasDiscountBulk(item)) {
				hasDiscount.add(item);
			}
		}
		return hasDiscount;
	}

	private HashMap<String, Integer> fillBulkAmount(ArrayList<String> hasDiscount) {
		HashMap<String, Integer> bulkAmount = new HashMap<>();
		for (String item : hasDiscount) {
			bulkAmount.put(item, Collections.frequency(items.values(), item));
		}
		return bulkAmount;
	}

	private HashSet<String[]> fillDiscountPairs(ArrayList<String> itemsLeft) {
		HashSet<String[]> discountPairs = new HashSet<>();
		ArrayList<String[]> comboPairs = inventory.getDiscountComboPairs();
		for (String[] pair : comboPairs) {
			if (containsAllItems(itemsLeft, pair)) {
				discountPairs.add(pair);
			}
		}
		return discountPairs;
	}

	private ArrayList<String> moveDiscountItems(ArrayList<String> itemsLeft, HashMap<String, Integer> bulkAmount) {
		ArrayList<String> discountItems = new ArrayList<>();
		for (String bulkItem : bulkAmount.keySet()) {
			int closestMultiple = calcClosestMultiple(bulkAmount, bulkItem);
			int movedItems = 0;
			for (int i = 0; i < itemsLeft.size(); i++) {
				if (!(movedItems < closestMultiple)) {
					break;
				}
				if (itemsLeft.get(i).equals(bulkItem)) {
					discountItems.add(bulkItem);
					itemsLeft.remove(i);
					i--;
					movedItems++;

				}
			}
		}
		return discountItems;
	}

	private void removeComboitems(ArrayList<String> itemsLeft, String[] pair, int smallest) {
		for (String s : pair) {
			int removed = 0;
			for (int j = 0; j < itemsLeft.size(); j++) {
				if (removed < smallest && itemsLeft.get(j).contains(s)) {
					itemsLeft.remove(j);
					removed++;
					j--;

				}
			}
			removed = 0;
		}
	}


	public String getInventory() {
		StringBuilder sb = new StringBuilder();
		sb.append(inventory.getItems());
		return sb.toString();
	}

	public String getName(String value) throws NotInInventoryException {
		return inventory.getName(value);
	}

	public String getType(String value) {
		return inventory.getType(value);
	}
}
