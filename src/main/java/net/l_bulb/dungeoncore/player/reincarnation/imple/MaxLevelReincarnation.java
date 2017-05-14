package net.l_bulb.dungeoncore.player.reincarnation.imple;

import org.bukkit.Material;

import net.l_bulb.dungeoncore.api.LevelType;
import net.l_bulb.dungeoncore.api.player.ReincarnationInterface;
import net.l_bulb.dungeoncore.api.player.TheLowPlayer;

public class MaxLevelReincarnation implements ReincarnationInterface {

  LevelType levelType;

  public MaxLevelReincarnation(LevelType levelType) {
    this.levelType = levelType;
  }

  @Override
  public boolean isShow(LevelType type, int count) {
    return levelType == type;
  }

  @Override
  public void addReincarnationEffect(TheLowPlayer p, LevelType levelType, int count) {
    // 最大Level取得
    int maxLevel = p.getMaxLevel(levelType);
    // 最大レベルを更新
    p.setMaxLevel(levelType, maxLevel + 5);
  }

  @Override
  public String getTitle() {
    return levelType.getName() + "の最大レベル増加";
  }

  @Override
  public String getDetail() {
    return levelType.getName() + "の最大レベル増加";
  }

  @Override
  public String getId() {
    return "maxlevel_" + levelType;
  }

  @Override
  public Material getMaterial() {
    return Material.ENCHANTMENT_TABLE;
  }

  @Override
  public int getItemStackData() {
    return 0;
  }

}
