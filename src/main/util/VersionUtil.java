package main.util;

import main.mob.customEntity1_7.CustomVillager;

import org.bukkit.craftbukkit.v1_8_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;

public class VersionUtil {
	public static boolean isCustomVillager(Entity entity) {
		return (((CraftEntity)entity).getHandle() instanceof CustomVillager);
	}
}
