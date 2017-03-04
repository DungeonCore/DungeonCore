package lbn.dungeoncore.SpletSheet;

import lbn.item.attackitem.weaponSkill.WeaponSkillData;
import lbn.item.attackitem.weaponSkill.WeaponSkillFactory;
import lbn.item.attackitem.weaponSkill.WeaponSkillInterface;
import lbn.item.attackitem.weaponSkill.imple.WeaponSkillForOneType;
import lbn.player.ItemType;
import lbn.util.JavaUtil;

import org.bukkit.command.CommandSender;

public class WeaponSkillSpletRunnable extends AbstractSheetRunable{

	public WeaponSkillSpletRunnable(CommandSender sender) {
		super(sender);
	}

	@Override
	protected String getQuery() {
		return null;
	}

	@Override
	public String getSheetName() {
		return "weaponsheet";
	}

	@Override
	public String[] getTag() {
		return new String[]{"type", "name", "level", "cooltime", "mp", "detail", "data0", "data1", "data2", "data3", "data4", "id"};
	}

	@Override
	protected void excuteOnerow(String[] row) {
		String name = row[1];

		ItemType itemType = getItemType(row[0]);
		if (itemType == null) {
			sendMessage(itemType + "が不正です(name:" + name + ")" );
			return;
		}


		int level = JavaUtil.getInt(row[2], -1);
		int cooltime = JavaUtil.getInt(row[3], -1);

		int needMp = JavaUtil.getInt(row[4], -1);

		double data0 = JavaUtil.getDouble(row[6], 0);
		double data1 = JavaUtil.getDouble(row[7], 0);
		double data2 = JavaUtil.getDouble(row[8], 0);
		double data3 = JavaUtil.getDouble(row[9], 0);
		double data4 = JavaUtil.getDouble(row[10], 0);

		String detail = row[5];
		detail = detail.replace("{0}", row[6]).
				replace("{1}", row[7]).
				replace("{2}", row[8]).
				replace("{3}", row[9]).
				replace("{4}", row[10]);

		if (level == -1 || cooltime == -1 || needMp == -1) {
			sendMessage("level, cooltime, needMpが不正です(name:" + name + ")" );
			return;
		}

		WeaponSkillData weaponSkillData = new WeaponSkillData(name, itemType, row[11]);
		weaponSkillData.setCooltime(cooltime);
		weaponSkillData.setSkillLevel(cooltime);
		weaponSkillData.setData(data0, 0);
		weaponSkillData.setData(data1, 1);
		weaponSkillData.setData(data2, 2);
		weaponSkillData.setData(data3, 3);
		weaponSkillData.setData(data4, 4);

		WeaponSkillInterface weaponSkill = WeaponSkillFactory.getWeaponSkill(weaponSkillData.getId());
		if (weaponSkill instanceof WeaponSkillForOneType) {
			((WeaponSkillForOneType) weaponSkill).setData(weaponSkillData);
		}
	}

	/**
	 * アイテムの種類を説明
	 * @param name
	 * @return
	 */
	ItemType getItemType(String name) {
		switch (name) {
		case "剣":
			return ItemType.SWORD;
		case "弓":
			return ItemType.BOW;
		case "魔法":
			return ItemType.MAGIC;
		default:
			break;
		}
		return null;
	}
}
