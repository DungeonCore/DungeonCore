package lbn.mob.customEntity1_8;

import lbn.mob.customEntity.ICustomUndeadEntity;
import lbn.mob.customEntity1_8.ai.PathfinderGoalNearestAttackableTargetNotTargetSub;
import lbn.util.spawn.LbnMobTag;
import net.minecraft.server.v1_8_R1.Enchantment;
import net.minecraft.server.v1_8_R1.EnchantmentManager;
import net.minecraft.server.v1_8_R1.EntityArrow;
import net.minecraft.server.v1_8_R1.EntityLiving;
import net.minecraft.server.v1_8_R1.EntityZombie;
import net.minecraft.server.v1_8_R1.EnumMonsterType;
import net.minecraft.server.v1_8_R1.IRangedEntity;
import net.minecraft.server.v1_8_R1.PathfinderGoalFloat;
import net.minecraft.server.v1_8_R1.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_8_R1.PathfinderGoalMoveTowardsRestriction;
import net.minecraft.server.v1_8_R1.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_8_R1.PathfinderGoalRandomStroll;
import net.minecraft.server.v1_8_R1.World;
import net.minecraft.server.v1_8_R1.WorldServer;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R1.event.CraftEventFactory;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityShootBowEvent;

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

    		//全てのAIを取り除く
    		AttackAISetter.removeAllAi(this);

    		//ターゲットAIを設定
    		this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, true));
    		PathfinderGoalNearestAttackableTargetNotTargetSub pathfinderGoalNearestAttackableTargetNotTargetSub = new PathfinderGoalNearestAttackableTargetNotTargetSub(this);
    		pathfinderGoalNearestAttackableTargetNotTargetSub.setSummon(tag.isSummonMob());
    		this.targetSelector.a(2, pathfinderGoalNearestAttackableTargetNotTargetSub);

    		this.goalSelector.a(0, new PathfinderGoalFloat(this));
    		//戦闘AIをセットする
    		AttackAISetter.setAttackAI(this, tag);

			this.goalSelector.a(7, new PathfinderGoalMoveTowardsRestriction(this, 1.0D));
//			this.goalSelector.a(8, new PathfinderGoalMoveThroughVillage(this, 1.0D, false));
			this.goalSelector.a(9, new PathfinderGoalRandomStroll(this, 1.0D));
			this.goalSelector.a(10, new PathfinderGoalRandomLookaround(this));

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
	public void a(EntityLiving entityliving, float f) {
		EntityArrow entityarrow = new EntityArrow(this.world, this, entityliving, 1.6F, 14 - this.world.getDifficulty().a() * 4);
		int i = EnchantmentManager.getEnchantmentLevel(Enchantment.ARROW_DAMAGE.id, bz());
		int j = EnchantmentManager.getEnchantmentLevel(Enchantment.ARROW_KNOCKBACK.id, bz());

		entityarrow.b(f * 2.0F + this.random.nextGaussian() * 0.25D + this.world.getDifficulty().a() * 0.11F);
		if (i > 0) {
			entityarrow.b(entityarrow.j() + i * 0.5D + 0.5D);
		}
		if (j > 0) {
			entityarrow.setKnockbackStrength(j);
		}
		if ((EnchantmentManager.getEnchantmentLevel(Enchantment.ARROW_FIRE.id, bz()) > 0)) {
			EntityCombustEvent event = new EntityCombustEvent(entityarrow.getBukkitEntity(), 100);
			this.world.getServer().getPluginManager().callEvent(event);
			if (!event.isCancelled()) {
				entityarrow.setOnFire(event.getDuration());
			}
		}
		EntityShootBowEvent event = CraftEventFactory.callEntityShootBowEvent(this, bz(), entityarrow, 0.8F);
		if (event.isCancelled()) {
			event.getProjectile().remove();
			return;
		}
		if (event.getProjectile() == entityarrow.getBukkitEntity()) {
			this.world.addEntity(entityarrow);
		}
		makeSound("random.bow", 1.0F, 1.0F / (bb().nextFloat() * 0.4F + 0.8F));
	}

}
