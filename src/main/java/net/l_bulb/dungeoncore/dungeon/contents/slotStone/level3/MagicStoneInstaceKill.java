package net.l_bulb.dungeoncore.dungeon.contents.slotStone.level3;

import org.bukkit.ChatColor;

import net.l_bulb.dungeoncore.common.event.player.PlayerCombatEntityEvent_old;
import net.l_bulb.dungeoncore.item.slot.SlotLevel;
import net.l_bulb.dungeoncore.item.slot.magicstone.CombatSlot;

public class MagicStoneInstaceKill extends CombatSlot {

  @Override
  public String getSlotName() {
    return "スレイヤー";
  }

  @Override
  public String getSlotDetail() {
    return "一定確率でボス以外の敵を即死させる";
  }

  @Override
  public String getId() {
    return "magic_stone_slyer";
  }

  @Override
  public ChatColor getNameColor() {
    return ChatColor.LIGHT_PURPLE;
  }

  @Override
  public SlotLevel getLevel() {
    return SlotLevel.LEVEL3;
  }

  @Override
  public void onCombat(PlayerCombatEntityEvent_old e) {
    // LivingEntity enemy = e.getEnemy();
    // AbstractMob<?> mob = MobHolder.getMob(enemy);
  }
}
