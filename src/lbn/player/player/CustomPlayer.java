package lbn.player.player;

import java.util.UUID;

import lbn.api.PlayerStatusType;
import lbn.api.TheLowLevelType;
import lbn.api.player.TheLowPlayer;
import lbn.common.event.player.PlayerChangeGalionsEvent;
import lbn.common.event.player.PlayerChangeStatusExpEvent;
import lbn.common.event.player.PlayerChangeStatusLevelEvent;
import lbn.common.event.player.PlayerLevelUpEvent;
import lbn.common.other.DungeonData;
import lbn.common.other.DungeonList;
import lbn.money.GalionEditReason;
import lbn.player.ability.AbilityInterface;
import lbn.player.status.StatusAddReason;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class CustomPlayer implements TheLowPlayer{
	public CustomPlayer(OfflinePlayer p) {
		this.player = p;
	}

	/**
	 * プレイヤーデータ初期者処理
	 */
	public void init() {
		levelData = new PlayerLevelIntData(0, 0, 0);
		expData = new PlayerLevelIntData(0, 0, 0);
		maxLevelData = new PlayerLevelIntData(60, 65, 70);
	}

	//レベル
	PlayerLevelIntData levelData = null;

	//経験値
	PlayerLevelIntData expData = null;

	//最大レベル
	PlayerLevelIntData maxLevelData = null;

	//所持金
	int galions = 0;

	OfflinePlayer player;

	//現在いるダンジョンID
	int inDungeonId = -1;

	//最大マジックポイント
	int maxMagicPointLevel = 100;

	//PlayerStatusData
	PlayerStatusData playerStatusData = new PlayerStatusData(this);

	@Override
	public int getLevel(TheLowLevelType type) {
		//最大レベルを上回っていた場合は最大レベルを返す
		return Math.min(levelData.get(type), getMaxLevel(type));
	}

	@Override
	public void setLevel(TheLowLevelType type, int level) {
		//最大レベルを上回っていたら最大レベルを返す
		levelData.put(type, Math.min(level, getMaxLevel(type)));
		//Eventを呼ぶ
		new PlayerChangeStatusLevelEvent(this, level, type).callEvent();
	}

	@Override
	public int getExp(TheLowLevelType type) {
		if (type == TheLowLevelType.MAIN) {
			return 0;
		}
		return expData.get(type);
	}

	@Override
	public void addExp(TheLowLevelType type, int addExp, StatusAddReason reason) {
		//メインレベルが指定された時は全ての経験値をセットする
		if (type == TheLowLevelType.MAIN) {
			addExp(TheLowLevelType.SWORD, addExp, reason);
			addExp(TheLowLevelType.BOW, addExp, reason);
			addExp(TheLowLevelType.MAGIC, addExp, reason);
			return;
		}

		PlayerChangeStatusExpEvent event = new PlayerChangeStatusExpEvent(this, addExp, type, reason);
		event.callEvent();
		addExp = event.getAddExp();

		//現在の経験値
		int nowExp = getExp(type) + addExp;

		//現在のレベル
		int nowLevel = getLevel(type) + 1;

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
		expData.put(type, nowExp);
	}

	/**
	 * レベルをアップさせる
	 * @param type
	 * @param level
	 */
	protected void levelUp(TheLowLevelType type, int level) {
		//レベルをセットする
		setLevel(type, level);
		//eventの発生させる
		PlayerLevelUpEvent event = new PlayerLevelUpEvent(this, type);
		event.callEvent();
	}

	@Override
	public int getMaxLevel(TheLowLevelType type) {
		return maxLevelData.get(type);
	}

	@Override
	public void setMaxLevel(TheLowLevelType type, int value) {
		maxLevelData.put(type, value);
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

	String dataType = "typeA";

	@Override
	public String getSaveType() {
		return dataType;
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((dataType == null) ? 0 : dataType.hashCode());
		result = prime * result + ((player == null) ? 0 : player.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CustomPlayer other = (CustomPlayer) obj;
		if (dataType == null) {
			if (other.dataType != null)
				return false;
		} else if (!dataType.equals(other.dataType))
			return false;
		if (player == null) {
			if (other.player != null)
				return false;
		} else if (!player.equals(other.player))
			return false;
		return true;
	}

	@Override
	public void addAbility(AbilityInterface ability) {
		playerStatusData.addData(ability);
	}

	@Override
	public void removeAbility(AbilityInterface ability) {
		playerStatusData.removeData(ability);
	}

	@Override
	public double getStatusData(PlayerStatusType type) {
		return playerStatusData.getData(type);
	}
}
