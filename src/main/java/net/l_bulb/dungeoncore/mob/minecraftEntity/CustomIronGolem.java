package net.l_bulb.dungeoncore.mob.minecraftEntity;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R1.CraftWorld;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.IronGolem;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import net.l_bulb.dungeoncore.mob.customMob.LbnMobTag;

import net.minecraft.server.v1_8_R1.EntityIronGolem;
import net.minecraft.server.v1_8_R1.PathfinderGoalFloat;
import net.minecraft.server.v1_8_R1.PathfinderGoalMoveTowardsRestriction;
import net.minecraft.server.v1_8_R1.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_8_R1.PathfinderGoalRandomStroll;
import net.minecraft.server.v1_8_R1.World;
import net.minecraft.server.v1_8_R1.WorldServer;

public class CustomIronGolem extends EntityIronGolem implements ICustomEntity<IronGolem> {

  public CustomIronGolem(World world) {
    this(world, new LbnMobTag(EntityType.IRON_GOLEM));
  }

  public CustomIronGolem(World world, LbnMobTag tag) {
    super(world);
    try {
      AttackAISetter.removeAllAi(this);
      // 攻撃対象のAIをセットする
      AttackAISetter.setTargetAI(this, tag);
      this.goalSelector.a(0, new PathfinderGoalFloat(this));

      // 戦闘AIをセットする
      AttackAISetter.setAttackAI(this, tag);
      this.goalSelector.a(7, new PathfinderGoalMoveTowardsRestriction(this, 1.0D));
      // this.goalSelector.a(8, new PathfinderGoalMoveThroughVillage(this, 1.0D, false));
      this.goalSelector.a(9, new PathfinderGoalRandomStroll(this, 1.0D));
      this.goalSelector.a(10, new PathfinderGoalRandomLookaround(this));

      this.tag = tag;
    } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
      e.printStackTrace();
    }

  }

  @Override
  public IronGolem spawn(Location loc) {
    WorldServer world = ((CraftWorld) loc.getWorld()).getHandle();
    // 位置を指定
    setPositionRotation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
    // ワールドにentityを追加
    world.addEntity(this, SpawnReason.CUSTOM);
    return (IronGolem) getBukkitEntity();
  }

  LbnMobTag tag = null;

  @Override
  public LbnMobTag getMobTag() {
    return tag;
  }
}
