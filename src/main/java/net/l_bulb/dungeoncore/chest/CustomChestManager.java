package net.l_bulb.dungeoncore.chest;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;

import net.l_bulb.dungeoncore.mob.customMob.BossMobable;

/**
 * チェストの場所と中身を管理する
 *
 * @author kensuke
 *
 */
public class CustomChestManager {
  private static HashMap<Location, AbstractCustomChest> chestList = new HashMap<>();

  public static boolean containts(Location loc) {
    return chestList.containsKey(loc);
  }

  /**
   * ボスチェストだけ特別処理
   *
   * @param e
   */
  public static void setBossRewardChest(BossMobable e) {
    BossChest bossChest = e.getBossChest();

    if (bossChest != null) {
      Location chestLoc = bossChest.setChest(e);
      if (chestLoc != null) {
        chestList.put(chestLoc, bossChest);
      }
    }
  }

  public static void registChest(Location loc, AbstractCustomChest chest) {
    if (chestList.containsKey(loc)) {
      AbstractCustomChest abstractCustomChest = chestList.get(loc);
      if (chest instanceof SpletSheetChest && abstractCustomChest instanceof SpletSheetChest) {
        ((SpletSheetChest) chest).setRefule((SpletSheetChest) abstractCustomChest);
      }
    }
    chestList.put(loc.getBlock().getLocation(), chest);

  }

  /**
   * 登録したチェストを取り除く
   *
   * @param loc
   */
  public static void removeChest(Location loc) {
    AbstractCustomChest remove = chestList.remove(loc.getBlock().getLocation());
    if (remove != null) {
      remove.removeChest(loc);
    }
  }

  /**
   * 場所からチェストを取得する。登録されていない場合はnull
   *
   * @param loc
   * @return
   */
  public static AbstractCustomChest getCustomChest(Location loc) {
    return chestList.get(loc.getBlock().getLocation());
  }

  /**
   * 登録されているチェストの場所を取得
   *
   * @param loc
   * @return
   */
  public static Map<Location, AbstractCustomChest> getChestLocationMap() {
    return chestList;
  }
}
