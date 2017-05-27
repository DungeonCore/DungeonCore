package net.l_bulb.dungeoncore.mob.minecraftEntity;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R1.CraftWorld;
import org.bukkit.entity.Snowman;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import net.l_bulb.dungeoncore.mob.customMob.LbnMobTag;
import net.l_bulb.dungeoncore.util.JavaUtil;

import net.minecraft.server.v1_8_R1.EntityHuman;
import net.minecraft.server.v1_8_R1.EntitySnowman;
import net.minecraft.server.v1_8_R1.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_8_R1.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_8_R1.PathfinderGoalRandomStroll;
import net.minecraft.server.v1_8_R1.World;
import net.minecraft.server.v1_8_R1.WorldServer;

public class CustomSnowman extends EntitySnowman implements ICustomEntity<Snowman> {

  private LbnMobTag tag;

  public CustomSnowman(World world, LbnMobTag tag) {
    super(world);
    this.tag = tag;
    try {
      AttackAISetter.removeAllAi(this);

      // 戦闘AIをセットする
      AttackAISetter.setAttackAI(this, tag);
      this.goalSelector.a(8, new PathfinderGoalRandomStroll(this, 1.0D));
      this.goalSelector.a(9, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
      this.goalSelector.a(9, new PathfinderGoalRandomLookaround(this));

      // 攻撃対象のAIをセットする
      AttackAISetter.setTargetAI(this, tag);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public Snowman spawn(Location loc) {
    WorldServer world = ((CraftWorld) loc.getWorld()).getHandle();
    // 位置を指定
    setPositionRotation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
    // ワールドにentityを追加
    world.addEntity(this, SpawnReason.CUSTOM);
    return (Snowman) getBukkitEntity();
  }

  @Override
  public void setPosition(double d0, double d1, double d2) {
    super.setPosition(d0, d1, d2);
    spawnLocation = new Location(world.getWorld(), d0, d1, d2);
  }

  Location spawnLocation = null;

  @Override
  public LbnMobTag getMobTag() {
    return null;
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
