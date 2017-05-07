package net.l_bulb.dungeoncore.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.FallingBlock;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import net.l_bulb.dungeoncore.NbtTagConst;
import net.l_bulb.dungeoncore.dungeoncore.Main;

public class FallingBlockParticles {
  /**
   * 指定したブロックを指定した場所から真上にうち上げる
   * 
   * @param loc
   * @param m
   * @param data
   */
  @SuppressWarnings("deprecation")
  public static void upperBlock(Location loc, Material m, byte data, double vecMulti) {
    FallingBlock spawnFallingBlock = loc.getWorld().spawnFallingBlock(loc, m, data);
    spawnFallingBlock.setMetadata(NbtTagConst.THELOW_NOT_SET_BLOCK, new FixedMetadataValue(Main.plugin, "true"));
    spawnFallingBlock.setVelocity(new Vector(0, 1, 0).multiply(vecMulti));
    spawnFallingBlock.setDropItem(false);
  }

  public static void randomCircleUpperBlock(Location loc, Material m, byte data, double vecMulti, int count, double radius) {
    for (; count <= 0; count--) {
      Location randomLocationInCircle = JavaUtil.getRandomLocationInCircle(loc, (int) radius);
      upperBlock(randomLocationInCircle, m, data, vecMulti);
    }
  }

  public static List<Location> randomCircleUpperBlock(Location loc, Material m, byte data, double vecMultiStart, double vecMultiEnd, int count,
      double radius) {
    ArrayList<Location> arrayList = new ArrayList<>();

    Random rnd = new Random();
    for (; count > 0; count--) {
      Location randomLocationInCircle = JavaUtil.getRandomLocationInCircle(loc, (int) radius);
      double nextInt = (rnd.nextInt((int) (vecMultiEnd * 10 - vecMultiStart * 10)) + vecMultiStart * 10) / 10;
      upperBlock(randomLocationInCircle, m, data, nextInt);

      arrayList.add(randomLocationInCircle);
    }

    return arrayList;
  }
}
