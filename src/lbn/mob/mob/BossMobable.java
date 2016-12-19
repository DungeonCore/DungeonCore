package lbn.mob.mob;

import java.util.Set;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import lbn.chest.BossChest;

public interface BossMobable{
	public BossChest getBossChest();

	public LivingEntity getEntity();

	public EntityType getEntityType();

	public void updateName(boolean addDamage);

	public String getName();

	public Set<Player> getCombatPlayer();

	public void setEntity(LivingEntity e);
}
