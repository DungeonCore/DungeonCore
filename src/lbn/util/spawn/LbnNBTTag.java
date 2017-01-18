package lbn.util.spawn;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public class LbnNBTTag {
	public LbnNBTTag(Entity bukkitEntity) {
		type = bukkitEntity.getType();
	}

	public EntityType type;

	public boolean isRiding = false;

	public boolean isWaterMonster = false;

	public void setRiding(boolean isRiding) {
		this.isRiding = isRiding;
	}

	public EntityType getType() {
		return type;
	}

	public boolean isRiding() {
		return isRiding;
	}

	public boolean isWaterMonster() {
		return isWaterMonster;
	}

	public void setWaterMonster(boolean isWaterMonster) {
		this.isWaterMonster = isWaterMonster;
	}
}
