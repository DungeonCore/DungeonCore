package lbn.common.particle;

import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

public class CircleParticleData extends ParticleData {

  double radius;

  public CircleParticleData(ParticleData data, double radius) {
    super(data.setDispersion(0, 0, 0));
    this.radius = radius;
  }

  @Override
  public void run(Collection<Location> locList) {
    for (Location location : locList) {
      runCircle(location);
    }
  }

  @Override
  public void run(Location... locList) {
    for (Location location : locList) {
      runCircle(location);
    }
  }

  Vector centerVec = null;

  protected void runCircle(Location center) {
    // center vecを登録する
    centerVec = center.toVector();

    ArrayList<Vector> pointList = CircleParticleCache.getCircleVecList(radius, true);
    run(center.getWorld(), pointList);
  }

  @Override
  protected void runParticle(World w, double x, double y, double z) {
    super.runParticle(w, x + centerVec.getX(), y + centerVec.getY(), z + centerVec.getZ());
  }
}
