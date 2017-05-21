package net.l_bulb.dungeoncore.mobspawn.old.point;

import org.bukkit.Location;

import net.l_bulb.dungeoncore.mobspawn.old.SpawnLevel;
import net.l_bulb.dungeoncore.mobspawn.old.gettter.SpawnMobGetterInterface;

public class SpletSheetMobSpawnerPoint extends MobSpawnerPoint {

  protected int id;

  public SpletSheetMobSpawnerPoint(int id, Location loc,
      SpawnMobGetterInterface mobGetter, int maxMobCount, SpawnLevel level) {
    super(loc, mobGetter, maxMobCount, level);
    this.id = id;
  }

  @Override
  public int getId() {
    return id;
  }
}
