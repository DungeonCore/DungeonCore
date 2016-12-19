package main.chest;

import java.util.HashSet;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

public abstract class WhiteListChest extends EachPlayerContentChest{

	public WhiteListChest(Location chestLoc, Location contentLoc,
			int refuelTick, Location moveLoc, int minItemCount,
			int maxItemCount, int movetick, boolean random) {
		super(chestLoc, contentLoc, refuelTick, moveLoc, minItemCount, maxItemCount,
				movetick, random);
		loc = chestLoc;
	}

	Location loc;


	@Override
	public String getName() {
		return "whiteListChest";
	}

	public Location getChestLocation () {
		return loc;
	}

	abstract public HashSet<UUID> getHashSet();

	@Override
	public boolean canOpen(Player p, Block block, PlayerInteractEvent e) {
		return getHashSet().contains(p.getUniqueId());
	}
}
