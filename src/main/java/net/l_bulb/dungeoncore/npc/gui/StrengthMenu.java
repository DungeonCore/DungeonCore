package net.l_bulb.dungeoncore.npc.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.common.menu.MenuSelectorInterface;
import net.l_bulb.dungeoncore.common.menu.MenuSelectorManager;
import net.l_bulb.dungeoncore.item.slot.table.SlotSetTableOperation;
import net.l_bulb.dungeoncore.item.system.craft.CraftItemSelectViewer;
import net.l_bulb.dungeoncore.item.system.repair.RepairUi;
import net.l_bulb.dungeoncore.item.system.strength.StrengthTables;
import net.l_bulb.dungeoncore.npc.villagerNpc.VillagerNpc;
import net.l_bulb.dungeoncore.util.ItemStackUtil;

public class StrengthMenu implements MenuSelectorInterface {
  private static final String INVENTOY_TITLE = "鍛冶屋メニュー";

  static {
    MenuSelectorManager.regist(new StrengthMenu());
  }

  public static void open(Player p, VillagerNpc npc) {
    // NPCが無い時は通常の画面を開く
    if (npc == null) {
      new StrengthMenu().open(p);
      return;
    }
    String data = npc.getData();
    // データが無い時は通常の画面を開く
    if (data == null || data.isEmpty()) {
      new StrengthMenu().open(p);
      return;
    }

    Inventory createInventory = Bukkit.createInventory(null, 9 * 3, INVENTOY_TITLE);

    // 修理
    createInventory.setItem(10, getButtonItem("修理", "1", Material.ANVIL, "アイテムの修理を行います。"));

    // 強化
    createInventory.setItem(12, getButtonItem("強化", "2", Material.LAVA_BUCKET,
        "アイテムを強化します。", "強化したいアイテムと素材を", "取引画面に置いてください。"));

    // 魔法石装着
    createInventory.setItem(14, getButtonItem("魔法石装着", "3", Material.BEACON,
        "武器に魔法石を装着します。", "武器と魔法石を置いてください。",
        "成功確率などの情報が、", "赤いガラスの部分に表示されます。"));

    // クラフト
    ItemStack craftButton = getButtonItem("アイテム製作", "4", Material.WORKBENCH, "作りたいアイテムを", "クリックしてください");
    ItemStackUtil.setNBTTag(craftButton, "the_low_villager_id", npc.getId());
    createInventory.setItem(16, craftButton);

    p.openInventory(createInventory);
  }

  private static final String THE_LOW_BACKSMITH_MENU_BUTTON_ID = "the_low_backsmith_menu_button_id";

  @Override
  public void open(Player p) {
    Inventory createInventory = Bukkit.createInventory(null, 9 * 3, getTitle());

    // 修理
    createInventory.setItem(11, getButtonItem("修理", "1", Material.ANVIL, "アイテムの修理を行います。"));

    // 強化
    createInventory.setItem(13, getButtonItem("強化", "2", Material.LAVA_BUCKET,
        "アイテムを強化します。", "強化したいアイテムと素材を", "取引画面に置いてください。"));

    // 魔法石装着
    createInventory.setItem(15, getButtonItem("魔法石装着", "3", Material.BEACON,
        "武器に魔法石を装着します。", "武器と魔法石を置いてください。",
        "成功確率などの情報が、", "赤いガラスの部分に表示されます。"));

    p.openInventory(createInventory);
  }

  @Override
  public void onSelectItem(Player p, ItemStack item, InventoryClickEvent e) {
    String buttonId = getButtonId(item);
    if (buttonId == null) { return; }

    switch (buttonId) {
      case "1":
        RepairUi.onOpenUi(p);
        break;
      case "2":
        StrengthTables.openStrengthTable(p);
        break;
      case "3":
        SlotSetTableOperation.openSlotTable(p);
        break;
      case "4":
        String nbtTag = ItemStackUtil.getNBTTag(item, "the_low_villager_id");
        if (nbtTag == null) {
          p.sendMessage(ChatColor.RED + "只今、アイテム作成を行えません");
          return;
        }
        CraftItemSelectViewer.open(p, nbtTag, 0);
        break;
      default:
        break;
    }
  }

  @Override
  public String getTitle() {
    return INVENTOY_TITLE;
  }

  /**
   * ボタンのIDを取得
   * 
   * @param item
   * @return
   */
  private String getButtonId(ItemStack item) {
    String nbtTag = ItemStackUtil.getNBTTag(item, THE_LOW_BACKSMITH_MENU_BUTTON_ID);
    if (nbtTag == null || nbtTag.isEmpty()) { return null; }
    return nbtTag;
  }

  private static ItemStack getButtonItem(String name, String id, Material m, String... lore) {
    for (int i = 0; i < lore.length; i++) {
      lore[i] = ChatColor.GREEN + lore[i];
    }
    // 強化
    ItemStack itemStack = ItemStackUtil.getItem(ChatColor.WHITE + "" + ChatColor.BOLD + name, m, lore);
    // IDをつける
    ItemStackUtil.setNBTTag(itemStack, THE_LOW_BACKSMITH_MENU_BUTTON_ID, id);

    return itemStack;
  }

}
