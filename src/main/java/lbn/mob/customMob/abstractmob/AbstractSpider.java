package lbn.mob.customMob.abstractmob;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Spider;

import lbn.mob.AbstractCustomMob;
import lbn.mob.minecraftEntity.CustomSpider;

public abstract class AbstractSpider extends AbstractCustomMob<CustomSpider, Spider> {

  @Override
  protected Spider spawnPrivate(Location loc) {
    return super.spawnPrivate(loc);
  }

  @Override
  public EntityType getEntityType() {
    return EntityType.SPIDER;
  }

  @Override
  protected CustomSpider getInnerEntity(World w) {
    return new CustomSpider(w, getLbnMobTag());
  }

}
