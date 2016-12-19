package main.mob.customEntity1_7;

import java.lang.reflect.Field;

import main.mob.customEntity.ICustomUndeadEntity;
import main.mob.customEntity1_7.ai.PathfinderGoalNearestAttackableTargetNotTargetSub;
import main.mob.mob.SummonMobable;
import main.mob.mob.abstractmob.AbstractZombie;
import net.minecraft.server.v1_8_R1.EntityInsentient;
import net.minecraft.server.v1_8_R1.EntityLiving;
import net.minecraft.server.v1_8_R1.EntityMonster;
import net.minecraft.server.v1_8_R1.EntityPlayer;
import net.minecraft.server.v1_8_R1.EntityZombie;
import net.minecraft.server.v1_8_R1.EnumMonsterType;
import net.minecraft.server.v1_8_R1.GenericAttributes;
import net.minecraft.server.v1_8_R1.PathfinderGoalAvoidTarget;
import net.minecraft.server.v1_8_R1.PathfinderGoalFloat;
import net.minecraft.server.v1_8_R1.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_8_R1.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_8_R1.PathfinderGoalMoveThroughVillage;
import net.minecraft.server.v1_8_R1.PathfinderGoalMoveTowardsRestriction;
import net.minecraft.server.v1_8_R1.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_8_R1.PathfinderGoalRandomStroll;
import net.minecraft.server.v1_8_R1.PathfinderGoalSelector;
import net.minecraft.server.v1_8_R1.World;
import net.minecraft.server.v1_8_R1.WorldServer;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R1.CraftWorld;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import com.google.common.base.Predicate;

public class CustomZombie extends EntityZombie implements ICustomUndeadEntity<Zombie>{
    public CustomZombie(org.bukkit.World bukkitWorld, AbstractZombie zombie) {
    	this(((CraftWorld)bukkitWorld).getHandle(), zombie);
    }

    public CustomZombie(World world) {
    	this(world, false, false, 1.0);
    }

	public CustomZombie(World world, boolean isSummon, boolean isAvoidPlayer, double nearingSpeed) {
    	super(world);
    	try {
			this.isSummon = isSummon;

			//targetSelectorを初期化
			Field field = EntityInsentient.class.getDeclaredField("targetSelector");
			field.setAccessible(true);
			field.set(this, new PathfinderGoalSelector((world != null) && (world.methodProfiler != null) ? world.methodProfiler : null));
			this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, true));
			PathfinderGoalNearestAttackableTargetNotTargetSub pathfinderGoalNearestAttackableTargetNotTargetSub = new PathfinderGoalNearestAttackableTargetNotTargetSub(this);
			pathfinderGoalNearestAttackableTargetNotTargetSub.setSummon(isSummon);
			this.targetSelector.a(2, pathfinderGoalNearestAttackableTargetNotTargetSub);

			//goalSelectorを初期化
			Field field2 = EntityInsentient.class.getDeclaredField("goalSelector");
			field2.setAccessible(true);
			field2.set(this, new PathfinderGoalSelector((world != null) && (world.methodProfiler != null) ? world.methodProfiler : null));
			this.goalSelector.a(0, new PathfinderGoalFloat(this));
//			プレイヤーから逃げる処理
			if (isAvoidPlayer) {
				this.goalSelector.a(1, new PathfinderGoalAvoidTarget(this, new AvoidPlayer(), 6.0F, 2.0D, 1.2D));
			}
			if (isSummon) {
				this.goalSelector.a(2, new PathfinderGoalMeleeAttack(this, EntityMonster.class, nearingSpeed, false));
			} else {
				this.goalSelector.a(2, new PathfinderGoalMeleeAttack(this, EntityLiving.class, nearingSpeed, false));
			}
//			if (world.spigotConfig.zombieAggressiveTowardsVillager) this.goalSelector.a(4, new PathfinderGoalMeleeAttack(this, EntityVillager.class, 1.0D, true));
			this.goalSelector.a(5, new PathfinderGoalMoveTowardsRestriction(this, 1.0D));
			this.goalSelector.a(6, new PathfinderGoalMoveThroughVillage(this, 1.0D, false));
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


    boolean isSummon = false;
	public CustomZombie(World world, AbstractZombie zombie) {
		this(world, zombie instanceof SummonMobable, zombie.isAvoidPlayer(), zombie.getNearingSpeed());
	}

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

	//TODO 燃えるかも
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

	boolean isUndead = true;

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
	public void setFlyMob(boolean isFly) {
		this.isFly = isFly;
	}

	@Override
	public boolean isFlyMob() {
		return isFly;
	}

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
	public boolean isIgnoreWater() {
		return isIgnoreWater;
	}

	@Override
	public void setIgnoreWater(boolean isIgnoreWater) {
		this.isIgnoreWater = isIgnoreWater;
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

	@Override
	public void setNoKnockBackResistnce(double val) {
		getAttributeInstance(GenericAttributes.c).setValue(val);
	}

	@Override
	public double getNoKnockBackResistnce() {
		return getAttributeInstance(GenericAttributes.c).getValue();
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