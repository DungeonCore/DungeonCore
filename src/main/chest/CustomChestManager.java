package main.chest;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.material.Chest;

import main.chest.wireless.RepositoryChest;
import main.chest.wireless.RepositoryType;
import main.mob.mob.BossMobable;
import net.md_5.bungee.api.ChatColor;

/**
 * チェストの場所と中身を管理する
 * @author kensuke
 *
 */
public class CustomChestManager {
	private static HashMap<Location, AbstractCustomChest> chestList = new HashMap<>();

	public static boolean containts(Location loc) {
		return chestList.containsKey(loc);
	}

	/**
	 * ボスチェストだけ特別処理
	 * @param e
	 */
	public static void setBossRewardChest(BossMobable e) {
		BossChest bossChest = e.getBossChest();

		if (bossChest != null) {
			Location chestLoc = bossChest.setChest(e);
			chestList.put(chestLoc, bossChest);
		}
	}

	public static void registChest(Location loc, AbstractCustomChest chest) {
		if (chestList.containsKey(loc)) {
			AbstractCustomChest abstractCustomChest = chestList.get(loc);
			if (chest instanceof SpletSheetChest && abstractCustomChest instanceof SpletSheetChest) {
				((SpletSheetChest)chest).setRefule((SpletSheetChest) abstractCustomChest);
			}
		}
		chestList.put(loc.getBlock().getLocation(), chest);

	}

	/**
	 * 登録したチェストを取り除く
	 * @param loc
	 */
	public static void removeChest(Location loc) {
		AbstractCustomChest remove = chestList.remove(loc.getBlock().getLocation());
		if (remove != null) {
			remove.removeChest(loc);
		}
	}

	/**
	 * 場所からチェストを取得する
	 * @param loc
	 * @return
	 */
	public static AbstractCustomChest getCustomChest(Location loc) {
		AbstractCustomChest abstractCustomChest = chestList.get(loc.getBlock().getLocation());
		if (abstractCustomChest != null) {
			return abstractCustomChest;
		}

		//チェストが登録されていない場合は看板があるか調べる
		if (!(loc.getBlock().getState().getData() instanceof Chest)) {
			return null;
		}
		Chest data = (Chest) loc.getBlock().getState().getData();
		BlockFace facing = data.getFacing();
		Block relative = loc.getBlock().getRelative(facing);
		if (relative.getType() != Material.SIGN_POST && relative.getType() != Material.WALL_SIGN) {
			return null;
		}

		Sign state = (Sign) relative.getState();
		String[] lines = state.getLines();
		//看板がある場合は特別処理
		if (lines[0].equals("[chest]")) {
			//repositoryチェストの場合
			if (lines[1].equals(ChatColor.GREEN + "REPOSITORY")) {
				RepositoryType instance = RepositoryType.getInstance(lines[2]);
				if (instance != null) {
					System.out.println("new chest: "+ loc);
					registChest(loc, new RepositoryChest(loc, instance));
				}
			}
		}

		return chestList.get(loc.getBlock().getLocation());
	}
}
