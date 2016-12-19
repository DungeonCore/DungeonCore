package lbn.dungeon.contents.mob.villager.other;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class BlackSmith2 extends Blacksmith{
	@Override
	public String getName() {
		return "Ms.Blacksmith(鍛冶屋)";
	}

	@Override
	public Location getLocation() {
		return new Location(Bukkit.getWorld("world"), 127, 67, 855);
	}
}
