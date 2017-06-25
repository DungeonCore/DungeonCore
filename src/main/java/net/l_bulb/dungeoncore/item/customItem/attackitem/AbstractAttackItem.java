package net.l_bulb.dungeoncore.item.customItem.attackitem;

import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.api.LevelType;
import net.l_bulb.dungeoncore.api.player.TheLowPlayer;
import net.l_bulb.dungeoncore.api.player.TheLowPlayerManager;
import net.l_bulb.dungeoncore.dungeon.contents.strength_template.StrengthTemplate;
import net.l_bulb.dungeoncore.item.customItem.AbstractItem;
import net.l_bulb.dungeoncore.item.customItem.attackitem.specialDamage.SpecialType;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.WeaponSkillSelector;
import net.l_bulb.dungeoncore.item.itemInterface.CombatItemable;
import net.l_bulb.dungeoncore.item.itemInterface.LeftClickItemable;
import net.l_bulb.dungeoncore.item.itemInterface.Strengthenable;
import net.l_bulb.dungeoncore.item.nbttag.CustomWeaponItemStack;
import net.l_bulb.dungeoncore.item.nbttag.ItemStackNbttagAccessor;
import net.l_bulb.dungeoncore.item.slot.magicstone.EmptySlot;
import net.l_bulb.dungeoncore.item.system.lore.ItemLoreData;
import net.l_bulb.dungeoncore.item.system.lore.ItemLoreToken;
import net.l_bulb.dungeoncore.item.system.lore.LoreLine;
import net.l_bulb.dungeoncore.util.ItemStackUtil;
import net.l_bulb.dungeoncore.util.JavaUtil;
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
  public boolean excuteOnRightClick(PlayerInteractEvent e) {
    // レベルを確認
    Player player = e.getPlayer();
    if (!isAvilable(player)) {
      sendNotAvailableMessage(player);
      e.setCancelled(true);
      return true;
    }
    return false;
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
  @Override
  public double getMaterialDamage() {
    return ItemStackUtil.getVanillaDamage(getMaterial());
  }

  @Override
  public ItemLoreToken getStandardLoreToken(ItemStackNbttagAccessor newParam) {
    ItemLoreToken loreToken = super.getStandardLoreToken(newParam);
    // 使用可能レベル
    loreToken.addLore(Message.getMessage("使用可能 ： {2}{0}{1}以上", getAttackType().getLevelType().getName(), getAvailableLevel(), ChatColor.GOLD));
    loreToken.addLore("スキルレベル ： " + ChatColor.GOLD + getSkillLevel() + "レベル");
    loreToken.addLore("耐久値 ： " + ChatColor.GOLD + newParam.getMaxDurability());
    // 通常ダメージ
    loreToken.addLore(LoreLine.getLoreLine("攻撃力", "+" + JavaUtil.round(newParam.getDamage() - getMaterialDamage(), 2)));

    return loreToken;
  }

  @Override
  public ItemStack getItem() {
    ItemStack item = super.getItem();

    CustomWeaponItemStack instance = CustomWeaponItemStack.getInstance(item, this);

    // Slotを追加
    EmptySlot slot = new EmptySlot();
    for (int i = 0; i < instance.getDefaultSlotCount(); i++) {
      instance.addSlot(slot);
    }
    instance.updateItem();
    return item;
  }

  @Override
  protected void setLore(ItemLoreData itemLoreData, ItemStackNbttagAccessor nbttagAccessor) {
    super.setLore(itemLoreData, nbttagAccessor);

    // 特殊攻撃のLoreを挿入する
    Map<SpecialType, Double> specialDamageTypeMap = getSpecialDamageTypeMap();

    // 追加ダメージのLoreを追加する
    ItemLoreToken loreToken = new ItemLoreToken("追加ダメージ");
    for (Entry<SpecialType, Double> entry : specialDamageTypeMap.entrySet()) {
      // 追加ダメージ
      double addDamage = nbttagAccessor.getDamage() * entry.getValue();
      loreToken.addLore(LoreLine.getLoreLine(entry.getKey().getName(), "+" + JavaUtil.round(addDamage, 2)));
    }
    if (loreToken.size() != 0) {
      itemLoreData.addLore(loreToken);
    }
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
