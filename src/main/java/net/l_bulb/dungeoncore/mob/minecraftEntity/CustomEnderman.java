package net.l_bulb.dungeoncore.mob.minecraftEntity;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R1.CraftWorld;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import net.l_bulb.dungeoncore.mob.customMob.LbnMobTag;
import net.l_bulb.dungeoncore.util.JavaUtil;
import net.minecraft.server.v1_8_R1.EntityEnderman;
import net.minecraft.server.v1_8_R1.NBTTagCompound;
import net.minecraft.server.v1_8_R1.World;
import net.minecraft.server.v1_8_R1.WorldServer;

public class CustomEnderman extends EntityEnderman implements ICustomEntity<Enderman> {

  private LbnMobTag tag;

  public CustomEnderman(World world, LbnMobTag tag) {
    super(world);
    this.tag = tag;

  }

  public CustomEnderman(World world) {
    this(world, new LbnMobTag(EntityType.ENDERMAN));
  }

  int newBt = 0;

  // @Override
  // protected Entity findTarget() {
  // Entity findTarget = super.findTarget();
  // if (findTarget == null) {
  // newBt = 0;
  // return findTarget;
  // }
  //
  // EntityHuman human = (EntityHuman)findTarget;
  //
  // if (newBt == 0) {
  // EndermanFindTargetEvent event = new EndermanFindTargetEvent((org.bukkit.entity.Entity)getBukkitEntity(), (Player)human.getBukkitEntity());
  // Bukkit.getServer().getPluginManager().callEvent(event);
  // } else if (newBt >= 5) {
  // newBt = -1;
  // }
  // newBt++;
  //
  // return findTarget;
  // }

  @Override
  public Enderman spawn(Location loc) {
    WorldServer world = ((CraftWorld) loc.getWorld()).getHandle();
    // 位置を指定
    setPositionRotation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
    // ワールドにentityを追加
    world.addEntity(this, SpawnReason.CUSTOM);
    return (Enderman) getBukkitEntity();
  }

  @Override
  public void setPosition(double d0, double d1, double d2) {
    super.setPosition(d0, d1, d2);
    spawnLocation = new Location(world.getWorld(), d0, d1, d2);
  }

  Location spawnLocation = null;

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

    if (getMobTag() == null) { return; }

    // 指定した距離以上離れていたら殺す
    spawnCount++;
    if (spawnCount >= 60) {
      spawnCount = 0;
      if (spawnLocation == null) { return; }
      if (JavaUtil.getDistanceSquared(spawnLocation, locX, locY, locZ) < tag.getRemoveDistance() * tag.getRemoveDistance()) { return; }
      if (getMobTag().isBoss()) {
        getBukkitEntity().teleport(spawnLocation);
      } else {
        die();
      }
    }
  }

}
