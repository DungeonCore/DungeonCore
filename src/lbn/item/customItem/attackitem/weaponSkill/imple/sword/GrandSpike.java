package lbn.item.customItem.attackitem.weaponSkill.imple.sword;

import java.util.List;

import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import lbn.common.other.Stun;
import lbn.item.customItem.attackitem.AbstractAttackItem;
import lbn.item.customItem.attackitem.weaponSkill.imple.WeaponSkillForOneType;
import lbn.player.ItemType;
import lbn.util.LivingEntityUtil;
import lbn.util.particle.ParticleType;
import lbn.util.particle.Particles;

public class GrandSpike extends WeaponSkillForOneType{

	public GrandSpike() {
		super(ItemType.SWORD);
	}

	@Override
	public String getId() {
		return "skill12";
	}

	@Override
	public boolean onClick(Player p, ItemStack item, AbstractAttackItem customItem) {
		List<Entity> nearbyEntities = p.getNearbyEntities(getData(0), 3, getData(0));
		for (Entity entity : nearbyEntities) {
			if (LivingEntityUtil.isEnemy(entity)) {
				entity.setVelocity(new Vector(0, getData(2), 0));
				Stun.addStun((LivingEntity)entity, (int) (getData(1) * 20));
				Particles.runParticle(p.getLocation(), ParticleType.splash, 20);
			}
		}
		Particles.runCircleParticle(p, ParticleType.smoke, 5, 5);

		p.getWorld().playSound(p.getLocation(), Sound.ENDERDRAGON_WINGS, 1, 1);

		return true;
	}
}
