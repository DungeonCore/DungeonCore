package net.l_bulb.dungeoncore.player.ability;

import net.l_bulb.dungeoncore.api.player.AbilityInterface;

public abstract class AbstractReincarnationAbility implements AbilityInterface {
  @Override
  public AbilityType getAbilityType() {
    return AbilityType.REINCARNATION_ABILITY;
  }
}
