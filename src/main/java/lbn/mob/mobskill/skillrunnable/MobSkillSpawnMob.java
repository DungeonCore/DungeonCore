package lbn.mob.mobskill.skillrunnable;

public class MobSkillSpawnMob extends MobSkillSpawnMob2 {

  public MobSkillSpawnMob(String data) {
    super(data);
  }

  @Override
  protected int getCount() {
    return 1;
  }
}
