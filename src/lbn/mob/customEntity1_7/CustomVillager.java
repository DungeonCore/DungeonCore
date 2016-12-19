package lbn.mob.customEntity1_7;

import net.minecraft.server.v1_8_R1.EntityVillager;
import net.minecraft.server.v1_8_R1.GenericAttributes;
import net.minecraft.server.v1_8_R1.World;
import net.minecraft.server.v1_8_R1.WorldServer;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R1.CraftWorld;
import org.bukkit.entity.Villager;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.util.Vector;

import lbn.mob.customEntity.ICustomEntity;

public class CustomVillager  extends EntityVillager implements ICustomEntity<Villager>{
	 public CustomVillager(World world) {
	        super(world);
	 }

		public CustomVillager(org.bukkit.World world) {
			super(((CraftWorld)world).getHandle());
		}

	    @Override
	    public void move(double d0, double d1, double d2) {
	        return;
	    }

	    @Override
	    public void g(double x, double y, double z) {
	        Vector vector = this.getBukkitEntity().getVelocity();
	        super.g(vector.getX(), vector.getY(), vector.getZ());
	    }

		@Override
		public Villager spawn(Location loc) {
			WorldServer world = ((CraftWorld)loc.getWorld()).getHandle();
			//位置を指定
			setPositionRotation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(),  loc.getPitch());
			ageLocked = true;
			 //ワールドにentityを追加
			 world.addEntity(this, SpawnReason.CUSTOM);
			 return (Villager) getBukkitEntity();
		}

		@Deprecated
		@Override
		public void setFlyMob(boolean isFly) {
		}

		@Deprecated
		@Override
		public boolean isFlyMob() {
			return false;
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
		public void setNoKnockBackResistnce(double val) {
			getAttributeInstance(GenericAttributes.c).setValue(val);
		}

		@Override
		public double getNoKnockBackResistnce() {
			return getAttributeInstance(GenericAttributes.c).getValue();
		}
}
