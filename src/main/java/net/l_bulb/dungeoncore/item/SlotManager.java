package net.l_bulb.dungeoncore.item;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

import net.l_bulb.dungeoncore.item.slot.SlotInterface;
import net.l_bulb.dungeoncore.item.slot.SlotLevel;
import net.l_bulb.dungeoncore.item.slot.item.AddEmptySlotItem;
import net.l_bulb.dungeoncore.item.slot.item.AddEmptySlotItem2;
import net.l_bulb.dungeoncore.item.slot.item.RemoveUnavailableSlot;
import net.l_bulb.dungeoncore.item.slot.item.RemoveUnavailableSlot2;
import net.l_bulb.dungeoncore.item.slot.magicstone.EmptySlot;
import net.l_bulb.dungeoncore.item.slot.magicstone.UnavailableSlot;

import com.google.common.collect.HashMultimap;

public class SlotManager {
  static HashMap<String, String> idNameMap = new HashMap<>();
  static HashMap<String, SlotInterface> slotIDMap = new HashMap<>();
  static HashMultimap<SlotLevel, SlotInterface> levelSlotMap = HashMultimap.create();

  static {
    registSlot(new UnavailableSlot());
    registSlot(new EmptySlot());
    ItemManager.registItem(new AddEmptySlotItem());
    ItemManager.registItem(new RemoveUnavailableSlot());
    ItemManager.registItem(new AddEmptySlotItem2());
    ItemManager.registItem(new RemoveUnavailableSlot2());
  }

  public static void registSlot(SlotInterface slot) {
    idNameMap.put(slot.getSlotName(), slot.getId());
    slotIDMap.put(slot.getId(), slot);
    levelSlotMap.put(slot.getLevel(), slot);
  }

  public static SlotInterface getSlotByID(String id) {
    return slotIDMap.get(id);
  }

  /**
   * 指定したLevelからそれにあったMagicStoneを取得する
   *
   * @param level
   * @return
   */
  public static Collection<SlotInterface> getSlotListByLevel(SlotLevel level) {
    Set<SlotInterface> set = levelSlotMap.get(level);
    if (set != null) {
      return set;
    } else {
      return Collections.emptyList();
    }
  }
}
