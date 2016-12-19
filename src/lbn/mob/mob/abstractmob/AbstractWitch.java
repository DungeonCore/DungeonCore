package lbn.mob.mob.abstractmob;

import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Witch;

import lbn.mob.AbstractCustomMob;
import lbn.mob.customEntity1_7.CustomWitch;

public abstract class AbstractWitch extends AbstractCustomMob<CustomWitch, Witch>{
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
	 * @return
	 */
	public boolean isChangeItem() {
		return true;
	}

}
