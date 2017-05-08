package net.l_bulb.dungeoncore.mob.customMob.abstractmob;

import net.l_bulb.dungeoncore.mob.AbstractCustomMob;
import net.l_bulb.dungeoncore.mob.minecraftEntity.CustomWitch;

import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Witch;

public abstract class AbstractWitch extends AbstractCustomMob<CustomWitch, Witch> {
  @Override
  protected CustomWitch getInnerEntity(World w) {
    return new CustomWitch(w, this);
  }

  @Override
  public EntityType getEntityType() {
    return EntityType.WITCH;
  }

  /**
   * アイテムを持ち変えるならTRUE
   * 
   * @return
   */
  public boolean isChangeItem() {
    return true;
  }

}
