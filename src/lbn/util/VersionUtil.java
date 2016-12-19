package lbn.util;

import org.bukkit.craftbukkit.v1_8_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;

import lbn.mob.customEntity1_7.CustomVillager;

public class VersionUtil {
	public static boolean isCustomVillager(Entity entity) {
		return (((CraftEntity)entity).getHandle() instanceof CustomVillager);
	}
}
