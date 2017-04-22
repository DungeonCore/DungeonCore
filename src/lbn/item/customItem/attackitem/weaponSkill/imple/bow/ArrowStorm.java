package lbn.item.customItem.attackitem.weaponSkill.imple.bow;

import lbn.common.projectile.ProjectileInterface;
import lbn.common.projectile.ProjectileManager;
import lbn.item.customItem.attackitem.AbstractAttackItem;
import lbn.item.customItem.attackitem.weaponSkill.imple.WeaponSkillForOneType;
import lbn.player.ItemType;
import lbn.util.LbnRunnable;
import lbn.util.LivingEntityUtil;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;

//{0}秒間、矢を連射する						当たった敵に{1}秒間のスタン
public class ArrowStorm extends WeaponSkillForOneType implements ProjectileInterface {

	public ArrowStorm() {
		super(ItemType.BOW);
	}

	@Override
	public String getId() {
		return "skill8";
	}

	@Override
	public boolean onClick(Player p, ItemStack item, AbstractAttackItem customItem) {
		new SkillRunnable(this, p, item).runTaskTimer((long) (20 * 0.2));
		return true;
	}

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
			long ageTick = getAgeTick();
			// {0}秒たったら終わりにする
			if (ageTick > getData(0) * 20) {
				cancel();
				return;
			}

			Arrow launchProjectile = p.launchProjectile(Arrow.class, p.getLocation().getDirection().multiply(2));
			ProjectileManager.onLaunchProjectile(launchProjectile, projectileInterface, item);
		}

	}

	@Override
	public void onProjectileLaunchEvent(ProjectileLaunchEvent e, ItemStack item) {
	}

	@Override
	public void onProjectileHit(ProjectileHitEvent event, ItemStack item) {
	}

	@Override
	public void onProjectileDamage(EntityDamageByEntityEvent e, ItemStack item, LivingEntity owner,
			LivingEntity target) {
		// 無敵時間を０にする
		LivingEntityUtil.setNoDamageTickZero(target);
	}
}
