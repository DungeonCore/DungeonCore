package lbn.mob.minecraftEntity;

import java.lang.reflect.Field;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R1.CraftWorld;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Spider;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import lbn.mob.customMob.LbnMobTag;
import lbn.mob.minecraftEntity.ai.PathfinderGoalNearestAttackableTargetNotTargetSub;
import lbn.util.JavaUtil;
import net.minecraft.server.v1_8_R1.EntityHuman;
import net.minecraft.server.v1_8_R1.EntityInsentient;
import net.minecraft.server.v1_8_R1.EntityIronGolem;
import net.minecraft.server.v1_8_R1.EntityPigZombie;
import net.minecraft.server.v1_8_R1.EntityZombie;
import net.minecraft.server.v1_8_R1.NBTTagCompound;
import net.minecraft.server.v1_8_R1.PathfinderGoalFloat;
import net.minecraft.server.v1_8_R1.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_8_R1.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_8_R1.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_8_R1.PathfinderGoalMoveTowardsRestriction;
import net.minecraft.server.v1_8_R1.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_8_R1.PathfinderGoalRandomStroll;
import net.minecraft.server.v1_8_R1.PathfinderGoalSelector;
import net.minecraft.server.v1_8_R1.World;
import net.minecraft.server.v1_8_R1.WorldServer;

public class CustomPigZombie extends EntityPigZombie implements ICustomEntity<Spider> {

  private LbnMobTag tag;

  public CustomPigZombie(World world) {
    this(world, new LbnMobTag(EntityType.PIG_ZOMBIE));
  }

  public CustomPigZombie(World world, LbnMobTag tag) {
    super(world);
    this.tag = tag;
    Field field2;
    try {
      field2 = EntityInsentient.class.getDeclaredField("goalSelector");
      field2.setAccessible(true);
      field2.set(this, new PathfinderGoalSelector((world != null) && (world.methodProfiler != null) ? world.methodProfiler : null));

      this.goalSelector.a(0, new PathfinderGoalFloat(this));
      this.goalSelector.a(2, new PathfinderGoalMeleeAttack(this,
          EntityHuman.class, 0.71D, false));
      this.goalSelector.a(2, this.a);
      this.goalSelector.a(5, new PathfinderGoalMoveTowardsRestriction(this,
          1.0D));
      this.goalSelector.a(7, new PathfinderGoalRandomStroll(this, 1.0D));
      this.goalSelector.a(8, new PathfinderGoalLookAtPlayer(this,
          EntityHuman.class, 8.0F));
      this.goalSelector.a(8, new PathfinderGoalRandomLookaround(this));
    } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
      e.printStackTrace();
    }
    angerLevel = (400 + this.random.nextInt(400));
  }

  @Override
  public Spider spawn(Location loc) {
    WorldServer world = ((CraftWorld) loc.getWorld()).getHandle();
    // 位置を指定
    setPositionRotation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());

    // ワールドにentityを追加
    world.addEntity(this, SpawnReason.CUSTOM);
    return (Spider) getBukkitEntity();
  }

  @Override
  public void setPosition(double d0, double d1, double d2) {
    super.setPosition(d0, d1, d2);
    spawnLocation = new Location(world.getWorld(), d0, d1, d2);
  }

  Location spawnLocation = null;

  protected void n() {
    this.goalSelector.a(4, new PathfinderGoalMeleeAttack(this, EntityIronGolem.class, 1.0D, true));
    this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, true, new Class[] { EntityZombie.class }));
    PathfinderGoalNearestAttackableTargetNotTargetSub pathfinderGoalNearestAttackableTargetNotTargetSub = new PathfinderGoalNearestAttackableTargetNotTargetSub(
        this);
    pathfinderGoalNearestAttackableTargetNotTargetSub.setSummon(false);
    this.targetSelector.a(2, pathfinderGoalNearestAttackableTargetNotTargetSub);
  }

  // @Override
  // protected Entity findTarget() {
  // if (angerLevel == 0) {
  // angerLevel = (400 + this.random.nextInt(400));
  // }
  // return super.findTarget();
  // }

  @Override
  public void a(NBTTagCompound nbttagcompound) {
    super.a(nbttagcompound);
    isIgnoreWater = nbttagcompound.getBoolean("IsWaterMonster");
  }

  @Override
  public boolean W() {
    if (!isIgnoreWater) {
      return super.W();
    } else {
      inWater = false;
      return false;
    }
  }

  @Override
  public boolean V() {
    if (!isIgnoreWater) {
      return super.V();
    } else {
      return false;
    }
  }

  boolean isIgnoreWater = false;

  @Override
  public LbnMobTag getMobTag() {
    return tag;
  }

  int spawnCount = 0;

  @Override
  protected void D() {
    super.D();

    if (getMobTag() == null) { return; }

    // 指定した距離以上離れていたら殺す
    spawnCount++;
    if (spawnCount >= 60) {
      spawnCount = 0;
      if (spawnLocation == null) { return; }
      if (JavaUtil.getDistanceSquared(spawnLocation, locX, locY, locZ) < tag.getRemoveDistance() * tag.getRemoveDistance()) { return; }
      if (getMobTag().isBoss()) {
        getBukkitEntity().teleport(spawnLocation);
      } else {
        die();
      }
    }
  }

}
