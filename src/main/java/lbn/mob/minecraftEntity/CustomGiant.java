package lbn.mob.minecraftEntity;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R1.CraftWorld;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Giant;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import lbn.mob.customMob.LbnMobTag;
import lbn.mob.minecraftEntity.ai.PathfinderGoalNearestAttackableTargetNotTargetSub;
import lbn.util.JavaUtil;
import net.minecraft.server.v1_8_R1.EntityGiantZombie;
import net.minecraft.server.v1_8_R1.GenericAttributes;
import net.minecraft.server.v1_8_R1.NBTTagCompound;
import net.minecraft.server.v1_8_R1.PathfinderGoalFloat;
import net.minecraft.server.v1_8_R1.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_8_R1.PathfinderGoalMoveTowardsRestriction;
import net.minecraft.server.v1_8_R1.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_8_R1.PathfinderGoalRandomStroll;
import net.minecraft.server.v1_8_R1.World;
import net.minecraft.server.v1_8_R1.WorldServer;

public class CustomGiant extends EntityGiantZombie implements ICustomUndeadEntity<Giant> {

  private LbnMobTag tag;

  public CustomGiant(World world) {
    this(world, new LbnMobTag(EntityType.GIANT));
  }

  public CustomGiant(World world, LbnMobTag tag) {
    super(world);
    this.tag = tag;
    try {
      AttackAISetter.removeAllAi(this);
      // ターゲットAIを設定
      this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, true));
      PathfinderGoalNearestAttackableTargetNotTargetSub pathfinderGoalNearestAttackableTargetNotTargetSub = new PathfinderGoalNearestAttackableTargetNotTargetSub(
          this);
      pathfinderGoalNearestAttackableTargetNotTargetSub.setSummon(tag.isSummonMob());
      this.targetSelector.a(2, pathfinderGoalNearestAttackableTargetNotTargetSub);

      this.goalSelector.a(0, new PathfinderGoalFloat(this));
      // 戦闘AIをセットする
      AttackAISetter.setAttackAI(this, tag);

      this.goalSelector.a(7, new PathfinderGoalMoveTowardsRestriction(this, 1.0D));
      // this.goalSelector.a(8, new PathfinderGoalMoveThroughVillage(this, 1.0D, false));
      this.goalSelector.a(9, new PathfinderGoalRandomStroll(this, 1.0D));
      this.goalSelector.a(10, new PathfinderGoalRandomLookaround(this));
    } catch (Exception exc) {
      exc.printStackTrace();
    }
  }

  @Override
  public float getHeadHeight() {
    return 14.440001F;
  }

  @Override
  protected void aW() {
    super.aW();
    this.getAttributeInstance(GenericAttributes.b).setValue(35D);
    this.getAttributeInstance(GenericAttributes.maxHealth).setValue(100.0D);
    this.getAttributeInstance(GenericAttributes.d).setValue(0.5D);
    this.getAttributeInstance(GenericAttributes.e).setValue(50.0D);
  }

  protected void n() {}
  // public CustomGiant(World arg0) {
  // super(arg0);
  // ((Navigation) getNavigation()).b(true);
  // this.goalSelector.a(0, new PathfinderGoalFloat(this));
  // this.goalSelector.a(2, new PathfinderGoalMeleeAttack(this,
  // EntityHuman.class, 1.0D, false));
  // this.goalSelector.a(2, this.a);
  // this.goalSelector.a(5, new PathfinderGoalMoveTowardsRestriction(this,
  // 1.0D));
  // this.goalSelector.a(7, new PathfinderGoalRandomStroll(this, 1.0D));
  // this.goalSelector.a(8, new PathfinderGoalLookAtPlayer(this,
  // EntityHuman.class, 8.0F));
  // this.goalSelector.a(8, new PathfinderGoalRandomLookaround(this));
  // n();
  // }
  //
  // protected void n() {
  //// if (this.world.spigotConfig.zombieAggressiveTowardsVillager) {
  //// this.goalSelector.a(4, new PathfinderGoalMeleeAttack(this,
  //// EntityVillager.class, 1.0D, true));
  //// }
  //// this.goalSelector.a(4, new PathfinderGoalMeleeAttack(this,
  //// EntityIronGolem.class, 1.0D, true));
  //// this.goalSelector.a(6, new PathfinderGoalMoveThroughVillage(this, 1.0D,
  //// false));
  // this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, true,
  // new Class[] { EntityPigZombie.class }));
  // this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(
  // this, EntityHuman.class, true));
  //// if (this.world.spigotConfig.zombieAggressiveTowardsVillager) {
  //// this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(
  //// this, EntityVillager.class, false));
  //// }
  //// this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(
  //// this, EntityIronGolem.class, true));
  // }

  @Override
  public Giant spawn(Location loc) {
    WorldServer world = ((CraftWorld) loc.getWorld()).getHandle();
    // 位置を指定
    setPositionRotation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
    // ワールドにentityを追加
    world.addEntity(this, SpawnReason.CUSTOM);
    return (Giant) getBukkitEntity();
  }

  @Override
  public void setPosition(double d0, double d1, double d2) {
    super.setPosition(d0, d1, d2);
    spawnLocation = new Location(world.getWorld(), d0, d1, d2);
  }

  Location spawnLocation = null;

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
  public void setUndead(boolean isUndead) {

  }

  @Override
  public boolean isUndead() {
    return false;
  }

  @Override
  public void setNonDayFire(boolean isNonDayFire) {

  }

  @Override
  public boolean isNonDayFire() {
    return false;
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

  @Override
  public LbnMobTag getMobTag() {
    return tag;
  }
}
