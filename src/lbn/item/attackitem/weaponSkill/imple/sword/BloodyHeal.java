package lbn.item.attackitem.weaponSkill.imple.sword;

import lbn.common.event.player.PlayerCombatEntityEvent;
import lbn.common.other.ItemStackData;
import lbn.item.attackitem.AbstractAttackItem;
import lbn.item.attackitem.weaponSkill.imple.WeaponSkillWithCombat;
import lbn.player.ItemType;
import lbn.util.LivingEntityUtil;
import lbn.util.particle.ParticleData;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BloodyHeal extends WeaponSkillWithCombat{

	public BloodyHeal() {
		super(ItemType.SWORD);
	}

	@Override
	public int getSkillLevel() {
		return 15;
	}

	@Override
	public String getName() {
		return "ブラッディ・ヒール";
	}

	@Override
	public String getId() {
		return "bloody_heal";
	}

	@Override
	public String[] getDetail() {
		return new String[]{"スキル発動後５秒以内の最初の攻撃で、", "通常攻撃を1.2倍にし、体力を回復する"};
	}

	@Override
	public int getCooltime() {
		return 120;
	}

	@Override
	public int getNeedMagicPoint() {
		return 30;
	}

	@Override
	public ItemStackData getViewItemStackData() {
		return new ItemStackData(Material.REDSTONE);
	}

	@Override
	protected void onCombat2(Player p, ItemStack item, AbstractAttackItem customItem, LivingEntity livingEntity, PlayerCombatEntityEvent e) {
		//パーティクルを発生させる
		ParticleData.runLava(p.getLocation());
		//ダメージを1.2倍
		e.setDamage(e.getDamage() * 1.2);
		//体力回復
		LivingEntityUtil.addHealth(p, 6.0);
	}
}
