package lbn.player.playerIO;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.libs.com.google.gson.Gson;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import lbn.Config;
import lbn.chest.wireless.WireLessChestManager;
import lbn.dungeoncore.Main;
import lbn.util.InOutputUtil;
import lbn.util.LbnRunnable;
import lbn.util.Message;

public class PlayerIODataManager {
	public static PlayerIOData getLoadData(UUID id, String saveType) {
		try {
			PlayerIOData fromJson = null;
			if (saveType.equals("l70")) {
//				fromJson = new PlayerIODataLevel70(p);
			} else {
				ArrayList<String> readFile = InOutputUtil.readFile("player_data" + File.separator + id + "_" + saveType + ".text");
				if (readFile.isEmpty()) {
					return null;
				}

				String json = StringUtils.join(readFile.iterator(), "");
				Gson gson = new Gson();
				fromJson = gson.fromJson(json, PlayerIOData.class);

				if (fromJson == null) {
					throw new RuntimeException("save data json is invalid!! player:" + id + ", type:" + saveType);
				}
			}
			return fromJson;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static PlayerIOData load(Player p, String saveType) {
		try {
			PlayerIOData fromJson;

			if (saveType.equals("l70")) {
				fromJson = new PlayerIODataLevel70(p);
			} else {
				ArrayList<String> readFile = InOutputUtil.readFile("player_data" + File.separator + p.getUniqueId() + "_" + saveType + ".text");
				if (readFile.isEmpty()) {
					return null;
				}

				String json = StringUtils.join(readFile.iterator(), "");
				Gson gson = new Gson();
				fromJson = gson.fromJson(json, PlayerIOData.class);

				if (fromJson == null) {
					throw new RuntimeException("save data json is invalid!! player:" + p.getCustomName() + ", type:" + saveType);
				}
			}

			//更新する
			fromJson.update(p);
			loadErrorList.remove(p.getUniqueId());

			//チェストをロードする
			WireLessChestManager.getInstance().load(p);
			return fromJson;
		} catch (Exception e) {
			e.printStackTrace();
			kickPlayerByLoadData(p);
			return null;
		}
	}

	public static void kickPlayerByLoadData(Player p) {
		loadErrorList.add(p.getUniqueId());
		Message.sendMessage(p, ChatColor.RED + "PlayerDataのロードに失敗しました。\n"
				+ ChatColor.RED + "管理者(twitter:{0})まで連絡ください。\n "
						+ "ChatColor.RED + 10秒後に自動的にログアウトします。", Config.DEVELOPER_TWITTER_ID);
		new BukkitRunnable() {
			@Override
			public void run() {
				p.kickPlayer("sorry, System cant load your game data. please tell developer (twitter:" + Config.DEVELOPER_TWITTER_ID + ")\n"
						+ "申し訳ありません。あなたのゲームデータをロード出来ませんでした。 開発者(twitter: "+ Config.DEVELOPER_TWITTER_ID + ")まで連絡ください。");
			}
		}.runTaskLater(Main.plugin, 20 * 10);
	}

	public static HashSet<UUID> loadErrorList = new HashSet<UUID>();

	public static PlayerIOData load(Player p) {
		String string = PlayerLastSaveType.getType(p);
		if (string == null) {
			return load(p, "A");
		} else {
			return load(p, string);
		}
	}

	public static void allLoad() {
		Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
		for (Player player : onlinePlayers) {
			load(player);
		}
	}

	public static void allSave(boolean instant) {
		PlayerLastSaveType.save();

		Player[] onlinePlayers = Bukkit.getOnlinePlayers().toArray(new Player[0]);
		if (instant) {
			for (Player player : onlinePlayers) {
				save(player);
			}
		} else {
			new LbnRunnable() {
				@Override
				public void run2() {
					int i = getRunCount();
					if (i >= onlinePlayers.length) {
						cancel();
						return;
					}
					Player p = onlinePlayers[i];
					save(p);
				}
			}.runTaskTimer(1);
		}
	}

	public static void saveAsZip() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HHmmss");
		String format = sdf.format(new Date());
		String fileName = "backup_data_" + format + ".zip";
		InOutputUtil.compressDirectory(new File(Main.dataFolder + File.separator + "debug" + File.separator + fileName).getAbsolutePath()
				, Main.dataFolder  + File.separator + "data");
	}

	public static void save(Player player) {
		String type = PlayerLastSaveType.getType(player);
		PlayerIOData playerIOData;
		if ("l70".equals(type)) {
			playerIOData = new PlayerIODataLevel70(player);
		} else {
			playerIOData = new PlayerIOData(player);
		}
		playerIOData.save(player);
//		WireLessChestManager.getInstance().save(player);
	}

	public static void remove(Player p) {
		PlayerIOData.remove(p);
	}
}
