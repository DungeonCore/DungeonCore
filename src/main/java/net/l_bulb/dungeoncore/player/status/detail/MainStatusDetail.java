package net.l_bulb.dungeoncore.player.status.detail;

import net.l_bulb.dungeoncore.api.LevelType;
import net.l_bulb.dungeoncore.api.player.TheLowPlayer;
import net.l_bulb.dungeoncore.player.status.IStatusDetail;

import org.bukkit.Material;

public class MainStatusDetail extends IStatusDetail {
  public MainStatusDetail(TheLowPlayer p) {
    super(p);
  }

  @Override
  public String[] getIndexDetail() {
    return new String[] { "プレイヤー自身のレベルです" };
  }

  @Override
  public String[] getDetailByLevel(int level) {
    return new String[] {};
  }

  @Override
  public String getDisplayName() {
    return "PLAYER LEVEL";
  }

  @Override
  public Material getViewIconMaterial() {
    return Material.SKULL_ITEM;
  }

  @Override
  public LevelType getLevelType() {
    return LevelType.MAIN;
  }
}
