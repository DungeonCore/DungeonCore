package lbn.common.particle;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.util.Vector;

class CircleParticleCache {
  static HashMap<Double, ArrayList<Vector>> cache = new HashMap<Double, ArrayList<Vector>>();

  public static ArrayList<Vector> getCircleVecList(double radius, boolean saveCache) {
    int stepSize = 1;
    if (radius <= 5) {
      stepSize = 2;
    }
    return getCircleVecList(radius, saveCache, stepSize);
  }

  public static ArrayList<Vector> getCircleVecList(double radius, boolean saveCache, double stepSize) {
    // キャッシュが存在しない場合は作成する
    if (!cache.containsKey(radius)) {
      ArrayList<Vector> point = new ArrayList<Vector>();
      for (float i = 0; i < 360; i = (float) (i += stepSize)) {
        point.add(new Vector(Math.sin(Math.toRadians(i)) * radius, 0.5, Math.cos(Math.toRadians(i)) * radius));
      }

      if (saveCache) {
        cache.put(radius, point);
      }
    }

    ArrayList<Vector> pointList = cache.get(radius);
    return pointList;
  }
}
