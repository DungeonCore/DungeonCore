package lbn.database.connection.gamedatasql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import lbn.database.connection.DatabaseConnection;
import lbn.player.playerIO.PlayerIOData;
import lbn.player.playerIO.PlayerIODataManager;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class PlayerDataSql {
	UUID id;
	public PlayerDataSql(OfflinePlayer p) {
		id = p.getUniqueId();
	}

	public void setSwordLevel(int l) {
		executeIntSql(l, "sword_level");
	}

	public void setBowLevel(int l) {
		executeIntSql(l, "bow_level");
	}
	public void setMagicLevel(int l) {
		executeIntSql(l, "magic_level");
	}

	public void setGalions(int l) {
		executeIntSql(l, "galions");
	}

	public void executeIntSql(int l, String string) {
//		try {
//			Connection connection = DatabaseConnection.getConnection();
//			Statement createStatement = connection.createStatement();
//
//			StringBuilder stringBuilder = new StringBuilder();
//			stringBuilder.append("update player set ");
//			stringBuilder.append(string);
//			stringBuilder.append(" = ");
//			stringBuilder.append(l);
//			stringBuilder.append(" where uuid=");
//			stringBuilder.append(id);
//			createStatement.executeUpdate(stringBuilder.toString());
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
	}

//	public void executeTextSql(String text, String string) {
//		try {
//			Connection connection = DatabaseConnection.getConnection();
//			Statement createStatement = connection.createStatement();
//
//			StringBuilder stringBuilder = new StringBuilder();
//			stringBuilder.append("update player set ");
//			stringBuilder.append(string);
//			stringBuilder.append(" = '");
//			stringBuilder.append(text);
//			stringBuilder.append("' where uuid=");
//			stringBuilder.append(id);
//			createStatement.executeUpdate(stringBuilder.toString());
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}

	/**
	 * プレイヤーがログインした時の処理
	 * @param type
	 * @param p
	 */
	public void insertIfNotExist(String type, Player p) {
		try {
			Connection connection = DatabaseConnection.getConnection();
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("select * from player where type = '" + type + "' and uuid='" + id + "'");
			if (rs.next()) {
				PlayerIOData playerIOData = new PlayerIOData(rs);
				playerIOData.update(p);
			} else {
				stmt.close();
				PreparedStatement pstmt = connection.prepareStatement("insert into values(?, ?, ?, ?, ?, ?, ?, ?, ?)");
				pstmt.setString(1, p.getUniqueId().toString());
				pstmt.setString(2, type);
				pstmt.setInt(3, 0);
				pstmt.setInt(4, 0);
				pstmt.setInt(5, 0);
				pstmt.setInt(6, 0);
				pstmt.setInt(7, 0);
				pstmt.setInt(8, 0);
				pstmt.setInt(9, 0);
				int executeUpdate = pstmt.executeUpdate();
				if (executeUpdate != 1) {
					PlayerIODataManager.kickPlayerByLoadData(p);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			PlayerIODataManager.kickPlayerByLoadData(p);
		}
	}
}
