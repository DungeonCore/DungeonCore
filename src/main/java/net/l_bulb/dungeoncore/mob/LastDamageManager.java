package net.l_bulb.dungeoncore.mob;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.WeakHashMap;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.util.DungeonLogger;

/**
 * Playerが最後に攻撃したMobを管理するためのクラス
 *
 */
public class LastDamageManager {
  static HashMap<Integer, Player> entityPlayerMap = new HashMap<>();

  static HashMap<Integer, LastDamageMethodType> entityAttackTypeMap = new HashMap<>();

  static WeakHashMap<Integer, ItemStack> entityItemIdMap = new WeakHashMap<>();

  static Queue<Integer> idList = new LinkedList<>();

  public static void onDamage(Entity e, Player p, LastDamageMethodType type, ItemStack item) {
    if (e == null || p == null) { return; }

    if (e.getType() != EntityType.PLAYER && !SummonPlayerManager.isSummonMob(e)) {
      int id = e.getEntityId();
      addData(p, type, id, item);
    }
  }

  private static void addData(Player p, LastDamageMethodType type, int id, ItemStack item) {
    if (type == null) { return; }

    // 常に21000以下になるようにする
    if (idList.size() > 25000) {
      while (25000 - 5000 < idList.size()) {
        Integer remove = idList.remove();
        entityPlayerMap.remove(remove);
        entityAttackTypeMap.remove(remove);
        entityItemIdMap.remove(remove);
      }
      DungeonLogger.development("Last Damage Manager size over 25000");
    }
    idList.remove(id);
    idList.add(id);
    entityPlayerMap.put(id, p);
    entityAttackTypeMap.put(id, type);
    entityItemIdMap.put(id, item);
  }

  /**
   * 最後に攻撃したPlayerを取得
   *
   * @param e
   * @return
   */
  public static Player getLastDamagePlayer(Entity e) {
    return entityPlayerMap.get(e.getEntityId());
  }

  /**
   * 最後の攻撃に使用したアイテムを取得
   *
   * @param e
   * @return item or null
   */
  public static ItemStack getLastDamageItem(Entity e) {
    return entityItemIdMap.get(e.getEntityId());
  }

  /**
   * 最後ダメージの攻撃方法を取得
   *
   * @param e
   * @return
   */
  public static LastDamageMethodType getLastDamageAttackType(Entity e) {
    LastDamageMethodType attackType = entityAttackTypeMap.get(e.getEntityId());
    if (attackType == null) {
      return LastDamageMethodType.OTHER;
    } else {
      return attackType;
    }
  }

  public static void remove(Entity e) {
    entityAttackTypeMap.remove(e.getEntityId());
    entityPlayerMap.remove(e.getEntityId());
  }
}
