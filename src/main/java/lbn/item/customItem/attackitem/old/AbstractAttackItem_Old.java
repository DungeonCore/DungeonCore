package lbn.item.customItem.attackitem.old;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import lbn.api.LevelType;
import lbn.api.player.TheLowPlayer;
import lbn.api.player.TheLowPlayerManager;
import lbn.common.event.player.PlayerCombatEntityEvent;
import lbn.item.customItem.attackitem.AbstractAttackItem;
import lbn.item.system.lore.ItemLoreToken;
import lbn.item.system.strength.StrengthOperator;
import lbn.util.Message;

public abstract class AbstractAttackItem_Old extends AbstractAttackItem {
  public boolean isAvilable(Player player) {
    // クリエイティブなら使えるようにする
    if (player.getGameMode() == GameMode.CREATIVE) { return true; }

    // Playerインスタンスを取得
    TheLowPlayer theLowPlayer = TheLowPlayerManager.getTheLowPlayer(player);
    if (theLowPlayer == null) { return false; }
    int level = theLowPlayer.getLevel(getLevelType());
    if (getAvailableLevel() > level) { return false; }
    return true;
  }

  @Override
  protected int getSkillLevel() {
    return getAvailableLevel() + 1;
  }

  @Override
  public short getMaxDurability(ItemStack item) {
    return getMaterial().getMaxDurability();
  }

  @Override
  public void onCombatEntity(PlayerCombatEntityEvent e) {}

  @Override
  public LevelType getLevelType() {
    return getAttackType().getLevelType();
  }

  /**
   * デフォルトのスロットの数
   * 
   * @return
   */
  public int getDefaultSlotCount() {
    return 1;
  }

  /**
   * デフォルトのスロットの数
   * 
   * @return
   */
  public int getMaxSlotCount() {
    return 3;
  }

  @Override
  public int getMaxStrengthCount() {
    int available = getAvailableLevel();
    if (available < 10) {
      return 5;
    } else if (available < 20) {
      return 6;
    } else if (available < 30) {
      return 7;
    } else if (available < 40) {
      return 8;
    } else if (available < 50) {
      return 9;
    } else if (available < 60) {
      return 10;
    } else if (available < 70) {
      return 11;
    } else if (available < 80) {
      return 12;
    } else if (available < 90) {
      return 12;
    } else if (available < 100) {
      return 13;
    } else if (available < 110) {
      return 13;
    } else if (available < 120) {
      return 14;
    } else if (available < 120) {
      return 14;
    } else {
      return 15;
    }
  }

  double a = 0.15;
  double b = -1;
  double c = -1;

  /**
   * 武器のダメージを取得 (武器本体のダメージも含まれます)
   * 
   * @param p
   * @param get_money_item
   * @return
   */
  public double getAttackItemDamage(int strengthLevel) {
    if (b == -1) {
      b = (-a * 0 * 0 + a * getMaxStrengthCount() * getMaxStrengthCount() + getMinAttackDamage() - getMaxAttackDamage())
          / (0 - getMaxStrengthCount());
    }
    if (c == -1) {
      c = getMinAttackDamage();
    }
    double damage = a * strengthLevel * strengthLevel + b * strengthLevel + c;

    return damage;
  }

  @Override
  public int getWeaponLevel() {
    return 0;
  }

  @Override
  public boolean isShowItemList() {
    return false;
  }

  public abstract double getMaterialDamage();

  protected void sendNotAvailableMessage(Player p) {
    Message.sendMessage(p, Message.CANCEL_USE_ITEM_BY_LEVEL, getAttackType().getLevelType().getName(), getAvailableLevel());
  }

  protected double getMaxAttackDamage() {
    return getAttackType().getMaxDamage(getAvailableLevel());
  }

  protected double getMinAttackDamage() {
    return getAttackType().getMinDamage(getAvailableLevel());
  }

  public void setStrengthDetail(int level, ItemLoreToken loreToken) {
    // もう使わないので記載しない
  }

  abstract protected String[] getStrengthDetail2(int level);

  @Override
  public int getBuyPrice(ItemStack item) {
    int availableLevel = Math.min(getAvailableLevel(), 70);
    return (int) (getBaseBuyPrice() + availableLevel * 5 + ((StrengthOperator.getLevel(item) + 1) * 200));
  }

  abstract protected int getBaseBuyPrice();
}
