package lbn.player.appendix;

import java.util.HashMap;

import lbn.api.PlayerStatusType;
import lbn.api.player.TheLowPlayer;

import org.bukkit.entity.Player;

public class PlayerAbilityIntData {
	protected HashMap<PlayerStatusType, Integer> dataMap = new HashMap<PlayerStatusType, Integer>();

	TheLowPlayer player;

	public PlayerAbilityIntData(TheLowPlayer player) {
		dataMap.put(PlayerStatusType.MAX_HP, 20);
		this.player = player;
	}

	/**
	 * 指定したデータを取得セットします
	 * @param status
	 * @param data
	 */
	public void setData(PlayerStatusType status, int data) {
		dataMap.put(status, data);

		//データを適応させる
		applyPlayerAbility(status);
	}

	/**
	 * 指定したデータを取得します
	 * @param status
	 * @return
	 */
	public int getData(PlayerStatusType status) {
		if (dataMap.containsKey(status)) {
			return dataMap.get(status);
		}
		return 0;
	}

	/**
	 * 指定したデータを現在のデータに追加します
	 * @param status
	 * @param data
	 */
	public void addData(PlayerStatusType status, int data) {
		//現在のデータ
		int nowData = 0;
		if (dataMap.containsKey(status)) {
			nowData = dataMap.get(status);
		}
		nowData += data;
		setData(status, nowData);

		//データを適応させる
		applyPlayerAbility(status);
	}

	/**
	 * もしStatusが登録されていたらTRUE
	 * @param status
	 * @return
	 */
	public boolean contains(PlayerStatusType status) {
		return dataMap.containsKey(status);
	}

	/**
	 * プレイヤーデータを実際のPlayerに適応させる
	 * @param p
	 */
	protected void applyAllPlayerAbility() {
		Player onlinePlayer = player.getOnlinePlayer();
		if (onlinePlayer == null || !onlinePlayer.isOnline()) {
			return;
		}
		if (dataMap.containsKey(PlayerStatusType.MAX_HP)) {
			onlinePlayer.setMaxHealth(getData(PlayerStatusType.MAX_HP));
		}
	}

	/**
	 * 必要あればプレイヤーデータを実際のPlayerに適応させる
	 * @param p
	 */
	protected void applyPlayerAbility(PlayerStatusType status) {
		//Online Playerを取得
		Player onlinePlayer = player.getOnlinePlayer();
		if (onlinePlayer == null || !onlinePlayer.isOnline()) {
			return;
		}

		//体力を適応
		if (status == PlayerStatusType.MAX_HP && dataMap.containsKey(PlayerStatusType.MAX_HP)) {
			onlinePlayer.setMaxHealth(getData(PlayerStatusType.MAX_HP));
		}
	}
}
