package net.l_bulb.dungeoncore.item.customItem.armoritem;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.util.ItemStackUtil;
import net.l_bulb.dungeoncore.util.JavaUtil;

/**
 * 武器のデータを保持するためのクラス
 */
public class SpreadSheetArmorData {
  // 最大耐久値をセット
  short maxDurability = -1;

  // アイテム名
  String name = null;

  // ID
  String id = null;

  // アイテムの素材
  ItemStack itemstack = null;

  // アイテムの利用可能レベル
  int availableLevel = 0;

  // Itemの詳細
  String[] detail = null;

  // クラフトに必要なアイテムとその数
  HashMap<String, Integer> craftMaterial = new HashMap<>();

  // エラーかどうか
  boolean isCraftItemError = false;

  // メインのクラフト素材
  String mainCraftMaterial = null;

  // 通常モンスターの防御ポイント
  double armorPointNormalMob = -1;
  // ボスモンスターの防御ポイント
  double armorPointBoss = -1;

  int price = 0;

  double addHp = 0;

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
    if (name == null || name.isEmpty()) {
      sendError(sender, "IDが不正です");
      isError = true;
    }
    if (armorPointNormalMob == -1) {
      sendError(sender, "通常モンスターの防具ポイントが不正です");
      isError = true;
    }
    if (armorPointBoss == -1) {
      sendError(sender, "ボスモンスターの防具ポイントが不正です");
      isError = true;
    }
    return !isError;
  }

  public int getPrice() {
    return price;
  }

  public void setPrice(String price) {
    this.price = JavaUtil.getInt(price, 0);
  }

  /**
   * 最大耐久を取得, もし設定されていない場合は-1を返す
   * 
   * @return
   */
  public short getMaxDurability() {
    return maxDurability;
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
   * アイテム名を取得
   * 
   * @return
   */
  public String getName() {
    return name;
  }

  /**
   * アイテム名をセットする
   * 
   * @param name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * アイテムIDを取得する
   * 
   * @return
   */
  public String getId() {
    return id;
  }

  /**
   * アイテムIDを取得する
   * 
   * @param id
   */
  public void setId(String id) {
    this.id = id;
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
      itemstack = new ItemStack(m);
      return;
    }

    // コマンドからItemを取得する
    itemstack = ItemStackUtil.getItemStackByCommand(item, sender);
  }

  /**
   * 使用可能レベルを取得
   * 
   * @return
   */
  public int getAvailableLevel() {
    return availableLevel;
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
   * アイテム制作に使うアイテムIDと個数をセットする
   * 
   * @param itemid アイテムID
   * @param count 個数
   */
  public void setCraftItem(String itemid, String count) {
    craftMaterial.put(itemid, JavaUtil.getInt(count, 1));
  }

  /**
   * 制作に必要なアイテムIDと個数を取得
   * 
   * @return
   */
  public Map<String, Integer> getCraftItem() {
    return craftMaterial;
  }

  /**
   * メインのクラフト素材をセットする
   * 
   * @param mainCraftMaterial
   */
  public void setMainCraftMaterial(String mainCraftMaterial) {
    this.mainCraftMaterial = mainCraftMaterial;
  }

  /**
   * メインのクラフト素材を取得する
   * 
   * @return
   */
  public String getMainCraftMaterial() {
    return mainCraftMaterial;
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
   * ItemStackを取得する
   * 
   * @return
   */
  public ItemStack getItemStack() {
    return itemstack;
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

  public double getArmorPointNormalMob() {
    return armorPointNormalMob;
  }

  public void setArmorPointNormalMob(String point) {
    this.armorPointNormalMob = JavaUtil.getDouble(point, -1);
  }

  public double getArmorPointBoss() {
    return armorPointBoss;
  }

  public void setArmorPointBoss(String point) {
    this.armorPointBoss = JavaUtil.getDouble(point, -1);
  }
}
