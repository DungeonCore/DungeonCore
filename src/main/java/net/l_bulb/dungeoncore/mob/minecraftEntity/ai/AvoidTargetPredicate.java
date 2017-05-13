package net.l_bulb.dungeoncore.mob.minecraftEntity.ai;

import org.bukkit.craftbukkit.v1_8_R1.entity.CraftEntity;

import net.l_bulb.dungeoncore.util.LivingEntityUtil;

import net.minecraft.server.v1_8_R1.Entity;

import com.google.common.base.Predicate;

public class AvoidTargetPredicate implements Predicate<Entity> {

  boolean isSummonMob;

  public AvoidTargetPredicate(boolean isSummonMob) {
    this.isSummonMob = isSummonMob;
  }

  @Override
  public boolean apply(Entity entity) {
    CraftEntity bukkitEntity = entity.getBukkitEntity();

    if (isSummonMob) {
      // summon mobの時は敵ならTRUE
      return LivingEntityUtil.isEnemy(bukkitEntity);
    } else {
      // summon mobで無い時は友好モブならTRUE
      return LivingEntityUtil.isFriendship(bukkitEntity);
    }
  }

}
