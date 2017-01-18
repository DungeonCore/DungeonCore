package lbn.mob.customEntity1_7;

import java.lang.reflect.Field;

import lbn.mob.customEntity.ICustomUndeadEntity;
import lbn.mob.customEntity1_7.ai.PathfinderGoalNearestAttackableTargetNotTargetSub;
import net.minecraft.server.v1_8_R1.EntityHuman;
import net.minecraft.server.v1_8_R1.EntityInsentient;
import net.minecraft.server.v1_8_R1.EntityLiving;
import net.minecraft.server.v1_8_R1.EntitySkeleton;
import net.minecraft.server.v1_8_R1.EnumMonsterType;
import net.minecraft.server.v1_8_R1.ItemStack;
import net.minecraft.server.v1_8_R1.Items;
import net.minecraft.server.v1_8_R1.NBTTagCompound;
import net.minecraft.server.v1_8_R1.PathfinderGoalArrowAttack;
import net.minecraft.server.v1_8_R1.PathfinderGoalFleeSun;
import net.minecraft.server.v1_8_R1.PathfinderGoalFloat;
import net.minecraft.server.v1_8_R1.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_8_R1.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_8_R1.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_8_R1.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_8_R1.PathfinderGoalRandomStroll;
import net.minecraft.server.v1_8_R1.PathfinderGoalRestrictSun;
import net.minecraft.server.v1_8_R1.PathfinderGoalSelector;
import net.minecraft.server.v1_8_R1.World;
import net.minecraft.server.v1_8_R1.WorldServer;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R1.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;


public class CustomSkeleton extends EntitySkeleton implements ICustomUndeadEntity<Skeleton>{
	boolean isUndead = true;
	boolean isNonDayFire = true;
	boolean isBatFly = false;

	boolean isCustom = false;

	public CustomSkeleton(World world) {
		this(world.getWorld(), false);
	}

	private PathfinderGoalArrowAttack bp = new PathfinderGoalArrowAttack(this, 1.0D, 20, 60, 15.0F);
	private PathfinderGoalMeleeAttack bq = new PathfinderGoalMeleeAttack(this, EntityLiving.class, 1.2D, false);

	public CustomSkeleton(org.bukkit.World bukkitWorld, boolean isSummon) {
		super(((CraftWorld) bukkitWorld).getHandle());

		isCustom = true;
		isNonDayFire = true;

		try {
			// targetSelectorを初期化
			Field field = EntityInsentient.class.getDeclaredField("targetSelector");
			field.setAccessible(true);
			field.set(this, new PathfinderGoalSelector((world != null) && (world.methodProfiler != null) ? world.methodProfiler : null));
			this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, false));
			PathfinderGoalNearestAttackableTargetNotTargetSub pathfinderGoalNearestAttackableTargetNotTargetSub = new PathfinderGoalNearestAttackableTargetNotTargetSub(this);
			pathfinderGoalNearestAttackableTargetNotTargetSub.setSummon(isSummon);
			this.targetSelector.a(2, pathfinderGoalNearestAttackableTargetNotTargetSub);

			// goalSelectorを初期化
			Field field2 = EntityInsentient.class.getDeclaredField("goalSelector");
			field2.setAccessible(true);
			field2.set(this, new PathfinderGoalSelector((world != null) && (world.methodProfiler != null) ? world.methodProfiler : null));

			this.goalSelector.a(1, new PathfinderGoalFloat(this));
			this.goalSelector.a(2, new PathfinderGoalRestrictSun(this));
			this.goalSelector.a(3, new PathfinderGoalFleeSun(this, 1.0D));
			this.goalSelector.a(5, new PathfinderGoalRandomStroll(this, 1.0D));
			this.goalSelector.a(6, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
			this.goalSelector.a(6, new PathfinderGoalRandomLookaround(this));
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}
		if ((world != null) && (!world.isStatic)) {
			n();
		}
	}

	@Override
	public void n() {
		if (!isCustom) {
			super.n();
			return;
		}

		this.goalSelector.a(this.bq);
		this.goalSelector.a(this.bp);
		ItemStack itemstack =  bz();

		if ((itemstack != null) && (itemstack.getItem() == Items.BOW)) {
			this.goalSelector.a(4, this.bp);
		} else {
			this.goalSelector.a(4, this.bq);
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
				//何もしない
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

	@Override
	public Skeleton spawn(Location loc) {
		WorldServer world = ((CraftWorld)loc.getWorld()).getHandle();
		//位置を指定
		setPositionRotation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(),  loc.getPitch());
		 //ワールドにentityを追加
		 world.addEntity(this, SpawnReason.CUSTOM);
		 return (Skeleton) getBukkitEntity();
	}

	boolean isIgnoreWater = false;

}
