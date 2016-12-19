package main;

import java.util.Collection;
import java.util.List;

import main.lbn.LbnRuntimeException;
import main.mob.AbstractMob;
import main.mob.MobHolder;
import main.mob.mob.abstractmob.villager.AbstractVillager;
import main.util.DungeonLog;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import com.google.common.collect.HashMultimap;

public class VillagerChunkManager {
	public static HashMultimap<ChunkInfo, AbstractVillager> chunkVillagerList = HashMultimap.create();

	public static void onLoadChank(Chunk c) {

		ChunkInfo pair = new ChunkInfo(c);
		//すでに処理済みなら何もしない
		if (!chunkVillagerList.containsKey(pair)) {
			return;
		}

		sendDebug(c, chunkVillagerList.get(pair).size() + " villagers will be spawned");

		//全Entityを取得
		Entity[] entities = c.getEntities();
		for (Entity entity : entities) {
			//村人でなければ無視する
			if (entity.getType() != EntityType.VILLAGER) {
				continue;
			}
			entity.remove();
		}
		sendDebug(c, chunkVillagerList.get(pair).toString());
		for (AbstractVillager mob : chunkVillagerList.get(pair)) {
			sendDebug(c, mob.getName());
			try {
				if (((AbstractVillager)mob).getLocation() != null) {
					sendDebug(c, "spawn:" + mob.getName());
					mob.spawn(((AbstractVillager)mob).getLocation());
				} else {
					//起こり得ないがが念のため
					new LbnRuntimeException("villager location is null").printStackTrace();
					continue;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		//一回処理をしたら取り除く
		chunkVillagerList.removeAll(pair);

	}

	public static void setAllRegistedVillager() {
		Collection<AbstractMob<?>> allMobs = MobHolder.getAllMobs();
		for (AbstractMob<?> abstractMob : allMobs) {
			if (abstractMob instanceof AbstractVillager) {
				//chunkごとに保管しておく
				Location location2 = ((AbstractVillager)abstractMob).getLocation();
				if (location2 != null) {
					chunkVillagerList.put(new ChunkInfo(location2.getChunk()), (AbstractVillager) abstractMob);
				}

			}
		}
	}

	public static void onPluginLoad() {
		DungeonLog.printDevelopln("villager system start: " + chunkVillagerList.size() + "chunk");
		//Loadされている全てのチャンクに対して処理を行う
		List<World> worlds = Bukkit.getWorlds();
		for (World world : worlds) {
//			DungeonLog.printDevelopln("villager system load world:" + world.getName());
			Chunk[] loadedChunks = world.getLoadedChunks();
			for (Chunk chunk : loadedChunks) {
				try {
					onLoadChank(chunk);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	protected static void sendDebug(Chunk c, String msg) {
		if (c.getWorld().getName().contains("newmap")) {
//			DungeonLog.printDevelopln("only newmap:" + msg);
		}
	}
}

class ChunkInfo {
	int x;
	int z;
	String name;
	public ChunkInfo(Chunk c) {
		this.x = c.getX();
		this.z = c.getZ();
		this.name = c.getWorld().getName();
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		ChunkInfo other = (ChunkInfo) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (x != other.x)
			return false;
		if (z != other.z)
			return false;
		return true;
	}

}