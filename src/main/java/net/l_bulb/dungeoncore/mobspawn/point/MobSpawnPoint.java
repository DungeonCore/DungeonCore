package net.l_bulb.dungeoncore.mobspawn.point;

import java.util.ArrayList;

import org.bukkit.Location;

import net.l_bulb.dungeoncore.mob.AbstractMob;
import net.l_bulb.dungeoncore.mobspawn.ChunkWrapper;

public class MobSpawnPoint {
  ArrayList<AbstractMob<?>> spawnMobList = new ArrayList<>();

  int id;

  Location location;

  ChunkWrapper chunkWrapper;

  public static void main(String[] args) {}
}
