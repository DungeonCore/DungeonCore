package net.l_bulb.dungeoncore.common.place.dungeon;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import org.bukkit.command.CommandSender;

import net.l_bulb.dungeoncore.dungeoncore.SpletSheet.DungeonListRunnable;
import net.l_bulb.dungeoncore.dungeoncore.SpletSheet.SpletSheetExecutor;

public class DungeonList {

  public static int maxId = -1;

  public static HashMap<String, DungeonData> dungeonMap = new HashMap<>();
  public static HashMap<Integer, DungeonData> iDMap = new HashMap<>();

  /**
   * ダンジョンリストを取得
   * 
   * @return
   */
  public static Collection<DungeonData> getDungeonList() {
    return dungeonMap.values();
  }

  /**
   * IDから検索する
   *
   * @param id
   * @return
   */
  public static DungeonData getDungeonById(int id) {
    return iDMap.get(id);
  }

  /**
   * 名前の一覧を取得
   *
   * @return
   */
  public static Set<String> names() {
    return dungeonMap.keySet();
  }

  /**
   * 名前から検索する
   *
   * @param name
   * @return
   */
  public static DungeonData getDungeonByName(String name) {
    return dungeonMap.get(name);
  }

  public static int getNextId() {
    return maxId + 1;
  }

  public static void clear() {
    for (DungeonData dungeon : iDMap.values()) {
      dungeon.disenable();
    }

    dungeonMap.clear();
    iDMap.clear();
    maxId = -1;
  }

  public static void addDungeon(DungeonData dungeon) {
    dungeon.enable();

    maxId = dungeon.getId();
    dungeonMap.put(dungeon.getName(), dungeon);
    iDMap.put(dungeon.getId(), dungeon);
  }

  public static void load(CommandSender sender) {
    DungeonListRunnable dungeonListRunnable = new DungeonListRunnable(sender);
    dungeonListRunnable.getData(null);
    SpletSheetExecutor.onExecute(dungeonListRunnable);
  }
}
