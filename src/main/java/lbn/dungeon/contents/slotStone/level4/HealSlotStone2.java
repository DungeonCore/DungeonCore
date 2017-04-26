package lbn.dungeon.contents.slotStone.level4;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import lbn.common.cooltime.Cooltimable;
import lbn.item.slot.SlotLevel;
import lbn.item.slot.slot.UnUseSlot;
import lbn.item.system.strength.StrengthOperator;

public class HealSlotStone2 extends UnUseSlot implements Cooltimable {

  @Override
  public String getSlotName() {
    return "リジェネレーション Level2";
  }

  @Override
  public String getSlotDetail() {
    return "シフト右クリックで周囲に再生効果(大)を付与";
  }

  @Override
  public String getId() {
    return "slot_src_heal2";
  }

  @Override
  public ChatColor getNameColor() {
    return ChatColor.LIGHT_PURPLE;
  }

  @Override
  public SlotLevel getLevel() {
    return SlotLevel.LEVEL4;
  }

  @Override
  public int getCooltimeTick(ItemStack item) {
    return 20 * 50 - StrengthOperator.getLevel(item) * 18;
  }

}
