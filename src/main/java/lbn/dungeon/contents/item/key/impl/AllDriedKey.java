package lbn.dungeon.contents.item.key.impl;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public class AllDriedKey extends SuikaCastle {
  private AllDriedKey(String no, String loc) {
    super(no, loc);
  }

  @Override
  public String getItemName() {
    return ChatColor.RED + "All Dried key " + no;
  }

  @Override
  protected Material getMaterial() {
    return Material.TRIPWIRE_HOOK;
  }

  @Override
  public String[] getDetail() {
    return new String[] { "All Driedで使用可能", loc };
  }

  public static AllDriedKey[] getAllKey() {
    return new AllDriedKey[] { getKey1(), getKey2() };
  }

  public static AllDriedKey getKey1() {
    return new AllDriedKey("Ⅰ", "x:1447 y:3 z:263");
  }

  public static AllDriedKey getKey2() {
    return new AllDriedKey("Ⅱ", "x:1532 y:7 z:253");
  }
}
