package net.l_bulb.dungeoncore.dungeon.contents.item.magic.normalItems.magicExcutor;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.common.dropingEntity.DamagedFallingBlockForPlayer;
import net.l_bulb.dungeoncore.item.ItemInterface;
import net.l_bulb.dungeoncore.item.ItemManager;
import net.l_bulb.dungeoncore.item.itemInterface.AvailableLevelItemable;
import net.l_bulb.dungeoncore.item.itemInterface.MagicExcuteable;
import net.l_bulb.dungeoncore.item.itemInterface.Strengthenable;

public abstract class FallingBlockMagicExcutor implements MagicExcuteable {
  int availableLevel = 0;
  int strengthLevel = 0;
  String id;

  ItemStack item;

  public FallingBlockMagicExcutor(ItemStack item, String id) {
    ItemInterface customItem = ItemManager.getCustomItem(item);
    if (customItem instanceof Strengthenable) {
      strengthLevel = ((Strengthenable) customItem).getMaxStrengthCount();
    }
    if (customItem instanceof AvailableLevelItemable) {
      availableLevel = ((AvailableLevelItemable) customItem).getAvailableLevel();
    }
    this.id = id;
    this.item = item;
  }

  @Override
  public int getCooltimeTick(ItemStack item) {
    return 15;
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public ItemStack getItem() {
    return item;
  }

  @Override
  public int getNeedMagicPoint() {
    // return (int) (3 * availableLevel * 0.1);
    return 4;
  }

  @Override
  public boolean isShowMessageIfUnderCooltime() {
    return false;
  }

  @Override
  public void excuteMagic(Player p, PlayerInteractEvent e) {
    getDamagedFallingBlock(p, e).runTaskTimer();
  }

  abstract protected DamagedFallingBlockForPlayer getDamagedFallingBlock(Player p, PlayerInteractEvent e);
}
