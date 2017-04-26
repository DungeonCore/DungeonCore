package lbn.player.reincarnation.imple;

import lbn.api.LevelType;
import lbn.api.PlayerStatusType;

import org.bukkit.Material;

public class AttackDamageReincarnation extends AbstractAbilityReincarnation {

  LevelType levelType;

  public AttackDamageReincarnation(LevelType levelType) {
    this.levelType = levelType;
  }

  @Override
  public String getTitle() {
    return levelType.getWeaponName() + "の攻撃力増加";
  }

  @Override
  public String getDetail() {
    return getTitle();
  }

  @Override
  public Material getMaterial() {
    switch (levelType) {
      case SWORD:
        return Material.DIAMOND_SWORD;
      case MAGIC:
        return Material.DIAMOND_HOE;
      case BOW:
        return Material.BOW;
      default:
        return Material.STONE;
    }
  }

  @Override
  public boolean isShow(LevelType type, int count) {
    return type == levelType;
  }

  @Override
  protected PlayerStatusType getStatusType() {
    switch (levelType) {
      case SWORD:
        return PlayerStatusType.ADD_SWORD_ATTACK;
      case MAGIC:
        return PlayerStatusType.ADD_MAGIC_ATTACK;
      case BOW:
        return PlayerStatusType.ADD_BOW_ATTACK;
      default:
        throw new RuntimeException("invaild Level type:" + levelType);
    }
  }

  @Override
  protected double getAddValue() {
    return 3;
  }
}
