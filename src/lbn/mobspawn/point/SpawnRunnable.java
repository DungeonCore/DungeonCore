package lbn.mobspawn.point;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

import lbn.SystemListener;
import lbn.mobspawn.ChunkWrapper;
import lbn.mobspawn.SpawnLevel;

import org.apache.commons.lang.StringUtils;
import org.bukkit.scheduler.BukkitRunnable;

public class SpawnRunnable extends BukkitRunnable{
	public SpawnRunnable(boolean isRunManage,
			HashMap<SpawnLevel, SpawnScheduler> schedulerList,
			HashSet<ChunkWrapper> loadedChunk) {
		this.schedulerList = schedulerList;
		this.loadedChunk = loadedChunk;
	}

	private HashMap<SpawnLevel, SpawnScheduler> schedulerList;
	private HashSet<ChunkWrapper> loadedChunk;

	public HashMap<SpawnLevel, String> schedulerDetail = new HashMap<>();

	@Override
	public void run() {
		if (SystemListener.loginPlayer == 0) {
			for (SpawnLevel level : SpawnLevel.values()) {
				schedulerDetail.put(level, "FALSE:not player");
			}
			return;
		}

		//全てのレベルを走査する
		for (SpawnLevel level : SpawnLevel.values()) {
			SpawnScheduler scheduler = schedulerList.get(level);
			if (scheduler == null) {
				schedulerDetail.put(level, "FALSE:scheduler is not found");
				continue;
			}

			if (scheduler.spawnPointList.isEmpty()) {
				schedulerDetail.put(level, "FALSE:spawnPoint is not found");
				continue;
			}

			int spawnMpbCount = 0;
			int spawnPointCount = 0;

			ArrayList<MobSpawnerPoint> spawnPointList = scheduler.getCurrentSpawnPoint();
			for (MobSpawnerPoint spawnPoint : spawnPointList) {
//				HashSet<String> maxSpawnedName = new HashSet<String>();
				//指定されたスポーンポイントがロードされたチャンクに含まれていたらそのチャンク内のモブを調べる
				if (loadedChunk.contains(new ChunkWrapper(spawnPoint))) {
					spawnPointCount++;
					//おそらくいらないので一旦コメントアウト
//					//すでに限界に達していたらスポーンしない
//					if (maxSpawnedName.contains(spawnPoint.getName())) {
//						continue;
//					}
					//mobをスポーンさせる
//					int spawnCount =
					spawnMpbCount += spawnPoint.spawnMob();
					//おそらくいらないので一旦コメントアウト
//					//スポーンしたMobが０ならこれ以上スポーンしないと考える
//					if (spawnCount == 0) {
//						maxSpawnedName.add(spawnPoint.getName());
//					}
				}
			}
			schedulerDetail.put(level, StringUtils.join(new Object[]{"TRUE:spawnpoint(", spawnPointCount, "), spawnmob(", spawnMpbCount, ")"}));
			Collections.shuffle(spawnPointList);
		}
	}


}
