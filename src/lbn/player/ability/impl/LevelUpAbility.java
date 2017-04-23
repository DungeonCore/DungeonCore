package lbn.player.ability.impl;

import java.util.HashMap;

import lbn.api.PlayerStatusType;
import lbn.api.player.AbilityInterface;
import lbn.player.ability.AbilityType;

public class LevelUpAbility implements AbilityInterface {

	private int nextMainLevel;

	public LevelUpAbility(int nextMainLevel) {
		this.nextMainLevel = nextMainLevel;
	}

	@Override
	public String getId() {
		return "MainLevelUp";
	}

	HashMap<PlayerStatusType, Double> abilityMap = new HashMap<PlayerStatusType, Double>();
	@Override
	public HashMap<PlayerStatusType, Double> getAbilityMap() {
		abilityMap.put(PlayerStatusType.MAX_HP, nextMainLevel /3.0);
		return abilityMap;
	}

	@Override
	public AbilityType getAbilityType() {
		return AbilityType.LEVEL_UP;
	}

}
