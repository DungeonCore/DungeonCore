package lbn.mob.customEntity1_8;

import lbn.mob.customEntity.ICustomEntity;
import lbn.mob.mob.LbnMobTag;
import lbn.util.JavaUtil;
import net.minecraft.server.v1_8_R1.DamageSource;
import net.minecraft.server.v1_8_R1.Entity;
import net.minecraft.server.v1_8_R1.EntityBat;
import net.minecraft.server.v1_8_R1.GenericAttributes;
import net.minecraft.server.v1_8_R1.World;
import net.minecraft.server.v1_8_R1.WorldServer;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R1.CraftWorld;
import org.bukkit.entity.Bat;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

public class CustomBat extends EntityBat implements ICustomEntity<Bat> {

	private LbnMobTag tag;
	private Location spawnLocation;

	public CustomBat(World world) {
		this(world, new LbnMobTag(EntityType.PIG));
	}

	public CustomBat(World world, LbnMobTag tag) {
		super(world);
		this.tag = tag;

//		//全てのAIを取り除く
//		try {
//			AttackAISetter.removeAllAi(this);
//			this.targetSelector.a(1, new TheLowPathfinderGoalHurtByTarget(this, true));
//			tag.setAiType(AIType.NORMAL);
//			goalSelector.a(6, new TheLowPathfinderGoalMeleeAttackForBat(this, EntityHuman.class, tag));
//		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
//			e.printStackTrace();
//		}
	}

	@Override
	public Bat spawn(Location loc) {
		WorldServer world = ((CraftWorld) loc.getWorld()).getHandle();
		// 位置を指定
		setPositionRotation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
		// ワールドにentityを追加
		world.addEntity(this, SpawnReason.CUSTOM);
		spawnLocation = loc;
		return (Bat) getBukkitEntity();
	}

	@Override
	protected void aW() {
		super.aW();
		getAttributeMap().b(GenericAttributes.e);
		getAttributeInstance(GenericAttributes.e).setValue(2.0D);
	}

	@Override
	public LbnMobTag getMobTag() {
		return tag;
	}

	int spawnCount = 0;

	@Override
	protected void D() {
		super.D();

		if (getMobTag() == null) {
			return;
		}

		// 指定した距離以上離れていたら殺す
		spawnCount++;
		if (spawnCount >= 60) {
			spawnCount = 0;
			if (spawnLocation == null) {
				return;
			}
			if (JavaUtil.getDistanceSquared(spawnLocation, locX, locY, locZ) < tag.getRemoveDistance() * tag.getRemoveDistance()) {
				return;
			}
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
