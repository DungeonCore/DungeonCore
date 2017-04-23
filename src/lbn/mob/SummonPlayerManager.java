package lbn.mob;

import java.util.HashMap;

import lbn.dungeoncore.LbnRuntimeException;
import lbn.mob.customMob.SummonMobable;
import lbn.player.ItemType;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class SummonPlayerManager {
	static HashMap<LivingEntity, Player> entityOwnerMap = new HashMap<>();

	public static void addSummon(LivingEntity e, Player p) {
		if (entityOwnerMap.size() > 100000) {
			new LbnRuntimeException("summon map is over 100000").printStackTrace();;
		}
		entityOwnerMap.put(e, p);
	}

	public static void removeSummon(LivingEntity e) {
		entityOwnerMap.remove(e);
	}

	public static void allRemoveSummon() {
		for (LivingEntity e : entityOwnerMap.keySet()) {
			e.remove();
		}
	}

	public static Player getOwner(Entity damager) {
		return entityOwnerMap.get(damager);
	}

	public static boolean isSummonMob(Entity e) {
		return entityOwnerMap.containsKey(e);
	}

	public static ItemType getItemType(Entity damage) {
		AbstractMob<?> mob = MobHolder.getMob(damage);
		if (mob.isSummonMob()) {
			return ((SummonMobable)mob).getUseItemType();
		}
		return ItemType.IGNORE;
	}
}
