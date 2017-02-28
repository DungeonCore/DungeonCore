package lbn.item.attackitem.weaponSkill.imple.sword;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import lbn.common.other.ItemStackData;
import lbn.item.attackitem.AbstractAttackItem;
import lbn.item.attackitem.weaponSkill.imple.WeaponSkillForOneType;
import lbn.player.ItemType;

public class LightningOrder extends WeaponSkillForOneType{

	public LightningOrder() {
		super(ItemType.SWORD);
	}

	@Override
	public int getSkillLevel() {
		return 30;
	}

	@Override
	public String getName() {
		return "ライトニング・オーダー";
	}

	@Override
	public String getId() {
		return "lightningorder";
	}

	@Override
	public String[] getDetail() {
		return new String[]{"敵に雷を落とし、1.5秒間スタンさせる", "加えて移動速度を10秒低下させる"};
	}

	@Override
	public void onClick(Player p, ItemStack item, AbstractAttackItem customItem) {
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
	public ItemStackData getItemStackData() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

}
