package main.mob.customEntity;

import org.bukkit.entity.Player;

public interface ICustomEntitySummonable {

	public void setSummon(boolean isSummon, Player owner);

	public boolean isSummon();

}
