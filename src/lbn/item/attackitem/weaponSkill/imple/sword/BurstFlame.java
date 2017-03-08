package lbn.item.attackitem.weaponSkill.imple.sword;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import lbn.common.event.player.PlayerCombatEntityEvent;
import lbn.item.attackitem.AbstractAttackItem;
import lbn.item.attackitem.weaponSkill.imple.WeaponSkillWithCombat;
import lbn.player.ItemType;
import lbn.util.particle.ParticleType;
import lbn.util.particle.Particles;

public class BurstFlame extends WeaponSkillWithCombat{

	public BurstFlame() {
		super(ItemType.SWORD);
	}

	@Override
	public String getId() {
		return "skill11";
	}

	@Override
	protected void runWaitParticleData(Location loc, int i) {
	}

	@Override
	public void onCombat(Player p, ItemStack item, AbstractAttackItem customItem, LivingEntity livingEntity, PlayerCombatEntityEvent event) {
		//スキル発動待機中ならスキルを発動する
		if (isWaitingSkill(p)) {
			livingEntity.setFireTicks((int) (20 * getData(1)));
			Particles.runParticle(livingEntity.getEyeLocation(), ParticleType.flame, 10);
		}
	}

	@Override
	public double getTimeLimit() {
		return getData(0);
	}

	@Override
	protected void onCombat2(Player p, ItemStack item, AbstractAttackItem customItem, LivingEntity livingEntity, PlayerCombatEntityEvent e) {
	}

}
