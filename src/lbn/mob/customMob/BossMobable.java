package lbn.mob.customMob;

import java.util.Set;

import lbn.api.player.TheLowPlayer;
import lbn.chest.BossChest;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public interface BossMobable{
	public BossChest getBossChest();

	public LivingEntity getEntity();

	public EntityType getEntityType();

	public void updateName(boolean addDamage);

	public String getName();

	public Set<TheLowPlayer> getCombatPlayer();

	public boolean isCombatPlayer(Player e);

	public void setEntity(LivingEntity e);
}
