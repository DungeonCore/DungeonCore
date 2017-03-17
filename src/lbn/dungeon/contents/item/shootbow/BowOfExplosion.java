package lbn.dungeon.contents.item.shootbow;

import java.util.List;

import lbn.dungeon.contents.strength_template.StrengthTemplate;
import lbn.dungeoncore.Main;
import lbn.item.attackitem.WeaponStrengthTemplate;
import lbn.item.attackitem.old.BowItemOld;
import lbn.item.itemInterface.Strengthenable;
import lbn.item.strength.old.StrengthOperator;
import lbn.mob.LastDamageMethodType;
import lbn.util.LivingEntityUtil;
import lbn.util.explosion.NoPlayerDamageExplotionForAttackType;
import lbn.util.explosion.NotMonsterDamageExplosion;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.projectiles.ProjectileSource;

public class BowOfExplosion extends BowItemOld implements Strengthenable{
	@Override
	public String getItemName() {
		return "BOW OF EXPLOTSION";
	}

	@Override
	public String getId() {
		return "bow of explotsion";
	}

	@Override
	public void excuteOnShootBow2(EntityShootBowEvent e) {
		Entity entity = e.getProjectile();
		entity.setMetadata("bow_date_lbn_doungeon_bow_of_explosion_can_explosion", new FixedMetadataValue(Main.plugin, "1"));

	}

	@Override
	public void onProjectileHit(ProjectileHitEvent e, ItemStack bow) {
		Projectile entity = e.getEntity();
		ProjectileSource shooter = entity.getShooter();

		if (!(shooter instanceof LivingEntity)) {
			return;
		}

		List<MetadataValue> metadataBow = entity.getMetadata("bow_date_lbn_doungeon_bow_of_explosion_can_explosion");
		//着地したものに弓の情報がある確認する
		if (metadataBow.size() == 0) {
			//一回爆発したので爆発させない
			return;
		} else {
			entity.removeMetadata("bow_date_lbn_doungeon_bow_of_explosion_can_explosion", Main.plugin);
		}

		int level = StrengthOperator.getLevel(bow);

		if (LivingEntityUtil.isFriendship((LivingEntity) shooter)) {
			new NoPlayerDamageExplotionForAttackType(entity.getLocation(), getExplosionSize(level), (LivingEntity) shooter, LastDamageMethodType.BOW).runExplosion();
		} else {
			new NotMonsterDamageExplosion(entity.getLocation(), getExplosionSize(level)).runExplosion();
		}
	}

	@Override
	public String[] getDetail() {
		return new String[]{"着弾地点が爆発します"};
	}

	static StrengthTemplate template = new WeaponStrengthTemplate();

	@Override
	public StrengthTemplate getStrengthTemplate() {
		return template;
	}


	protected int getExplosionSize(int level) {
		switch (level) {
		case 0:
			return 1;
		case 1:
			return 1;
		case 2:
			return 1;
		case 3:
			return 1;
		case 4:
			return 2;
		case 5:
			return 2;
		case 6:
			return 2;
		case 7:
			return 2;
		case 8:
			return 2;
		case 9:
			return 2;
		case 10:
			return 3;
		case 11:
			return 4;
		case 12:
			return 3;
		default:
			return 1;
		}
	}

	@Override
	protected void excuteOnLeftClick2(PlayerInteractEvent e) {

	}

	@Override
	public int getAvailableLevel() {
		return 40;
	}

	@Override
	protected String[] getStrengthDetail2(int level) {
		return new String[]{"爆発力：" + getExplosionSize(level)};
	}

	@Override
	protected int getBaseBuyPrice() {
		return 2000;
	}

	@Override
	public void onProjectileLaunchEvent(ProjectileLaunchEvent e, ItemStack item) {
	}

}
