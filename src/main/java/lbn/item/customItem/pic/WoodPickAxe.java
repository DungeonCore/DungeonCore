package lbn.item.customItem.pic;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import lbn.item.ItemInterface;
import lbn.player.magicstoneOre.MagicStoneOreType;

public class WoodPickAxe extends AbstractPickaxe {

  private static final StonePickaxe STONE_PICKAXE = new StonePickaxe(1);

  public WoodPickAxe(int level) {
    super(level);
  }

  @Override
  public int getBuyPrice(ItemStack item) {
    return 10;
  }

  @Override
  String getMaterialName() {
    return "木";
  }

  @Override
  public AbstractPickaxe getNextPickAxe() {
    int nextLevel = level + 1;

    if (nextLevel >= 11) {
      return STONE_PICKAXE;
    } else {
      return new WoodPickAxe(nextLevel);
    }
  }

  @Override
  public short getMaxExp() {
    switch (level) {
      case 1:
        return 50;
      case 2:
        return 100;
      case 3:
        return 150;
      case 4:
        return 300;
      case 5:
        return 500;
      case 6:
        return 600;
      case 7:
        return 700;
      case 8:
        return 800;
      case 9:
        return 1000;
      case 10:
        return 1500;
      default:
        break;
    }
    return 1500;
  }

  @Override
  public boolean canDestory(MagicStoneOreType type) {
    return type == MagicStoneOreType.COAL_ORE;
  }

  @Override
  protected Material getMaterial() {
    return Material.WOOD_PICKAXE;
  }

  @Override
  public String[] getDetail() {
    return new String[] { "鉱石を採掘するとレベルが上がります", "石炭鉱石を採掘できます" };
  }

  @Override
  public int getLapisCount(short level) {
    return 1;
  }

  @Override
  public String getGiveItemId() {
    return "wooden_pickaxe";
  }

  /**
   * 全てのレベルの木のピッケルを取得する
   * 
   * @return
   */
  public List<ItemInterface> getAllLevelPick() {
    ArrayList<ItemInterface> woodPicks = new ArrayList<ItemInterface>();
    for (int i = 1; i <= 10; i++) {
      woodPicks.add(new WoodPickAxe(i));
    }
    return woodPicks;
  }
}
