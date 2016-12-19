package main.mob.mob;

import java.util.List;

import org.bukkit.entity.LivingEntity;

public interface SaveMobEntity {
	public List<LivingEntity> getAliveEntity();
	public void clearAllMob();
}
