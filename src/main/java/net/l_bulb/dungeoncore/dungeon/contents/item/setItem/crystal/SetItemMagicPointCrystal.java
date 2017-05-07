package net.l_bulb.dungeoncore.dungeon.contents.item.setItem.crystal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.api.PlayerStatusType;
import net.l_bulb.dungeoncore.dungeon.contents.strength_template.StrengthTemplate;
import net.l_bulb.dungeoncore.item.setItem.AbstractAbilitySetItem;
import net.l_bulb.dungeoncore.item.setItem.SetItemPartable;
import net.l_bulb.dungeoncore.item.setItem.SetItemPartsType;
import net.l_bulb.dungeoncore.item.setItem.SetStrengthableItemParts;
import net.l_bulb.dungeoncore.item.system.lore.ItemLoreToken;
import net.l_bulb.dungeoncore.item.system.lore.LoreLine;
import net.l_bulb.dungeoncore.item.system.strength.StrengthOperator;
import net.l_bulb.dungeoncore.player.ability.impl.SetItemAbility;

public class SetItemMagicPointCrystal extends AbstractAbilitySetItem {
  @Override
  public void doRutine(Player p, ItemStack[] itemStacks) {}

  @Override
  public List<String> getLore() {
    return Arrays.asList("インベントリの左上に置くことで", "最大体力上昇。");
  }

  @Override
  public String getName() {
    return "HEALTH CRYSTAL";
  }

  @Override
  protected List<SetItemPartable> getAllItemParts() {
    ArrayList<SetItemPartable> list = new ArrayList<>();
    list.add(new HealthCystal());
    return list;
  }

  @Override
  protected void addAbility(SetItemAbility emptyAbility, Player p, ItemStack... item) {
    // 効果をつける
    emptyAbility.addData(PlayerStatusType.MAX_MAGIC_POINT, getMaxMagicPoint(StrengthOperator.getLevel(item[0])));
  }

  public int getMaxMagicPoint(int level) {
    return (1 + level) * 4;
  }
}

class MagicPointCystal extends SetStrengthableItemParts implements StrengthTemplate {
  static SetItemMagicPointCrystal setItem = new SetItemMagicPointCrystal();

  public MagicPointCystal() {
    super(setItem);
  }

  @Override
  public StrengthTemplate getStrengthTemplate() {
    return this;
  }

  @Override
  public int getMaxStrengthCount() {
    return 10;
  }

  @Override
  public void setStrengthDetail(int level, ItemLoreToken loreToken) {
    int maxPoint = ((SetItemMagicPointCrystal) getBelongSetItem()).getMaxMagicPoint(level);
    loreToken.addLore(LoreLine.getLoreLine("最大マジックポイント", maxPoint));
  }

  @Override
  public ItemStack getStrengthMaterials(int level) {
    return null;
  }

  @Override
  public int successChance(int level) {
    switch (level) {
      case 0:
        return 100;
      case 1:
        return 100;
      case 2:
        return 98;
      case 3:
        return 93;
      case 4:
        return 85;
      case 5:
        return 78;
      case 6:
        return 65;
      case 7:
        return 52;
      case 8:
        return 45;
      case 9:
        return 30;
      case 10:
        return 10;
      default:
        return 100;
    }
  }

  @Override
  public Material getMaterial() {
    return Material.PRISMARINE_SHARD;
  }

  @Override
  public SetItemPartsType getItemSetPartsType() {
    return SetItemPartsType.SLOT1;
  }

  @Override
  public int getStrengthGalions(int level) {
    return 100 + 50 * level;
  }

  @Override
  public boolean isStrengthItem() {
    return true;
  }

}
