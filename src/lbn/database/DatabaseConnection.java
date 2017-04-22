package lbn.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import lbn.dungeoncore.Main;

public class DatabaseConnection {
	// public static void main(String[] args) throws SQLException,
	// ClassNotFoundException {
	//
	// long currentTimeMillis = System.currentTimeMillis();
	// for (int i = 0; i < 100; i++) {
	// Connection connection =
	// DriverManager.getConnection("jdbc:sqlite:F:\\Users\\Ken\\Download\\sqlite-tools-win32-x86-3150200\\thelow.db");
	// connection.close();
	// }
	// }

	static Connection con;

	public static Connection getConnection() {
		try {
			if (con == null) {
				Class.forName("org.sqlite.JDBC");
				return DriverManager.getConnection(
						"jdbc:sqlite:" + Main.dataFolder + File.separator + "data" + File.separator + "thelow.db");
			} else {
				return con;
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
}
