package net.l_bulb.dungeoncore.item.system.lore;

import org.bukkit.ChatColor;

public class ItemLoreSlotToken extends ItemLoreToken {

  public ItemLoreSlotToken(String title) {
    super(title, false);
  }

  public ItemLoreSlotToken(int maxSlotCount) {
    super(ChatColor.GREEN + "[SLOT] " + ChatColor.AQUA + " 最大" + maxSlotCount + "個", true);
  }

  @Override
  public String getTitle() {
    return TITLE_SLOT;
  }

}
