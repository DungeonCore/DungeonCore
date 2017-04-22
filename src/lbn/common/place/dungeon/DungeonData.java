package lbn.common.place.dungeon;

import org.bukkit.ChatColor;
import org.bukkit.Location;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;

import lbn.common.place.HolographicDisplaysManager;
import lbn.common.place.PlaceBean;
import lbn.common.place.PlaceInterface;
import lbn.common.place.PlaceType;
import lbn.dungeoncore.Main;

public class DungeonData implements PlaceInterface {

	public DungeonData(int id, String name) {
		PlaceBean placeBean = new PlaceBean();
		placeBean.setId(id);
		placeBean.setName(name);

		this.bean = placeBean;
	}

	private PlaceBean bean;

	Hologram hologram;

	public DungeonData(PlaceBean bean) {
		this.bean = bean;
	}

	@Override
	public int getId() {
		return bean.getId();
	}

	@Override
	public String getName() {
		return bean.getName();
	}

	@Override
	public Location getTeleportLocation() {
		return bean.getTpLocation();
	}

	@Override
	public void enable() {
		if (HolographicDisplaysManager.isUseHolographicDisplays()) {
			Location location = getEntranceLocation();
			if (location != null) {
				if (!location.getChunk().isLoaded()) {
					location.getChunk().load();
				}

				Hologram hologram = HologramsAPI.createHologram(Main.plugin, location);
				hologram.appendTextLine(ChatColor.AQUA + ChatColor.BOLD.toString() + getName());
				hologram.appendTextLine(ChatColor.GOLD + "DIFFICULTY : " + getLevel() + "レベル");
				this.hologram = hologram;
			}
		}
	}

	@Override
	public void disenable() {
		if (hologram != null) {
			hologram.delete();
		}
	}

	/**
	 * 対象のレベル
	 * 
	 * @return
	 */
	public int getLevel() {
		return bean.getLevel();
	}

	/**
	 * ダンジョンの入口の座標
	 * 
	 * @return
	 */
	public Location getEntranceLocation() {
		return bean.getEntranceLocation();
	}

	/**
	 * ダンジョンのスタート地点の座標
	 * 
	 * @return
	 */
	public Location getStartLocation() {
		return bean.getDungeonStartLocation();
	}

	@Override
	public PlaceType getPlaceType() {
		return PlaceType.DUNGEON;
	}

}
