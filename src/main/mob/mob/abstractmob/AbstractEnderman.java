package main.mob.mob.abstractmob;

import main.common.event.EndermanFindTargetEvent;
import main.mob.AbstractCustomMob;
import main.mob.customEntity1_7.CustomEnderman;

import org.bukkit.entity.Enderman;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityTeleportEvent;

public abstract class AbstractEnderman extends AbstractCustomMob<CustomEnderman, Enderman>{

	@Override
	public EntityType getEntityType() {
		return EntityType.ENDERMAN;
	}

	@Override
	abstract public void onTeleport(EntityTeleportEvent e);

	abstract public void onFindPlayer(EndermanFindTargetEvent e);


}
