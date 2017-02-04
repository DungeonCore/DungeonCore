package lbn.player.ability;


public abstract class AbstractRevivalAbility implements AbilityInterface{
	@Override
	public AbilityType getAbilityType() {
		return AbilityType.REVIVAL_ABILITY;
	}
}
