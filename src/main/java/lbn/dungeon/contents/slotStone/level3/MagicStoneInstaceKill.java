package lbn.dungeon.contents.slotStone.level3;

import org.bukkit.ChatColor;

import lbn.common.event.player.PlayerCombatEntityEvent;
import lbn.item.slot.SlotLevel;
import lbn.item.slot.slot.CombatSlot;

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
  public void onCombat(PlayerCombatEntityEvent e) {
    // LivingEntity enemy = e.getEnemy();
    // AbstractMob<?> mob = MobHolder.getMob(enemy);
  }
}
