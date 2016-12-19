package main.player.playerIO;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

import main.money.galion.GalionEditReason;
import main.money.galion.GalionManager;
import main.player.status.StatusAddReason;
import main.player.status.bowStatus.BowStatusManager;
import main.player.status.magicStatus.MagicStatusManager;
import main.player.status.swordStatus.SwordStatusManager;
import main.quest.Quest;
import main.quest.QuestData;
import main.quest.QuestManager;
import main.quest.quest.NullQuest;
import main.util.DungeonLog;
import main.util.InOutputUtil;

import org.bukkit.craftbukkit.libs.com.google.gson.Gson;
import org.bukkit.craftbukkit.libs.com.google.gson.GsonBuilder;
import org.bukkit.entity.Player;

public class PlayerIOData {
	/**
	 * JSONデシリアライズ用
	 * @param swordLevel
	 * @param swordExp
	 * @param bowLevel
	 * @param bowExp
	 * @param magicLevel
	 * @param magicExp
	 * @param galions
	 * @param complateQuestId
	 * @param doingQuestId
	 * @param saveType
	 * @param playerExpLevel
	 */
	public PlayerIOData(int swordLevel, int swordExp,
			int bowLevel, int bowExp, int magicLevel, int magicExp,
			int galions, String[] complateQuestId, String[] doingQuestId, String saveType, int playerExpLevel,
			Map<String, Integer> questDataMap) {
		this.swordLevel = swordLevel;
		this.swordExp = swordExp;
		this.bowLevel = bowLevel;
		this.bowExp = bowExp;
		this.magicLevel = magicLevel;
		this.magicExp = magicExp;
		this.galions = galions;
		this.complateQuestId = complateQuestId;
		this.doingQuestId = doingQuestId;
		this.saveType = saveType;
		this.playerExpLevel = playerExpLevel;
		this.questDataMap = questDataMap;
	}

	public PlayerIOData(Player p) {
		inputData(p);
	}

	public PlayerIOData(ResultSet rs) throws SQLException {
		this.swordLevel = rs.getInt("sword_level");
		this.swordExp = rs.getInt("sword_exp");
		this.bowLevel = rs.getInt("bow_level");
		this.bowExp = rs.getInt("bow_exp");
		this.magicLevel = rs.getInt("magic_level");
		this.magicExp = rs.getInt("magic_exp");
		this.galions = rs.getInt("galions");
		this.saveType = rs.getString("save_type");
		this.playerExpLevel = 100;
	}

	protected void inputData(Player p) {
		String type = PlayerLastSaveType.getType(p);
		if (type == null) {
			type = "A";
		}

		SwordStatusManager instance = SwordStatusManager.getInstance();
		swordLevel = instance.getLevel(p);
		swordExp = instance.getExp(p);

		BowStatusManager instance2 = BowStatusManager.getInstance();
		bowLevel = instance2.getLevel(p);
		bowExp = instance2.getExp(p);

		MagicStatusManager instance3 = MagicStatusManager.getInstance();
		magicLevel = instance3.getLevel(p);
		magicExp = instance3.getExp(p);

		Set<Quest> complateQuest = QuestManager.getComplateQuest(p);
		complateQuestId = new String[complateQuest.size()];
		int i = 0;
		for (Quest quest : complateQuest) {
			complateQuestId[i] = quest.getId();
			i++;
		}

		Set<Quest> doingQuest = QuestManager.getDoingQuest(p);
		doingQuestId = new String[doingQuest.size()];
		int j = 0;
		for (Quest quest : doingQuest) {
			doingQuestId[j] = quest.getId();
			j++;
		}

		playerExpLevel = p.getLevel();

		galions = GalionManager.getGalion(p);

		Map<String, Integer> jsonFormat = QuestData.getJsonFormat(p);
		questDataMap = jsonFormat;

		saveType = type;
	}

	public int swordLevel;
	public int swordExp;

	public int bowLevel;
	public int bowExp;

	public int magicLevel;
	public int magicExp;

	public int galions;

	public String saveType;

	public int playerExpLevel;

	public String[] complateQuestId;

	public String[] doingQuestId;

	public Map<String, Integer> questDataMap;

	public void update(Player p) {
		PlayerLastSaveType.regist(p, saveType);

		SwordStatusManager instance = SwordStatusManager.getInstance();
		instance.remove(p);
		instance.setLevel(p, swordLevel);
		instance.addExp(p, swordExp, StatusAddReason.system);

		BowStatusManager instance2 = BowStatusManager.getInstance();
		instance2.remove(p);
		instance2.setLevel(p, bowLevel);
		instance2.addExp(p, bowExp, StatusAddReason.system);

		MagicStatusManager instance3 = MagicStatusManager.getInstance();
		instance3.remove(p);
		instance3.setLevel(p, magicLevel);
		instance3.addExp(p, magicExp, StatusAddReason.system);

//		QuestManager.remove(p);
//		//クエスト追加
//		UUID uniqueId = p.getUniqueId();
//		for (String string : complateQuestId) {
//			QuestManager.complateQuestList.put(uniqueId, getQuest(string));
//		}
//		for (String string : doingQuestId) {
//			QuestManager.doingQuestList.put(uniqueId, getQuest(string));
//		}

		GalionManager.setGalion(p, galions, GalionEditReason.system);

//		QuestData.setJsonFormat(p, questDataMap);
	}

	public Quest getQuest(String string) {
		Quest questById = QuestManager.getQuestById(string);
		if (questById == null) {
			return new NullQuest(string);
		}
		return questById;
	}

	protected boolean isSave() {
		return true;
	}

	/**
	 * データをセーブする
	 * @param p
	 * @return
	 */
	public boolean save(Player p) {
		if (PlayerIODataManager.loadErrorList.contains(p.getUniqueId())) {
			DungeonLog.errorln("Load中にエラーが発生したためSaveできませんでした。(PlayerIOData)");
			return false;
		}

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(this);

		return InOutputUtil.write(json, "player_data" + File.separator + p.getUniqueId() + "_" + saveType + ".text");
	}

	public static void remove(Player p) {
		SwordStatusManager instance = SwordStatusManager.getInstance();
		instance.remove(p);

		BowStatusManager instance2 = BowStatusManager.getInstance();
		instance2.remove(p);

		MagicStatusManager instance3 = MagicStatusManager.getInstance();
		instance3.remove(p);

		QuestManager.remove(p);

		GalionManager.remove(p);
	}
}

