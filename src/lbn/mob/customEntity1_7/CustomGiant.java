package lbn.mob.customEntity1_7;

import java.lang.reflect.Field;

import lbn.mob.customEntity.ICustomUndeadEntity;
import lbn.mob.customEntity1_7.ai.PathfinderGoalNearestAttackableTargetNotTargetSub;
import net.minecraft.server.v1_8_R1.EntityGiantZombie;
import net.minecraft.server.v1_8_R1.EntityHuman;
import net.minecraft.server.v1_8_R1.EntityIronGolem;
import net.minecraft.server.v1_8_R1.EntityVillager;
import net.minecraft.server.v1_8_R1.GenericAttributes;
import net.minecraft.server.v1_8_R1.PathfinderGoalFloat;
import net.minecraft.server.v1_8_R1.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_8_R1.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_8_R1.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_8_R1.PathfinderGoalMoveThroughVillage;
import net.minecraft.server.v1_8_R1.PathfinderGoalMoveTowardsRestriction;
import net.minecraft.server.v1_8_R1.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_8_R1.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_8_R1.PathfinderGoalRandomStroll;
import net.minecraft.server.v1_8_R1.PathfinderGoalSelector;
import net.minecraft.server.v1_8_R1.World;
import net.minecraft.server.v1_8_R1.WorldServer;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R1.util.UnsafeList;
import org.bukkit.entity.Giant;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

public class CustomGiant extends EntityGiantZombie implements ICustomUndeadEntity<Giant>{

	public CustomGiant(World world) {
		super(world);
		try {
			Field bField = PathfinderGoalSelector.class.getDeclaredField("b");
			bField.setAccessible(true);
			Field cField = PathfinderGoalSelector.class.getDeclaredField("c");
			cField.setAccessible(true);
			bField.set(goalSelector, new UnsafeList<PathfinderGoalSelector>());
			bField.set(targetSelector, new UnsafeList<PathfinderGoalSelector>());
			cField.set(goalSelector, new UnsafeList<PathfinderGoalSelector>());
			cField.set(targetSelector, new UnsafeList<PathfinderGoalSelector>());
			this.n();
		} catch (Exception exc) {
			exc.printStackTrace();
		}

		this.goalSelector.a(0, new PathfinderGoalFloat(this));
		this.goalSelector.a(2, new PathfinderGoalMeleeAttack(this, EntityHuman.class, 0.7D, false));
		this.goalSelector.a(5, new PathfinderGoalMoveTowardsRestriction(this, 1.0D));
		this.goalSelector.a(6, new PathfinderGoalMoveThroughVillage(this, 1.0D, false));
		this.goalSelector.a(7, new PathfinderGoalRandomStroll(this, 1.0D));
		this.goalSelector.a(8, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
		this.goalSelector.a(8, new PathfinderGoalRandomLookaround(this));
		this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, true));
		PathfinderGoalNearestAttackableTargetNotTargetSub pathfinderGoalNearestAttackableTargetNotTargetSub = new PathfinderGoalNearestAttackableTargetNotTargetSub(this);
		pathfinderGoalNearestAttackableTargetNotTargetSub.setSummon(false);
		this.targetSelector.a(2, pathfinderGoalNearestAttackableTargetNotTargetSub);
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

	protected void n() {
        this.goalSelector.a(4, new PathfinderGoalMeleeAttack(this, EntityVillager.class, 1.0D, true));
        this.goalSelector.a(4, new PathfinderGoalMeleeAttack(this, EntityIronGolem.class, 1.0D, true));
        this.goalSelector.a(6, new PathfinderGoalMoveThroughVillage(this, 1.0D, false));
        this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, true));
        this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntityHuman.class, true));
    }
//	public CustomGiant(World arg0) {
//		super(arg0);
//		((Navigation) getNavigation()).b(true);
//		this.goalSelector.a(0, new PathfinderGoalFloat(this));
//		this.goalSelector.a(2, new PathfinderGoalMeleeAttack(this,
//				EntityHuman.class, 1.0D, false));
//		this.goalSelector.a(2, this.a);
//		this.goalSelector.a(5, new PathfinderGoalMoveTowardsRestriction(this,
//				1.0D));
//		this.goalSelector.a(7, new PathfinderGoalRandomStroll(this, 1.0D));
//		this.goalSelector.a(8, new PathfinderGoalLookAtPlayer(this,
//				EntityHuman.class, 8.0F));
//		this.goalSelector.a(8, new PathfinderGoalRandomLookaround(this));
//		n();
//	}
//
//	protected void n() {
////		if (this.world.spigotConfig.zombieAggressiveTowardsVillager) {
////			this.goalSelector.a(4, new PathfinderGoalMeleeAttack(this,
////					EntityVillager.class, 1.0D, true));
////		}
////		this.goalSelector.a(4, new PathfinderGoalMeleeAttack(this,
////				EntityIronGolem.class, 1.0D, true));
////		this.goalSelector.a(6, new PathfinderGoalMoveThroughVillage(this, 1.0D,
////				false));
//		this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, true,
//				new Class[] { EntityPigZombie.class }));
//		this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(
//				this, EntityHuman.class, true));
////		if (this.world.spigotConfig.zombieAggressiveTowardsVillager) {
////			this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(
////					this, EntityVillager.class, false));
////		}
////		this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(
////				this, EntityIronGolem.class, true));
//	}

	@Override
	public Giant spawn(Location loc) {
		WorldServer world = ((CraftWorld)loc.getWorld()).getHandle();
		//位置を指定
		setPositionRotation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(),  loc.getPitch());
		 //ワールドにentityを追加
		 world.addEntity(this, SpawnReason.CUSTOM);
		 return (Giant) getBukkitEntity();
	}

	@Override
	public void setNoKnockBackResistnce(double val) {

	}

	@Override
	public double getNoKnockBackResistnce() {
		return 0;
	}

	@Override
	public void setFlyMob(boolean isFly) {

	}

	@Override
	public boolean isFlyMob() {
		return false;
	}

	@Override
	public boolean isIgnoreWater() {
		return false;
	}

	@Override
	public void setIgnoreWater(boolean isIgnoreWater) {

	}

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

}
