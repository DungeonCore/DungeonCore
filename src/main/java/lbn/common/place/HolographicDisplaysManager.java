package lbn.common.place;

import java.util.Collection;

import org.bukkit.Location;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;

import lbn.dungeoncore.Main;

public class HolographicDisplaysManager {
  private static boolean useHolographicDisplays = false;

  public static boolean isUseHolographicDisplays() {
    return useHolographicDisplays;
  }

  public static void setUseHolographicDisplays(boolean useHolographicDisplays) {
    HolographicDisplaysManager.useHolographicDisplays = useHolographicDisplays;
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
