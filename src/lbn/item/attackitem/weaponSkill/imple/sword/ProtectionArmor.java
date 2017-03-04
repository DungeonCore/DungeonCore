package lbn.item.attackitem.weaponSkill.imple.sword;

import lbn.common.other.ItemStackData;
import lbn.item.attackitem.weaponSkill.imple.WeaponSkillForOneType;
import lbn.player.ItemType;

public class ProtectionArmor extends WeaponSkillForOneType{

	public ProtectionArmor(ItemType type) {
		super(type);
	}

	@Override
	public int getSkillLevel() {
		return 50;
	}

	@Override
	public String getName() {
		return "プロテクションアーマー";
	}

	@Override
	public String getId() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public String[] getDetail() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public int getCooltime() {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}

	@Override
	public int getNeedMagicPoint() {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}

	@Override
	public ItemStackData getViewItemStackData() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

}
