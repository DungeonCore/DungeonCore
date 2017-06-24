package net.l_bulb.dungeoncore.item.customItem.attackitem;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.item.customItem.attackitem.specialDamage.SpecialType;
import net.l_bulb.dungeoncore.player.ItemType;
import net.l_bulb.dungeoncore.util.ItemStackUtil;
import net.l_bulb.dungeoncore.util.JavaUtil;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
/**
 * 武器のデータを保持するためのクラス
 */
public class SpreadSheetWeaponData {
  // 最大耐久値をセット
  short maxDurability = -1;

  // アイテム名
  String name = null;

  // ID
  String id = null;

  // アイテムの種類
  ItemType itemType = null;

  // アイテムの素材
  ItemStack itemStack = null;

  // スキルレベル
  int skillLevel = 0;

  // 最大スロット数
  int maxSlot = 3;

  // デフォルトスロット数
  int defaultSlot = 1;

  // アイテムのランク
  int rank = 0;

  // アイテムの利用可能レベル
  int availableLevel = 0;

  // Itemの詳細
  String[] detail = null;

  // 攻撃倍率
  double damageParcent = 1;

  // エラーかどうか
  boolean isCraftItemError = false;

  // メインのクラフト素材
  String mainCraftMaterial;

  // 特殊ダメージデータ
  HashMap<SpecialType, Double> specialDamageMap = new HashMap<>();

  /**
   * エラーがどうか確認し、エラーならFALSEを返し、エラーメッセージを送信する。ただし実行者がコンソールの時はメッセージを送信しない
   *
   * @param sender
   * @return
   */
  public boolean check(CommandSender sender) {
    boolean isError = false;
    ;
    if (isCraftItemError) {
      sendError(sender, "クラフトアイテムにエラーがあります");
      isError = true;
    }
    if (name == null || name.isEmpty()) {
      sendError(sender, "名前が不正です");
      isError = true;
    }
    if (id == null || id.isEmpty()) {
      sendError(sender, "IDが不正です");
      isError = true;
    }
    if (itemStack == null) {
      sendError(sender, "アイテムの素材が不正です");
      isError = true;
    }
    return !isError;
  }

  /**
   * 最大耐久をセットする
   *
   * @param maxDurability
   */
  public void setMaxDurability(String maxDurability) {
    this.maxDurability = JavaUtil.getShort(maxDurability, (short) -1);
  }

  /**
   * アイテム自体の素材, またはコマンドをセットする
   *
   * @param item
   */
  public void setItemMaterial(String item, CommandSender sender) {
    if (item == null || item.isEmpty()) { return; }
    // アイテムの素材
    Material m = null;
    // アイテムの素材をセットする
    try {
      m = Material.getMaterial(item.toUpperCase());
    } catch (Exception e) {}

    // 素材が設定されていなければコマンドを取得する
    if (m != null) {
      itemStack = new ItemStack(m);
      return;
    }

    // コマンドからItemを取得する
    itemStack = ItemStackUtil.getItemStackByCommand(item, sender);
  }

  /**
   * アイテムのタイプをセットする
   *
   * @param itemType
   */
  public void setItemType(String itemType) {
    try {
      this.itemType = ItemType.valueOf(itemType.toUpperCase());
    } catch (Exception e) {
      this.itemType = null;
    }
  }

  /**
   * 攻撃力の倍率をセットする
   *
   * @param damageParcent
   */
  public void setDamageParcent(String damageParcent) {
    this.damageParcent = JavaUtil.getDouble(damageParcent, 1);
  }

  /**
   * スキルレベルをセット
   *
   * @param skillLevel
   */
  public void setSkillLevel(String skillLevel) {
    this.skillLevel = JavaUtil.getInt(skillLevel, 0);
  }

  /**
   * 最大スロットをセットする
   *
   * @param maxSlot
   */
  public void setMaxSlot(String maxSlot) {
    this.maxSlot = JavaUtil.getInt(maxSlot, this.maxSlot);
  }

  /**
   * デフォルトスロットをセットする
   *
   * @param defaultSlot
   */
  public void setDefaultSlot(String defaultSlot) {
    this.defaultSlot = JavaUtil.getInt(defaultSlot, this.defaultSlot);
  }

  /**
   * 武器のランクをセット
   *
   * @param rank
   */
  public void setRank(String rank) {
    this.rank = JavaUtil.getInt(rank, this.rank);
  }

  /**
   * 使用可能レベルをセット
   *
   * @param availableLevel
   */
  public void setAvailableLevel(String availableLevel) {
    this.availableLevel = JavaUtil.getInt(availableLevel, this.availableLevel);
  }

  /**
   * コンソール以外の時、エラーを送信する
   *
   * @param sender
   */
  public void sendError(CommandSender sender, String error) {
    if (!(sender instanceof ConsoleCommandSender)) {
      sender.sendMessage(error);
    }
  }

  /**
   * 詳細を取得する
   *
   * @return
   */
  public String[] getDetail() {
    if (detail == null) {
      detail = new String[0];
    }
    return detail;
  }

  /**
   * アイテムの詳細をセットする
   *
   * @param detail
   */
  public void setDetail(String detail) {
    if (detail != null) {
      this.detail = detail.split(",");
    } else {
      this.detail = new String[0];
    }
  }

  /**
   * 特殊ダメージ効果を追加
   *
   * @param type type
   * @param value 増加倍率
   */
  public boolean addSpecialDamage(String type, String value) {
    SpecialType specialType = SpecialType.fromName(type);
    double specialDamageParcent = JavaUtil.getDouble(value, -1);

    // 値が不正ならfalse
    if (specialType == null || specialDamageParcent == -1) { return false; }

    // 値を格納する
    specialDamageMap.put(specialType, specialDamageParcent / 100.0);

    return true;
  }

}
