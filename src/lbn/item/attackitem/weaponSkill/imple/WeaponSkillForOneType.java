package lbn.item.attackitem.weaponSkill.imple;

import lbn.common.event.player.PlayerCombatEntityEvent;
import lbn.common.other.ItemStackData;
import lbn.item.attackitem.AbstractAttackItem;
import lbn.item.attackitem.weaponSkill.WeaponSkillData;
import lbn.item.attackitem.weaponSkill.WeaponSkillInterface;
import lbn.player.ItemType;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class WeaponSkillForOneType implements WeaponSkillInterface{
	ItemType type;

	public WeaponSkillForOneType(ItemType type) {
		this.type = type;
	}

	WeaponSkillData data;

	public void setData(WeaponSkillData data) {
		this.data = data;
	}

	@Override
	public String getName() {
		return data.getName();
	}

	public ItemType getType() {
		return data.getType();
	}

	@Override
	public String[] getDetail() {
		return data.getDetail();
	}

	@Override
	public int getCooltime() {
		return data.getCooltime();
	}

	@Override
	public int getNeedMagicPoint() {
		return data.getNeedMp();
	}

	@Override
	public int getSkillLevel() {
		return data.getSkillLevel();
	}

	@Override
	public boolean canUse(ItemType type) {
		return this.type == type;
	}

	@Override
	public boolean onClick(Player p, ItemStack item, AbstractAttackItem customItem) {
		return false;
	}


	@Override
	public void onCombat(Player p, ItemStack item, AbstractAttackItem customItem, LivingEntity livingEntity, PlayerCombatEntityEvent event) {
	}

	protected String getDataString(int i) {
		return String.valueOf(data.getData(i));
	}

	protected double getData(int i) {
		return data.getData(i);
	}

	@Override
	public ItemStackData getViewItemStackData() {
		return new ItemStackData(data.getMaterial(), data.getMaterialdata());
	}
}
