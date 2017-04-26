package lbn.player.ability.impl;

import java.util.HashMap;

import lbn.api.PlayerStatusType;
import lbn.item.setItem.SetItemPartsType;
import lbn.player.ability.AbstractItemEquipAbility;

public class SetItemAbility extends AbstractItemEquipAbility{

	public SetItemAbility(SetItemPartsType partsType) {
		id = "setitemability_" + partsType;
	}

	String id;
	public SetItemAbility(String id) {
		this.id = id;
	}

	@Override
	public String getId() {
		return id;
	}

	HashMap<PlayerStatusType, Double> statusMap = new HashMap<PlayerStatusType, Double>();

	@Override
	public HashMap<PlayerStatusType, Double> getAbilityMap() {
		return statusMap;
	}

	/**
	 * 追加ステータスを登録する
	 * @param type ステータスの種類
	 * @param value 増加値
	 */
	public void addData(PlayerStatusType type, double value) {
		statusMap.put(type, value);
	}
}
