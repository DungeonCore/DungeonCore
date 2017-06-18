package net.l_bulb.dungeoncore.item;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.common.projectile.ProjectileInterface;
import net.l_bulb.dungeoncore.common.projectile.ProjectileManager;
import net.l_bulb.dungeoncore.item.customItem.other.GalionItem;
import net.l_bulb.dungeoncore.item.customItem.other.MagicStoneOre;
import net.l_bulb.dungeoncore.item.customItem.other.StrengthScrollArmor;
import net.l_bulb.dungeoncore.item.customItem.other.StrengthScrollWeapon;
import net.l_bulb.dungeoncore.item.customItem.pic.DiamondPickaxe;
import net.l_bulb.dungeoncore.item.customItem.pic.GoldPickaxe;
import net.l_bulb.dungeoncore.item.customItem.pic.IronPickaxe;
import net.l_bulb.dungeoncore.item.customItem.pic.MagicOreRegistPic;
import net.l_bulb.dungeoncore.item.customItem.pic.StonePickaxe;
import net.l_bulb.dungeoncore.item.customItem.pic.WoodPickAxe;
import net.l_bulb.dungeoncore.item.slot.SlotInterface;
import net.l_bulb.dungeoncore.player.magicstoneOre.MagicStoneOreType;
import net.l_bulb.dungeoncore.util.ItemStackUtil;
import net.l_bulb.dungeoncore.util.JavaUtil;

public class ItemManager {
  static HashMap<String, ItemInterface> allItemNameList = new HashMap<>();
  static HashMap<String, ItemInterface> allItemIdList = new HashMap<>();

  static HashMap<Class<?>, HashMap<String, ItemInterface>> allItemNameClassList = new HashMap<>();

  static HashMap<Class<?>, HashMap<String, ItemInterface>> allItemIdClassList = new HashMap<>();

  public static void registItem(ItemInterface[] item) {
    for (ItemInterface itemInterface : item) {
      registItem(itemInterface);
    }
  }

  /**
   * itemを登録する
   *
   * @param item
   */
  public static void registItem(ItemInterface item) {
    Set<Class<?>> interfaces = JavaUtil.getInterface(item.getClass());
    for (Class<?> clazz : interfaces) {
      if (clazz.isAssignableFrom(ItemInterface.class) || !clazz.equals(ItemInterface.class)) {
        registItem(clazz, item);
      }
    }
    // 全てのアイテムを登録する
    allItemNameList.put(ChatColor.stripColor(item.getItemName()).toUpperCase(), item);
    allItemIdList.put(ChatColor.stripColor(item.getId()).toUpperCase(), item);

    if (item instanceof SlotInterface) {
      SlotManager.registSlot((SlotInterface) item);
    }

    if (item instanceof ProjectileInterface) {
      ProjectileManager.regist((ProjectileInterface) item);
    }
  }

  private static void registItem(Class<?> clazz, ItemInterface item) {
    if (!allItemNameClassList.containsKey(clazz)) {
      allItemNameClassList.put(clazz, new HashMap<String, ItemInterface>());
    }
    allItemNameClassList.get(clazz).put(ChatColor.stripColor(item.getItemName()).toUpperCase(), item);

    if (!allItemIdClassList.containsKey(clazz)) {
      allItemIdClassList.put(clazz, new HashMap<String, ItemInterface>());
    }
    allItemIdClassList.get(clazz).put(item.getId(), item);
  }

  /**
   * 全てのアイテムを取得する
   *
   * @return
   */
  public static List<ItemInterface> getAllItem() {
    return new ArrayList<>(allItemIdList.values());
  }

  /**
   * 全てのアイテム名を取得する
   *
   * @return
   */
  public static Set<String> getAllItemName() {
    return allItemNameList.keySet();
  }

  /**
   * 全てのアイテムIDを取得する
   *
   * @return
   */
  public static Set<String> getAllItemID() {
    return allItemIdList.keySet();
  }

