package lbn.item.customItem.pic;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import lbn.item.ItemInterface;
import lbn.player.magicstoneOre.MagicStoneOreType;

public class GoldPickaxe extends AbstractPickaxe {

  public GoldPickaxe(int level) {
    super(level);
  }

  private static final IronPickaxe IRON_PICKAXE = new IronPickaxe(1);

  @Override
  public int getBuyPrice(ItemStack item) {
    return 50;
  }

  @Override
  String getMaterialName() {
    return "金";
  }

  @Override
  public AbstractPickaxe getNextPickAxe() {
    int nextLevel = level + 1;

    if (nextLevel >= 11) {
      return IRON_PICKAXE;
    } else {
      return new GoldPickaxe(nextLevel);
    }
  }

  @Override
  public short getMaxExp() {
    return 40;
  }

  @Override
  public boolean canDestory(MagicStoneOreType type) {
    switch (type) {
      case COAL_ORE:
      case IRON_ORE:
      case LAPIS_ORE:
        return true;
      default:
        break;
    }
    return false;
  }

  @Override
  protected Material getMaterial() {
    return Material.GOLD_PICKAXE;
  }

  @Override
  public String[] getDetail() {
    return new String[] { "鉱石を採掘するとレベルが上がります", "石炭鉱石・鉄鉱石・ラピス鉱石を", "採掘できます" };
  }

  @Override
  public int getLapisCount(short level) {
    return 2;
  }

  @Override
  public String getGiveItemId() {
    return "golden_pickaxe";
  }

  // @Override
  // protected ItemStack getItemStackBase() {
  // return ItemStackUtil.getItemStackByCommand("give @p minecraft:gold_pickaxe 1 0
  // {Unbreakable:1,CanDestroy:[\"minecraft:coal_ore\",\"minecraft:iron_ore\",\"minecraft:lapis_ore\"]}");
  // }
  public List<ItemInterface> getAllLevelPick() {
    ArrayList<ItemInterface> woodPicks = new ArrayList<ItemInterface>();
    for (int i = 1; i <= 10; i++) {
      woodPicks.add(new GoldPickaxe(i));
    }
    return woodPicks;
  }

}
