package lbn.dungeon.contents.item.key.impl;

import org.bukkit.Location;
import org.bukkit.Material;

import lbn.item.customItem.key.CommandBlockExceteKey;

public class NativeUnderground extends CommandBlockExceteKey {

  @Override
  public String getItemName() {
    return "The Natives Key";
  }

  @Override
  protected Material getMaterial() {
    return Material.GHAST_TEAR;
  }

  @Override
  public String[] getDetail() {
    return new String[] { "Native Undergroundで使用可能", "x:1534 y:38 z:243" };
  }

  @Override
  public String getId() {
    return "The Natives Key";
  }

  @Override
  protected String getDungeonName() {
    return null;
  }

  @Override
  protected Location getDungeonLocation() {
    return null;
  }
}
