package lbn.player.ability;


public abstract class AbstractItemEquipAbility implements AbilityInterface {
	@Override
	public AbilityType getAbilityType() {
		return AbilityType.ITEM_ABILITY;
	}
}
