package main.mob.customEntity1_7;

import main.mob.customEntity.ICustomEntity;
import net.minecraft.server.v1_8_R1.EntityPigZombie;
import net.minecraft.server.v1_8_R1.World;
import net.minecraft.server.v1_8_R1.WorldServer;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R1.CraftWorld;
import org.bukkit.entity.Spider;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

public class CustomPigZombie extends EntityPigZombie implements ICustomEntity<Spider>{

	public CustomPigZombie(World world) {
		super(world);
		angerLevel = (400 + this.random.nextInt(400));
	}

	@Override
	public Spider spawn(Location loc) {
		WorldServer world = ((CraftWorld)loc.getWorld()).getHandle();
		//位置を指定
		setPositionRotation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(),  loc.getPitch());

		 //ワールドにentityを追加
		 world.addEntity(this, SpawnReason.CUSTOM);
		 return (Spider) getBukkitEntity();
	}

	@Override
	public void setNoKnockBackResistnce(double val) {

	}
//	@Override
//	protected Entity findTarget() {
//		if (angerLevel == 0) {
//			angerLevel = (400 + this.random.nextInt(400));
//		}
//		return super.findTarget();
//	}

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

}
