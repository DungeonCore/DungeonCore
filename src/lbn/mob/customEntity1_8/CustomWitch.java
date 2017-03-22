package lbn.mob.customEntity1_8;

import lbn.mob.customEntity.ICustomEntity;
import lbn.mob.mob.LbnMobTag;
import lbn.mob.mob.abstractmob.AbstractWitch;
import lbn.util.JavaUtil;
import net.minecraft.server.v1_8_R1.EntityWitch;
import net.minecraft.server.v1_8_R1.NBTTagCompound;
import net.minecraft.server.v1_8_R1.World;
import net.minecraft.server.v1_8_R1.WorldServer;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R1.CraftWorld;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Witch;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

public class CustomWitch extends EntityWitch implements ICustomEntity<Witch>{

	private LbnMobTag tag;

	public CustomWitch(World w) {
		this(w, new LbnMobTag(EntityType.WITCH));
	}

	public CustomWitch(World w, LbnMobTag tag) {
		super(w);
		this.tag = tag;
	}

	boolean isChangeItem = true;

	public CustomWitch(org.bukkit.World w, AbstractWitch mobInstance) {
		super(((CraftWorld)w).getHandle());

		isChangeItem = mobInstance.isChangeItem();
	}

	@Override
	public Witch spawn(Location loc) {
		WorldServer world = ((CraftWorld)loc.getWorld()).getHandle();
		//位置を指定
		setPositionRotation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(),  loc.getPitch());
		 //ワールドにentityを追加
		 world.addEntity(this, SpawnReason.CUSTOM);
		 spawnLocation = loc;
		 return (Witch) getBukkitEntity();
	}
	Location spawnLocation = null;

//	@Override
//	public boolean N() {
//		if (!isIgnoreWater) {
//			return super.N();
//		} else {
//			inWater = false;
//			return false;
//		}
//	}
//
//	@Override
//	public boolean M() {
//		if (!isIgnoreWater) {
//			return super.M();
//		} else {
//			return false;
//		}
//	}

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

		if (getMobTag() == null) {
			return;
		}

		//指定した距離以上離れていたら殺す
		spawnCount++;
		if (spawnCount >= 60) {
			spawnCount = 0;
			if (spawnLocation == null) {
				return;
			}
			if (JavaUtil.getDistanceSquared(spawnLocation, locX, locY, locZ) < 100 * 100) {
				return;
			}
			if (getMobTag().isBoss()) {
				getBukkitEntity().teleport(spawnLocation);
			} else {
				die();
			}
		}
	}

//	@Override
//	public void e() {
//		ItemStack itemstack = this.be();
//		super.e();
//		//持っているアイテムを戻す
//		if (!isChangeItem) {
//			setEquipment(0, itemstack);
//		}
//
//	}
}
