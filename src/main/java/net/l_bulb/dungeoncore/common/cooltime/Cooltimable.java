package net.l_bulb.dungeoncore.common.cooltime;

import org.bukkit.inventory.ItemStack;

public interface Cooltimable {
  int getCooltimeTick(ItemStack item);

  String getId();
}
