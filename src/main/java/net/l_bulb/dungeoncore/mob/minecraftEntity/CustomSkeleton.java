package net.l_bulb.dungeoncore.mob.minecraftEntity;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R1.CraftWorld;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import net.l_bulb.dungeoncore.mob.AIType;
import net.l_bulb.dungeoncore.mob.customMob.LbnMobTag;
import net.l_bulb.dungeoncore.mob.minecraftEntity.ai.TheLowPathfinderGoalMeleeAttack;
import net.l_bulb.dungeoncore.util.JavaUtil;

import net.minecraft.server.v1_8_R1.EntityHuman;
import net.minecraft.server.v1_8_R1.EntitySkeleton;
import net.minecraft.server.v1_8_R1.EnumMonsterType;
import net.minecraft.server.v1_8_R1.ItemStack;
import net.minecraft.server.v1_8_R1.Items;
import net.minecraft.server.v1_8_R1.PathfinderGoalArrowAttack;
import net.minecraft.server.v1_8_R1.PathfinderGoalFloat;
import net.minecraft.server.v1_8_R1.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_8_R1.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_8_R1.PathfinderGoalRandomStroll;
import net.minecraft.server.v1_8_R1.World;
import net.minecraft.server.v1_8_R1.WorldServer;

public class CustomSkeleton extends EntitySkeleton implements ICustomUndeadEntity<Skeleton> {
  boolean isUndead = true;
  boolean isNonDayFire = true;

  static LbnMobTag DEFAULT_TAG = new LbnMobTag(EntityType.SKELETON);

  public CustomSkeleton(World world) {
    this(world.getWorld(), DEFAULT_TAG);
  }

  public CustomSkeleton(World world, LbnMobTag tag) {
    this(world.getWorld(), tag);
  }

  LbnMobTag tag = null;

  public CustomSkeleton(org.bukkit.World bukkitWorld, LbnMobTag tag) {
    super(((CraftWorld) bukkitWorld).getHandle());
    this.tag = tag;

    isNonDayFire = true;
    try {
      // AIを初期化する
      AttackAISetter.removeAllAi(this);

      // 攻撃対象のAIをセットする
      AttackAISetter.setTargetAI(this, tag);

      this.goalSelector.a(1, new PathfinderGoalFloat(this));

      // 戦闘AIをセットする
      AttackAISetter.setAttackAI(this, tag);

      this.goalSelector.a(8, new PathfinderGoalRandomStroll(this, 1.0D));
      this.goalSelector.a(9, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
      this.goalSelector.a(9, new PathfinderGoalRandomLookaround(this));
    } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
      e.printStackTrace();
    }
    if ((world != null) && (!world.isStatic)) {
      n();
    }
  }

  private PathfinderGoalArrowAttack b = new PathfinderGoalArrowAttack(this, 1.0D, 20, 60, 15.0F);
  private TheLowPathfinderGoalMeleeAttack c = null;

  @Override
  public void n() {
    // tagがnullのときは親クラスのコンストラクタが呼ばれる時だけで、その後AIは全てリセットされるのでここで別のAIをセットしても問題なし
    if (tag == null) {
      super.n();
      return;
    }

    // tagがnullかもしれないのでここでセットする
    if (c == null) {
      c = new TheLowPathfinderGoalMeleeAttack(this, AttackAISetter.getTargetEntityClass(tag.isSummonMob()), tag);
    }

    // AIが通常のものなら通常の処理を行う(tagがnullの時は無視する)
    if (tag.getAiType() == AIType.NORMAL) {
      this.goalSelector.a(this.c);
      this.goalSelector.a(this.b);
      ItemStack itemstack = bz();
      if ((itemstack != null) && (itemstack.getItem() == Items.BOW)) {
        this.goalSelector.a(4, this.b);
      } else {
        this.goalSelector.a(4, this.c);
      }
      return;
    }
  }

  @Override
  public EnumMonsterType getMonsterType() {
    if (isUndead) {
      return EnumMonsterType.UNDEAD;
    } else {
      return EnumMonsterType.UNDEFINED;
    }
  }

  @Override
  public void m() {
    if (isNonDayFire) {
      int nowFireTick = fireTicks;
      super.m();
      if (nowFireTick == 0) {
        extinguish();
      } else if (nowFireTick < 8 * 20) {
        extinguish();
        fireTicks = nowFireTick;
      } else {
        // 何もしない
      }
    } else {
      super.m();
    }
  }

  @Override
  public void setUndead(boolean isUndead) {
    this.isUndead = isUndead;
  }

  @Override
  public boolean isUndead() {
    return isUndead;
  }

  @Override
  public void setNonDayFire(boolean isNonDayFire) {
    this.isNonDayFire = isNonDayFire;
  }

  @Override
  public boolean isNonDayFire() {
    return isNonDayFire;
  }

  Player owner = null;

  @Override
  public boolean W() {
    if (!tag.isWaterMonster()) {
      return super.W();
    } else {
      inWater = false;
      return false;
    }
  }

  @Override
  public boolean V() {
    if (!tag.isWaterMonster()) {
      return super.V();
    } else {
      return false;
    }
  }

  @Override
  public Skeleton spawn(Location loc) {
    WorldServer world = ((CraftWorld) loc.getWorld()).getHandle();
    // 位置を指定
    setPositionRotation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
    // ワールドにentityを追加
    world.addEntity(this, SpawnReason.CUSTOM);
    return (Skeleton) getBukkitEntity();
  }

  @Override
  public void setPosition(double d0, double d1, double d2) {
    super.setPosition(d0, d1, d2);
    spawnLocation = new Location(world.getWorld(), d0, d1, d2);
  }

  Location spawnLocation = null;

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
