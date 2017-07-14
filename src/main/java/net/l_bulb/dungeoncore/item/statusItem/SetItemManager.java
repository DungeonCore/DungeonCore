package net.l_bulb.dungeoncore.item.statusItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.item.ItemManager;
import net.l_bulb.dungeoncore.item.itemInterface.StatusItemable;
import net.l_bulb.dungeoncore.util.TheLowExecutor;

import com.google.common.collect.HashMultimap;

/**
 * StatusItemの情報を管理するためのクラス
 */
public class SetItemManager {
  /**
   * Playerが装備しているステータスアイテム一覧
   */
  private static HashMultimap<UUID, StatusItemData> playerUsingMap = HashMultimap.create();

  public static void regist(StatusItemData setitem) {}

  /**
   * 対象のプレイヤーの全てのSetItem情報を取り除く
   *
   * @param p
   */
  public static void removeAll(Player p) {
    playerUsingMap.removeAll(p.getUniqueId()).stream().forEach(i -> i.onEndJob(p));
  }

  /**
   * check無しでSetItemを取得する
   *
   * @param p
   * @return
   */
  public static Collection<StatusItemData> getWearSetItemTypeNotCheck(Player p) {
    return playerUsingMap.get(p.getUniqueId());
  }

  /**
   * プレイヤーの全てのSetItemをチェックする
   *
   * @param p
   * @param isInstant 即時でチェックを行うならTRUE、遅延させるならFALSE
   * @return
   */
  public static void updateAllSetItem(Player p, boolean isInstant) {
    TheLowExecutor.executeLater(isInstant ? 0 : 2, () -> updateAllSetItem(p));
  }

  /**
   * プレイヤーの全てのSetItemをチェックする
   *
   * @param p
   * @return
   */
  public static void updateAllSetItem(Player p) {
    ArrayList<StatusItemData> statusItemList = new ArrayList<>();

    // 今持っているステータスアイテムをセットする
    for (ItemStack armorItem : p.getEquipment().getArmorContents()) {
      System.out.println(armorItem);
      // ステータスアイテムでないなら無視する
      StatusItemable customItem = ItemManager.getCustomItem(StatusItemable.class, armorItem);
      if (customItem == null) {
        System.out.println(armorItem + "@1");
        continue;
      }
      // ステータスアイテムを取得
      StatusItemData statusItem = customItem.getStatusItem(armorItem);

      // リストに格納する
      if (statusItem != null) {
        statusItemList.add(statusItem);
      }
    }

    // 同じセットアイテムでも効果が違う場合があるので今まで装備していたものを一旦全て取り除く
    for (StatusItemData setItemInterface : playerUsingMap.removeAll(p.getUniqueId())) {
      setItemInterface.onEndJob(p);
    }

    // 今装備しているものを全て追加する
    for (StatusItemData beforeStatus : statusItemList) {
      beforeStatus.onStartJob(p);
    }
    playerUsingMap.putAll(p.getUniqueId(), statusItemList);

  }

  // TODO ルーチン処理を実行する
  // public static void startRutine() {
  // final int ROUTINE_TERM_SECOUND = 5;
  // new BukkitRunnable() {
  //
  // @Override
  // public void run() {
  //
  // }
  // }.runTaskTimer(Main.plugin, 0, 20 * ROUTINE_TERM_SECOUND);
  // }

  /**
   * ItemStackからSetItem名を取得
   *
   * @param item
   * @return
   */
  public static String getSetItemName(ItemStack item) {
    return null;
  }

  /**
   * サーバー始動のときの処理
   */
  public static void initServer() {
    for (Player p : Bukkit.getOnlinePlayers()) {
      updateAllSetItem(p);
    }
  }
}
