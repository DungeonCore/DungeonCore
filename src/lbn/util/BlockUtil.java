package lbn.util;

import lbn.dungeoncore.Main;

import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

public class BlockUtil {

	/**
	 * 感圧板が踏まれていたらTRUE
	 * @param b
	 * @return
	 */
	public static boolean isPressed(Block b) {
		if (b == null) {
			return false;
		}
		Material type = b.getType();
		if (type == Material.STONE_PLATE || type == Material.WOOD_PLATE || type == Material.GOLD_PLATE || type == Material.IRON_PLATE) {
			@SuppressWarnings("deprecation")
			byte data = b.getData();
			return data == 1;
		}
		return false;
	}

	/**
	 * 感圧板系のブロックならTRUE
	 * @param b
	 * @return
	 */
	public static boolean isPressable(Block b) {
		if (b == null) {
			return false;
		}
		Material type = b.getType();
		if (type == Material.STONE_PLATE || type == Material.WOOD_PLATE || type == Material.GOLD_PLATE || type == Material.IRON_PLATE) {
			return true;
		}
		return false;
	}

	public static void setRedstone(Location loc) {
		loadChunk(loc);

		loc.getBlock().setType(Material.REDSTONE_BLOCK);
		new BukkitRunnable() {
			@Override
			public void run() {
				loadChunk(loc);
				loc.getBlock().setType(Material.AIR);
			}
		}.runTaskLater(Main.plugin, 2);
	}

	public static void loadChunk(Location loc) {
		Chunk chunk = loc.getChunk();
		if (!chunk.isLoaded()) {
			chunk.load();
		}
	}

	public static BlockData getBlockData(String bData, CommandSender sender) {
		String id = bData.substring(0, bData.indexOf(":") == -1 ? bData.length() : bData.indexOf(":") );
		final String data;
		if (bData.contains(":") && !bData.endsWith(":")) {
			data = bData.substring(bData.indexOf(":") + 1);
		} else {
			data = "0";
		}

		if (!NumberUtils.isNumber(id) || !NumberUtils.isNumber(data)) {
			sender.sendMessage(bData + "は不正なブロックIDです");
			return null;
		}

		@SuppressWarnings("deprecation")
		Material material = Material.getMaterial(JavaUtil.getInt(id, -1));
		if (material == null) {
			sender.sendMessage(id + "は不正なブロックIDです");
			return null;
		}

		try {
			BlockData blockData = new BlockData();
			blockData.b = Byte.parseByte(data);
			blockData.m = material;
			return blockData;
		} catch (Exception e) {
			return null;
		}
	}


	public static class BlockData {
		Material m;
		byte b;

		public Material getM() {
			return m;
		}
		public byte getB() {
			return b;
		}
	}
}
