package net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill;

import java.util.HashMap;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.common.menu.MenuSelectorInterface;
import net.l_bulb.dungeoncore.common.menu.MenuSelectorManager;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.all.WeaponSkillCancel;
import net.l_bulb.dungeoncore.util.ItemStackUtil;
import net.l_bulb.dungeoncore.util.Message;

import net.md_5.bungee.api.ChatColor;

/**
 * 武器スキルを選択する
 *
 */
public class WeaponSkillSelector implements MenuSelectorInterface {

  private WeaponSkillSet weaponSkillSet;

  static {
    MenuSelectorManager.regist(null);
  }

  /**
   * スキルセットID
   *
   * @param weaponSkillSetId
   */
  public WeaponSkillSelector(WeaponSkillSet weaponSkillSet) {
    this.weaponSkillSet = weaponSkillSet;
  }

  @Override
  public void open(Player p) {
    // TODO
  }

  // ItemStackと武器スキルを結びつけたMap
  static HashMap<ItemStack, WeaponSkillInterface> viewItemMap = new HashMap<>();

  private ItemStack getViewItemStack(WeaponSkillInterface skill) {
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
    ItemStackUtil.addLore(itemStack, ChatColor.RED + skill.geWeaponSkillType().getJpName());
    ItemStackUtil.addLore(itemStack, ChatColor.YELLOW + " 消費MP:" + skill.getNeedMagicPoint());
    ItemStackUtil.addLore(itemStack, ChatColor.YELLOW + " クールタイム:" + skill.getCooltime() + "秒");

    viewItemMap.put(itemStack, skill);
    return itemStack;
  }

  @Override
  public void onSelectItem(Player p, ItemStack item, InventoryClickEvent e) {
    // クリックしたアイテムがWeaponSkillのViewアイテムでないなら無視する
    if (!viewItemMap.containsKey(item)) { return; }
    ItemStack itemInHand = p.getItemInHand();
    // 武器スキルをクリックしたViewから取得
    WeaponSkillInterface weaponSkill = viewItemMap.get(item);

    // 手持ちのアイテムのNBTTagに武器スキルのIDをセットする
    if (weaponSkill.geWeaponSkillType() == WeaponSkillType.NORMAL_SKILL) {
      ItemStackUtil.setNBTTag(itemInHand, "normal_weaponskill", weaponSkill.getId());
    } else {
      ItemStackUtil.setNBTTag(itemInHand, "special_weaponskill", weaponSkill.getId());
    }

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
    viewItemMap.clear();
  }
}
