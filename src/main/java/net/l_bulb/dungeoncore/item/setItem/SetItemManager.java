package net.l_bulb.dungeoncore.item.setItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.collect.HashMultimap;

import net.l_bulb.dungeoncore.dungeoncore.Main;
import net.l_bulb.dungeoncore.item.ItemManager;
import net.l_bulb.dungeoncore.util.ItemStackUtil;

/**
 * Set Itemの情報を管理するためのクラス
 */
public class SetItemManager {
  /**
   * Playerが装備しているセットアイテム一覧
   */
  private static HashMultimap<UUID, SetItemInterface> playerUsingMap = HashMultimap.create();

  private static HashMultimap<Material, SetItemPartsType> partsMaterialMap = HashMultimap.create();

  private static Map<String, SetItemInterface> setItemMap = new HashMap<String, SetItemInterface>();

  public static void regist(SetItemInterface setitem) {
    setItemMap.put(setitem.getName(), setitem);
    for (SetItemPartable item : setitem.getFullSetItem().values()) {
      // itemとして登録する
      ItemManager.registItem(item);
      // partsとMaterialを登録
      partsMaterialMap.put(item.getMaterial(), item.getItemSetPartsType());
    }
  }

  /**
   * 名前からSetItemInterfaceを取得
   * 
   * @param name
   * @return
   */
  public static SetItemInterface getSetItem(String name) {
    return setItemMap.get(name);
  }

  /**
   * 対象のプレイヤーの全てのSetItem情報を取り除く
   * 
   * @param p
   */
  public static void removeAll(Player p) {
    Set<SetItemInterface> removeAll = playerUsingMap.removeAll(p.getUniqueId());
    for (SetItemInterface setItemInterface : removeAll) {
      setItemInterface.endJob(p);
    }
  }

  /**
   * check無しでSetItemを取得する
   * 
   * @param p
   * @return
   */
  public static Collection<SetItemInterface> getWearSetItemTypeNotCheck(Player p) {
    return playerUsingMap.get(p.getUniqueId());
  }

  /**
   * プレイヤーの全てのSetItemをチェックする
   * 
   * @param p
   * @return
   */
  public static void updateAllSetItem(Player p) {
    // 現在セットしているセットアイテム一覧
    HashMap<SetItemInterface, ItemStack[]> setedSetItem = new HashMap<SetItemInterface, ItemStack[]>();

    HashSet<String> setitemCache = new HashSet<String>();
    // 関連する全てのパーツを取得
    for (SetItemPartsType partsType : SetItemPartsType.values()) {
      // プレイヤーが装備しているアイテムを取得
      ItemStack setItemPartsItem = partsType.getItemStackByParts(p);
      if (setItemPartsItem == null) {
        continue;
      }

      // アイテムからsetitem名を取得
      String setItemName = getSetItemName(setItemPartsItem);
      // setItemでないならスキップ
      if (setItemName == null) {
        continue;
      }
      // すでにチェックを行っていればスキップ
      if (setitemCache.contains(setItemName)) {
        continue;
      }

      // SetItem名からSetItemインスタンスを取得する
      SetItemInterface setItemInterface = getSetItem(setItemName);
      if (setItemInterface != null) {
        // checkを行う
        if (setItemInterface.isWearSetItem(p)) {
          // もし正常にセットされていればリストに追加する
          setedSetItem.put(setItemInterface, setItemInterface.getWearedSetItem(p));
        }
        // チェック済みに追加する
        setitemCache.add(setItemName);
      } else {
        // ありえないが念のため
        new RuntimeException("setItem is not registed!!:" + setItemName).printStackTrace();
        // チェック済みに追加する
        setitemCache.add(setItemName);
        continue;
      }
    }

    // 同じセットアイテムでも効果が違う場合があるので今まで装備していたものを一旦全て取り除く
    for (SetItemInterface setItemInterface : playerUsingMap.removeAll(p.getUniqueId())) {
      setItemInterface.endJob(p);
    }

    // 今装備しているものを全て追加する
    for (Entry<SetItemInterface, ItemStack[]> entry : setedSetItem.entrySet()) {
      entry.getKey().startJob(p, entry.getValue());
    }

    playerUsingMap.putAll(p.getUniqueId(), setedSetItem.keySet());
  }

  public static void startRutine() {
    new BukkitRunnable() {
      @Override
      public void run() {

        ArrayList<Object[]> removePlayers = new ArrayList<>();

        for (Entry<UUID, SetItemInterface> e : playerUsingMap.entries()) {
          Player player = Bukkit.getPlayer(e.getKey());

          // もしプレイヤーがいないなら何もしない
          if (player == null || !player.isOnline()) {
            removePlayers.add(new Object[] { e.getKey(), e.getValue() });
            continue;
          }

          SetItemInterface instance = e.getValue();
          // 効果を実行する
          instance.doRutine(player, instance.getWearedSetItem(player));
        }

        // とりあえずmapから削除する
        for (Object[] pair : removePlayers) {
          playerUsingMap.remove(pair[0], pair[1]);
        }
      }
    }.runTaskTimer(Main.plugin, 0, 20 * 1);
  }

  public static Set<String> getSetItemNameList() {
    return setItemMap.keySet();
  }

  public static Collection<SetItemPartsType> getSetItemPartsTypeListByMaterial(Material m) {
    return partsMaterialMap.get(m);
  }

  public static Collection<SetItemPartsType> getPartsTypeListByMaterial(Material type) {
    return partsMaterialMap.get(type);
  }

  /**
   * ItemStackからSetItem名を取得
   * 
   * @param item
   * @return
   */
  public static String getSetItemName(ItemStack item) {
    List<String> lore = ItemStackUtil.getLore(item);
    for (String string : lore) {
      // "SET:"が含まれていたらsetitemとして処理する
      if (string.contains("SET:")) { return string.replace("SET:", ""); }
    }
    return null;
  }

  /**
   * ItemStackからSetItemを取得
   * 
   * @param item
   * @return
   */
  public static SetItemInterface getSetItemFromItemStack(ItemStack item) {
    if (item == null) { return null; }

    // IDを取得
    String setItemName = getSetItemName(item);
    return getSetItem(setItemName);
  }

  /**
   * もしSetItemならTRUE
   * 
   * @param item
   * @return
   */
  public static boolean isSetItem(ItemStack item) {
    if (item == null) { return false; }
    // IDを取得
    String setItemName = getSetItemName(item);
    return setItemMap.containsKey(setItemName);
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
