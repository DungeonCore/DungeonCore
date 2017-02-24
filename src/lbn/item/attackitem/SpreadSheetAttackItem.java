package lbn.item.attackitem;

import java.util.Map;

import lbn.common.event.player.PlayerCombatEntityEvent;
import lbn.dungeon.contents.strength_template.StrengthTemplate;
import lbn.item.ItemInterface;
import lbn.player.ItemType;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;


public abstract class SpreadSheetAttackItem extends AbstractAttackItem{
	SpreadSheetWeaponData data;
	public SpreadSheetAttackItem(SpreadSheetWeaponData data) {
		this.data = data;
	}

	@Override
	public StrengthTemplate getStrengthTemplate() {
		return null;
	}

	@Override
	public String getItemName() {
		return data.getName();
	}

	@Override
	public String getId() {
		return data.getId();
	}

	@Override
	public int getBuyPrice(ItemStack item) {
		return Math.max(getCraftLevel() * 100, 10);
	}

	@Override
	public void onCombatEntity(PlayerCombatEntityEvent e) {

	}

	@Override
	public short getMaxDurability(ItemStack e) {
		if (data.getMaxDurability() != -1) {
			return data.getMaxDurability();
		} else {
			return getMaterial().getMaxDurability();
		}
	}

	@Override
	public int getAvailableLevel() {
		return data.getAvailableLevel();
	}

	@Override
	protected int getSkillLevel() {
		return data.getSkillLevel();
	}

	@Override
	public int getCraftLevel() {
		return data.getRank();
	}

	@Override
	protected Material getMaterial() {
		return data.getItemStack().getType();
	}

	@Override
	public String[] getDetail() {
		return data.getDetail();
	}

	@Override
	public int getMaxSlotCount() {
		return data.getMaxSlot();
	}

	@Override
	public int getDefaultSlotCount() {
		return data.getDefaultSlot();
	}

	@Override
	abstract public ItemType getAttackType();

	@Override
	public int getMaxStrengthCount() {
		return 10;
	}

	@Override
	public double getAttackItemDamage(int strengthLevel) {
		return super.getAttackItemDamage(strengthLevel) * data.getDamageParcent();
	}

	@Override
	protected ItemStack getItemStackBase() {
		return data.getItemStack();
	}

	/**
	 * クラフトに使うアイテムをセットする
	 * @return
	 */
	public Map<ItemInterface, Integer> getCraftItemMap() {
		return data.getCraftItem();
	}
}
