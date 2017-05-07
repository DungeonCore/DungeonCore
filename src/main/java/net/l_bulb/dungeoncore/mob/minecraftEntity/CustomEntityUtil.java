package net.l_bulb.dungeoncore.mob.minecraftEntity;

import org.bukkit.craftbukkit.v1_8_R1.entity.CraftLivingEntity;

import net.minecraft.server.v1_8_R1.EntityLiving;

public class CustomEntityUtil {
  public static EntityLiving getEntity(org.bukkit.entity.LivingEntity entity) {
    return ((CraftLivingEntity) entity).getHandle();
  }

}
