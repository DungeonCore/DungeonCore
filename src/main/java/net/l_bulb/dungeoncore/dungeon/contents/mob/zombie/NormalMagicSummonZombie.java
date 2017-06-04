package net.l_bulb.dungeoncore.dungeon.contents.mob.zombie;

public class NormalMagicSummonZombie extends AbstractSummonZombie {
  int availableLevel;
  int strengthLevel;

  public NormalMagicSummonZombie(int availableLevel, int strengthLevel) {
    this.availableLevel = availableLevel;
    this.strengthLevel = strengthLevel;
  }

  @Override
  public int getDeadlineTick() {
    return (int) (10 + (availableLevel / 10.0) * 1.3 + strengthLevel * 2.7);
  }

  @Override
  public double getNearingSpeed() {
    return 1.0 + availableLevel * 0.1;
  }
}
