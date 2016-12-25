package lbn.mob;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

import lbn.dungeon.contents.mob.NormalMob;
import lbn.mob.attribute.Attribute;
import lbn.mob.mob.NullMob;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.projectiles.ProjectileSource;

public class MobHolder{
	private static final NullMob NULL_MOB = new NullMob();

	private static HashMap<String, AbstractMob<?>> mobs = new HashMap<>();

	/**
	 * CustomMobならTRUE
	 * @param e
	 * @return
	 */
	public static boolean isCustomMob(Entity e) {
		//normal mob実装のため、全てのMobがCustomMobとなるので一旦すべてTrueとする
		if (e.getType().isAlive()) {
//			if (((LivingEntity)e).getCustomName() == null) {
//				return false;
//			}
//			String name = getRealName(e);
//			return mobs.containsKey(name);
//		} else {
//			return false;
			return e.getType() != EntityType.PLAYER;
		}
		return false;
	}

	protected static String getRealName(String name) {
		if (name == null) {
			return "";
		}
		name = ChatColor.stripColor(name).toUpperCase();
		if (name.contains("[") && name.contains("]") ) {
			name = name.substring(0, name.lastIndexOf("[")).trim();
		}
		//属性タグ削除
		name = Attribute.removePrefix(name);
		return name;
	}
	protected static String getRealName(Entity e) {
		if (e == null) {
			return "";
		}
		return getRealName(((LivingEntity)e).getCustomName());
	}

	/**
	 * mobを登録する
	 * @param mob
	 */
	public static void registMob(AbstractMob<?> mob) {
		if (mob != null) {
			mobs.put(mob.getName().toUpperCase(), mob);
		}
	}

	/**
	 * CustomMobならTRUE
	 * @param e
	 * @return
	 */
	public static boolean isExtraMobByProjectile(ProjectileSource souce) {
		if (souce instanceof LivingEntity) {
			return isCustomMob((LivingEntity) souce);
		} else {
			return false;
		}
	}

	/**
	 * mobInstanceを取得
	 * @param e
	 * @return
	 */
	public static AbstractMob<?> getMob(EntityEvent e) {
		Entity entity = e.getEntity();
		if (entity.getType().isAlive()) {
			return getMob((LivingEntity) entity);
		} else {
			return new NormalMob(e.getEntityType());
		}
	}


	/**
	 * Normal mobを含めたMobを取得
	 * @param name
	 * @return
	 */
	public static AbstractMob<?> getMobWithNormal(String name) {
//		try {
//			//もしLivingEntity名と一致するならそれを返す
//			EntityType valueOf = EntityType.valueOf(name.toUpperCase());
//			if (valueOf != null && valueOf.isAlive()) {
//				return new NormalMob(valueOf);
//			}
//		} catch (Exception e) {
//		}
//
//		AbstractMob<?> mob = MobHolder.getMob(name);
//		if (mob != null && !mob.getClass().equals(NullMob.class)) {
//			return mob;
//		}
//		return null;
		if (name == null || name.isEmpty()) {
			return null;
		}

		String alias = name.replace("_", " ");
		String alias1 = name.replace(" ", "_");

		//指定されたMob名が存在したらそれを返す
		for (String mobName : Arrays.asList(alias, alias1, name)) {
			AbstractMob<?> mob = MobHolder.getMob(mobName);
			if (!mob.isNullMob()) {
				return mob;
			}
		}

		//指定されたMob名が存在したらそれを返す
		for (String mobName : Arrays.asList(alias, alias1, name)) {
			//EntityTypeが全て大文字のため大文字にする
			mobName = mobName.toUpperCase();
			try {
				EntityType valueOf = EntityType.valueOf(mobName);
				//もしLivingEntityでないなら失敗
				if (!valueOf.isAlive()) {
					continue;
				}
				//normalMobでラッピングする
				return new NormalMob(valueOf);
			} catch (Exception e) {
				continue;
			}
		}
		return null;
	}
	/**
	 * mobInstanceを取得
	 * @param e
	 * @return
	 */
	public static AbstractMob<?> getMob(String name) {
		name = getRealName(name);
		if (mobs.containsKey(name)) {
			return mobs.get(name);
		} else {
			return NULL_MOB;
		}
	}
	/**
	 * mobInstanceを取得
	 * @param e
	 * @return
	 */
	public static AbstractMob<?> getMob(Entity e) {
		if (e == null) {
			return NULL_MOB;
		}
		if (e.getCustomName() == null) {
			return NULL_MOB;
		}
		AbstractMob<?> mob = mobs.get(getRealName(e));
		if (mob == null) {
			return NULL_MOB;
		}

		if (mob.getEntityType() == e.getType()) {
			return mob;
		}
		return NULL_MOB;
	}

	public static AbstractMob<?> getMobByProjectile(ProjectileSource souce) {
		if (souce instanceof LivingEntity) {
			return getMob((LivingEntity)souce);
		}
		return NULL_MOB;
	}

	/**
	 * 全てのモブの名前を取得
	 * @return
	 */
	public static Collection<String> getAllNames() {
		return mobs.keySet();
	}

	/**
	 * 全てのモブを取得
	 * @return
	 */
	public static Collection<AbstractMob<?>> getAllMobs() {
		return mobs.values();
	}

}
