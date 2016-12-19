package main.player.playerIO;

import org.bukkit.entity.Player;

public class PlayerIODataLevel70 extends PlayerIOData{

	public PlayerIODataLevel70(Player p) {
		super(p);

		swordLevel = 70;
		bowLevel = 70;
		magicLevel = 70;
		galions = Integer.MAX_VALUE;
		saveType = "l70";
	}

	@Override
	protected boolean isSave() {
		return false;
	}

	@Override
	public boolean save(Player p) {
		return true;
	}
}
