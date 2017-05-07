package lbn.mob.minecraftEntity;

import net.minecraft.server.v1_8_R1.DamageSource;
import net.minecraft.server.v1_8_R1.Entity;
import net.minecraft.server.v1_8_R1.EntityHuman;
import net.minecraft.server.v1_8_R1.EntityPig;
import net.minecraft.server.v1_8_R1.GenericAttributes;
import net.minecraft.server.v1_8_R1.Items;
import net.minecraft.server.v1_8_R1.PathfinderGoalBreed;
import net.minecraft.server.v1_8_R1.PathfinderGoalFloat;
import net.minecraft.server.v1_8_R1.PathfinderGoalFollowParent;
import net.minecraft.server.v1_8_R1.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_8_R1.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_8_R1.PathfinderGoalPanic;
import net.minecraft.server.v1_8_R1.PathfinderGoalPassengerCarrotStick;
import net.minecraft.server.v1_8_R1.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_8_R1.PathfinderGoalRandomStroll;
import net.minecraft.server.v1_8_R1.PathfinderGoalTempt;
import net.minecraft.server.v1_8_R1.World;
import net.minecraft.server.v1_8_R1.WorldServer;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R1.CraftWorld;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Pig;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import lbn.mob.AIType;
import lbn.mob.customMob.LbnMobTag;
import lbn.mob.minecraftEntity.ai.PathfinderGoalNearestAttackableTargetNotTargetSub;
import lbn.util.JavaUtil;

public class CustomPig extends EntityPig implements ICustomEntity<Pig> {

  private LbnMobTag tag;

  private PathfinderGoalPassengerCarrotStick bk;

  public CustomPig(World world) {
    this(world, new LbnMobTag(EntityType.PIG));
  }

  public CustomPig(World world, LbnMobTag tag) {
    super(world);
    this.tag = tag;

    // 全てのAIを取り除く
    try {
      AttackAISetter.removeAllAi(this);
      this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, true));

      // ターゲットAIを設定
      if (tag.isSummonMob()) {
        PathfinderGoalNearestAttackableTargetNotTargetSub pathfinderGoalNearestAttackableTargetNotTargetSub = new PathfinderGoalNearestAttackableTargetNotTargetSub(
            this);
        pathfinderGoalNearestAttackableTargetNotTargetSub.setSummon(tag.isSummonMob());
        this.targetSelector.a(2, pathfinderGoalNearestAttackableTargetNotTargetSub);
      }

      this.goalSelector.a(0, new PathfinderGoalFloat(this));

      if (tag.getAiType() == AIType.NO_ATACK) {
        this.goalSelector.a(1, new PathfinderGoalPanic(this, 1.25D));
        this.goalSelector.a(3, new PathfinderGoalBreed(this, 1.0D));
        this.goalSelector.a(4, new PathfinderGoalTempt(this, 1.2D, Items.CARROT_ON_A_STICK, false));
        this.goalSelector.a(4, new PathfinderGoalTempt(this, 1.2D, Items.CARROT, false));
        this.goalSelector.a(5, new PathfinderGoalFollowParent(this, 1.1D));
      }

      // 戦闘AIをセットする
      AttackAISetter.setAttackAI(this, tag);
      this.goalSelector.a(7, this.bk = new PathfinderGoalPassengerCarrotStick(this, 0.3F));
      this.goalSelector.a(11, new PathfinderGoalRandomStroll(this, 1.0D));
      this.goalSelector.a(12, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 6.0F));
      this.goalSelector.a(13, new PathfinderGoalRandomLookaround(this));

      getAttributeMap().b(GenericAttributes.e);
      getAttributeInstance(GenericAttributes.e).setValue(2.0D);
    } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
      e.printStackTrace();
    }
  }

  @Override
  public PathfinderGoalPassengerCarrotStick ck() {
    return bk;
  }

  @Override
  public Pig spawn(Location loc) {
    WorldServer world = ((CraftWorld) loc.getWorld()).getHandle();
    // 位置を指定
    setPositionRotation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
    // ワールドにentityを追加
    world.addEntity(this, SpawnReason.CUSTOM);
    return (Pig) getBukkitEntity();
  }

  @Override
  public void setPosition(double d0, double d1, double d2) {
    super.setPosition(d0, d1, d2);
    spawnLocation = new Location(world.getWorld(), d0, d1, d2);
  }

  Location spawnLocation = null;

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

  @Override
  public boolean r(Entity entity) {
    boolean flag = entity.damageEntity(DamageSource.mobAttack(this), (int) getAttributeInstance(GenericAttributes.e).getValue());
    if (flag) {
      a(this, entity);
    }
    return flag;
  }

}
