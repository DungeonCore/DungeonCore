package lbn.dungeon.contents.item.setItem.crystal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lbn.api.PlayerStatusType;
import lbn.dungeon.contents.strength_template.StrengthTemplate;
import lbn.item.ItemManager;
import lbn.item.setItem.AbstractAbilitySetItem;
import lbn.item.setItem.SetItemPartable;
import lbn.item.setItem.SetItemPartsType;
import lbn.item.setItem.SetStrengthableItemParts;
import lbn.item.strength.old.StrengthOperator;
import lbn.player.ability.impl.SetItemAbility;
import lbn.util.ItemStackUtil;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SetItemHealthCrystal extends AbstractAbilitySetItem{
	@Override
	public void doRutine(Player p, ItemStack[] itemStacks) {
	}

	@Override
	public List<String> getLore() {
		return Arrays.asList("インベントリの左上に置くことで", "最大体力上昇。");
	}

	@Override
	public String getName() {
		return "HEALTH CRYSTAL";
	}

	@Override
	protected List<SetItemPartable> getAllItemParts() {
		ArrayList<SetItemPartable> list = new ArrayList<SetItemPartable>();
		list.add(new HealthCystal());
		return list;
	}

	@Override
	protected void addAbility(SetItemAbility emptyAbility, Player p, ItemStack... item) {
		//ヒールクリスタル取得
		HealthCystal customItem = (HealthCystal) ItemManager.getCustomItemByName(ItemStackUtil.getName(item[0]));
		//効果をつける
		emptyAbility.addData(PlayerStatusType.MAX_HP, customItem.getMaxHealth(StrengthOperator.getLevel(item[0])));
	}
}

class HealthCystal extends SetStrengthableItemParts implements StrengthTemplate {
	public HealthCystal() {
		super(new SetItemHealthCrystal() );
	}

	@Override
	public StrengthTemplate getStrengthTemplate() {
		return this;
	}

	@Override
	public int getMaxStrengthCount() {
		return 10;
	}

	@Override
	public String[] getStrengthDetail(int level) {
		return new String[]{"最大体力+" + getMaxHealth(level)};
	}

	public int getMaxHealth(int level) {
		return 1 + level;
	}

	@Override
	public ItemStack getStrengthMaterials(int level) {
		return null;
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
	public Material getMaterial() {
		return Material.INK_SACK;
	}

	@SuppressWarnings("deprecation")
	@Override
	public ItemStack getItem() {
		ItemStack item = super.getItem();
		item.setDurability((short) 4);
		item.getData().setData((byte) 4);
		return item;
	}

	@Override
	public SetItemPartsType getItemSetPartsType() {
		return SetItemPartsType.SLOT1;
	}

	@Override
	public int getStrengthGalions(int level) {
		return 100 + 50 * level;
	}

	@Override
	public boolean isStrengthItem() {
		return true;
	}

}
