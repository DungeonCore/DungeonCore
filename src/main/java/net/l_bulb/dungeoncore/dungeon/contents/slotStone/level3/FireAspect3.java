package net.l_bulb.dungeoncore.dungeon.contents.slotStone.level3;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import net.l_bulb.dungeoncore.common.event.player.CombatEntityEvent;
import net.l_bulb.dungeoncore.item.slot.SlotLevel;
import net.l_bulb.dungeoncore.item.slot.magicstone.CombatSlot;
import net.l_bulb.dungeoncore.util.LivingEntityUtil;

public class FireAspect3 extends CombatSlot {

  @Override
  public String getSlotName() {
    return "レーヴァティン Level3";
  }

  @Override
  public String getSlotDetail() {
    return "攻撃を与えた敵と周りの敵に延焼効果を付与";
  }

  @Override
  public String getId() {
    return "slot_fire_3";
  }

  @Override
  public ChatColor getNameColor() {
    return ChatColor.RED;
  }

  @Override
  public SlotLevel getLevel() {
    return SlotLevel.LEVEL3;
  }

  @Override
  public void onCombat(CombatEntityEvent e, Player p) {
    e.getEnemy().setFireTicks(20 * 6);

    for (Entity entity : e.getEnemy().getNearbyEntities(3, 3, 3)) {
      if (LivingEntityUtil.isEnemy(entity)) {
        ((LivingEntity) entity).setFireTicks(20 * 4);
      }
    }
  }

}
