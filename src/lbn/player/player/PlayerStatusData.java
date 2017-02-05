package lbn.player.player;

import java.util.HashMap;
import java.util.Map.Entry;

import lbn.api.PlayerStatusType;
import lbn.api.player.TheLowPlayer;
import lbn.player.ability.AbilityInterface;
import lbn.player.ability.AbilityType;

import org.bukkit.entity.Player;

import com.google.common.collect.HashMultimap;

/**
 * プレイヤーの追加ステータスを変更・取得を行う			<br />
 * 追加ステータスの変更はAbilityを用いて行う				<br />
 * @see AbilityInterface
 *
 */
public class PlayerStatusData {
	//実際のステータスデータ値をいれておくMap
	protected HashMap<PlayerStatusType, Double> dataMapDouble = new HashMap<PlayerStatusType, Double>();

	/**
	 * 現在適応中のAbilityをセットするためのMap
	 */
	HashMultimap<AbilityType, AbilityInterface> abilityType = HashMultimap.create();

	/**
	 * 現在適応中のAbilityをセットするためのMap
	 */
	HashMap<String, AbilityInterface> idMap = new HashMap<>();

	TheLowPlayer player;

	public PlayerStatusData(TheLowPlayer player) {
		dataMapDouble.put(PlayerStatusType.MAX_HP, 20.0);
		dataMapDouble.put(PlayerStatusType.MAX_MAGIC_POINT, 100.0);
		this.player = player;
	}

	/**
	 * 指定したAbilityをセットします
	 * @param status
	 * @param data
	 */
	public void addData(AbilityInterface ability) {
		//過去に登録されたAbilityを取得
		AbilityInterface beforeAbility = idMap.get(ability.getId());
		//もし過去に存在したAbilityがある場合は削除する
		if (beforeAbility != null) {
			unsafeDeapplayAbility(beforeAbility);
		}
		//今のを適応させる
		unsafeApplayAbility(ability);

		//データを残す
		abilityType.put(ability.getAbilityType(), ability);
		idMap.put(ability.getId(), ability);

		//データを適応させる
		applyAllAbility();
	}

	/**
	 * 指定したAbilityを削除します
	 * @param status
	 * @param data
	 */
	public void removeData(AbilityInterface ability) {
		//過去に登録されたAbilityを取得
		AbilityInterface beforeAbility = idMap.get(ability.getId());
		//もし過去に存在したAbilityがない場合は無視する
		if (beforeAbility == null) {
			return;
		}
		//前のを削除する
		unsafeDeapplayAbility(beforeAbility);

		//データを削除する
		abilityType.remove(beforeAbility.getAbilityType(), beforeAbility);
		idMap.remove(beforeAbility.getId());

		//データを適応させる
		applyAllAbility();
	}

	/**
	 * このメソッドではすでにAbilityが対応済みか、対応済みでないかチェックを行わないでAbilityを適応します。
	 * 事前に適応されてないことを確認しないと効果が重複する可能性があります
	 * @param ability
	 */
	private void unsafeApplayAbility(AbilityInterface ability) {
		HashMap<PlayerStatusType, Double> dataMap = ability.getAbilityMap();
		//適応効果がない場合は何もしない
		if (dataMap == null) {
			return;
		}

		//1つずつ効果を追加していく
		for (Entry<PlayerStatusType, Double> entry : dataMap.entrySet()) {
			//もしすでにデータがある場合はそれに追加する
			if (dataMapDouble.containsKey(entry.getKey())) {
				dataMapDouble.put(entry.getKey(), dataMapDouble.get(entry.getKey()) + entry.getValue());
			} else {
				dataMapDouble.put(entry.getKey(), entry.getValue());
			}
		}
	}

	/**
	 * このメソッドではすでにAbilityが対応済みか、対応済みでないかチェックを行わないでAbilityを削除します。
	 * 事前に適応されてないことを確認しないと効果がついていないのにステータスが減少する可能性があります
	 * @param ability
	 */
	private void unsafeDeapplayAbility(AbilityInterface ability) {
		HashMap<PlayerStatusType, Double> dataMap = ability.getAbilityMap();
		//適応効果がない場合は何もしない
		if (dataMap == null) {
			return;
		}

		//1つずつ効果を削除していく
		for (Entry<PlayerStatusType, Double> entry : dataMap.entrySet()) {
			dataMapDouble.put(entry.getKey(), dataMapDouble.get(entry.getKey()) - entry.getValue());
		}
	}

	/**
	 * 指定したStatusデータを取得します
	 * @param status
	 * @return
	 */
	public double getData(PlayerStatusType status) {
		if (dataMapDouble.containsKey(status)) {
			return dataMapDouble.get(status);
		}
		return 0;
	}

	/**
	 * プレイヤーデータを実際のPlayerに適応させる
	 * @param p
	 */
	protected void applyAllAbility() {
		Player onlinePlayer = player.getOnlinePlayer();
		if (onlinePlayer == null || !onlinePlayer.isOnline()) {
			return;
		}
		if (dataMapDouble.containsKey(PlayerStatusType.MAX_HP)) {
			onlinePlayer.setMaxHealth(getData(PlayerStatusType.MAX_HP));
		}
	}
}
