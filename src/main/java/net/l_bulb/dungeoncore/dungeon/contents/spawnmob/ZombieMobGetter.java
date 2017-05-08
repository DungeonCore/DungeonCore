package net.l_bulb.dungeoncore.dungeon.contents.spawnmob;

import java.util.ArrayList;
import java.util.List;

import net.l_bulb.dungeoncore.dungeon.contents.mob.NormalMob;
import net.l_bulb.dungeoncore.mob.AbstractMob;
import net.l_bulb.dungeoncore.mobspawn.gettter.AbstractSpawnMobGetter;

import org.bukkit.entity.EntityType;

public class ZombieMobGetter extends AbstractSpawnMobGetter {

  @Override
  public String getName() {
    return "zombie";
  }

  static ArrayList<AbstractMob<?>> mobList = new ArrayList<AbstractMob<?>>();
  static {
    mobList.add(new NormalMob(EntityType.ZOMBIE));
  }

  @Override
  public List<AbstractMob<?>> getAllMobList() {
    return mobList;
  }

}
