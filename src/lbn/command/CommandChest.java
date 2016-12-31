package lbn.command;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.material.Chest;
import org.bukkit.material.MaterialData;
import org.bukkit.util.StringUtil;

import com.google.common.collect.ImmutableList;

import lbn.chest.AllPlayerSameContentChest;
import lbn.chest.ChestLocationManager;
import lbn.chest.CustomChestManager;
import lbn.chest.EachPlayerContentChest;
import lbn.chest.SpletSheetChest;
import lbn.chest.wireless.RepositoryType;
import lbn.dungeoncore.SpletSheet.ChestSheetRunable;
import lbn.dungeoncore.SpletSheet.SpletSheetExecutor;

public class CommandChest implements CommandExecutor, TabCompleter{

	public static HashMap<Player, Location> chestClickMap = new HashMap<Player, Location>();

	//command x y z [refuel_time, allPlayerSameFlg, min, max, x y z]
	@Override
	public boolean onCommand(CommandSender paramCommandSender, Command paramCommand, String paramString, String[] params) {
		ChestSheetRunable chestSheetRunable = new ChestSheetRunable(paramCommandSender);

		if (chestSheetRunable.isTransaction()) {
			paramCommandSender.sendMessage("現在別の人が実行中です。");
		}

		boolean result = false;
		if (params.length >= 1 && params[0].equalsIgnoreCase("reload")) {
			result = reload(paramCommandSender, params, chestSheetRunable);
		} else if (params.length == 1 && params[0].equalsIgnoreCase("delete")) {
			result = delete(paramCommandSender, chestSheetRunable);
		} else if (params.length == 2 && params[0].equalsIgnoreCase("souko")) {
			result = addRepository(paramCommandSender, params, chestSheetRunable);
		} else {
			result = add(paramCommandSender, params, chestSheetRunable);
		}

		return result;
	}

	private boolean addRepository(CommandSender paramCommandSender, String[] params,
			ChestSheetRunable chestSheetRunable) {
		Player p = (Player) paramCommandSender;
		Location chestLoc = getChestLoc(p);
		if (chestLoc == null) {
			p.sendMessage("chestを先に左クリックして下さい");
			return true;
		}

		RepositoryType instance = RepositoryType.getInstance(params[1]);
		if (instance == null) {
			p.sendMessage("typeが不正です。[" + RepositoryType.getNames() + "]のみ許可されます");
		}

		MaterialData data = chestLoc.getBlock().getState().getData();
		if (!(data instanceof Chest)) {
			p.sendMessage("選択場所にチェストが存在しません");
			return true;
		}
		Chest c = (Chest) data;
		BlockFace facing = c.getFacing();
		Block relative = chestLoc.getBlock().getRelative(facing);
//		BlockFace oppositeFace = facing.getOppositeFace();
		relative.setType(Material.WALL_SIGN);
		Sign sign = (Sign) relative.getState();
		sign.setLine(0, "[chest]");
		sign.setLine(1, ChatColor.GREEN + "REPOSITORY");
		sign.setLine(2, instance.getType());
		sign.setLine(3, instance.getPrice() + "Galion");
		org.bukkit.material.Sign metadeta = (org.bukkit.material.Sign) sign.getData();
		metadeta.setFacingDirection(facing);
		sign.setData(metadeta);
		sign.update();

		return true;
	}

	public static void allReload() {
		ChestSheetRunable chest = new ChestSheetRunable(Bukkit.getConsoleSender());
		chest.getData(null);
		SpletSheetExecutor.onExecute(chest);
	}

	protected boolean delete(CommandSender paramCommandSender, ChestSheetRunable chestSheetRunable) {
		paramCommandSender.sendMessage("現在この機能は使えません。");
		boolean result;
//		Player p = (Player) paramCommandSender;
//		Location chestLoc = getChestLoc(p);
//		if (chestLoc == null) {
//			p.sendMessage("削除するchestを先にクリックして下さい");
//		}
//		CustomChestManager.removeChest(chestLoc);
//		chestSheetRunable.deleteData("chest_location=" + ChestSheetRunable.getLocationString(chestLoc));
//		SpletSheetExecutor.onExecute(chestSheetRunable);
		result = true;
		return result;
	}

