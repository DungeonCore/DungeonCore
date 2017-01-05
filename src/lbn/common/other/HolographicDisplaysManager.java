package lbn.common.other;

import java.util.Collection;

import lbn.dungeoncore.Main;

import org.bukkit.ChatColor;
import org.bukkit.Location;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;

public class HolographicDisplaysManager {
	private static boolean useHolographicDisplays  = false;

	public static boolean isUseHolographicDisplays() {
		return useHolographicDisplays;
	}

	public static void setUseHolographicDisplays(boolean useHolographicDisplays) {
		HolographicDisplaysManager.useHolographicDisplays = useHolographicDisplays;
	}

	public static void addDungeon(DungeonData dungeonData) {
		if (dungeonData.dungeonLoc == null) {
			return;
		}
		if (isUseHolographicDisplays()) {
			if (!dungeonData.dungeonLoc.getChunk().isLoaded()) {
				dungeonData.dungeonLoc.getChunk().load();
			}

			Hologram hologram = HologramsAPI.createHologram(Main.plugin, dungeonData.dungeonLoc);
			hologram.appendTextLine(ChatColor.AQUA  + ChatColor.BOLD.toString() + dungeonData.getDungeonName());
			hologram.appendTextLine(ChatColor.GOLD + "DIFFICULTY : " + dungeonData.difficulty.toUpperCase());
			dungeonData.setHologram(hologram);
		}
	}

	public static void removeDungeon(DungeonData dungeonData) {
		if (dungeonData.dungeonLoc == null) {
			return;
		}
		if (isUseHolographicDisplays()) {
			if (!dungeonData.dungeonLoc.getChunk().isLoaded()) {
				dungeonData.dungeonLoc.getChunk().load();
			}
			Hologram hologram = dungeonData.getHologram();
			if (hologram != null) {
				hologram.delete();
			}
		}
	}

	public static void removeAllHologram() {
		if (isUseHolographicDisplays()) {
			Collection<Hologram> holograms = HologramsAPI.getHolograms(Main.plugin);
			for (Hologram hologram : holograms) {
				Location location = hologram.getLocation();
				if (!location.getChunk().isLoaded()) {
					location.getChunk().load();
				}
				hologram.delete();
			}
		}
	}
}
