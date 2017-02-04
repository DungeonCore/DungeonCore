package lbn.npc;

import java.util.HashMap;

import lbn.dungeoncore.SpletSheet.AbstractSheetRunable;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.EntityType;

/**
 * NPCのデータを管理するためのクラス
 * @author KENSUKE
 *
 */
public class VillagerData {
	static HashMap<String, VillagerData> villagerMap = new HashMap<String, VillagerData>();

	public static void registSpletsheetVillager(CommandSender p ,String name, String type, String texts, String location, String adult, String data, String mobtype, String skin) {
		VillagerData villagerData = new VillagerData(p, name, type, texts, location, adult, data, mobtype, skin);
		if (villagerData.isError) {
			if (!(p instanceof ConsoleCommandSender)) {
				p.sendMessage("エラーがあったため、スキップしました。[" + StringUtils.join(new Object[]{name, type, texts}, ",") + "]");
			}
			return;
		}
		villagerMap.put(name, villagerData);

		//もしまだNPCが登録されていなければ新しく登録する
		VillagerNpc villagerNpc = NpcManager.getVillagerNpc(name);
		if (villagerNpc == null) {
			NpcManager.regist(new VillagerNpc(villagerData));
		} else {
		//登録されている時はデータを上書きする
			villagerNpc.data = villagerData;
			//NPCを更新する
			villagerNpc.updateNpc();
		}
	}

	public static VillagerData getInstance(String name) {
		return villagerMap.get(name);
	}

	String name;
	String data;
	VillagerType type;
	String[] texts;
	boolean isAdult = true;

	EntityType entityType = EntityType.VILLAGER;

	String skin = null;

	boolean isError = false;

	private VillagerData(CommandSender p, String name, String type, String texts, String location, String adult, String data, String mobtype, String skin) {
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

		if ("子供".equals(adult)) {
			isAdult = false;
		}

		try {
			entityType = EntityType.valueOf(mobtype);
		} catch (Exception e) {
			entityType = EntityType.VILLAGER;
		}

		this.skin = skin;

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

	public String getSkin() {
		return skin;
	}

	public EntityType getEntityType() {
		if (entityType == null) {
			return EntityType.VILLAGER;
		}
		return entityType;
	}

	@Override
	public String toString() {
		return StringUtils.join(new Object[]{"name:", name, ", type:", type,  ", texts:", texts});
	}

}
