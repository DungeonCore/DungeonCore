package net.l_bulb.dungeoncore.command;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.common.menu.MenuSelectorInterface;
import net.l_bulb.dungeoncore.common.menu.MenuSelectorManager;
import net.l_bulb.dungeoncore.dungeon.contents.item.key.KeyItemable;
import net.l_bulb.dungeoncore.item.ItemInterface;
import net.l_bulb.dungeoncore.item.ItemManager;
import net.l_bulb.dungeoncore.item.itemInterface.ArmorItemable;
import net.l_bulb.dungeoncore.item.itemInterface.AvailableLevelItemable;
import net.l_bulb.dungeoncore.item.itemInterface.CombatItemable;
import net.l_bulb.dungeoncore.item.itemInterface.FoodItemable;
import net.l_bulb.dungeoncore.item.itemInterface.MaterialItemable;
import net.l_bulb.dungeoncore.item.setItem.SetItemParts;
import net.l_bulb.dungeoncore.item.slot.SlotInterface;
import net.l_bulb.dungeoncore.player.ItemType;
import net.l_bulb.dungeoncore.util.ItemStackUtil;

import lombok.Getter;
import net.md_5.bungee.api.ChatColor;

public class CommandGetItem implements CommandExecutor, MenuSelectorInterface {

  private static final String SUB_MENU_TITLE = "/getitem";
  private static final String TITLE = "thelow item 一覧";

  static {
    MenuSelectorManager.regist(new CommandGetItem());
  }

  private static final String THELOW_GETITEM_TAG = "thelow_getitem_tag";
  private static final String THELOW_GETITEM_PAGE_TAG = "thelow_getitem_page_tag";

