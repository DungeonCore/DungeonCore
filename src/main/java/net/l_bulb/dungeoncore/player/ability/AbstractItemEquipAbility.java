package net.l_bulb.dungeoncore.player.ability;

import net.l_bulb.dungeoncore.api.player.AbilityInterface;

public abstract class AbstractItemEquipAbility implements AbilityInterface {
  @Override
  public AbilityType getAbilityType() {
    return AbilityType.SET_ITEM_ABILITY;
  }
}
