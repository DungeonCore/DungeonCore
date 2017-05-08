package net.l_bulb.dungeoncore.dungeon.contents.slotStone.level4;

import java.util.Random;

import net.l_bulb.dungeoncore.common.event.player.PlayerCombatEntityEvent;
import net.l_bulb.dungeoncore.item.slot.SlotLevel;
import net.l_bulb.dungeoncore.item.slot.slot.CombatSlot;
import net.l_bulb.dungeoncore.util.LivingEntityUtil;

import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;

public class CombatLightningStone extends CombatSlot {

  @Override
  public String getSlotName() {
    return "ライトニング +++";
  }

  @Override
  public String getSlotDetail() {
    return "攻撃をした時に、一定確率で雷を落とす";
  }

  @Override
  public String getId() {
    return "slot_combat_lightning";
  }

  @Override
  public ChatColor getNameColor() {
    return ChatColor.YELLOW;
  }

  @Override
  public SlotLevel getLevel() {
    return SlotLevel.LEVEL4;
  }

  static Random rnd = new Random();

  @Override
  public void onCombat(PlayerCombatEntityEvent e) {
    int nextInt = rnd.nextInt(4);
    if (nextInt == 0) {
      LivingEntity enemy = e.getEnemy();
      LivingEntityUtil.strikeLightningEffect(enemy.getLocation());
      enemy.damage(4.0);
      enemy.setFireTicks(3 * 20);
    }
  }

}
