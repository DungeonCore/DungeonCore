package lbn.common.other;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.gmail.filoghost.holographicdisplays.api.Hologram;

public class DungeonData {
	String dungeonName;
	Location dungeonLoc;
	String difficulty;
	int id;
	Hologram hologram;

	static public final String DIFFICULTY_VERY_EASY = "very_easy";
	static public final String DIFFICULTY_EASY = "easy";
	static public final String DIFFICULTY_NORMAL = "normal";
	static public final String DIFFICULTY_HARD = "hard";
	static public final String DIFFICULTY_VERY_HARD = "very_hard";
	static public final String DIFFICULTY_IMPOSSIBLE = "impossible";

	public DungeonData(String dungeonName, Location startLoc, String difficulty, int id, Location entranceLoc) {
		super();
		this.dungeonName = dungeonName;
		this.dungeonLoc = startLoc;
		this.difficulty = difficulty;
		this.id = id;
	}

	public String getDungeonName() {
		return dungeonName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Location getDungeonLoc() {
		return dungeonLoc;
	}

	public String getDifficulty() {
		return difficulty;
	}

	public Hologram getHologram() {
		return hologram;
	}

	public void setHologram(Hologram hologram) {
		this.hologram = hologram;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((difficulty == null) ? 0 : difficulty.hashCode());
		result = prime * result + ((dungeonLoc == null) ? 0 : dungeonLoc.hashCode());
		result = prime * result + ((dungeonName == null) ? 0 : dungeonName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DungeonData other = (DungeonData) obj;
		if (difficulty == null) {
			if (other.difficulty != null)
				return false;
		} else if (!difficulty.equals(other.difficulty))
			return false;
		if (dungeonLoc == null) {
			if (other.dungeonLoc != null)
				return false;
		} else if (!dungeonLoc.equals(other.dungeonLoc))
			return false;
		if (dungeonName == null) {
			if (other.dungeonName != null)
				return false;
		} else if (!dungeonName.equals(other.dungeonName))
			return false;
		return true;
	}

	public void sendInfo(Player p) {
		p.sendMessage("");
		p.sendMessage(ChatColor.GREEN + dungeonName + ",  " + difficulty);
	}
}
