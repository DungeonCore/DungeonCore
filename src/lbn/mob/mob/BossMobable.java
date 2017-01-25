package lbn.mob.mob;

import java.util.Set;

import lbn.chest.BossChest;
import lbn.player.TheLowPlayer;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

public interface BossMobable{
	public BossChest getBossChest();

	public LivingEntity getEntity();

	public EntityType getEntityType();

	public void updateName(boolean addDamage);

	public String getName();

	public Set<TheLowPlayer> getCombatPlayer();

	public void setEntity(LivingEntity e);
}
