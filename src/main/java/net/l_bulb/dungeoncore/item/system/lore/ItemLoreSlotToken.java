package net.l_bulb.dungeoncore.item.system.lore;

import org.bukkit.ChatColor;

public class ItemLoreSlotToken extends ItemLoreToken {

  public ItemLoreSlotToken(String title) {
    super(title, false);
  }

  public ItemLoreSlotToken(int slotCount) {
    super(ChatColor.GREEN + "[SLOT] " + ChatColor.AQUA + " 最大" + slotCount + "個", false);
  }

  @Override
  public String getTitle() {
    return TITLE_SLOT;
  }

}
