package lbn.item.customItem.armoritem;

import org.apache.commons.lang.math.RandomUtils;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

import lbn.common.event.player.PlayerSetStrengthItemResultEvent;
import lbn.common.event.player.PlayerStrengthFinishEvent;
import lbn.item.ItemManager;
import lbn.item.system.craft.TheLowCraftRecipeInterface;
import lbn.item.system.lore.ItemLoreToken;
import lbn.util.ItemStackUtil;

public class TestArmorItem extends AbstractArmorItem {
  int level;
  private Material m;

  public TestArmorItem(int level, Material m) {
    this.level = level;
    this.m = m;

    ItemManager.registItem(this);
  }

  @Override
  public double getArmorPointForNormalMob() {
    return 3 + (34.2 - 3) / 60 * level;
  }

  @Override
  public double getArmorPointForBossMob() {
    return 3.5 + (40.3 - 3) / 60 * level;
  }

  @Override
  public double getOtherArmorPoint(double damage, Player me, EntityDamageEvent e, boolean isBoss, LivingEntity mob) {
    return 0;
  }

  @Override
  public short getMaxDurability(ItemStack e) {
    return 2000;
  }

  @Override
  public boolean isShowItemList() {
    return false;
  }

  @Override
  public String getItemName() {
    return "レベル" + level + " テスト用 装備";
  }

  @Override
  public String getId() {
    return "armor_test_" + level + "_" + getMaterial();
  }

  @Override
  public int getBuyPrice(ItemStack item) {
    return 0;
  }

  @Override
  protected ItemStack getItemStackBase() {
    long nextLong = RandomUtils.nextLong();

    String command = "/give @p minecraft:" + getMaterial().toString().toLowerCase() + " 1 0 {AttributeModifiers:[{Name: HaruEditor,UUIDLeast: "
        + nextLong++ + "L,UUIDMost: " + nextLong + "L,Operation: 0,AttributeName: generic.maxHealth,Amount: "
        + getHealthAdd(getAvailableLevel()) + "d}]}";
    ItemStack itemStackBase = ItemStackUtil.getItemStackByCommand(command);
    return itemStackBase;
  }

  @Override
  public int getAvailableLevel() {
    return level;
  }

  @Override
  public void setStrengthDetail(int level, ItemLoreToken loreToken) {

  }

  @Override
  public TheLowCraftRecipeInterface getCraftRecipe() {
    return null;
  }

  @Override
  public void onSetStrengthItemResult(PlayerSetStrengthItemResultEvent event) {

  }

  @Override
  public void onPlayerStrengthFinishEvent(PlayerStrengthFinishEvent event) {

  }

  @Override
  protected Material getMaterial() {
    return m;
  }

  @Override
  public String[] getDetail() {
    return null;
  }

  public static double getHealthAdd(int level) {
    return (20.0 / (-0.0002 * level * level + 1) - 20.0) / 4.0;
  }
}
