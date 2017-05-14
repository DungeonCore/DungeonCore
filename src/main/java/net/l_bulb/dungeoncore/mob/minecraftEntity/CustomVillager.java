package net.l_bulb.dungeoncore.mob.minecraftEntity;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R1.CraftWorld;
import org.bukkit.entity.Villager;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.util.Vector;

import net.l_bulb.dungeoncore.mob.customMob.LbnMobTag;

import net.minecraft.server.v1_8_R1.EntityVillager;
import net.minecraft.server.v1_8_R1.NBTTagCompound;
import net.minecraft.server.v1_8_R1.World;
import net.minecraft.server.v1_8_R1.WorldServer;

public class CustomVillager extends EntityVillager implements ICustomEntity<Villager> {
  public CustomVillager(World world) {
    super(world);
  }

  public CustomVillager(org.bukkit.World world) {
    super(((CraftWorld) world).getHandle());
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
    WorldServer world = ((CraftWorld) loc.getWorld()).getHandle();
    // 位置を指定
    setPositionRotation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
    ageLocked = true;
    // ワールドにentityを追加
    world.addEntity(this, SpawnReason.CUSTOM);
    return (Villager) getBukkitEntity();
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

  @Override
  public LbnMobTag getMobTag() {
    return null;
  }
}
