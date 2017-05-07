package lbn.player.ability;

import lbn.api.player.AbilityInterface;

public abstract class AbstractReincarnationAbility implements AbilityInterface {
  @Override
  public AbilityType getAbilityType() {
    return AbilityType.REINCARNATION_ABILITY;
  }
}
