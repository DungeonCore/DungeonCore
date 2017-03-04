package lbn.item.attackitem.weaponSkill.imple;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import lbn.common.event.player.PlayerCombatEntityEvent;
import lbn.item.attackitem.AbstractAttackItem;
import lbn.item.attackitem.weaponSkill.WeaponSkillData;
import lbn.item.attackitem.weaponSkill.WeaponSkillInterface;
import lbn.player.ItemType;

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
	public boolean onCombat(Player p, ItemStack item, AbstractAttackItem customItem, LivingEntity livingEntity, PlayerCombatEntityEvent event) {
		return false;
	}

	protected String getDataString(int i) {
		return String.valueOf(data.getData(i));
	}

	protected double getData(int i) {
		return data.getData(i);
	}
}
