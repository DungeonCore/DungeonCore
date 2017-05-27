package net.l_bulb.dungeoncore.mob.mobskill.skillrunnable;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

import net.l_bulb.dungeoncore.dungeoncore.SpletSheet.AbstractSheetRunable;
import net.l_bulb.dungeoncore.mob.mobskill.MobSkillRunnable;

public class MobSkillTeleportPlayerToPoint extends MobSkillRunnable {

  private Location location;

  public MobSkillTeleportPlayerToPoint(String data) {
    super(data);
    location = AbstractSheetRunable.getLocationByString(data);
  }

  @Override
  public void execute(Entity target, Entity mob) {
    if (location == null) { return; }
    target.teleport(location);
  }

}
