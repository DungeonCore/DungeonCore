package lbn.mob.customEntity1_7;

import java.lang.reflect.Field;

import lbn.mob.customEntity.ICustomUndeadEntity;
import lbn.mob.customEntity1_7.ai.PathfinderGoalNearestAttackableTargetNotTargetSub;
import lbn.mob.customEntity1_7.ai.TheLowPathfinderGoalMeleeAttack;
import lbn.util.LivingEntityUtil;
import lbn.util.spawn.LbnMobTag;
import net.minecraft.server.v1_8_R1.EntityInsentient;
import net.minecraft.server.v1_8_R1.EntityLiving;
import net.minecraft.server.v1_8_R1.EntityMonster;
import net.minecraft.server.v1_8_R1.EntityPlayer;
import net.minecraft.server.v1_8_R1.EntitySnowball;
import net.minecraft.server.v1_8_R1.EntityZombie;
import net.minecraft.server.v1_8_R1.EnumMonsterType;
import net.minecraft.server.v1_8_R1.IRangedEntity;
import net.minecraft.server.v1_8_R1.MathHelper;
import net.minecraft.server.v1_8_R1.PathfinderGoalAvoidTarget;
import net.minecraft.server.v1_8_R1.PathfinderGoalFloat;
import net.minecraft.server.v1_8_R1.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_8_R1.PathfinderGoalMoveTowardsRestriction;
import net.minecraft.server.v1_8_R1.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_8_R1.PathfinderGoalRandomStroll;
import net.minecraft.server.v1_8_R1.PathfinderGoalSelector;
import net.minecraft.server.v1_8_R1.World;
import net.minecraft.server.v1_8_R1.WorldServer;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.projectiles.ProjectileSource;

import com.google.common.base.Predicate;

public class CustomZombie extends EntityZombie implements ICustomUndeadEntity<Zombie>, IRangedEntity{
    public CustomZombie(org.bukkit.World bukkitWorld, LbnMobTag tag) {
    	this(((CraftWorld)bukkitWorld).getHandle(), tag);
    }

    public CustomZombie(World world) {
    	this(world, new LbnMobTag(EntityType.ZOMBIE));
    }

	public CustomZombie(World world, LbnMobTag tag) {
    	super(world);
    	try {
    		isIgnoreWater = tag.isWaterMonster();

			//targetSelectorを初期化
			Field field = EntityInsentient.class.getDeclaredField("targetSelector");
			field.setAccessible(true);
			field.set(this, new PathfinderGoalSelector((world != null) && (world.methodProfiler != null) ? world.methodProfiler : null));
			this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, true));
			PathfinderGoalNearestAttackableTargetNotTargetSub pathfinderGoalNearestAttackableTargetNotTargetSub = new PathfinderGoalNearestAttackableTargetNotTargetSub(this);
			pathfinderGoalNearestAttackableTargetNotTargetSub.setSummon(tag.isSummonMob());
			this.targetSelector.a(2, pathfinderGoalNearestAttackableTargetNotTargetSub);

			//goalSelectorを初期化
			Field field2 = EntityInsentient.class.getDeclaredField("goalSelector");
			field2.setAccessible(true);
			field2.set(this, new PathfinderGoalSelector((world != null) && (world.methodProfiler != null) ? world.methodProfiler : null));
			this.goalSelector.a(0, new PathfinderGoalFloat(this));

			//プレイヤーから逃げる処理
			if (tag.isAvoidPlayer()) {
				this.goalSelector.a(1, new PathfinderGoalAvoidTarget(this, new AvoidPlayer(), 6.0F, 2.0D, 1.2D));
			}

			//テスト用なので一時的に削除
//			//弓を打つ処理
//			this.goalSelector.a(2, new TheLoWPathfinderGoalArrowAttack(this, 1.25D, 20, 10.0F));
			//近距離攻撃の処理
			if (tag.isSummonMob()) {
				this.goalSelector.a(3, new TheLowPathfinderGoalMeleeAttack(this, EntityMonster.class, tag));
			} else {
				this.goalSelector.a(3, new TheLowPathfinderGoalMeleeAttack(this, EntityLiving.class, tag));
			}
			this.goalSelector.a(5, new PathfinderGoalMoveTowardsRestriction(this, 1.0D));
//			this.goalSelector.a(6, new PathfinderGoalMoveThroughVillage(this, 1.0D, false));
			this.goalSelector.a(7, new PathfinderGoalRandomStroll(this, 1.0D));
			this.goalSelector.a(8, new PathfinderGoalRandomLookaround(this));

		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
    }

