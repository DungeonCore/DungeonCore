package net.l_bulb.dungeoncore.dungeon.contents.slotStone.level2;

import net.l_bulb.dungeoncore.common.event.player.PlayerCombatEntityEvent;
import net.l_bulb.dungeoncore.item.slot.SlotLevel;
import net.l_bulb.dungeoncore.item.slot.slot.CombatSlot;

import org.bukkit.ChatColor;

public class FireAspect2 extends CombatSlot {

  @Override
  public String getSlotName() {
    return "レーヴァティン Level2";
  }

  @Override
  public String getSlotDetail() {
    return "攻撃を与えた敵に延焼効果(中)を付与";
  }

  @Override
  public String getId() {
    return "slot_fire_2";
  }

  @Override
  public ChatColor getNameColor() {
    return ChatColor.RED;
  }

  @Override
  public SlotLevel getLevel() {
    return SlotLevel.LEVEL2;
  }

  @Override
  public void onCombat(PlayerCombatEntityEvent e) {
    e.getEnemy().setFireTicks(20 * 6);
  }

}
