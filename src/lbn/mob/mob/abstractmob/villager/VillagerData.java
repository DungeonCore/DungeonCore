package lbn.mob.mob.abstractmob.villager;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import lbn.dungeoncore.SpletSheet.AbstractComplexSheetRunable;
import lbn.dungeoncore.SpletSheet.AbstractSheetRunable;
import lbn.mob.AbstractMob;
import lbn.mob.MobHolder;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

public class VillagerData {
	static HashMap<String, VillagerData> villagerMap = new HashMap<String, VillagerData>();

	public static void registSpletsheetVillager(CommandSender p ,String name, String type, String quests, String texts, String location, String adult, String data) {
		VillagerData villagerData = new VillagerData(p, name, type, quests, texts, location, adult, data);
		if (villagerData.isError) {
			if (!(p instanceof ConsoleCommandSender)) {
				p.sendMessage("エラーがあったため、スキップしました。[" + StringUtils.join(new Object[]{name, type, quests, texts}, ",") + "]");
			}
			return;
		}
		villagerMap.put(name, villagerData);

		AbstractMob<?> mob = MobHolder.getMob(name);
		if (mob == null || mob.isNullMob()) {
			MobHolder.registMob(new SpletSheetVillager(villagerData));
		}
	}

	public static VillagerData getInstance(String name) {
		return villagerMap.get(name);
	}

	String name;
	String data;
	VillagerType type;
	Set<String> questList = new HashSet<>();
	String[] texts;
	boolean isAdult = true;

	Location loc = null;

	boolean isError = false;

	private VillagerData(CommandSender p, String name, String type, String quests, String texts, String location, String adult, String data) {
		this.name = name;
		if (name == null || name.isEmpty()) {
			sendMsg(p, "名前は絶対必要です。");
			isError = true;
		}

		this.type = VillagerType.getValue(type);
		if (this.type == null) {
			this.type = VillagerType.NORMAL;
			sendMsg(p, "typeは[normal, shop, BLACKSMITH]のみ許可されます");
			isError = true;
		}

		//データ処理
		if (this.type == VillagerType.SHOP) {
			//座標があるか確認する
			Location locationByString = AbstractSheetRunable.getLocationByString(data);
			if (locationByString != null) {
				this.data = data;
			}
		}

		if (texts != null) {
			this.texts = texts.split(",");
		} else {
			this.texts = new String[0];
		}

		if (location != null && !location.isEmpty()) {
			loc = AbstractComplexSheetRunable.getLocationByString(location);
			if (loc == null) {
				sendMsg(p, "locationが不正です");
				isError = true;
			}
		}

		if (quests != null) {
			//クエストを取得する
			for (String id : quests.split(",")) {
				questList.add(id);
			}
		}

		if ("子供".equals(adult)) {
			isAdult = false;
		}

	}

	protected void sendMsg(CommandSender p, String msg) {
		if (!(p instanceof ConsoleCommandSender)) {
			p.sendMessage(msg);
		}
	}

	public String getName() {
		return name;
	}

	public VillagerType getType() {
		return type;
	}

	public Set<String> getQuestList() {
		return questList;
	}

	public String[] getTexts() {
		return texts;
	}

	public boolean isError() {
		return isError;
	}

	public boolean isAdult() {
		return isAdult;
	}

	public String getData() {
		return data;
	}

	public Location getLocation() {
		return loc;
	}

	@Override
	public String toString() {
		return StringUtils.join(new Object[]{"name:", name, ", type:", type, ", quests:", questList, ", texts:", texts});
	}
}
