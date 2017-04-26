package lbn.common.particle;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

public class StraightParticleData extends ParticleData {
  Location center;

  public StraightParticleData(ParticleType particle, Location center) {
    super(particle, 4);
    this.center = center;
    setDispersion(0, 0, 0);
  }

  public StraightParticleData(ParticleData data, Location center) {
    super(data);
    this.center = center;
    setDispersion(0, 0, 0);
    setAmount(4);
  }

  /**
   * 実行する
   */
  @Override
  public void run(Location... locList) {
    for (Location location : locList) {
      straight(center, location);
    }
  }

  /**
   * 実行する
   * 
   * @param entity
   */
  public void run(Entity... entity) {
    for (Entity e : entity) {
      if (e != null && e.isValid()) {
        straight(center, e.getLocation());
      }
    }
  }

  protected void straight(Location center, Location direction) {
    Location l = center.clone();
    Location direction2 = direction.clone();
    // 実行するロケーションのリスト
    ArrayList<Location> runLocList = new ArrayList<Location>();
    center.add(0, 1.2, 0);
    direction2.add(0, 0.5, 0);
    l.add(0, 1.2, 0);
    int _distance = (int) direction2.distance(center) * 20;
    direction2.subtract(center.getX(), center.getY(), center.getZ());
    double d = 1D / _distance;
    double x = direction2.getX() * d;
    double y = direction2.getY() * d;
    double z = direction2.getZ() * d;
    for (int i = 0; i < _distance; i++) {
      runLocList.add(new Location(center.getWorld(), l.getX() + x * i, l.getY() + y * i, l.getZ() + z * i));
    }
    // 実行する
    super.run(runLocList.toArray(new Location[0]));
  }
}
