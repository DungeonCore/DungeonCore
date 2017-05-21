package net.l_bulb.dungeoncore.mobspawn.old.gettter;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

import net.l_bulb.dungeoncore.mob.AbstractMob;
import net.l_bulb.dungeoncore.mob.MobHolder;
import net.l_bulb.dungeoncore.mobspawn.old.SpawnLevel;
import net.l_bulb.dungeoncore.mobspawn.old.point.MobSpawnerPoint;
import net.l_bulb.dungeoncore.mobspawn.old.point.SpletSheetMobSpawnerPoint;

public class SpletSheetSpawnMobGetter extends AbstractSpawnMobGetter {

  public SpletSheetSpawnMobGetter(int id) {
    this.id = id;
  }

  public ArrayList<AbstractMob<?>> moblist = new ArrayList<>();

  protected int id;

  @Override
  public String getName() {
    return Integer.toString(id);
  }

  /**
   * 追加に成功したらTRUE
   *
   * @param name
   * @return
   */
  public boolean addMob(String name) {
    AbstractMob<?> mobWithNormal = MobHolder.getMobWithNormal(name);
    if (mobWithNormal != null) {
      moblist.add(mobWithNormal);
      return true;
    } else {
      return false;
    }
  }

  @Override
  public List<AbstractMob<?>> getAllMobList() {
    return moblist;
  }

  @Override
  public MobSpawnerPoint getMobSpawnerPoint(Location loc, int maxMobCount,
      SpawnLevel level) {
    return new SpletSheetMobSpawnerPoint(id, loc, this, maxMobCount, level);
  }

}
