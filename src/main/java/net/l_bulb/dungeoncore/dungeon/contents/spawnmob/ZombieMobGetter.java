package net.l_bulb.dungeoncore.dungeon.contents.spawnmob;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.EntityType;

import net.l_bulb.dungeoncore.dungeon.contents.mob.NormalMob;
import net.l_bulb.dungeoncore.mob.AbstractMob;
import net.l_bulb.dungeoncore.mobspawn.gettter.AbstractSpawnMobGetter;

public class ZombieMobGetter extends AbstractSpawnMobGetter {

  @Override
  public String getName() {
    return "zombie";
  }

  static ArrayList<AbstractMob<?>> mobList = new ArrayList<>();
  static {
    mobList.add(new NormalMob(EntityType.ZOMBIE));
  }

  @Override
  public List<AbstractMob<?>> getAllMobList() {
    return mobList;
  }

}
