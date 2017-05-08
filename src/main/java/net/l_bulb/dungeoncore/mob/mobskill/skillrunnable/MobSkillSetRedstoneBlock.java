package net.l_bulb.dungeoncore.mob.mobskill.skillrunnable;

import net.l_bulb.dungeoncore.dungeoncore.SpletSheet.AbstractSheetRunable;
import net.l_bulb.dungeoncore.mob.mobskill.MobSkillRunnable;
import net.l_bulb.dungeoncore.util.BlockUtil;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

public class MobSkillSetRedstoneBlock extends MobSkillRunnable {

  public MobSkillSetRedstoneBlock(String data) {
    super(data);
  }

  @Override
  public void execute(Entity target, Entity mob) {
    Location loc = AbstractSheetRunable.getLocationByString(data);
    if (loc != null) {
      BlockUtil.setRedstone(loc);
    }
  }

}