//	public CustomZombie(World world) {
//		super(world);
//		((Navigation) getNavigation()).b(true);
//		this.goalSelector.a(0, new PathfinderGoalFloat(this));
//		this.goalSelector.a(2, new PathfinderGoalMeleeAttack(this,
//				EntityHuman.class, 1.0D, false));
//		this.goalSelector.a(5, new PathfinderGoalMoveTowardsRestriction(this,
//				1.0D));
//		this.goalSelector.a(7, new PathfinderGoalRandomStroll(this, 1.0D));
//		this.goalSelector.a(8, new PathfinderGoalLookAtPlayer(this,
//				EntityHuman.class, 8.0F));
//		this.goalSelector.a(8, new PathfinderGoalRandomLookaround(this));
//		n();
//		setSize(0.6F, 1.95F);
//	}
//
//	protected void n() {
//		if (this.world.spigotConfig.zombieAggressiveTowardsVillager) {
//			this.goalSelector.a(4, new PathfinderGoalMeleeAttack(this,
//					EntityVillager.class, 1.0D, true));
//		}
//		this.goalSelector.a(4, new PathfinderGoalMeleeAttack(this,
//				EntityIronGolem.class, 1.0D, true));
//		this.goalSelector.a(6, new PathfinderGoalMoveThroughVillage(this, 1.0D,
//				false));
//		this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, true,
//				new Class[] { EntityPigZombie.class }));
//		this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<EntityHuman>(
//				this, EntityHuman.class, true));
//		if (this.world.spigotConfig.zombieAggressiveTowardsVillager) {
//			this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<EntityVillager>(
//					this, EntityVillager.class, false));
//		}
//		this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget<EntityIronGolem>(
//				this, EntityIronGolem.class, true));
//	}

	@Override
	public EnumMonsterType getMonsterType() {
		if (isUndead) {
			return EnumMonsterType.UNDEAD;
		} else {
			return EnumMonsterType.UNDEFINED;
		}
	}

//	private ChunkCoordinates h;

//	@Override
//	protected void bn() {
//		super.bn();
//		if (isFly) {
//			CustomEntityUtil.onFly(h, this);
//		}
//	}

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
				//何もしない
			}
		} else {
			super.m();
		}
	}

	boolean isUndead = false;

	@Override
	public void setUndead(boolean isUndead) {
		this.isUndead = isUndead;
	}

	@Override
	public boolean isUndead() {
		return isUndead;
	}

	boolean isNonDayFire = true;

	@Override
	public void setNonDayFire(boolean isNonDayFire) {
		this.isNonDayFire = isNonDayFire;
	}

	@Override
	public boolean isNonDayFire() {
		return isNonDayFire;
	}

	boolean isFly = false;

	@Override
	public Zombie spawn(Location loc) {
		WorldServer world = ((CraftWorld)loc.getWorld()).getHandle();
		//位置を指定
		setPositionRotation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(),  loc.getPitch());
		 //ワールドにentityを追加
		 world.addEntity(this, SpawnReason.CUSTOM);
		 return (Zombie) getBukkitEntity();
	}

	boolean isIgnoreWater = false;

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

	@Override
	public void a(EntityLiving entityliving, float paramFloat) {
		double d0 = entityliving.locY + entityliving.getHeadHeight()
				- 1.100000023841858D;
		double d1 = entityliving.locX - this.locX;
		double d3 = entityliving.locZ - this.locZ;
		float f1 = MathHelper.sqrt(d1 * d1 + d3 * d3) * 0.2F;

		CraftEntity bukkitEntity2 = getBukkitEntity();
		Location location = entityliving.getBukkitEntity().getLocation();

		if ("test mob".equals(getName())) {
			EntitySnowball entitysnowball = new EntitySnowball(this.world, this);
			double d2 = d0 - entitysnowball.locY;
			entitysnowball.shoot(d1, d2 + f1, d3, 1.6F, 12.0F);
			makeSound("random.bow", 1.0F, 1.0F / (bb().nextFloat() * 0.4F + 0.8F));
			this.world.addEntity(entitysnowball);
		} else if ("test mob2".equals(getName())) {
			Player player = Bukkit.getPlayer("Namiken");
			LivingEntityUtil.strikeLightningEffect(entityliving.getBukkitEntity().getLocation(), player);
		} else if ("test mob3".equals(getName())) {
			Fireball launchProjectile = ((ProjectileSource)bukkitEntity2).launchProjectile(Fireball.class);
			launchProjectile.setIsIncendiary(false);
			launchProjectile.setShooter((ProjectileSource)bukkitEntity2);
			location.getWorld().playSound(location, Sound.BLAZE_HIT, 1, 0.5f);
		}
	}

}

@SuppressWarnings("rawtypes")
class AvoidPlayer implements Predicate {
	AvoidPlayer() {
	}

	@Override
	public boolean apply(Object paramT) {
		return  paramT instanceof EntityPlayer;
	}
}