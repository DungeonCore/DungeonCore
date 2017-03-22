package lbn.item.customItem.attackitem.weaponSkill.imple.bow;

import lbn.common.particle.ParticleData;
import lbn.common.particle.ParticleType;
import lbn.common.projectile.ProjectileInterface;
import lbn.common.projectile.ProjectileManager;
import lbn.dungeoncore.Main;
import lbn.item.customItem.attackitem.AbstractAttackItem;
import lbn.item.customItem.attackitem.weaponSkill.imple.WeaponSkillForOneType;
import lbn.player.ItemType;
import lbn.util.LbnRunnable;

import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

public class Finale extends WeaponSkillForOneType implements ProjectileInterface{

	public Finale() {
		super(ItemType.BOW);
	}

	@Override
	public void onProjectileLaunchEvent(ProjectileLaunchEvent e, ItemStack item) {

	}

	static ParticleData particleData = new ParticleData(ParticleType.reddust, 20);
	static {
		particleData.setDispersion(1, 1, 1);
		particleData.setLastArgument(2);
	}

	@Override
	public void onProjectileDamage(EntityDamageByEntityEvent e, ItemStack item, LivingEntity owner, LivingEntity target) {
		if (e.getDamager().hasMetadata(THELOW_WEAPON_SKILL13)) {
			e.setDamage(e.getDamage() * getData(1));
			particleData.run(target.getLocation());
		}
	}

	@Override
	public String getId() {
		return "skill13";
	}

	@Override
	public void onProjectileHit(ProjectileHitEvent event, ItemStack item) {

	}

	@Override
	public boolean onClick(Player p, ItemStack item, AbstractAttackItem customItem) {
		new SkillRunnable(this, p, item).runTaskTimer((long) (20 * 0.2));
		return true;
	}

	private static final String THELOW_WEAPON_SKILL13 = "thelow_weapon_skill13";

	class SkillRunnable extends LbnRunnable {
		ProjectileInterface projectileInterface;
		Player p;
		ItemStack item;
		public SkillRunnable(ProjectileInterface projectileInterface, Player p, ItemStack item) {
			this.projectileInterface = projectileInterface;
			this.p = p;
			this.item = item;
		}

		@Override
		public void run2() {
			//{0}発打ったら終わりにする
			if (getRunCount() >= getData(0)) {
				cancel();
				return;
			}

			Arrow launchProjectile = p.launchProjectile(Arrow.class, p.getLocation().getDirection().multiply(2));

			if (getRunCount() == 0) {
				launchProjectile.setMetadata(THELOW_WEAPON_SKILL13, new FixedMetadataValue(Main.plugin, "1"));
				p.getWorld().playSound(p.getLocation(), Sound.FIREWORK_LARGE_BLAST, 0.8f, 0.8f);
			}

			ProjectileManager.launchProjectile(launchProjectile, projectileInterface, item);
		}

	}

}
