package lbn.mob.customEntity1_8;

import net.minecraft.server.v1_8_R1.EntityLiving;

import org.bukkit.craftbukkit.v1_8_R1.entity.CraftLivingEntity;

public class CustomEntityUtil {
	public static EntityLiving getEntity(org.bukkit.entity.LivingEntity entity) {
		return ((CraftLivingEntity)entity).getHandle();
	}

}
