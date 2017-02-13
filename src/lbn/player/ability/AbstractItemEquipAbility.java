package lbn.player.ability;

import lbn.api.player.AbilityInterface;


public abstract class AbstractItemEquipAbility implements AbilityInterface {
	@Override
	public AbilityType getAbilityType() {
		return AbilityType.SET_ITEM_ABILITY;
	}
}
