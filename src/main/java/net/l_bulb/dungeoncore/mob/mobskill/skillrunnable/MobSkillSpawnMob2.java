package net.l_bulb.dungeoncore.mob.mobskill.skillrunnable;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import net.l_bulb.dungeoncore.dungeon.contents.mob.NormalMob;
import net.l_bulb.dungeoncore.dungeoncore.SpletSheet.MobSheetRunnable;
import net.l_bulb.dungeoncore.mob.AbstractMob;
import net.l_bulb.dungeoncore.mob.MobHolder;
import net.l_bulb.dungeoncore.mob.mobskill.MobSkillRunnable;

public class MobSkillSpawnMob2 extends MobSkillRunnable {

  public MobSkillSpawnMob2(String data) {
    super(data);
  }

  Random rnd = new Random();

  long mobLastUpdate = -1;

  ArrayList<AbstractMob<?>> customMobList = new ArrayList<>();

  NormalMob normalMob = new NormalMob(EntityType.ZOMBIE);

  @Override
  public void execute(Entity target, Entity mob) {
    int count = getCount();

    // initした後にモブを更新してたらinitする
    long lastUpdate = new MobSheetRunnable(Bukkit.getConsoleSender()).getLastUpdate();
    if (lastUpdate > mobLastUpdate) {
      init(lastUpdate);
    }

    if (customMobList.size() == 0) {
      for (int i = 0; i < count; i++) {
        normalMob.spawn(mob.getLocation());
      }
    } else {
      for (int i = 0; i < count; i++) {
        customMobList.get(rnd.nextInt(customMobList.size())).spawn(mob.getLocation());
      }
    }
  }

  protected int getCount() {
    return 3;
  }

  protected void init(long lastUpdate) {
    if (data != null) {
      for (String mobName : data.split(",")) {
        AbstractMob<?> mobWithNormal = MobHolder.getMobWithNormal(mobName);
        if (mobWithNormal != null) {
          customMobList.add(mobWithNormal);
        }
      }
    }
    mobLastUpdate = lastUpdate;
  }

}