  /**
   * clazz, itemに対応したアイテムを取得する
   *
   * @param clazz
   * @param item
   * @return
   */
  @SuppressWarnings("unchecked")
  public static <T extends ItemInterface> T getCustomItem(Class<T> clazz, ItemStack item) {
    if (item == null) { return null; }

    // IDが存在するとき
    String id = ItemStackUtil.getId(item);
    if (id != null && !id.isEmpty()) {
      HashMap<String, T> hashMap = (HashMap<String, T>) allItemIdClassList.get(clazz);
      if (hashMap != null) {
        T t = hashMap.get(id);
        if (t != null) { return t; }
      }
    }

    // 名前から取得
    String name = ItemStackUtil.getName(item);
    if (name == null || name.isEmpty()) { return null; }

    // ヘルスクリスタルだけ別処理
    if (!name.contains("CRYSTAL")) { return null; }

    name = ChatColor.stripColor(name).toUpperCase();
    // interfaceが登録されていない場合は無視する
    if (!allItemNameClassList.containsKey(clazz)) { return null; }

    HashMap<String, T> hashMap2 = (HashMap<String, T>) allItemNameClassList.get(clazz);
    // +1などがついてない場合はそのまま返す
    if (hashMap2.containsKey(name)) { return hashMap2.get(name); }
    // +1などが付いている場合
    if (name.contains("+")) {
      // +1などを取り除く
      name = name.substring(0, name.indexOf("+")).trim();
      return hashMap2.get(name);
    }
    return null;
  }

  /**
   * 指定したItemが指定したInterfaceを実装しているかどうかを調べる。(instanceOfの高速版)
   *
   * @param clazz
   * @param item
   * @return
   */
  public static boolean isImplemental(Class<? extends ItemInterface> clazz, ItemInterface item) {
    if (item == null) { return false; }

    HashMap<String, ItemInterface> hashMap = allItemIdClassList.get(clazz);
    // 指定したInterfaceを実装しているItemがない場合はfalse
    if (hashMap == null) { return false; }
    return hashMap.containsKey(item.getId());
  }

  public static ItemInterface getCustomItem(ItemStack item) {
    if (item == null) { return null; }
    // IDから取るのでコメントアウト
    String id = ItemStackUtil.getId(item);
    if (id == null) {
      // IDがない場合は名前から取る
      String name = ItemStackUtil.getName(item);
      return getCustomItemByName(name);
    } else {
      return getCustomItemById(id);
    }
  }

  public static ItemInterface getCustomItemById(String id) {
    if (id == null) { return null; }
    id = ChatColor.stripColor(id).toUpperCase();
    return allItemIdList.get(id);
  }

  public static ItemInterface getCustomItemByName(String name) {
    if (name.isEmpty()) { return null; }

    name = ChatColor.stripColor(name).toUpperCase();
    // +1などがついてない場合はそのまま返す
    if (allItemNameList.containsKey(name)) { return allItemNameList.get(name); }

    // +1などが付いている場合
    if (name.contains("+")) {
      // +1などを取り除く
      name = name.substring(0, name.indexOf("+")).trim();
      return allItemNameList.get(name);
    }
    return null;
  }

  public static void registItem(Collection<ItemInterface> itemList) {
    registItem(itemList.toArray(new ItemInterface[0]));
  }

  /**
   * 同じCustomItemならTRUE
   *
   * @param item1
   * @param item2
   * @return
   */
  public static boolean equals(ItemStack item1, ItemStack item2) {
    String id1 = ItemStackUtil.getId(item1);
    String id2 = ItemStackUtil.getId(item2);

    return id1 != null && id1.equals(id2);
  }

  static {
    registItem(new StrengthScrollArmor());
    registItem(new StrengthScrollWeapon());
    // 鉱石
    registItem(new MagicStoneOre(MagicStoneOreType.DIAOMOD_ORE));
    registItem(new MagicStoneOre(MagicStoneOreType.IRON_ORE));
    registItem(new MagicStoneOre(MagicStoneOreType.GOLD_ORE));
    registItem(new MagicStoneOre(MagicStoneOreType.EMERALD_ORE));
    registItem(new MagicStoneOre(MagicStoneOreType.COAL_ORE));
    registItem(new MagicStoneOre(MagicStoneOreType.REDSTONE_ORE));
    registItem(new MagicStoneOre(MagicStoneOreType.LAPIS_ORE));
    // ピッケル
    registItem(new WoodPickAxe(0).getAllRelativeItem());
    registItem(new StonePickaxe(0).getAllRelativeItem());
    registItem(new GoldPickaxe(0).getAllRelativeItem());
    registItem(new IronPickaxe(0).getAllRelativeItem());
    registItem(new DiamondPickaxe(0).getAllRelativeItem());
    registItem(new MagicOreRegistPic());
    // お金
    registItem(GalionItem.getInstance(0));
  }
}
