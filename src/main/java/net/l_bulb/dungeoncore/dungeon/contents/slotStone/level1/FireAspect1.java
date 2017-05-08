package net.l_bulb.dungeoncore.dungeon.contents.slotStone.level1;

import net.l_bulb.dungeoncore.common.event.player.PlayerCombatEntityEvent;
import net.l_bulb.dungeoncore.item.slot.SlotLevel;
import net.l_bulb.dungeoncore.item.slot.slot.CombatSlot;

import org.bukkit.ChatColor;

public class FireAspect1 extends CombatSlot {

  @Override
  public String getSlotName() {
    return "レーヴァティン Level1";
  }

  @Override
  public String getSlotDetail() {
    return "攻撃を与えた敵に延焼効果(小)を付与";
  }

  @Override
  public String getId() {
    return "slot_fire_1";
  }

  @Override
  public ChatColor getNameColor() {
    return ChatColor.RED;
  }

  @Override
  public SlotLevel getLevel() {
    return SlotLevel.LEVEL1;
  }

  @Override
  public void onCombat(PlayerCombatEntityEvent e) {
    e.getEnemy().setFireTicks(20 * 2);
  }

}
