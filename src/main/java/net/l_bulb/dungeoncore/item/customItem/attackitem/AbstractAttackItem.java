package net.l_bulb.dungeoncore.item.customItem.attackitem;

import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.api.LevelType;
import net.l_bulb.dungeoncore.api.player.TheLowPlayer;
import net.l_bulb.dungeoncore.api.player.TheLowPlayerManager;
import net.l_bulb.dungeoncore.common.event.player.PlayerRightShiftClickEvent;
import net.l_bulb.dungeoncore.dungeon.contents.strength_template.StrengthTemplate;
import net.l_bulb.dungeoncore.item.customItem.AbstractItem;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.WeaponSkillSelector;
import net.l_bulb.dungeoncore.item.itemInterface.CombatItemable;
import net.l_bulb.dungeoncore.item.itemInterface.LeftClickItemable;
import net.l_bulb.dungeoncore.item.itemInterface.Strengthenable;
import net.l_bulb.dungeoncore.item.slot.magicstone.EmptySlot;
import net.l_bulb.dungeoncore.item.system.lore.ItemLoreToken;
import net.l_bulb.dungeoncore.util.ItemStackUtil;
import net.l_bulb.dungeoncore.util.Message;

public abstract class AbstractAttackItem extends AbstractItem implements Strengthenable, CombatItemable, LeftClickItemable {
  /**
   * この武器が使用可能ならTRUE
   *
   * @param player
   * @return
   */
  protected boolean isAvilable(Player player) {
    // クリエイティブなら使えるようにする
    if (player.getGameMode() == GameMode.CREATIVE) { return true; }

    // Playerインスタンスを取得
    TheLowPlayer theLowPlayer = TheLowPlayerManager.getTheLowPlayer(player);
    if (theLowPlayer == null) {
      TheLowPlayerManager.sendLoingingMessage(player);
      return false;
    }
    // レベルを確認
    int level = theLowPlayer.getLevel(getLevelType());
    if (getAvailableLevel() > level) { return false; }
    return true;
  }

  static WeaponStrengthTemplate strengthTemplate = new WeaponStrengthTemplate();

  @Override
  public StrengthTemplate getStrengthTemplate() {
    return strengthTemplate;
  }

  @Override
  public void excuteOnRightClick(PlayerInteractEvent e) {
    // レベルを確認
    Player player = e.getPlayer();
    if (!isAvilable(player)) {
      sendNotAvailableMessage(player);
      e.setCancelled(true);
      return;
    }

    ItemStack item = e.getItem();
    if (player.isSneaking()) {
      new PlayerRightShiftClickEvent(player, item).callEvent();
    }
  }

  @Override
  public LevelType getLevelType() {
    return getAttackType().getLevelType();
  }

  /**
   * スキルレベルを取得
   *
   * @return
   */
  abstract protected int getSkillLevel();

  /**
   * 武器のダメージを取得 (武器本体のダメージも含まれます)
   *
   * @param p
   * @param strengthLevel 強化レベル
   * @return
   */
  abstract public double getAttackItemDamage(int strengthLevel);

  /**
   * クラフトレベルを取得する
   *
   * @return
   */
  abstract public int getWeaponLevel();

  @Override
  public boolean isShowItemList() {
    return true;
  }

  /**
   * この武器のアイテムのデフォルトの攻撃力を取得
   *
   * @return
   */
  public double getMaterialDamage() {
    double vanillaDamage = ItemStackUtil.getVanillaDamage(getMaterial());
    return vanillaDamage;
  }

  @Override
  public ItemLoreToken getStandardLoreToken() {
    ItemLoreToken loreToken = super.getStandardLoreToken();
    // 使用可能レベル
    loreToken.addLore(Message.getMessage("使用可能 ： {2}{0}{1}以上", getAttackType().getLevelType().getName(), getAvailableLevel(), ChatColor.GOLD));
    loreToken.addLore("スキルレベル ： " + ChatColor.GOLD + getSkillLevel() + "レベル");
    // 武器は耐久とレベルが関係ないのでnullでも問題ない
    loreToken.addLore("耐久値 ： " + ChatColor.GOLD + getMaxDurability(null));
    return loreToken;
  }

  @Override
  public ItemStack getItem() {
    ItemStack item = super.getItem();
    // SLOTを追加
    ArrayList<String> arrayList = new ArrayList<>();
    arrayList.add(ChatColor.GREEN + "[SLOT]  " + ChatColor.AQUA + "最大" + getMaxSlotCount() + "個" + ItemLoreToken.TITLE_TAG);
    EmptySlot slot = new EmptySlot();
    for (int i = 0; i < getDefaultSlotCount(); i++) {
      arrayList.add(StringUtils.join(new Object[] { slot.getNameColor(), "    ■ ", slot.getSlotName(), ChatColor.BLACK, "id:", slot.getId() }));
    }
    arrayList.add("");
    ItemStackUtil.addLore(item, arrayList.toArray(new String[0]));
    return item;
  }

  /**
   * 使用不可のときに表示するメッセージ
   *
   * @param p
   */
  protected void sendNotAvailableMessage(Player p) {
    Message.sendMessage(p, Message.CANCEL_USE_ITEM_BY_LEVEL, getAttackType().getLevelType().getName(), getAvailableLevel());
  }

  @Override
  public void onPlayerDropItemEvent(PlayerDropItemEvent e) {
    Player player = e.getPlayer();
    // スニーク状態でないなら無視
    if (!player.isSneaking()) { return; }
    // 捨てるのをキャンセル
    e.setCancelled(true);

    // レベルが足りないなら何もしない
    if (isAvilable(player)) {
      // 選択メニューを開く
      new WeaponSkillSelector(getAttackType(), getSkillLevel()).open(player);
    } else {
      sendNotAvailableMessage(player);
    }
  }
}
