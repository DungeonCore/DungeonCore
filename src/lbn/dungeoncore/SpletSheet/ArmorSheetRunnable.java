package lbn.dungeoncore.SpletSheet;

import lbn.item.ItemManager;
import lbn.item.SpreadSheetItem.SpreadSheetArmor;
import lbn.item.armoritem.SpreadSheetArmorData;

import org.bukkit.command.CommandSender;

public class ArmorSheetRunnable extends AbstractSheetRunable{

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
		return new String[]{"id", "name", "detail", "materal", "armorPoint", "armorPointBoss", "dummy1", "durability", "uselevel", "price", "maincraftmaterial", //10
				"craftmatrial1", "craftcount1", "craftmatrial2", "craftcount2", "craftmatrial3", "craftcount3"};
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
		data.setMainCraftMaterial(row[10]);
		data.setCraftItem(row[11], row[12]);
		data.setCraftItem(row[13], row[14]);
		data.setCraftItem(row[15], row[16]);

		if (!data.check(sender)) {
			return;
		}

		SpreadSheetArmor spreadSheetArmor = new SpreadSheetArmor(data);
		ItemManager.registItem(spreadSheetArmor);
	}

}
