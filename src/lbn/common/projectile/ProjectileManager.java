package lbn.common.projectile;

import java.util.HashMap;
import java.util.List;

import lbn.dungeoncore.Main;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

public class ProjectileManager {
	private static final String THELOW_PROJECTILE_DATA_ITEMSTACK = "thelow_projectile_data_itemstack";
	private static final String THELOW_PROJECTILE_DATA_PROJECTILE = "thelow_projectile_data_projectile";

	static HashMap<String, ProjectileInterface> projectileMap = new HashMap<String, ProjectileInterface>();

	/**
	 * Projectileを登録する
	 */
	public static void regist(ProjectileInterface projectileInterface) {
		projectileMap.put(projectileInterface.getId(), projectileInterface);
	}

	/**
	 * Projectileを発射した時の処理
	 * @param projectile
	 * @param projectileInterface
	 */
	public static void launchProjectile(Projectile projectile, ProjectileInterface projectileInterface, ItemStack item) {
		projectile.setMetadata(THELOW_PROJECTILE_DATA_PROJECTILE, new FixedMetadataValue(Main.plugin, projectileInterface.getId()));
		projectile.setMetadata(THELOW_PROJECTILE_DATA_ITEMSTACK, new FixedMetadataValue(Main.plugin, item));
	}

	/**
	 * Entity(Projectile)から対応するProjectileInterfaceを取得。もし存在しない場合はnullを返す
	 * @param entity
	 */
	public static ProjectileInterface getProjectileInterface(Entity entity) {
		//メタデータを見る
		List<MetadataValue> metadata = entity.getMetadata(THELOW_PROJECTILE_DATA_PROJECTILE);
		//メタデータが無いなら何もしない
		if (metadata == null || metadata.size() == 0) {
			return null;
		}

		//IDを取得
		MetadataValue metadataValue = metadata.get(0);
		String projectileId = metadataValue.asString();

		//projectileを返す
		ProjectileInterface projectileInterface = projectileMap.get(projectileId);
		return projectileInterface;
	}

	/**
	 * Entity(Projectile)から対応するItemStackを取得。もし存在しない場合はnullを返す
	 * @param entity
	 */
	public static ItemStack getItemStack(Entity entity) {
		//メタデータを見る
		List<MetadataValue> metadata = entity.getMetadata(THELOW_PROJECTILE_DATA_ITEMSTACK);
		//メタデータが無いなら何もしない
		if (metadata == null || metadata.size() == 0) {
			return null;
		}

		//IDを取得
		MetadataValue metadataValue = metadata.get(0);
		ItemStack item = (ItemStack) metadataValue.value();

		return item;
	}

	/**
	 * ProjectileLaunchEvent時の処理
	 * @param e
	 */
	public static void onProjectileLaunchEvent(ProjectileLaunchEvent e) {
		Projectile entity = e.getEntity();

		//projectileInterfaceを取得
		ProjectileInterface projectileInterface = getProjectileInterface(entity);
		ItemStack item = getItemStack(entity);
		if (projectileInterface != null) {
			projectileInterface.onProjectileLaunchEvent(e, item);
		}

	}

	/**
	 * ProjectileHitEvent時の処理
	 * @param e
	 */
	public static void onProjectileHit(ProjectileHitEvent e) {
		Projectile entity = e.getEntity();
		//projectileInterfaceを取得
		ProjectileInterface projectileInterface = getProjectileInterface(entity);
		ItemStack item = getItemStack(entity);
		if (projectileInterface != null) {
			projectileInterface.onProjectileHit(e, item);
		}
	}
}