  @Override
  public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
    if (sender instanceof Player && args.length == 0) {
      MenuSelectorManager.open((Player) sender, TITLE);
    } else {
      giveItem(sender, args);
    }
    return true;

  }

  /**
   * ItemIDからアイテムを取得しPlayerに渡す
   *
   * @param sender
   * @param paramArrayOfString
   */
  private void giveItem(CommandSender sender, String[] paramArrayOfString) {
    if (paramArrayOfString.length == 0) { return; }

    Player target = null;
    if (sender instanceof Player) {
      target = (Player) sender;
    }

    Player playerExact = Bukkit.getPlayerExact(paramArrayOfString[0]);
    int startIndex = 0;
    if (playerExact != null) {
      target = playerExact;
      startIndex = 1;
    }

    // Itemidを取得
    StringBuilder itemId = new StringBuilder();
    for (int i = startIndex; i < paramArrayOfString.length; i++) {
      itemId.append(paramArrayOfString[i]).append(' ');
    }

    // アイテムを付与する
    ItemInterface customItemById = ItemManager.getCustomItemById(itemId.toString().trim());
    if (customItemById != null && target != null) {
      target.getInventory().addItem(customItemById.getItem());
    }
  }

  public static void onClick(InventoryInteractEvent e) {
    Inventory inventory = e.getInventory();
    if (!SUB_MENU_TITLE.equals(inventory.getName())) { return; }

    // ドラッグの時は何もしない
    if (e instanceof InventoryDragEvent) {
      e.setCancelled(true);
      return;
    }

    // クリックでないなら無視する
    if (!(e instanceof InventoryClickEvent)) { return; }

    // クリックしたPlayer
    Player p = (Player) e.getWhoClicked();

    ItemStack clickItem = ((InventoryClickEvent) e).getCurrentItem();
    // アイテムをクリックしていないなら何もしない
    if (clickItem == null) { return; }

    short pageIndex = ItemStackUtil.getNBTTagShort(clickItem, THELOW_GETITEM_PAGE_TAG);
    // 値が設定されていないなら何もしない
    if (pageIndex == 0) {
      return;
    } else if (pageIndex == -1) {
      // 戻る
      e.setCancelled(true);
      MenuSelectorManager.open(p, TITLE);
    } else {
      // 指定したページを表示
      e.setCancelled(true);
      ButtonType fromFlg = ButtonType.fromFlg(ItemStackUtil.getNBTTagShort(clickItem, THELOW_GETITEM_TAG));
      if (fromFlg != null) {
        p.openInventory(new CommandGetItem().getInventory(fromFlg, (short) (pageIndex - 1)));
      }
    }

  }

  @Override
  public void open(Player p) {
    Inventory inv = Bukkit.createInventory(null, 9 * 3, TITLE);

    for (ButtonType type : ButtonType.values()) {
      ItemStack item = ItemStackUtil.getItem(type.getName2(), type.getM(), type.getData());
      ItemStackUtil.setNBTTag(item, THELOW_GETITEM_TAG, type.getFlg());
      inv.setItem(type.getIndex(), item);
    }
    p.openInventory(inv);
  }

  @Override
  public void onSelectItem(Player p, ItemStack item, InventoryClickEvent e) {
    // アイテムをクリックしていないなら何もしない
    if (item == null) { return; }
    short nbtTagShort = ItemStackUtil.getNBTTagShort(item, THELOW_GETITEM_TAG);
    // ボタンフラグがついてないなら何もしない
    ButtonType fromFlg = ButtonType.fromFlg(nbtTagShort);
    if (fromFlg == null) { return; }

    short page = ItemStackUtil.getNBTTagShort(item, THELOW_GETITEM_PAGE_TAG);

    Inventory inventory = getInventory(fromFlg, page);
    p.openInventory(inventory);
  }

  private Inventory getInventory(ButtonType fromFlg, short page) {
    ArrayList<ItemInterface> itemList = new ArrayList<>();

    List<ItemInterface> allItem = ItemManager.getAllItem();
    for (ItemInterface item : allItem) {
      // 表示しないアイテムなら無視する
      if (!item.isShowItemList()) {
        continue;
      }

      ButtonType type;
      if (ItemManager.isImplemental(CombatItemable.class, item)) {
        type = ButtonType.WEAPON;
      } else if (ItemManager.isImplemental(SetItemParts.class, item)) {
        type = ButtonType.ARMOR;
      } else if (ItemManager.isImplemental(ArmorItemable.class, item)) {
        type = ButtonType.ARMOR;
      } else if (ItemManager.isImplemental(FoodItemable.class, item)) {
        type = ButtonType.FOOD;
      } else if (ItemManager.isImplemental(MaterialItemable.class, item)) {
        type = ButtonType.CRAFT;
      } else if (item instanceof SlotInterface) {
        type = ButtonType.MAGIC_ORE;
      } else if (ItemManager.isImplemental(KeyItemable.class, item)) {
        type = ButtonType.KEY_QUEST;
      } else if (item.isQuestItem()) {
        type = ButtonType.KEY_QUEST;
      } else {
        type = ButtonType.OTHER;
      }

      // もし欲しいアイテムのタイプと同じならListに追加する
      if (type == fromFlg) {
        itemList.add(item);
      }
    }

    // 並び替えを行う
    itemList.sort(new ComparatorImplemention());

    // 1ページを切り取る
    List<ItemInterface> subList = itemList.subList(page * 7 * 9, Math.min((page + 1) * 7 * 9, itemList.size()));

    // インベントリのサイズを取得(最大 9 * 9, 最小 3 * 9, その他 アイテム数行 + ボタン行)
    int invSize = itemList.size() < 2 * 9 ? 3 * 9 : itemList.size() > 7 * 9 ? 9 * 9 : (itemList.size() / 9 + 3) * 9;

    Inventory createInventory = Bukkit.createInventory(null, invSize, SUB_MENU_TITLE);
    subList.stream().map(customItem -> customItem.getItem()).forEach(createInventory::addItem);

    // 2ページ移行になる場合はページ遷移ボタンを設置
    if (itemList.size() > invSize) {
      for (short i = 0; i < itemList.size() / (invSize - 9) + 1; i++) {
        ItemStack item = null;
        if (i == page) {
          item = ItemStackUtil.getItem((i + 1) + "ページ目", Material.WOOL, (byte) 5, ChatColor.GREEN + "現在のページ");
        } else {
          item = ItemStackUtil.getItem((i + 1) + "ページ目", Material.WOOL, (byte) 0, ChatColor.WHITE.toString() + (i + 1) + "ページ目を表示する");
        }
        // NBTTagを設定
        ItemStackUtil.setNBTTag(item, THELOW_GETITEM_PAGE_TAG, i + 1);
        ItemStackUtil.setNBTTag(item, THELOW_GETITEM_TAG, fromFlg.getFlg());
        // ボタンを設置する
        createInventory.setItem(invSize - 9 + i, item);
      }
    }

    // 戻るボタンを設置
    ItemStack backButton = ItemStackUtil.getItem("メニューに戻る", Material.CHEST);
    ItemStackUtil.setNBTTag(backButton, THELOW_GETITEM_PAGE_TAG, -1);
    createInventory.setItem(invSize - 1, backButton);

    return createInventory;
  }

  @Override
  public String getTitle() {
    return TITLE;
  }

  @Getter
  enum ButtonType {
    WEAPON("武器", Material.DIAMOND_SWORD, (byte) 0, 1, 0), ARMOR("装備", Material.DIAMOND_CHESTPLATE, (byte) 0, 2, 2), MAGIC_ORE("魔法石",
        Material.INK_SACK, (byte) 10, 3, 4), KEY_QUEST("鍵・クエストアイテム", Material.TRIPWIRE_HOOK, (byte) 0, 4, 6), FOOD("特殊食べ物", Material.APPLE, (byte) 0,
            7, 8), CRAFT("クラフト素材", Material.STICK, (byte) 0, 8, 18), OTHER("その他", Material.FEATHER, (byte) 0, 1000, 20);

    private String name2;
    private Material m;
    private byte data;
    private short flg;
    private int index;

    private ButtonType(String name, Material m, byte data, int flg, int index) {
      name2 = name;
      this.m = m;
      this.data = data;
      this.flg = (short) flg;
      this.index = index;
    }

    static ButtonType fromFlg(short flg) {
      for (ButtonType type : values()) {
        if (type.getFlg() == flg) { return type; }
      }
      return null;
    }
  }
}

