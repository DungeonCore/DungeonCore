package main.common.other;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CommandBlock;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import main.util.DungeonLog;

public class RouteSearcher {
	Block start;
	Player p;

	public RouteSearcher(Block start, Player p) {
		this.start = start;
		this.p = p;
	}

	ArrayList<Location> soilList = new ArrayList<Location>();

	public void startSearchingPath() {
		searchAndSetSoil(start, null);
		DungeonLog.println(sb.toString());

		Location location = p.getLocation();
		location.getBlock().setType(Material.COMMAND);

		CommandBlock block =  (CommandBlock) location.getBlock().getState();
		block.setCommand(sb.toString());
		block.update();
	}

	final SearchDirection[] directionList = SearchDirection.values();

	StringBuilder sb = new StringBuilder();

	private void searchAndSetSoil(Block block, SearchDirection beforeDirection) {
		if (block.getType() != Material.DIAMOND_BLOCK && block.getType() != Material.DIAMOND_ORE) {
			return;
		}



		Location location = block.getLocation();
		if (soilList.contains(location)) {
			return;
		}
		//リストにセットする
		soilList.add(location);
		if (block.getType() == Material.DIAMOND_BLOCK) {
//			DungeonLog.println((int)location.getX() + " " + (int)location.getY() + " " + (int)location.getZ() + " & ");
			sb.append((int)location.getX() + " " + (int)location.getY() + " " + (int)location.getZ() + " & ");
		}

		for (SearchDirection direction : directionList) {
			//前と逆の方向なら何もしない
			if (beforeDirection != null && direction.isInverce(beforeDirection)) {
				continue;
			}
			Location add = block.getLocation().add(direction.getAddVector());
			searchAndSetSoil(add.getBlock(), direction);
		}
	}

	public static RouteSearcher getInstance(Player p) {
		return new RouteSearcher(p.getLocation().add(0, -1, 0).getBlock(), p);
	}

	enum SearchDirection {
		UP(0, 1, 0),
		DOWN(0, -1, 0),
		RIGHT(1, 0, 0),
		LEFT(-1, 0 , 0),
		FRONT(0, 0, 1),
		BACK(0, 0, -1);

		int x;
		int y;
		int z;

		private SearchDirection(int x, int y, int z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}

		public boolean isInverce(SearchDirection d) {
			if (x != -1 * d.x) {
				return false;
			}
			if (y != -1 * d.y) {
				return false;
			}
			if (z != -1 * d.z) {
				return false;
			}
			return true;
		}

		Vector getAddVector() {
			return new Vector(x, y, z);
		}


		static List<SearchDirection> getDirection(SearchDirection exclusionDirection) {
			List<SearchDirection> asList = Arrays.asList(values());

			if (exclusionDirection != null) {
				asList.remove(exclusionDirection);
			}
			return asList;
		}
	}
}
