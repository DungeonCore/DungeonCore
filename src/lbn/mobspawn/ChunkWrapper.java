package lbn.mobspawn;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;

import lbn.mobspawn.point.MobSpawnerPoint;

public class ChunkWrapper {
	public ChunkWrapper(String wolrdName, int x, int z) {
		this.wolrdName = wolrdName;
		this.x = x;
		this.z = z;
	}

	String wolrdName;
	int x;
	int z;

	//*16 + 8 y *16+8
	public ChunkWrapper(Chunk c) {
		wolrdName = c.getWorld().getName();
		x = c.getX();
		z = c.getZ();
	}



	public ChunkWrapper(MobSpawnerPoint point) {
		this(point.getChunk());
	}

	Location center = null;

	public Chunk getChunk() {
		if (center == null) {
			center = new Location(Bukkit.getWorld(wolrdName), x * 16 + 8, 64, z * 16 + 8);
		}
		return center.getChunk();
	}

	public boolean isLoaded() {
		return getChunk().isLoaded();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((wolrdName == null) ? 0 : wolrdName.hashCode());
		result = prime * result + x;
		result = prime * result + z;
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
		ChunkWrapper other = (ChunkWrapper) obj;
		if (wolrdName == null) {
			if (other.wolrdName != null)
				return false;
		} else if (!wolrdName.equals(other.wolrdName))
			return false;
		if (x != other.x)
			return false;
		if (z != other.z)
			return false;
		return true;
	}

}
