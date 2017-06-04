package net.l_bulb.dungeoncore.item.customItem.SpreadSheetItem;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.common.event.player.PlayerSetStrengthItemResultEvent;
import net.l_bulb.dungeoncore.common.event.player.PlayerStrengthFinishEvent;
import net.l_bulb.dungeoncore.item.customItem.armoritem.AbstractArmorItem;
import net.l_bulb.dungeoncore.item.customItem.armoritem.SpreadSheetArmorData;
import net.l_bulb.dungeoncore.item.nbttag.ItemStackNbttagAccessor;
import net.l_bulb.dungeoncore.item.system.lore.ItemLoreToken;
import net.l_bulb.dungeoncore.item.system.lore.LoreLine;
import net.l_bulb.dungeoncore.item.system.strength.StrengthOperator;

public class SpreadSheetArmor extends AbstractArmorItem {
  SpreadSheetArmorData data;

  public SpreadSheetArmor(SpreadSheetArmorData data) {
    this.data = data;
  }

  @Override
  public double getArmorPointForNormalMob() {
    return data.getArmorPointNormalMob();
  }

  @Override
  public double getArmorPointForBossMob() {
    return data.getArmorPointBoss();
  }

  @Override
  public double getOtherArmorPoint(double damage, Player me, EntityDamageEvent e, boolean isBoss, LivingEntity mob,
      ItemStackNbttagAccessor accessor) {
    return 0;
  }

  @Override
  public short getMaxDurability(ItemStack e) {
    return (short) (data.getMaxDurability() + getAddDurability(StrengthOperator.getLevel(e), data.getMaxDurability()));
  }

  @Override
  public String getItemName() {
    return data.getName();
  }

  @Override
  public String getId() {
    return data.getId();
  }

  @Override
  public int getBuyPrice(ItemStack item) {
    return data.getPrice();
  }

  @Override
  public int getAvailableLevel() {
    return data.getAvailableLevel();
  }

  @Override
  public void setStrengthDetail(int level, ItemLoreToken loreToken, ItemStackNbttagAccessor nbttagSetter) {
    loreToken.addLore(LoreLine.getLoreLine("最大耐久力", nbttagSetter.getMaxDurability() + getAddDurability(level, nbttagSetter.getMaxDurability())));
  }

  @Override
  public void onPlayerStrengthFinishEvent(PlayerStrengthFinishEvent event) {}

  @Override
  protected Material getMaterial() {
    return data.getItemStack().getType();
  }

  @Override
  public String[] getDetail() {
    return data.getDetail();
  }

  @Override
  public void onSetStrengthItemResult(PlayerSetStrengthItemResultEvent event) {
    if (!event.isSuccess()) {
      int level = event.getNextLevel();
      // +6までの強化の時は失敗しても+0にしないで元にもどす
      if (level <= 6) {
        ItemStack item = event.getItem();
        StrengthOperator.updateLore(item, Math.max(0, level - 1));
        event.setItem(item);
      }
    }
  }

  @Override
  protected ItemStack getItemStackBase() {
    return data.getItemStack().clone();
  }

}
