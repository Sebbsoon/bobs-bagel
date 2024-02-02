package com.booleanuk.core.models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseHandler {
	private static final String DB_URL = System.getenv("DB_URL");
	private static final String USERNAME = System.getenv("USERNAME");
	private static final String PASSWORD = System.getenv("PASSWORD");

	private Connection connection;

	public DatabaseHandler() {
		establishConnection();
	}

	private void establishConnection() {
		try {
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void closeConnection() {
		try {
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public ResultSet executeQuery(String sql, Object... parameters) {
		try {
			PreparedStatement statement = connection.prepareStatement(sql);

			// Set parameters for the prepared statement
			for (int i = 0; i < parameters.length; i++) {
				statement.setObject(i + 1, parameters[i]);
			}

			return statement.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}
}
