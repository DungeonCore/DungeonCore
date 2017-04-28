package net.l_bulb.dungeoncore.common.other;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

/**
 * Materialとデータを管理する
 */
public class ItemStackData {
  Material m;

  byte data = 0;

  Map<Enchantment, Integer> enchantMap = null;

  public ItemStackData(Material m) {
    this.m = m;
  }

  public ItemStackData(Material m, int data) {
    this.m = m;
    this.data = (byte) data;
  }

  public ItemStackData(Material m, int data, Enchantment enchantment, Integer level) {
    this.m = m;
    this.data = (byte) data;

    // エンチャントを追加
    enchantMap = new HashMap<>();
    enchantMap.put(enchantment, level);
  }

  public Material getMaterial() {
    return m;
  }

  public void setMaterial(Material m) {
    this.m = m;
  }

  public byte getData() {
    return data;
  }

  public void setData(int data) {
    this.data = (byte) data;
  }

  /**
   * エンチャントマップを取得。もしエンチャントがない場合はnullを返す
   * 
   * @return
   */
  public Map<Enchantment, Integer> getEnchantMap() {
    return enchantMap;
  }

  /**
   * エンチャントを追加
   * 
   * @param enchantment
   * @param level
   */
  public void addEnchant(Enchantment enchantment, int level) {
    // エンチャントを追加
    enchantMap = new HashMap<>();
    enchantMap.put(enchantment, level);
  }

  /**
   * ItemStackに変換
   * 
   * @return
   */
  @SuppressWarnings("deprecation")
  public ItemStack toItemStack() {
    ItemStack itemStack = new ItemStack(m, 1);
    // データ値を設定する
    MaterialData data2 = itemStack.getData();
    data2.setData(getData());
    itemStack.setData(data2);
    itemStack.setDurability(getData());
    if (enchantMap != null) {
      for (Entry<Enchantment, Integer> entry : enchantMap.entrySet()) {
        itemStack.addEnchantment(entry.getKey(), entry.getValue());
      }
    }
    return itemStack;
  }
}
