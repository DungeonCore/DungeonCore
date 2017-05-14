package net.l_bulb.dungeoncore.common.particle;

import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

import net.l_bulb.dungeoncore.util.LbnRunnable;

public class SpringParticleData extends ParticleData {

  double radius;
  double hight;
  // 一ループの高さ
  double hightPeriod;
  long roundTick;

  public SpringParticleData(ParticleData data, double radius, double hight, double hightPeriod, long roundTick) {
    super(data);
    this.radius = radius;
    this.hight = hight;
    this.hightPeriod = hightPeriod;
    this.roundTick = roundTick;
  }

  @Deprecated
  @Override
  public void run(Collection<Location> locList) {
    new UnsupportedOperationException("このメソッドは使用できません。").printStackTrace();
  }

  @Override
  public void run(Location... locList) {
    Location center = locList[0];

    ArrayList<Vector> pointList = CircleParticleCache.getCircleVecList(radius, true);

    // 最大tick数
    long maxTick = (long) (hight / hightPeriod * roundTick);

    // 1tickのパーティクルを出す数
    final int oneTickRunParticleCount = (int) Math.ceil(360 / roundTick);

    new LbnRunnable() {
      int i = 0;
      int allCount = -1;

      @Override
      public void run2() {
        if (maxTick < getAgeTick()) {
          cancel();
        }
        allCount++;

        for (int j = 0; j < oneTickRunParticleCount; j++) {
          if (i >= pointList.size()) {
            i = 0;
            return;
          }
          Vector vector = pointList.get(i);
          runParticle(center.getWorld(), center.getX() + vector.getX(), center.getY() + vector.getY() + hight * allCount / maxTick, center.getZ()
              + vector.getZ());
          i++;
        }

      }
    }.runTaskTimer(1);
  }

  @Deprecated
  @Override
  protected void run(World w, Vector... v) {
    new UnsupportedOperationException("このメソッドは使用できません。").printStackTrace();
  }
}
