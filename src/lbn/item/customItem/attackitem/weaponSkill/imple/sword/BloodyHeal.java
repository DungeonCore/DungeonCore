package lbn.item.customItem.attackitem.weaponSkill.imple.sword;

import lbn.common.event.player.PlayerCombatEntityEvent;
import lbn.item.customItem.attackitem.AbstractAttackItem;
import lbn.item.customItem.attackitem.weaponSkill.imple.WeaponSkillWithCombat;
import lbn.player.ItemType;
import lbn.util.LivingEntityUtil;
import lbn.util.particle.ParticleType;
import lbn.util.particle.Particles;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BloodyHeal extends WeaponSkillWithCombat{

	public BloodyHeal() {
		super(ItemType.SWORD);
	}

	@Override
	public String getId() {
		return "skill2";
	}

	@Override
	protected void onCombat2(Player p, ItemStack item, AbstractAttackItem customItem, LivingEntity livingEntity, PlayerCombatEntityEvent e) {
		//パーティクルを発生させる
		Particles.runParticle(p.getLocation(), ParticleType.lava);
		//ダメージを1.2倍
		e.setDamage(e.getDamage() * getData(1));
		//体力回復
		LivingEntityUtil.addHealth(p, getData(2)*2);
	}

	@Override
	protected void runWaitParticleData(Location loc, int count) {
		Particles.runParticle(loc, ParticleType.reddust);
	}
}
