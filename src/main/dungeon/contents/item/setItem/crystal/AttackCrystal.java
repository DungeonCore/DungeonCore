package main.dungeon.contents.item.setItem.crystal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import main.dungeon.contents.strength_template.StrengthTemplate;
import main.item.setItem.AbstractCommonSetItem;
import main.item.setItem.SetItemInterface;
import main.item.setItem.SetItemParts;
import main.item.setItem.SetItemPartsType;
import main.item.setItem.SetStrengthableItemParts;
import main.item.strength.StrengthOperator;
import main.player.appendix.PlayerAppendixManager;
import main.player.appendix.appendixObject.SetItemAppendix;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AttackCrystal extends AbstractCommonSetItem{
	public AttackCrystal() {
	}

	@Override
	public void startJob(Player p, ItemStack ...items) {
		SetItemAppendix appendix =  new SetItemAppendix(SetItemPartsType.SLOT2);

		ItemStack item = items[0];
		//レベルを取得する
		int level = StrengthOperator.getLevel(item);
		//レベルに応じて攻撃力を上昇させる
		appendix.setAttack(((AttackCrystalItem)getAllItemParts().get(0)).getAttackVal(level));

		PlayerAppendixManager.addAppendix(p, appendix);
	}

	@Override
	public void endJob(Player p) {
		PlayerAppendixManager.removeAppendix(p, new SetItemAppendix(SetItemPartsType.SLOT2));
	}

	@Override
	public List<String> getLore() {
		return Arrays.asList( "インベントリの右上に置くことで", "攻撃力上昇。");
	}

	@Override
	public String getName() {
		return "ATTACK CRYSTAL";
	}

	@Override
	protected List<SetItemParts> getAllItemParts() {
		ArrayList<SetItemParts> list = new ArrayList<SetItemParts>();
		list.add(new AttackCrystalItem(this));
		return list;
	}

	@Override
	public void doRutine(Player p, ItemStack[] itemStacks) {
	}

}

class AttackCrystalItem extends SetStrengthableItemParts implements StrengthTemplate{
	public AttackCrystalItem(SetItemInterface belongSetItem) {
		super(belongSetItem);
	}

	@Override
	public StrengthTemplate getStrengthTemplate() {
		return this;
	}

	@Override
	public boolean isDispList() {
		return false;
	}

	@Override
	public String[] getStrengthDetail(int level) {
		return new String[]{"攻撃力+" + getAttackVal(level)};
	}

	protected double getAttackVal(int level) {
		if (level > getMaxStrengthCount() || level < 0) {
			level = 0;
		}
		return level * 0.4 ;
	}

	@Override
	public Material getMaterial() {
		return Material.EMERALD;
	}

	@Override
	public SetItemPartsType getItemSetPartsType() {
		return SetItemPartsType.SLOT2;
	}

	@Override
	public int successChance(int level) {
		switch (level) {
		case 0:
			return 100;
		case 1:
			return 100;
		case 2:
			return 98;
		case 3:
			return 93;
		case 4:
			return 85;
		case 5:
			return 78;
		case 6:
			return 65;
		case 7:
			return 52;
		case 8:
			return 45;
		case 9:
			return 30;
		case 10:
			return 10;
		default:
			return 100;
		}
	}

	@Override
	public int getMaxStrengthCount() {
		return 10;
	}

	@Override
	public int getStrengthGalions(int level) {
		return 100 + 50 * level;
	}

	@Override
	public ItemStack[] getStrengthMaterials(int level) {
		return new ItemStack[0];
	}

}
