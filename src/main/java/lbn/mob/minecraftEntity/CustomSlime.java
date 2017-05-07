package lbn.mob.minecraftEntity;

import net.minecraft.server.v1_8_R1.EntitySlime;
import net.minecraft.server.v1_8_R1.GenericAttributes;
import net.minecraft.server.v1_8_R1.World;
import net.minecraft.server.v1_8_R1.WorldServer;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R1.CraftWorld;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Slime;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import lbn.mob.customMob.LbnMobTag;
import lbn.util.JavaUtil;

public class CustomSlime extends EntitySlime implements ICustomEntity<Slime> {

  private LbnMobTag tag;

  public CustomSlime(World world) {
    this(world, new LbnMobTag(EntityType.SLIME));
  }

  public CustomSlime(World world, LbnMobTag tag) {
    super(world);
    this.tag = tag;
    getAttributeInstance(GenericAttributes.maxHealth).setValue(10);
  }

  @Override
  protected void aW() {
    super.aW();
    getAttributeMap().b(GenericAttributes.e);
    this.getAttributeInstance(GenericAttributes.e).setValue(2.0D);
  }

  public void setSize(int i) {
    this.datawatcher.watch(16, Byte.valueOf((byte) i));
    a(0.51000005F * i, 0.51000005F * i);
    setPosition(this.locX, this.locY, this.locZ);
    getAttributeInstance(GenericAttributes.d).setValue(0.2F + 0.1F * i);
    setHealth(getMaxHealth());
    this.b_ = i;
  }

  @Override
  public void setPosition(double d0, double d1, double d2) {
    super.setPosition(d0, d1, d2);
    spawnLocation = new Location(world.getWorld(), d0, d1, d2);
  }

  Location spawnLocation = null;

  @Override
  public Slime spawn(Location loc) {
    WorldServer world = ((CraftWorld) loc.getWorld()).getHandle();
    // 位置を指定
    setPositionRotation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
    // ワールドにentityを追加
    world.addEntity(this, SpawnReason.CUSTOM);
    return (Slime) getBukkitEntity();
  }

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
