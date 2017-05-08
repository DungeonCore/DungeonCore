package net.l_bulb.dungeoncore.player.reincarnation.imple;

import net.l_bulb.dungeoncore.api.PlayerStatusType;

import org.bukkit.Material;

public class MaxHpReincarnation extends AbstractAbilityReincarnation {

  @Override
  public String getTitle() {
    return "最大体力増加";
  }

  @Override
  public String getDetail() {
    return "最大体力が増加する";
  }

  @Override
  public Material getMaterial() {
    return Material.APPLE;
  }

  @Override
  protected PlayerStatusType getStatusType() {
    return PlayerStatusType.MAX_HP;
  }

  @Override
  protected double getAddValue() {
    return 2;
  }

}
