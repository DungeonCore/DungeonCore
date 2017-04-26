package lbn.item.customItem.other;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import lbn.item.ItemInterface;
import lbn.item.ItemManager;
import lbn.item.customItem.AbstractItem;
import lbn.item.itemInterface.RightClickItemable;
import lbn.player.customplayer.MagicPointManager;
import lbn.player.magicstoneOre.MagicStoneOreType;
import lbn.util.ItemStackUtil;

public class MagicStoneOre extends AbstractItem implements RightClickItemable {

  MagicStoneOreType type;

  /**
   * MagicStoneOreTypeからインスタンスを取得する
   * 
   * @param type
   * @return
   */
  public static ItemInterface getMagicStoneOre(MagicStoneOreType type) {
    // 登録されている時はそれを取得
    ItemInterface customItemById = ItemManager.getCustomItemById("ore_" + type.toString().toLowerCase());
    if (customItemById != null) { return customItemById; }
    return new MagicStoneOre(type);
  }

  /**
   * 魔法鉱石の種類
   * 
   * @param type
   */
  public MagicStoneOre(MagicStoneOreType type) {
    this.type = type;
  }

  @Override
  public String getItemName() {
    if (type == MagicStoneOreType.COAL_ORE) {
      return "石炭";
    } else if (type == MagicStoneOreType.LAPIS_ORE) { return "MP回復石"; }
    return type.getJpName();
  }

  @Override
  public String getId() {
    return "ore_" + type.toString().toLowerCase();
  }

  @Override
  public int getBuyPrice(ItemStack item) {
    if (type == MagicStoneOreType.COAL_ORE || type == MagicStoneOreType.LAPIS_ORE) { return 50; }
    return 300;
  }

  @Override
  protected Material getMaterial() {
    if (type == MagicStoneOreType.COAL_ORE) {
      return Material.COAL;
    } else if (type == MagicStoneOreType.LAPIS_ORE) { return Material.ICE; }
    return type.getMaterial();
  }

  @Override
  public ItemStack getItem() {
    ItemStack item = super.getItem();
    item.setAmount(1);
    return item;
  }

  @Override
  public String[] getDetail() {
    if (type == MagicStoneOreType.COAL_ORE) {
      return new String[] { "魔法鉱石を精錬するときに使います" };
    } else if (type == MagicStoneOreType.GOLD_ORE) {
      return new String[] { "精錬するとお金になります" };
    } else if (type == MagicStoneOreType.LAPIS_ORE) { return new String[] { "右クリックでMPを20即時回復する" }; }
    return new String[] { "精錬すると魔法石になります" };
  }

  @Override
  public void excuteOnRightClick(PlayerInteractEvent e) {
    if (type == MagicStoneOreType.LAPIS_ORE) {
      MagicPointManager.addMagicPoint(e.getPlayer(), 20);
      e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.DRINK, 1, 5);
      // アイテムを1つ消費する
      ItemStackUtil.consumeItemInHand(e.getPlayer());
    }
  }

}
