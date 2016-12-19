package lbn.mob.customEntity1_7;

import net.minecraft.server.v1_8_R1.EntityWitch;
import net.minecraft.server.v1_8_R1.GenericAttributes;
import net.minecraft.server.v1_8_R1.World;
import net.minecraft.server.v1_8_R1.WorldServer;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R1.CraftWorld;
import org.bukkit.entity.Witch;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import lbn.mob.customEntity.ICustomEntity;
import lbn.mob.mob.abstractmob.AbstractWitch;

public class CustomWitch extends EntityWitch implements ICustomEntity<Witch>{

	public CustomWitch(World w) {
		super(w);
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
		 return (Witch) getBukkitEntity();
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
	public void setNoKnockBackResistnce(double val) {
		getAttributeInstance(GenericAttributes.c).setValue(val);
	}

	@Override
	public double getNoKnockBackResistnce() {
		return getAttributeInstance(GenericAttributes.c).getValue();
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
