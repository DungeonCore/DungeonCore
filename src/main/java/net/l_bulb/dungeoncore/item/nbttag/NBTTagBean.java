package net.l_bulb.dungeoncore.item.nbttag;

import java.util.Random;

import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.item.ItemInterface;
import net.l_bulb.dungeoncore.item.ItemManager;
import net.l_bulb.dungeoncore.item.itemInterface.ArmorItemable;
import net.l_bulb.dungeoncore.item.itemInterface.CombatItemable;
import net.l_bulb.dungeoncore.item.itemInterface.EquipItemable;
import net.l_bulb.dungeoncore.util.NormalDistributionRandomGenerator;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NBTTagBean {
  public NBTTagBean(ItemStack item, ItemInterface customItem) {
    this.itemId = customItem.getId();

    // 耐久値
    if (ItemManager.isImplemental(EquipItemable.class, customItem)) {
      maxDurability = ((EquipItemable) customItem).getMaxDurability(item);
      nowDurability = ((EquipItemable) customItem).getMaxDurability(item);
    }

    // 武器ならスロットなどを追加
    if (ItemManager.isImplemental(CombatItemable.class, customItem)) {
      CombatItemable combatItemable = (CombatItemable) customItem;
      // 攻撃力
      damage = combatItemable.getAttackItemDamage(0);
      // デフォルトスロット数
      defaultSlot = combatItemable.getDefaultSlotCount();
      // 最大スロット数
      maxSlot = combatItemable.getMaxSlotCount();
    }

    // 防具なら防御ポイントを追加
    if (ItemManager.isImplemental(ArmorItemable.class, customItem)) {
      normalArmorPoint = ((ArmorItemable) customItem).getArmorPointForNormalMob();
      bossArmorPoint = ((ArmorItemable) customItem).getArmorPointForBossMob();
    }
  }

  // ダメージ
  double damage;

  // アイテムID
  String itemId;

  // 最大耐久値
  short maxDurability;

  // 今の耐久値
  short nowDurability;

  // デフォルトスロット数
  int defaultSlot;

  // 最大スロット数
  int maxSlot;

  // 通常モンスターの防御ポイント
  double normalArmorPoint;

  // ボスモンスターの防御ポイント
  double bossArmorPoint;

  // 乱数生成
  static NormalDistributionRandomGenerator generator = new NormalDistributionRandomGenerator(0.9, 0.13);

  /**
   * ランダムで値を適切なものに置き換える
   */
  public void fixValue(ItemInterface item) {
    if (ItemManager.isImplemental(CombatItemable.class, item)) {
      // ダメージ
      damage *= generator.next();

      // 耐久値
      maxDurability = (short) (maxDurability * generator.next());
      nowDurability = maxDurability;

      Random random = new Random();
      // 最大スロット数
      maxSlot = random.nextInt(maxSlot) + 1;
    } else if (ItemManager.isImplemental(ArmorItemable.class, item)) {
      // 耐久値
      maxDurability = (short) (maxDurability * generator.next());
      nowDurability = maxDurability;

      // 防御ポイント
      normalArmorPoint *= generator.next();
      bossArmorPoint *= generator.next();
    }
  }

  enum ItemNbttagType {
    WEAPON, ARMOR, OTHER
  }
}
