package net.l_bulb.dungeoncore.player.status.detail;

import org.bukkit.Material;

import net.l_bulb.dungeoncore.api.LevelType;
import net.l_bulb.dungeoncore.api.player.TheLowPlayer;
import net.l_bulb.dungeoncore.player.status.IStatusDetail;

public class SwordStatusDetail extends IStatusDetail {
  public SwordStatusDetail(TheLowPlayer p) {
    super(p);
  }

  @Override
  public String[] getIndexDetail() {
    return new String[] { "近接戦闘で敵を倒すたびに", "レベルが上がっていきます。" };
  }

  @Override
  public String[] getDetailByLevel(int level) {
    return new String[] {};
  }

  @Override
  public String getDisplayName() {
    return "SWORD LEVEL";
  }

  @Override
  public Material getViewIconMaterial() {
    return Material.DIAMOND_SWORD;
  }

  @Override
  public LevelType getLevelType() {
    return LevelType.SWORD;
  }

}
