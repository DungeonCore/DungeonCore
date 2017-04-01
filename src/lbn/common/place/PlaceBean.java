package lbn.common.place;

import lbn.dungeoncore.SpletSheet.AbstractSheetRunable;
import lbn.util.JavaUtil;

import org.bukkit.Location;

public class PlaceBean {
	public int id;

	public String name;

	public PlaceType type;

	public Location tpLocation;

	public Location entranceLocation;

	public Location dungeonStartLocation;

	public int level;

	public int getId() {
		return id;
	}

	public void setId(String id) {
		this.id = JavaUtil.getInt(id, -1);
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public PlaceType getType() {
		return type;
	}

	public void setType(String type) {
		this.type = PlaceType.getInstance(type);
	}

	public Location getTpLocation() {
		return tpLocation;
	}

	public void setTpLocation(String tpLocation) {
		this.tpLocation = AbstractSheetRunable.getLocationByString(tpLocation);
	}

	public Location getEntranceLocation() {
		return entranceLocation;
	}

	public void setEntranceLocation(String getEntranceLocation) {
		this.entranceLocation = AbstractSheetRunable.getLocationByString(getEntranceLocation);
	}

	public Location getDungeonStartLocation() {
		return dungeonStartLocation;
	}

	public void setDungeonStartLocation(String dungeonStartLocation) {
		this.dungeonStartLocation = AbstractSheetRunable.getLocationByString(dungeonStartLocation);
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = JavaUtil.getInt(level, 0);
	}

	public boolean isError() {
		return name == null || id == -1;
	}

}
