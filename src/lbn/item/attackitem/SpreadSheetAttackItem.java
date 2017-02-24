package lbn.item.attackitem;

import java.util.Map;

import lbn.common.event.ChangeStrengthLevelItemEvent;
import lbn.common.event.player.PlayerCombatEntityEvent;
import lbn.common.event.player.PlayerSetStrengthItemResultEvent;
import lbn.common.event.player.PlayerStrengthFinishEvent;
import lbn.dungeon.contents.strength_template.StrengthTemplate;
import lbn.item.ItemInterface;
import lbn.item.itemInterface.StrengthChangeItemable;
import lbn.item.strength.StrengthOperator;
import lbn.player.ItemType;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;


public abstract class SpreadSheetAttackItem extends AbstractAttackItem implements StrengthChangeItemable{
	SpreadSheetWeaponData data;
	public SpreadSheetAttackItem(SpreadSheetWeaponData data) {
		this.data = data;
	}

	static WeaponStrengthTemplate strengthTemplate = new WeaponStrengthTemplate();
	@Override
	public StrengthTemplate getStrengthTemplate() {
		return strengthTemplate;
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

	@Override
	public void onSetStrengthItemResult(PlayerSetStrengthItemResultEvent event) {
	}

	@Override
	public void onChangeStrengthLevelItemEvent(ChangeStrengthLevelItemEvent event) {
	}

	@Override
	public void onPlayerStrengthFinishEvent(PlayerStrengthFinishEvent event) {
		if (!event.isSuccess()) {
			int level = event.getLevel();
			//+6までの強化の時は失敗しても+0にしないで元にもどす
			if (level <= 6) {
				ItemStack item = event.getItem();
				StrengthOperator.updateLore(item, Math.max(0, level - 1));
			}
		}
	}
}
