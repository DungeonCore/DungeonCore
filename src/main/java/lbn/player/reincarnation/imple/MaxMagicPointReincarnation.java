package lbn.player.reincarnation.imple;

import org.bukkit.Material;

import lbn.api.PlayerStatusType;

public class MaxMagicPointReincarnation extends AbstractAbilityReincarnation {

  @Override
  public String getTitle() {
    return "最大マジックポイント増加";
  }

  @Override
  public String getDetail() {
    return "最大マジックポイント増加させる";
  }

  @Override
  public Material getMaterial() {
    return Material.EXP_BOTTLE;
  }

  @Override
  protected PlayerStatusType getStatusType() {
    return PlayerStatusType.MAX_MAGIC_POINT;
  }

  @Override
  protected double getAddValue() {
    return 15;
  }
}
