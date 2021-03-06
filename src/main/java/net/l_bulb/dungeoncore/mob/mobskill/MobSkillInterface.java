package net.l_bulb.dungeoncore.mob.mobskill;

import org.bukkit.entity.Entity;

public interface MobSkillInterface {
  public MobSkillExcuteTimingType getTiming();

  public MobSkillExcuteConditionType getCondtion();

  public MobSkillTargetingMethodType getTargetingMethod();

  public String getName();

  public int excutePercent();

  public int getLaterTick();

  public void execute(Entity target, Entity mob);

  default public boolean canUseWhenStun() {
    return false;
  }
}
