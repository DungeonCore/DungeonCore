package lbn.player.ability.impl;

import java.util.HashMap;

import lbn.api.PlayerStatusType;
import lbn.player.ability.AbstractRevivalAbility;

/**
 * PlayerStatusに結びついたAbility
 *
 */
public class RevivalAbilityStatusWrapper extends AbstractRevivalAbility{
	HashMap<PlayerStatusType, Double> ability = new HashMap<PlayerStatusType, Double>();

	private RevivalAbilityStatusWrapper(PlayerStatusType status, double addValue) {
		ability.put(status, addValue);

		id = getAbilityType() + "_" + status.toString();
	}
	String id;

	@Override
	public String getId() {
		return id;
	}

	static HashMap<PlayerStatusType, Integer> addValueMap = new HashMap<PlayerStatusType, Integer>();
	static {
		addValueMap.put(PlayerStatusType.SWORD_ATTACK, 3);
		addValueMap.put(PlayerStatusType.BOW_ATTACK, 3);
		addValueMap.put(PlayerStatusType.MAGIC_ATTACK, 3);
		addValueMap.put(PlayerStatusType.MAX_MAGIC_POINT, 10);
		addValueMap.put(PlayerStatusType.MAX_HP, 2);
	}

	/**
	 * 指定したPlayerStatusのインスタンスを取得する　もし存在しない場合はnullを返す
	 * @param status
	 * @return
	 */
	public static RevivalAbilityStatusWrapper getAbilityInstance(PlayerStatusType status) {
		if (addValueMap.containsKey(status)) {
			return new RevivalAbilityStatusWrapper(status, addValueMap.get(status));
		}
		return null;
	}

	@Override
	public HashMap<PlayerStatusType, Double> getAbilityMap() {
		return ability;
	}
}
