package net.l_bulb.dungeoncore.dungeon.contents.item.shootbow;

import net.l_bulb.dungeoncore.dungeon.contents.strength_template.NormalWeaponStrengthTemplate;
import net.l_bulb.dungeoncore.dungeon.contents.strength_template.StrengthTemplate;
import net.l_bulb.dungeoncore.item.customItem.attackitem.old.BowItemOld;

public abstract class LevelBow extends BowItemOld {
  @Override
  abstract protected String[] getStrengthDetail2(int level);

  @Override
  public StrengthTemplate getStrengthTemplate() {
    return new NormalWeaponStrengthTemplate(getAvailableLevel(), getMaxStrengthCount());
  }

}
