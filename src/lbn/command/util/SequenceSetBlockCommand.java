package lbn.command.util;

import java.util.Arrays;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import lbn.dungeoncore.Main;

public class SequenceSetBlockCommand implements CommandExecutor, UsageCommandable{

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(final CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		if (arg3.length <= 3) {
			return false;
		}

		if (!NumberUtils.isNumber(arg3[0])) {
			arg0.sendMessage(arg3[0] + " is not number!!");
			return false;
		}

		String id = arg3[1].substring(0, arg3[1].indexOf(":") == -1 ? arg3[1].length() : arg3[1].indexOf(":") );
		final String data;
		if (arg3[1].contains(":") && !arg3[1].endsWith(":")) {
			data = arg3[1].substring(arg3[1].indexOf(":") + 1);
		} else {
			data = "0";
		}

		if (!NumberUtils.isNumber(id) || !NumberUtils.isNumber(data)) {
			arg0.sendMessage(arg3[1] + "は不正なブロックIDです");
			return false;
		}

		final Material material = Material.getMaterial(Integer.parseInt(id));
		if (material == null) {
			arg0.sendMessage(id + "は不正なブロックIDです");
			return false;
		}

		String commandList = StringUtils.join(Arrays.copyOfRange(arg3, 2, arg3.length), " ");
		final String[] split = commandList.split("&");

		final World w;
		if (arg0 instanceof Player) {
			w = ((Player) arg0).getWorld();
		} else if (arg0 instanceof BlockCommandSender) {
			w = ((BlockCommandSender) arg0).getBlock().getWorld();
		} else {
			arg0.sendMessage("その方法では実行できません。");
			return true;
		}

		if (arg3[0].trim().equals("0")) {
			for (String string : split) {
				try {
					String pointStr = string.trim();
					String[] pointList = pointStr.split(" ");
					double x = Double.parseDouble(pointList[0]);
					double y = Double.parseDouble(pointList[1]);
					double z = Double.parseDouble(pointList[2]);

					Block block = new Location(w, x, y, z).getBlock();
					block.setType(material);
					block.setData(Byte.parseByte(data));
				} catch (Exception e) {
					e.printStackTrace();
					break;
				}
			}
		} else {
			new BukkitRunnable() {
				int i = 0;
				@Override
				public void run() {
					try {
						String pointStr = split[i].trim();
						String[] pointList = pointStr.split(" ");
						double x = Double.parseDouble(pointList[0]);
						double y = Double.parseDouble(pointList[1]);
						double z = Double.parseDouble(pointList[2]);

						Block block = new Location(w, x, y, z).getBlock();
						block.setType(material);
						block.setData(Byte.parseByte(data));

						i++;
						if (split.length <= i) {
							cancel();
						}
					} catch (Exception e) {
						e.printStackTrace();
						cancel();
					}
				}
			}.runTaskTimer(Main.plugin, 0, (long) (Double.parseDouble(arg3[0]) * 20));
		}
		return true;
	}

	@Override
	public String getUsage() {
		return "\n /sequence_setblock second blockid:data x y z & x y z & ... "
				+ ChatColor.GREEN + "\n '/sequence_setblock 1 5:2 20 60 20 & 20 61 20' "
				+ ChatColor.GRAY + "\n ---- 1秒間隔でブロック'5:2(白樺の木材)'を(20 60 20), (20 61 20)に設置します"
				+ ChatColor.GREEN + "\n '/sequence_setblock 0 1 20 60 20 & 20 61 20 & 20 62 2' "
				+ ChatColor.GRAY + "\n ---- 一気にブロック'1(焼石)'を(20 60 20), (20 61 20), (20 62 20)に設置します";
	}

	@Override
	public String getDescription() {
		return "指定した時間間隔で指定したブロックを設置します。";
	}
}
