package lbn.mob;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import lbn.util.DungeonLogger;
import lbn.util.ItemStackUtil;

import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;

/**
 * Playerが最後に攻撃したMobを管理するためのクラス
 *
 */
public class LastDamageManager {
	static HashMap<Integer, Player> entityPlayerMap = new HashMap<>();

	static HashMap<Integer, LastDamageMethodType> entityAttackTypeMap = new HashMap<>();

	static Queue<Integer> idList = new LinkedList<Integer>();

	public static void onDamage(LivingEntity e, Player p, LastDamageMethodType type) {
		if (e == null || p == null) {
			return;
		}

		if (e.getType() != EntityType.PLAYER && !SummonPlayerManager.isSummonMob(e)) {
			int id = e.getEntityId();
			addData(p, type, id);
		}
	}

	public static void addData(Player p, LastDamageMethodType type, Entity entity) {
		addData(p, type, entity.getEntityId());
	}

	public static void addData(Player p, LastDamageMethodType type, int id) {
		if (type == null) {
			return;
		}

		// 常に21000以下になるようにする
		if (idList.size() > 25000) {
			while (25000 - 5000 < idList.size()) {
				Integer remove = idList.remove();
				entityPlayerMap.remove(remove);
				entityAttackTypeMap.remove(remove);
			}
			DungeonLogger.development("Last Damage Manaer size over 25000");
		}
		idList.remove(id);
		idList.add(id);
		entityPlayerMap.put(id, p);
		entityAttackTypeMap.put(id, type);
	}

	/**
	 * Player以外のMobがダメージを与えたときのLastDamage情報を登録
	 * @param target ダメージを受けたmob
	 * @param damager ダメージを与えたmob
	 */
	public static void registLastDamageByLivingEntityWithoutPlayer(LivingEntity target, LivingEntity damager) {
		//ダメージを受けたのがPlayer, または, summonなら無視
		if (target.getType() == EntityType.PLAYER || SummonPlayerManager.isSummonMob(target)) {
			return;
		}

		//ダメージを与えたmobの召喚主を取得
		Player owner = SummonPlayerManager.getOwner(damager);
		if (owner != null) {
			//データをセット
			addData(owner, LastDamageMethodType.fromAttackType(SummonPlayerManager.getItemType(damager), true), target.getEntityId());
		}
	}

	/**
	 * Projectileがダメージを与えたときのLastDamage情報を登録
	 * @param target  ダメージを受けたmob
	 * @param projectile ダメージを与えたProjectile
	 */
	public static void registLastDamageByProjectile(LivingEntity target, Projectile projectile) {
		//ダメージを受けたのがPlayer, または, summonなら無視
		if (target.getType() == EntityType.PLAYER || SummonPlayerManager.isSummonMob(target)) {
			return;
		}

		//撃ち主を取得
		ProjectileSource shooter = projectile.getShooter();
		if (shooter == null) {
			return;
		}

		//打ったのがEntityでないなら無視
		if (!(shooter instanceof Entity)) {
			return;
		}
		Entity shooterEntity = (Entity) shooter;

		//打ったのがPlayerの時
		if (shooterEntity.getType() == EntityType.PLAYER) {
			addData((Player) shooter, LastDamageMethodType.BOW, target.getEntityId());
		//打ったのがPlayer以外の生き物の時
		} else if (shooterEntity.getType().isAlive()) {
			registLastDamageByLivingEntityWithoutPlayer(target, (LivingEntity) shooter);
		}
	}

	/**
	 * 最後に攻撃したPlayerを取得
	 * @param e
	 * @return
	 */
	public static Player getLastDamagePlayer(Entity e) {
		return entityPlayerMap.get(e.getEntityId());
	}

	/**
	 * 最後ダメージの攻撃方法を取得
	 * @param e
	 * @return
	 */
	public static LastDamageMethodType getLastDamageAttackType(Entity e) {
		LastDamageMethodType attackType = entityAttackTypeMap.get(e.getEntityId());
		if (attackType == null) {
			return LastDamageMethodType.OTHER;
		} else {
			return attackType;
		}
	}

	public static void remove(Entity e) {
		entityAttackTypeMap.remove(e.getEntityId());
		entityPlayerMap.remove(e.getEntityId());
	}

	/**
	 * EventからLastDamage情報を登録する
	 * @param e
	 */
	public static void registLastDamageByEvent(EntityDamageByEntityEvent e) {
		if (e.isCancelled()) {
			return;
		}
		// ダメージを与えたモブ
		Entity damager = e.getDamager();

		// ダメージを受けたモブが生き物でないなら無視する
		if (!(e.getEntity().getType().isAlive())) {
			return;
		}
		//ダメージを受けたMob
		LivingEntity entityDamaged = (LivingEntity) e.getEntity();

		//ダメージを与えた対象のEntityType
		EntityType type = damager.getType();

		// ダメージを与えたのがPlayerによる攻撃のとき
		if (type == EntityType.PLAYER) {
			Player p = (Player) damager;
			onPlayerDamage(p, entityDamaged);
		//ダメージを与えたのが弓の時
		} else if (type == EntityType.ARROW) {
			LastDamageManager.registLastDamageByProjectile(entityDamaged, (Arrow) damager);
		}

		// 攻撃者がSummonのとき
		if (SummonPlayerManager.isSummonMob(damager)) {
			registLastDamageByLivingEntityWithoutPlayer(entityDamaged, (LivingEntity) damager);
		}
	}

	private static void onPlayerDamage(Player p, LivingEntity entity) {
		if (p == null || entity == null) {
			return;
		}
		ItemStack itemInHand = p.getItemInHand();
		// ダメージを与えたのが剣
		if (ItemStackUtil.isSword(itemInHand)) {
			LastDamageManager.onDamage(entity, p, LastDamageMethodType.SWORD);
		} else if (itemInHand == null || itemInHand.getType() == Material.AIR) {
			LastDamageManager.onDamage(entity, p, LastDamageMethodType.BARE_HAND);
		} else {
			LastDamageManager.onDamage(entity, p, LastDamageMethodType.MELEE_ATTACK_WITH_OTHER);
		}
	}
}
