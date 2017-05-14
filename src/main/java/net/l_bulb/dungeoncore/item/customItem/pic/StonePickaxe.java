package net.l_bulb.dungeoncore.item.customItem.pic;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.item.ItemInterface;
import net.l_bulb.dungeoncore.player.magicstoneOre.MagicStoneOreType;

public class StonePickaxe extends AbstractPickaxe {

  private static final GoldPickaxe GOLD_PICKAXE = new GoldPickaxe(1);

  public StonePickaxe(int level) {
    super(level);
  }

  @Override
  public int getBuyPrice(ItemStack item) {
    return 100;
  }

  @Override
  String getMaterialName() {
    return "石";
  }

  @Override
  public AbstractPickaxe getNextPickAxe() {
    int nextLevel = level + 1;

    if (nextLevel >= 11) {
      return GOLD_PICKAXE;
    } else {
      return new StonePickaxe(nextLevel);
    }
  }

  @Override
  public short getMaxExp() {
    switch (level) {
      case 1:
        return 500;
      case 2:
        return 1000;
      case 3:
        return 1000;
      case 4:
        return 500;
      case 5:
        return 500;
      case 6:
        return 500;
      case 7:
        return 500;
      case 8:
        return 500;
      case 9:
        return 1500;
      case 10:
        return 2000;
      default:
        break;
    }
    return 2000;
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
    return Material.STONE_PICKAXE;
  }

  @Override
  public String[] getDetail() {
    return new String[] { "鉱石を採掘するとレベルが上がります", "石炭鉱石・鉄鉱石・ラピス鉱石を採掘できます" };
  }

  @Override
  public int getLapisCount(short level) {
    return 1;
  }

  @Override
  public String getGiveItemId() {
    return "stone_pickaxe";
  }

  // @Override
  // protected ItemStack getItemStackBase() {
  // return ItemStackUtil.getItemStackByCommand("give @p minecraft:stone_pickaxe 1 0
  // {Unbreakable:1,CanDestroy:[\"minecraft:coal_ore\",\"minecraft:iron_ore\",\"minecraft:lapis_ore\"]}");
  // }

  /**
   * 全てのレベルの石のピッケルを取得する
   *
   * @return
   */
  @Override
  public List<ItemInterface> getAllLevelPick() {
    ArrayList<ItemInterface> woodPicks = new ArrayList<>();
    for (int i = 1; i <= 10; i++) {
      woodPicks.add(new StonePickaxe(i));
    }
    return woodPicks;
  }
}
