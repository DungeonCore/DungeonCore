package lbn.database.connection.systemsql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.bukkit.configuration.file.FileConfiguration;

import lbn.database.connection.DatabaseConnection;
import lbn.dungeoncore.Main;
import lbn.util.DungeonLog;

public abstract class SystemSqlRunnable {
	abstract int getExecuteId();

	abstract String getSql();

	public void execute() {
		Connection connection = DatabaseConnection.getConnection();
		if (connection != null) {
			try {
				PreparedStatement prepareStatement = connection.prepareStatement(getSql());
				prepareStatement.executeUpdate();

				//Log
				DungeonLog.printDevelopln("system sql:" + getSql());

				FileConfiguration config = Main.plugin.getConfig();
				config.set("systemsql", getExecuteId());
				Main.plugin.saveConfig();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
