package net.l_bulb.dungeoncore.dungeon.contents.item.shootbow;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.common.event.player.PlayerCombatEntityEvent;
import net.l_bulb.dungeoncore.common.event.player.PlayerKillEntityEvent;
import net.l_bulb.dungeoncore.common.event.player.PlayerStrengthFinishEvent;
import net.l_bulb.dungeoncore.item.customItem.attackitem.SpreadSheetWeaponData;
import net.l_bulb.dungeoncore.item.customItem.itemAbstract.BowItem;
import net.l_bulb.dungeoncore.item.itemInterface.EntityKillable;
import net.l_bulb.dungeoncore.item.itemInterface.StrengthChangeItemable;
import net.l_bulb.dungeoncore.item.system.strength.StrengthOperator;
import net.l_bulb.dungeoncore.player.ItemType;

public class DebugBow extends BowItem implements StrengthChangeItemable, EntityKillable {

  public DebugBow() {
    super(new SpreadSheetWeaponDataForDebug());
  }

  @Override
  public String getItemName() {
    return "デバック弓";
  }

  @Override
  public String getId() {
    return "DebugBow";
  }

  @Override
  public void onPlayerStrengthFinishEvent(PlayerStrengthFinishEvent event) {
    super.onPlayerStrengthFinishEvent(event);

    availableLevel = event.getLevel();
  }

  @Override
  public void excuteOnLeftClick(PlayerInteractEvent e) {
    super.excuteOnLeftClick(e);

    e.getPlayer().sendMessage("ランク１５ ダメージ：" + getAttackItemDamage(0));

    ItemStack item = e.getItem();
    int level = StrengthOperator.getLevel(item);
    availableLevel = level;

    if (e.getPlayer().isSneaking()) {
      Bukkit.dispatchCommand(e.getPlayer(), "statusCommand MAIN_LEVEL set " + level);
    }
  }

  public static int availableLevel = 0;

  @Override
  public int getAvailableLevel() {
    return availableLevel * 10;
  }

  @Override
  public void onCombatEntity(PlayerCombatEntityEvent e) {
    super.onCombatEntity(e);
    e.getPlayer().sendMessage("debug bow:onCombatEntity");
  }

  @Override
  public void onKillEvent(PlayerKillEntityEvent e) {
    e.getPlayer().sendMessage("debug bow:onKillEvent");
  }
}

class SpreadSheetWeaponDataForDebug extends SpreadSheetWeaponData {
  public SpreadSheetWeaponDataForDebug() {}

  @Override
  public String getId() {
    return "debug_bow";
  }

  @Override
  public String getName() {
    return "デバック用弓";
  }

  @Override
  public int getRank() {
    return 15;
  }

  @Override
  public ItemType getItemType() {
    return ItemType.BOW;
  }

  @Override
  public ItemStack getItemStack() {
    return new ItemStack(Material.BOW);
  }
}