class ComparatorImplemention implements Comparator<ItemInterface> {

  @Override
  public int compare(ItemInterface o1, ItemInterface o2) {
    // まずはアイテムの種類ごと
    int order1 = getOrder(o1);
    int order2 = getOrder(o2);
    if (order1 != order2) { return order1 - order2; }

    // アイテムのレベルごと
    int level1 = -1;
    int level2 = -1;
    if (o1 instanceof AvailableLevelItemable) {
      level1 = ((AvailableLevelItemable) o1).getAvailableLevel();
    }
    if (o2 instanceof AvailableLevelItemable) {
      level2 = ((AvailableLevelItemable) o2).getAvailableLevel();
    }
    if (level1 != level2) { return Integer.compare(level1, level2); }

    // 魔法石の場合は特別
    if (o1 instanceof SlotInterface && o2 instanceof SlotInterface) {
      if (((SlotInterface) o1).getSlotType() != ((SlotInterface) o2)
          .getSlotType()) { return ((SlotInterface) o1).getSlotType().compareTo(((SlotInterface) o2).getSlotType()); }

      if (((SlotInterface) o1).getLevel().getSucessPer() != ((SlotInterface) o2).getLevel()
          .getSucessPer()) { return Double.compare(((SlotInterface) o2).getLevel().getSucessPer(),
              ((SlotInterface) o1).getLevel().getSucessPer()); }
    }

    return o1.getId().compareTo(o2.getId());
  }

  HashMap<ItemInterface, Integer> cache = new HashMap<>();

  // アイテムの並び順
  public int getOrder(ItemInterface item) {
    if (cache.containsKey(item)) { return cache.get(item); }

    int rtn;
    if (item.getAttackType() == ItemType.SWORD) {
      rtn = 1;
    } else if (item.getAttackType() == ItemType.BOW) {
      rtn = 2;
    } else if (item.getAttackType() == ItemType.MAGIC) {
      rtn = 3;
    } else if (item instanceof KeyItemable) {
      rtn = 4;
    } else if (item instanceof SetItemParts) {
      rtn = 5;
    } else if (item instanceof ArmorItemable) {
      rtn = 7;
    } else if (item instanceof SlotInterface) {
      rtn = 8;
    } else if (item.isQuestItem()) {
      rtn = 99;
    } else {
      rtn = 1000;
    }
    cache.put(item, rtn);
    return rtn;
  }
}
