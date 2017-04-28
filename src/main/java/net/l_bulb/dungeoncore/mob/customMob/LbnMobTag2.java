package net.l_bulb.dungeoncore.mob.customMob;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public class LbnMobTag2 {
  public LbnMobTag2(Entity bukkitEntity) {
    type = bukkitEntity.getType();
  }

  public LbnMobTag2(EntityType type) {
    this.type = type;
  }

  EntityType type;

  int level = 0;

  double attack = -1;

  double hp = 0;

  boolean isRiding = false;

  public EntityType getType() {
    return type;
  }

  public int getLevel() {
    return level;
  }

  public void setLevel(int level) {
    this.level = level;
  }

  public double getAttack() {
    return attack;
  }

  public void setAttack(double attack) {
    this.attack = attack;
  }

  public double getHp() {
    return hp;
  }

  public void setHp(double hp) {
    this.hp = hp;
  }

  public void setRiding(boolean isRiding) {
    this.isRiding = isRiding;
  }

  public boolean isRiding() {
    return isRiding;
  }

  public LbnMobTag getLbnMobTag() {
    LbnMobTag lbnMobTag = new LbnMobTag(getType());
    lbnMobTag.setLevel(getLevel());
    lbnMobTag.setRiding(isRiding());
    return lbnMobTag;
  }

}
