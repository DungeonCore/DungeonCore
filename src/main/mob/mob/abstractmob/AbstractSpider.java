package main.mob.mob.abstractmob;

import main.mob.AbstractCustomMob;
import main.mob.customEntity1_7.CustomSpider;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Spider;

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
		return new CustomSpider(w);
	}

}
