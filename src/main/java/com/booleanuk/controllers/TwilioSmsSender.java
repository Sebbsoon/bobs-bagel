package com.booleanuk.controllers;

import com.booleanuk.core.models.Basket;
import com.booleanuk.core.models.Inventory;
import com.booleanuk.core.models.NotInBasketException;
import com.booleanuk.core.models.NotInInventoryException;
import com.twilio.Twilio;
import com.twilio.base.ResourceSet;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import com.twilio.exception.AuthenticationException;


import java.util.*;


public class TwilioSmsSender {


	public static final String ACCOUNT_SID = System.getenv("ACCOUNT_SID");
	public static final String AUTH_TOKEN = System.getenv("AUTH_TOKEN");
	public static final String PHONE_FROM = System.getenv("PHONE_FROM");
	public static final String PHONE_TO = System.getenv("PHONE_TO");

	public static int messagesSize = 0;


	public static void main(String[] args) throws AuthenticationException {

		Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

		Iterable<Message> messages = Message.reader()
				.setTo(new PhoneNumber(PHONE_FROM))
				.read();

		List<Message> messageList = new ArrayList<>();
		messages.forEach(messageList::add);
		messagesSize = messageList.size();

		running();
	}

	private static void running() {
		Inventory inventory = new Inventory();
		Basket basket = new Basket(inventory);
		String answer = "";
		sendMessage(printItems(basket));
		while (!answer.equals("q")) {
			sendMessage(printMenu());
			answer = receiveMessage();
			switch (answer) {
				case "0": {
					sendMessage(seeBasket(basket));
					break;
				}
				case "1": {
					sendMessage("Item to add:");
					answer = receiveMessage();
					try {
						basket.addItem(answer);
					} catch (NotInInventoryException e) {
						sendMessage(e.getMessage());
					}
					answer = "";
					break;

				}
				case "2": {
					sendMessage("Item to remove:");
					answer = receiveMessage();
					int index = -1;
					try {
						index = Integer.parseInt(answer);

					} catch (Exception e) {
						sendMessage("Invalid input");
					}
					try {
						basket.removeItem(index);
					} catch (NotInBasketException e) {
						sendMessage(e.getMessage());
					}
					break;
				}
				case "3": {
					sendMessage("Item to add to:");
					answer = receiveMessage();
					int index = -1;
					try {
						index = Integer.parseInt(answer);

					} catch (Exception e) {
						sendMessage("Invalid input");
					}
					sendMessage("Extra to add:");
					answer = receiveMessage();

					try {
						basket.addExtra(index, answer);
					} catch (NotInInventoryException e) {
						sendMessage(e.getMessage());
					}
					break;
				}
				case "4": {
					sendMessage("Item to remove from:");
					answer = receiveMessage();
					int index = -1;
					try {
						index = Integer.parseInt(answer);

					} catch (Exception e) {
						sendMessage("Invalid input");
					}
					sendMessage("Extra to remove:");
					answer = receiveMessage();
					basket.removeExtra(index, answer);
					break;
				}
				case "5": {
					sendMessage(printMessagesTo());
					break;
				}
				case "6": {
					sendMessage(printMessagesFrom());
					break;
				}
				case "7": {
					sendMessage(endSale(basket));
					answer = "q";
					break;
				}
				default:
			}
		}

	}

	private static String printMessagesTo() {
		StringBuilder sb = new StringBuilder();
		ResourceSet<Message> messagesTo = Message.reader()
				.setTo(new PhoneNumber(PHONE_TO)).setFrom(PHONE_FROM)
				.read();

		for (Message message : messagesTo) {
			sb.append("From: " + message.getFrom().getEndpoint() + "\n");
			sb.append("Body: " + message.getBody() + "\n");
			sb.append("-------------\n");
		}
		return sb.toString();
	}

	private static String printMessagesFrom() {
		StringBuilder sb = new StringBuilder();
		ResourceSet<Message> messagesTo = Message.reader()
				.setFrom(new PhoneNumber(PHONE_TO)).setTo(PHONE_FROM)
				.read();

		for (Message message : messagesTo) {
			sb.append("From: " + message.getFrom().getEndpoint() + "\n");
			sb.append("Body: " + message.getBody() + "\n");
			sb.append("-------------\n");
		}
		return sb.toString();
	}

	private static String endSale(Basket basket) {
		return basket.printReceipt();
	}

	private static String seeBasket(Basket basket) {
		StringBuilder sb = new StringBuilder();
		sb.append("Items in basket:\n");
		basket.getItems().forEach((key, value) -> {
			try {
				sb.append(key + ": " + basket.getType(value) + " " + basket.getName(value) + "\n");

			} catch (NotInInventoryException e) {
				throw new RuntimeException(e);
			}
		});
		sb.append("Extra in basket:\n");
		basket.getExtra().forEach((key, value) -> {
			sb.append(key + ": ");
			for (String item : value) {
				try {
					sb.append(basket.getType(item) + " " + basket.getName(item) + "\n");
				} catch (NotInInventoryException e) {
					throw new RuntimeException(e);
				}
			}


		});


		return sb.toString();
	}

	private static String printMenu() {
		return
				"0: See basket\n" +
						"1: Add item\n" +
						"2: Remove item\n" +
						"3: Add extra\n" +
						"4: Remove extra\n" +
						"5: See received messages\n" +
						"6: See sent messages\n" +
						"7: Check out\n" +
						"q: Quit";
	}

	private static String printItems(Basket basket) {
		StringBuilder sb = new StringBuilder();
		sb.append("Available items:\n");
		sb.append(basket.getInventory());
		return sb.toString();
	}

	private static void sendMessage(String body) {
		int maxLength = 1200;

		if (body.length() > maxLength) {
			body = body.substring(0, maxLength);
		}
		Message message = Message
				.creator(
						new PhoneNumber(PHONE_TO),
						new PhoneNumber(PHONE_FROM),
						body
				)
				.create();
	}

	private static String receiveMessage() {
		while (true) {

			Iterable<Message> messages = Message.reader()
					.setTo(new PhoneNumber(PHONE_FROM))
					.read();

			List<Message> messageList = new ArrayList<>();
			messages.forEach(messageList::add);
			messageList.sort((m1, m2) -> m2.getDateSent().compareTo(m1.getDateSent()));

			if (!messageList.isEmpty()) {
				Message latestMessage = messageList.get(0);
				if (messageList.size() > messagesSize) {
					messagesSize = messageList.size();
					return latestMessage.getBody();
				}

			} else {
				System.out.println("No messages found.");
			}


		}
	}
}