package net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill;

import java.util.Collection;
import java.util.HashMap;

import net.l_bulb.dungeoncore.common.menu.MenuSelectorInterface;
import net.l_bulb.dungeoncore.common.menu.MenuSelectorManager;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.all.WeaponSkillCancel;
import net.l_bulb.dungeoncore.player.ItemType;
import net.l_bulb.dungeoncore.util.ItemStackUtil;
import net.l_bulb.dungeoncore.util.Message;
import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * 武器スキルを選択する
 *
 */
public class WeaponSkillSelector implements MenuSelectorInterface {
  static {
    MenuSelectorManager.regist(new WeaponSkillSelector(ItemType.SWORD, 0));
  }

  /**
   * @param type 武器のタイプ
   * @param weaponSkillLevel 武器スキルレベル
   */
  public WeaponSkillSelector(ItemType type, int weaponSkillLevel) {
    this.type = type;
    this.weaponSkillLevel = weaponSkillLevel;
  }

  ItemType type;

  int weaponSkillLevel;

  @Override
  public void open(Player p) {
    // インベントリ作成
    Inventory createInventory = Bukkit.createInventory(null, 9 * 2, getTitle());
    // 武器スキルを全て取得
    Collection<WeaponSkillInterface> sortedSkillList = WeaponSkillFactory.getSortedSkillList();
    for (WeaponSkillInterface weaponSkillInterface : sortedSkillList) {
      // スキルレベルを確認
      if (weaponSkillLevel < weaponSkillInterface.getSkillLevel()) {
        continue;
      }
      // ItemTypeを確認
      if (!weaponSkillInterface.canUse(type)) {
        continue;
      }

      // アイテムを配置する
      createInventory.addItem(getViewItemStack(weaponSkillInterface));
    }
    p.openInventory(createInventory);
  }

  static HashMap<WeaponSkillInterface, ItemStack> viewItemCache = new HashMap<WeaponSkillInterface, ItemStack>();

  // ItemStackと武器スキルを結びつけたMap
  static HashMap<ItemStack, WeaponSkillInterface> viewItemIdCache = new HashMap<ItemStack, WeaponSkillInterface>();

  private ItemStack getViewItemStack(WeaponSkillInterface skill) {
    // キャッシュから取得する
    ItemStack cacheViewItem = viewItemCache.get(skill);
    // キャッシュが存在するならそれを返す
    if (cacheViewItem != null) { return cacheViewItem; }
    // アイテムを生成
    ItemStack itemStack = skill.getViewItemStackData().toItemStack();
    // 名前をセットする
    ItemStackUtil.setDispName(itemStack, ChatColor.AQUA + skill.getName());

    // 説明をセットする
    for (String detail : skill.getDetail()) {
      ItemStackUtil.addLore(itemStack, ChatColor.WHITE + detail);
    }

    ItemStackUtil.addLore(itemStack, "");
    ItemStackUtil.addLore(itemStack, ChatColor.GREEN + "[INFO]");
    ItemStackUtil.addLore(itemStack, ChatColor.YELLOW + "    スキルレベル:" + skill.getSkillLevel());
    ItemStackUtil.addLore(itemStack, ChatColor.YELLOW + "    消費MP:" + skill.getNeedMagicPoint());
    ItemStackUtil.addLore(itemStack, ChatColor.YELLOW + "    クールタイム:" + skill.getCooltime() + "秒");

    // キャッシュにセットする
    viewItemCache.put(skill, itemStack);
    viewItemIdCache.put(itemStack, skill);
    return itemStack;
  }

  @Override
  public void onSelectItem(Player p, ItemStack item, InventoryClickEvent e) {
    // クリックしたアイテムがWeaponSkillのViewアイテムでないなら無視する
    if (!viewItemIdCache.containsKey(item)) { return; }
    ItemStack itemInHand = p.getItemInHand();
    // 武器スキルをクリックしたViewから取得
    WeaponSkillInterface weaponSkill = viewItemIdCache.get(item);
    // 手持ちのアイテムのNBTTagに武器スキルのIDをセットする
    ItemStackUtil.setNBTTag(itemInHand, "weaponskill", weaponSkill.getId());

    // スキル解除の時は通知しない
    if (WeaponSkillCancel.isThisSkill(weaponSkill)) {
      Message.sendMessage(p, "{0}武器スキルを解除しました", ChatColor.GREEN);
    } else {
      Message.sendMessage(p, "{2}武器スキルを[{1}{0}{2}]に変更しました", weaponSkill.getName(), ChatColor.GREEN, ChatColor.GRAY);
    }

    p.playSound(p.getLocation(), Sound.CLICK, 0.5f, 10);

    p.closeInventory();
  }

  @Override
  public String getTitle() {
    return "weapon skill";
  }

  /**
   * キャッシュをクリア
   */
  public static void clearCache() {
    viewItemCache.clear();
    viewItemIdCache.clear();
  }
}