	protected boolean add(CommandSender paramCommandSender, String[] params, ChestSheetRunable chestSheetRunable) {
		double x = 0;
		double y = 0;
		double z = 0;
		double refuelTime = 180;
		boolean allPlayerSameFlg = true;
		int minItemCount = 1;
		int maxItemCount = 4;

		double moveX = 0;
		double moveY = 0;
		double moveZ = 0;
//
//		if (params.length < 3) {
//			return false;
//		}
//
//		if (params.length == 8 || params.length == 9) {
//			paramCommandSender.sendMessage("チェストを開けた後の移動先の座標が足りません。x y z全て必要です");
//			return true;
//		}
//
//		try {
//			switch (params.length) {
//			case 10:
//				moveX = Double.parseDouble(params[9]);
//			case 9:
//				moveY = Double.parseDouble(params[8]);
//			case 8:
//				moveZ = Double.parseDouble(params[7]);
//			case 7:
//				maxItemCount = Integer.parseInt(params[6]);
//			case 6:
//				minItemCount = Integer.parseInt(params[5]);
//			case 5:
//				allPlayerSameFlg = Boolean.parseBoolean(params[4]);
//			case 4:
//				refuelTime = Double.parseDouble(params[3]);
//			case 3:
//				x = Double.parseDouble(params[0]);
//				y = Double.parseDouble(params[1]);
//				z = Double.parseDouble(params[2]);
//			default:
//				break;
//			}
//		} catch (Exception e) {
//			paramCommandSender.sendMessage("入力したデータに誤りがあります。");
//			return true;
//		}
		Player p = (Player) paramCommandSender;

		Location contentsLoc = null;
		if (params.length > 0) {
			contentsLoc = ChestLocationManager.getChestLocation(params[0]);
		}

		if (contentsLoc == null) {
			contentsLoc = new Location(p.getWorld(), x, y, z);
		}

		Location moveLoc = null;
		if (moveX == 0 && moveY == 0 && moveZ == 0) {
		} else {
			moveLoc = new Location(p.getWorld(), moveX, moveY, moveZ);
		}

		Location chestLoc = getChestLoc(p);
		if (chestLoc == null) {
			p.sendMessage("chestを先に左クリックして下さい");
			return true;
		}

		chestClickMap.remove(p);
		if (CustomChestManager.containts(chestLoc)) {
			HashMap<String, Object> hashMap = new HashMap<String, Object>();
			hashMap.put("contentlocation", ChestSheetRunable.getLocationString(contentsLoc));
			chestSheetRunable.updateData(hashMap, "chestlocation=\"" + ChestSheetRunable.getLocationString(chestLoc) + "\"");
		} else {
			SpletSheetChest chest;
			if (allPlayerSameFlg) {
				chest = new AllPlayerSameContentChest(chestLoc, contentsLoc, (int) (refuelTime * 20), moveLoc, minItemCount, maxItemCount, 10, true);
			} else {
				chest = new EachPlayerContentChest(chestLoc, contentsLoc, (int) (refuelTime * 20), moveLoc, minItemCount, maxItemCount,  10, true);
			}
			CustomChestManager.registChest(chestLoc, chest);

			//バグるのでなし
//		chestSheetRunable.deleteData("chestlocation=" + ChestSheetRunable.getLocationString(chestLoc));
			chestSheetRunable.addData(ChestSheetRunable.createDataMap(chestLoc, contentsLoc, refuelTime, allPlayerSameFlg, moveLoc, minItemCount, maxItemCount));

		}
		SpletSheetExecutor.onExecute(chestSheetRunable);



		return true;
	}



	protected boolean reload(CommandSender paramCommandSender, String[] params, ChestSheetRunable chestSheetRunable) {
		chestSheetRunable.getData(null);
		SpletSheetExecutor.onExecute(chestSheetRunable);
		return true;
	}

	private Location getChestLoc(Player p) {
		return chestClickMap.get(p);
	}

	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		if (arg3.length == 1) {
			return (List<String>)StringUtil.copyPartialMatches(arg3[0], ChestLocationManager.getNames(), new ArrayList<String>(ChestLocationManager.getNames().size()));
		}
		return ImmutableList.of();
	}
}
