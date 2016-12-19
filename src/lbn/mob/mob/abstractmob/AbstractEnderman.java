package lbn.mob.mob.abstractmob;

import org.bukkit.entity.Enderman;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityTeleportEvent;

import lbn.common.event.EndermanFindTargetEvent;
import lbn.mob.AbstractCustomMob;
import lbn.mob.customEntity1_7.CustomEnderman;

public abstract class AbstractEnderman extends AbstractCustomMob<CustomEnderman, Enderman>{

	@Override
	public EntityType getEntityType() {
		return EntityType.ENDERMAN;
	}

	@Override
	abstract public void onTeleport(EntityTeleportEvent e);

	abstract public void onFindPlayer(EndermanFindTargetEvent e);


}
