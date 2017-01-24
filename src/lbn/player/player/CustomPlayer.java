package lbn.player.player;

import java.util.HashMap;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import lbn.common.other.DungeonData;
import lbn.common.other.DungeonList;
import lbn.player.TheLowLevelType;
import lbn.player.TheLowPlayer;

public class CustomPlayer implements TheLowPlayer{
	HashMap<TheLowLevelType, Integer> theLowLevelMap = new HashMap<TheLowLevelType, Integer>();
	HashMap<TheLowLevelType, Integer> theLowExpMap = new HashMap<TheLowLevelType, Integer>();

	int galions = 0;

	OfflinePlayer player;

	int inDungeonId = -1;

	@Override
	public int getTheLowLevel(TheLowLevelType type) {
		if (type == TheLowLevelType.MAIN) {
			return (int) ((getTheLowLevel(TheLowLevelType.SWORD) + getTheLowLevel(TheLowLevelType.BOW) + getTheLowLevel(TheLowLevelType.MAGIC))/ 3.0);
		}
		return theLowLevelMap.get(type);
	}

	@Override
	public int getTheLowExp(TheLowLevelType type) {
		if (type == TheLowLevelType.MAIN) {
			return 0;
		}
		return theLowExpMap.get(type);
	}

	@Override
	public void setTheLowLevel(TheLowLevelType type, int level) {
		//メインレベルが指定された時は全てのレベルをセットする
		if (type == TheLowLevelType.MAIN) {
			setTheLowLevel(TheLowLevelType.SWORD, level);
			setTheLowLevel(TheLowLevelType.BOW, level);
			setTheLowLevel(TheLowLevelType.MAGIC, level);
			return;
		}
		theLowLevelMap.put(type, level);
	}

	@Override
	public void setTheLowExp(TheLowLevelType type, int exp) {
		//メインレベルが指定された時は全ての経験値をセットする
		if (type == TheLowLevelType.MAIN) {
			setTheLowExp(TheLowLevelType.SWORD, exp);
			setTheLowExp(TheLowLevelType.BOW, exp);
			setTheLowExp(TheLowLevelType.MAGIC, exp);
			return;
		}
		theLowExpMap.put(type, exp);
	}

	@Override
	public int getGalions() {
		return galions;
	}

	@Override
	public void setGalions(int galions) {
		this.galions = galions;
	}

	@Override
	public OfflinePlayer getOfflinePlayer() {
		return player;
	}

	@Override
	public Player getOnlinePlayer() {
		return player.getPlayer();
	}

	@Override
	public DungeonData getInDungeonId() {
		if (inDungeonId == -1) {
			return null;
		}
		return DungeonList.getDungeonByID(inDungeonId);
	}

	@Override
	public void setInDungeonId(DungeonData dungeon) {
		if (dungeon == null) {
			inDungeonId = -1;
		} else {
			inDungeonId = dungeon.getId();
		}
	}


}
