package net.l_bulb.dungeoncore.dungeon.contents.slotStone.level4;

import org.bukkit.ChatColor;

import net.l_bulb.dungeoncore.common.event.player.PlayerKillEntityEvent;
import net.l_bulb.dungeoncore.item.slot.SlotLevel;
import net.l_bulb.dungeoncore.item.slot.magicstone.KillSlot;

public class MagicStoneSmash extends KillSlot {

  @Override
  public String getSlotName() {
    return "Smash";
  }

  @Override
  public String getSlotDetail() {
    return "一定確率でボス以外の敵を即死させます。";
  }

  @Override
  public String getId() {
    return null;
  }

  @Override
  public ChatColor getNameColor() {
    // TODO 自動生成されたメソッド・スタブ
    return null;
  }

  @Override
  public SlotLevel getLevel() {
    // TODO 自動生成されたメソッド・スタブ
    return null;
  }

  @Override
  public void onKill(PlayerKillEntityEvent e) {
    // TODO 自動生成されたメソッド・スタブ

  }

}
