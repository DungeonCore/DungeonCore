package lbn.mob.customEntity1_7;

import net.minecraft.server.v1_8_R1.EntityEnderman;
import net.minecraft.server.v1_8_R1.World;
import net.minecraft.server.v1_8_R1.WorldServer;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R1.CraftWorld;
import org.bukkit.entity.Enderman;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import lbn.mob.customEntity.ICustomEntity;

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
	public void setNoKnockBackResistnce(double val) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public double getNoKnockBackResistnce() {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}

	@Override
	public void setFlyMob(boolean isFly) {
		// TODO 自動生成されたメソッド・スタブ

	}

	@Override
	public boolean isFlyMob() {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

	@Override
	public boolean isIgnoreWater() {
		// TODO 自動生成されたメソッド・スタブ
		return false;
	}

	@Override
	public void setIgnoreWater(boolean isIgnoreWater) {
		// TODO 自動生成されたメソッド・スタブ

	}

}
