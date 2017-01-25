package lbn.player.player;

import java.util.HashMap;
import java.util.UUID;

import lbn.common.event.player.PlayerChangeGalionsEvent;
import lbn.common.event.player.PlayerChangeStatusExpEvent;
import lbn.common.event.player.PlayerChangeStatusLevelEvent;
import lbn.common.other.DungeonData;
import lbn.common.other.DungeonList;
import lbn.money.GalionEditReason;
import lbn.player.TheLowLevelType;
import lbn.player.TheLowPlayer;
import lbn.player.status.StatusAddReason;
import lbn.util.Message;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class CustomPlayer implements TheLowPlayer{
	public CustomPlayer(OfflinePlayer p) {
		this.player = p;
	}

	public void init() {
		theLowLevelMap.put(TheLowLevelType.SWORD, 0);
		theLowLevelMap.put(TheLowLevelType.BOW, 0);
		theLowLevelMap.put(TheLowLevelType.MAGIC, 0);
		theLowExpMap.put(TheLowLevelType.SWORD, 0);
		theLowExpMap.put(TheLowLevelType.BOW, 0);
		theLowExpMap.put(TheLowLevelType.MAGIC, 0);
	}

	HashMap<TheLowLevelType, Integer> theLowLevelMap = new HashMap<TheLowLevelType, Integer>();
	HashMap<TheLowLevelType, Integer> theLowExpMap = new HashMap<TheLowLevelType, Integer>();

	int galions = 0;

	OfflinePlayer player;

	int inDungeonId = -1;

	int maxMagicPointLevel = 100;

	@Override
	public int getTheLowLevel(TheLowLevelType type) {
		if (type == TheLowLevelType.MAIN) {
			return (int) ((getTheLowLevel(TheLowLevelType.SWORD) + getTheLowLevel(TheLowLevelType.BOW) + getTheLowLevel(TheLowLevelType.MAGIC))/ 3.0);
		}
		return theLowLevelMap.get(type);
	}

	@Override
	public void setTheLowLevel(TheLowLevelType type, int level) {
		//メインレベルが指定された時は全てのレベルをセットする
		if (type == TheLowLevelType.MAIN) {
			setTheLowLevel(TheLowLevelType.SWORD, level);
			setTheLowLevel(TheLowLevelType.BOW, level);
			setTheLowLevel(TheLowLevelType.MAGIC, level);
			return;
		}

		//最大レベルを超えていなければレベルを加算させない
		if (level <= getMaxLevel(type)) {
			theLowLevelMap.put(type, level);

			//Eventを呼ぶ
			new PlayerChangeStatusLevelEvent(this, level, type).callEvent();
		}
	}

	@Override
	public int getTheLowExp(TheLowLevelType type) {
		if (type == TheLowLevelType.MAIN) {
			return 0;
		}
		return theLowExpMap.get(type);
	}

	@Override
	public void addTheLowExp(TheLowLevelType type, int addExp, StatusAddReason reason) {
		//メインレベルが指定された時は全ての経験値をセットする
		if (type == TheLowLevelType.MAIN) {
			addTheLowExp(TheLowLevelType.SWORD, addExp, reason);
			addTheLowExp(TheLowLevelType.BOW, addExp, reason);
			addTheLowExp(TheLowLevelType.MAGIC, addExp, reason);
			return;
		}

		PlayerChangeStatusExpEvent event = new PlayerChangeStatusExpEvent(this, addExp, type, reason);
		event.callEvent();
		addExp = event.getAddExp();

		//現在の経験値
		int nowExp = getTheLowExp(type) + addExp;

		//現在のレベル
		int nowLevel = getTheLowLevel(type) + 1;

		for (; nowExp >= getNeedExp(type, nowLevel); nowLevel++) {
			//最大レベルを超えていたらレベルアップさせない
			if (getMaxLevel(type) < nowLevel) {
				nowExp = 0;
				break;
			}
			//レベルアップする
			levelUp(type, nowLevel);
			//必要な経験値を引く
			nowExp -= getNeedExp(type, nowLevel);
		}
		theLowExpMap.put(type, nowExp);
	}

	/**
	 * レベルをアップさせる
	 * @param type
	 * @param level
	 */
	protected void levelUp(TheLowLevelType type, int level) {
		setTheLowLevel(type, level);
		Message.sendMessage(this, ChatColor.GREEN + " === LEVEL UP === ");
	}

	@Override
	public int getMaxLevel(TheLowLevelType type) {
		return 63;
	}

	@Override
	public int getNeedExp(TheLowLevelType type, int level) {
		return (int) Math.ceil(180 + 1.3 * Math.pow(level, 2.5));
	}

	@Override
	public int getGalions() {
		return galions;
	}

	@Override
	public void setGalions(int galions, GalionEditReason reason) {
		addGalions(galions - this.galions, reason);
	}

	@Override
	public void addGalions(int galions, GalionEditReason reason) {
		PlayerChangeGalionsEvent event = new PlayerChangeGalionsEvent(this, galions, reason);
		Bukkit.getServer().getPluginManager().callEvent(event);
		this.galions += event.getAddGalions();
	}

	@Override
	public OfflinePlayer getOfflinePlayer() {
		return player;
	}

	@Override
	public Player getOnlinePlayer() {
		return player.getPlayer();
	}

	@Override
	public boolean isOnline() {
		return player.isOnline();
	}

	@Override
	public DungeonData getInDungeonId() {
		if (inDungeonId == -1) {
			return null;
		}
		return DungeonList.getDungeonByID(inDungeonId);
	}

	@Override
	public void setInDungeonId(DungeonData dungeon) {
		if (dungeon == null) {
			inDungeonId = -1;
		} else {
			inDungeonId = dungeon.getId();
		}
	}

	@Override
	public String getName() {
		return player.getName();
	}

	@Override
	public UUID getUUID() {
		return player.getUniqueId();
	}

	@Override
	public String getSaveType() {
		return "typeA";
	}

	long lastDeathMilleTime = -1;

	@Override
	public Long getLastDeathTimeMillis() {
		return lastDeathMilleTime;
	}

	@Override
	public void setLastDeathTimeMillis(long time) {
		this.lastDeathMilleTime = time;
	}

	@Override
	public int getMaxMagicPoint() {
		return maxMagicPointLevel;
	}

	@Override
	public void setMaxMagicPoint(int maxMagicPointLevel) {
		this.maxMagicPointLevel = maxMagicPointLevel;
	}

}
