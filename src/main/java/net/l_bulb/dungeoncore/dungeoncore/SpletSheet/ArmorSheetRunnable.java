package net.l_bulb.dungeoncore.dungeoncore.SpletSheet;

import org.bukkit.command.CommandSender;

import net.l_bulb.dungeoncore.item.ItemManager;
import net.l_bulb.dungeoncore.item.customItem.SpreadSheetItem.SpreadSheetArmor;
import net.l_bulb.dungeoncore.item.customItem.armoritem.SpreadSheetArmorData;

public class ArmorSheetRunnable extends AbstractSheetRunable {

  public ArmorSheetRunnable(CommandSender sender) {
    super(sender);
  }

  @Override
  protected String getQuery() {
    return "id!=\"\"";
  }

  @Override
  public String getSheetName() {
    return "armor";
  }

  @Override
  public String[] getTag() {
    return new String[] { "id", "name", "detail", "materal", "armorPoint", "armorPointBoss", "dummy1", "durability", "uselevel", "price"};
  }

  @Override
  protected void excuteOnerow(String[] row) {
    SpreadSheetArmorData data = new SpreadSheetArmorData();
    data.setId(row[0]);
    data.setName(row[1]);
    data.setDetail(row[2]);
    data.setItemMaterial(row[3], sender);
    data.setArmorPointNormalMob(row[4]);
    data.setArmorPointBoss(row[5]);
    data.setMaxDurability(row[7]);
    data.setAvailableLevel(row[8]);
    data.setPrice(row[9]);

    if (!data.check(sender)) { return; }

    SpreadSheetArmor spreadSheetArmor = new SpreadSheetArmor(data);
    ItemManager.registItem(spreadSheetArmor);
  }

}
