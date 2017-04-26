package lbn.mob.mobskill;

import org.bukkit.entity.Entity;

public interface MobSkillInterface {
  public MobSkillExcuteTimingType getTiming();

  public MobSkillExcuteConditionType getCondtion();

  public MobSkillTargetingMethodType getTargetingMethod();

  public String getName();

  public int excutePercent();

  public void execute(Entity target, Entity mob);
}
