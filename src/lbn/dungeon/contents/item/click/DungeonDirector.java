package lbn.dungeon.contents.item.click;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import lbn.common.other.DungeonData;
import lbn.item.AbstractItem;
import lbn.item.ItemInterface;
import lbn.item.itemInterface.RightClickItemable;

public class DungeonDirector extends AbstractItem implements RightClickItemable{

	String dufficulty;
	public DungeonDirector(String dufficulty) {
		this.dufficulty = dufficulty;
	}

	public static List<ItemInterface> getItemList() {
		return Arrays.asList(
				new DungeonDirector(DungeonData.DIFFICULTY_VERY_EASY),
				new DungeonDirector(DungeonData.DIFFICULTY_EASY),
				new DungeonDirector(DungeonData.DIFFICULTY_NORMAL),
				new DungeonDirector(DungeonData.DIFFICULTY_HARD),
				new DungeonDirector(DungeonData.DIFFICULTY_VERY_HARD),
				new DungeonDirector(DungeonData.DIFFICULTY_IMPOSSIBLE)
			);
	}

	@Override
	public String getItemName() {
		return "Dungeon Director [" + getDifficulty() + "]";
	}

	@Override
	public String getId() {
		return "dungeon_director_" + getDifficulty();
	}

	public String getDifficulty() {
		return dufficulty;
	}

	@Override
	public int getBuyPrice(ItemStack item) {
		return 100;
	}

	@Override
	public void excuteOnRightClick(PlayerInteractEvent e) {
		//TODO クリックするごとに近いダンジョン5つを順番に指定し、その場所をコンパスで指す
		//TODO アイテム名を　”ダンジョン名 [距離m]”に変える
	}

	@Override
	protected Material getMaterial() {
		return Material.COMPASS;
	}

	@Override
	protected String[] getDetail() {
		return new String[]{"自分の近くにある難易度が[" + getDifficulty() + "]な",
				"ダンジョンを5つ順番に指し示します。"};
	}

}
