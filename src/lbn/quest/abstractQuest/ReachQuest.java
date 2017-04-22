package lbn.quest.abstractQuest;

import java.util.Iterator;
import java.util.Set;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.google.common.collect.HashMultimap;

public class ReachQuest extends AbstractQuest {
	static HashMultimap<Chunk, ReachQuest> questChunkMap = HashMultimap.create();

	public static Set<ReachQuest> fromChunk(Location loc) {
		Set<ReachQuest> set = questChunkMap.get(loc.getChunk());
		Iterator<ReachQuest> iterator = set.iterator();
		while (iterator.hasNext()) {
			// 指定された座標を取得
			ReachQuest next = iterator.next();
			Location location2 = next.getLocation();

			// yが7以上離れていたらその場所にいないと判断する
			if (Math.abs(location2.getBlockY() - loc.getBlockY()) > 7) {
				iterator.remove();
			}
		}
		return set;
	}

	String locationName;
	Location location;

	public ReachQuest(String id, String locationName, Location loc) {
		super(id);
		this.locationName = locationName;
		this.location = loc;

		questChunkMap.put(loc.getChunk(), this);
	}

	public Location getLocation() {
		return location;
	}

	@Override
	public String getCurrentInfo(Player p) {
		return "達成度(0/1)";
	}

	@Override
	public QuestType getQuestType() {
		return QuestType.REACH_QUEST;
	}

	@Override
	public boolean isComplate(int data) {
		return data == 1;
	}

	@Override
	public String getComplateCondition() {
		return locationName + "(" + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ()
				+ ")へ行こう";
	}

}
