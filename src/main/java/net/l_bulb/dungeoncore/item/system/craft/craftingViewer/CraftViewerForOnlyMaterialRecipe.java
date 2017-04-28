package net.l_bulb.dungeoncore.item.system.craft.craftingViewer;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import net.l_bulb.dungeoncore.api.player.TheLowPlayer;
import net.l_bulb.dungeoncore.api.player.TheLowPlayerManager;
import net.l_bulb.dungeoncore.common.event.player.PlayerCraftCustomItemEvent;
import net.l_bulb.dungeoncore.common.menu.MenuSelectorInterface;
import net.l_bulb.dungeoncore.common.menu.MenuSelectorManager;
import net.l_bulb.dungeoncore.dungeoncore.Main;
import net.l_bulb.dungeoncore.item.ItemInterface;
import net.l_bulb.dungeoncore.item.ItemManager;
import net.l_bulb.dungeoncore.item.itemInterface.CraftItemable;
import net.l_bulb.dungeoncore.item.system.craft.CraftItemSelectViewerItems;
import net.l_bulb.dungeoncore.item.system.craft.TheLowCraftRecipeInterface;
import net.l_bulb.dungeoncore.item.system.lore.ItemLoreData;
import net.l_bulb.dungeoncore.item.system.lore.ItemLoreToken;
import net.l_bulb.dungeoncore.util.ItemStackUtil;

public class CraftViewerForOnlyMaterialRecipe implements MenuSelectorInterface {
  static {
    MenuSelectorManager.regist(new CraftViewerForOnlyMaterialRecipe());
  }

  private static final String TITLE = "アイテム製作1";
  private static final String THELOW_CRAFT_BUTTON_NBTTAG = "thelow_craft_button";

  public static void open(Player p, CraftItemable item) {
    TheLowPlayer theLowPlayer = TheLowPlayerManager.getTheLowPlayer(p);
    if (theLowPlayer == null) {
      TheLowPlayerManager.sendLoingingMessage(p);
      return;
    }

    Inventory createInventory = Bukkit.createInventory(null, 9 * 3, TITLE);

    TheLowCraftRecipeInterface recipe = item.getCraftRecipe();

    // アイテムを作成するボタンを配置する
    // tokenがnullならエラーとする
    ItemLoreToken loreToken = CraftItemSelectViewerItems.getLoreTokenFromRecipe(recipe);
    // エラーの時はエラーの内容を表示する
    if (loreToken == null) {
      ItemStack errorItem = ItemStackUtil.getItem("アイテムを作成できません", Material.BARRIER, "エラーが発生したため", "アイテムを作成出来ません");
      ItemStackUtil.setNBTTag(errorItem, THELOW_CRAFT_BUTTON_NBTTAG, "close");
      createInventory.setItem(11, errorItem);
    } else {
      // Loreを作成
      ItemLoreData itemLoreData = new ItemLoreData();
      itemLoreData.addBefore(item.getItemName() + "を作成します。");
      itemLoreData.addBefore("必要素材は自動で消費します。");
      itemLoreData.addLore(loreToken);
      // itemを作成
      ItemStack acceptItem = ItemStackUtil.getItem("アイテムを作成する", Material.WOOL, (byte) 5, itemLoreData.getLore().toArray(new String[0]));
      ItemStackUtil.setNBTTag(acceptItem, THELOW_CRAFT_BUTTON_NBTTAG, "craft");
      ItemStackUtil.setNBTTag(acceptItem, "thelow_craft_item_id", item.getId());
      createInventory.setItem(11, acceptItem);
    }

    ItemStack closeButton = ItemStackUtil.getItem("アイテムを作成しない", Material.WOOL, (byte) 14, "インベントリを閉める");
    ItemStackUtil.setNBTTag(closeButton, THELOW_CRAFT_BUTTON_NBTTAG, "close");
    createInventory.setItem(15, closeButton);

    // アイテムを作成しないボタンを作成する
    p.openInventory(createInventory);
  }

  @Override
  public void open(Player p) {
    p.sendMessage("この操作はサポートされていません");
  }

  @Override
  public void onSelectItem(Player p, ItemStack item, InventoryClickEvent e) {
    String nbtTag = ItemStackUtil.getNBTTag(item, THELOW_CRAFT_BUTTON_NBTTAG);
    if (nbtTag == null || nbtTag.isEmpty()) { return; }
    String craftItemId = ItemStackUtil.getNBTTag(item, "thelow_craft_item_id");
    if (craftItemId == null || craftItemId.isEmpty()) {
      p.closeInventory();
      return;
    }

    ItemInterface customItemById = ItemManager.getCustomItemById(craftItemId);
    if (customItemById == null) {
      p.closeInventory();
      p.sendMessage("エラーが発生したため、インベントリを閉じました。(1)");
      return;
    }

    if (!(customItemById instanceof CraftItemable)) {
      p.closeInventory();
      p.sendMessage("エラーが発生したため、インベントリを閉じました。(2)");
      return;
    }

    // インベントリの空きチェック
    if (p.getInventory().firstEmpty() == -1) {
      p.closeInventory();
      p.sendMessage("インベントリに空きがありません");
      return;
    }

    // 念のため素材をもっているか確認
    if (((CraftItemable) customItemById).getCraftRecipe().hasAllMaterial(p, false)) {
      // アイテムを削除する
      ((CraftItemable) customItemById).getCraftRecipe().removeMaterial(p.getInventory());
      // アイテムを追加する
      ItemStack craftedItem = customItemById.getItem();
      p.getInventory().addItem(craftedItem);

      // 取引欄に0個のアイテムが残るので2tick後に実行する
      new BukkitRunnable() {
        @Override
        public void run() {
          p.updateInventory();
        }
      }.runTaskLater(Main.plugin, 2);

      new PlayerCraftCustomItemEvent(TheLowPlayerManager.getTheLowPlayer(p), ((CraftItemable) customItemById), craftedItem).callEvent();
    } else {
      p.sendMessage("素材が足りないためアイテムを作成出来ません");
    }
    p.closeInventory();
  }

  @Override
  public String getTitle() {
    return TITLE;
  }
}