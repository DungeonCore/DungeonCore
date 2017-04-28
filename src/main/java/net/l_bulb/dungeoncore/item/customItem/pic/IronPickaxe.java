package net.l_bulb.dungeoncore.item.customItem.pic;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.item.ItemInterface;
import net.l_bulb.dungeoncore.player.magicstoneOre.MagicStoneOreType;

public class IronPickaxe extends AbstractPickaxe {

  public IronPickaxe(int level) {
    super(level);
  }

  private static final DiamondPickaxe DIAMOND_PICKAXE = new DiamondPickaxe(1);

  @Override
  public int getBuyPrice(ItemStack item) {
    return 200;
  }

  @Override
  String getMaterialName() {
    return "鉄";
  }

  @Override
  public AbstractPickaxe getNextPickAxe() {
    int nextLevel = level + 1;

    if (nextLevel >= 11) {
      return DIAMOND_PICKAXE;
    } else {
      return new IronPickaxe(nextLevel);
    }
  }

  @Override
  public short getMaxExp() {
    return 50;
  }

  @Override
  public boolean canDestory(MagicStoneOreType type) {
    switch (type) {
      case COAL_ORE:
      case IRON_ORE:
      case LAPIS_ORE:
      case REDSTONE_ORE:
      case GOLD_ORE:
        return true;
      default:
        break;
    }
    return false;
  }

  @Override
  protected Material getMaterial() {
    return Material.IRON_PICKAXE;
  }

  @Override
  public String[] getDetail() {
    return new String[] { "鉱石を採掘するとレベルが上がります", "石炭鉱石・鉄鉱石・ラピス鉱石・", "レッドストーン鉱石・金鉱石を採掘できます" };
  }

  @Override
  public int getLapisCount(short level) {
    return 3;
  }

  @Override
  public String getGiveItemId() {
    return "iron_pickaxe";
  }

  @Override
  public List<ItemInterface> getAllLevelPick() {
    ArrayList<ItemInterface> woodPicks = new ArrayList<ItemInterface>();
    for (int i = 1; i <= 10; i++) {
      woodPicks.add(new IronPickaxe(i));
    }
    return woodPicks;
  }
}
