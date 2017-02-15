package lbn.mob.customEntity1_8;

import lbn.mob.customEntity.ICustomEntity;
import net.minecraft.server.v1_8_R1.EntityEnderman;
import net.minecraft.server.v1_8_R1.NBTTagCompound;
import net.minecraft.server.v1_8_R1.World;
import net.minecraft.server.v1_8_R1.WorldServer;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R1.CraftWorld;
import org.bukkit.entity.Enderman;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

public class CustomEnderman extends EntityEnderman implements ICustomEntity<Enderman>{

	public CustomEnderman(World world) {
		super(world);
	}

	int newBt = 0;

//	@Override
//	protected Entity findTarget() {
//		Entity findTarget = super.findTarget();
//		if (findTarget == null) {
//			newBt = 0;
//			return findTarget;
//		}
//
//		EntityHuman human = (EntityHuman)findTarget;
//
//		if (newBt == 0) {
//			EndermanFindTargetEvent event = new EndermanFindTargetEvent((org.bukkit.entity.Entity)getBukkitEntity(), (Player)human.getBukkitEntity());
//			Bukkit.getServer().getPluginManager().callEvent(event);
//		} else if (newBt >= 5) {
//			newBt = -1;
//		}
//		newBt++;
//
//		return findTarget;
//	}

	@Override
	public Enderman spawn(Location loc) {
		WorldServer world = ((CraftWorld)loc.getWorld()).getHandle();
		//位置を指定
		setPositionRotation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(),  loc.getPitch());
		 //ワールドにentityを追加
		 world.addEntity(this, SpawnReason.CUSTOM);
		 return (Enderman) getBukkitEntity();
	}

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

}
