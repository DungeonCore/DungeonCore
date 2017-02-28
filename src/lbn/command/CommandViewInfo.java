package lbn.command;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

import lbn.common.buff.BuffData;
import lbn.common.buff.BuffDataFactory;
import lbn.common.cooltime.CooltimeManager;
import lbn.dungeoncore.Main;
import lbn.item.slot.table.SlotSetTableOperation;
import lbn.mob.mobskill.MobSkillManager;
import lbn.mobspawn.ChunkWrapper;
import lbn.mobspawn.point.MobSpawnerPointManager;
import lbn.player.playerIO.PlayerIODataManager;
import lbn.player.reincarnation.ReincarnationTypeMune;
import lbn.util.InOutputUtil;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.collect.HashMultimap;

public class CommandViewInfo implements CommandExecutor{
	@Override
	public boolean onCommand(CommandSender paramCommandSender, Command paramCommand, String paramString, String[] paramArrayOfString) {
		if (paramArrayOfString.length == 0) {
			paramCommandSender.sendMessage("パラメータ:appendix");
			return false;
		}


		if (!(paramCommandSender instanceof Player)) {
			return false;
		}

		if (!((Player)paramCommandSender).isOp()) {
			return false;
		}

		if (paramArrayOfString[0].equals("chunk")) {
			showLoadedChunk(paramCommandSender);
		}

		String param = paramArrayOfString[0];
		if (param == null) {
			paramCommandSender.sendMessage("param is null");
			return false;
		}

		Player target = (Player) paramCommandSender;;
		if (paramArrayOfString.length == 2) {
			target = Bukkit.getPlayer(paramArrayOfString[1]);
		}

		switch (param) {
		case "slot":
			SlotSetTableOperation.openSlotTable(target);
			break;
		case "cooltime":
			CooltimeManager.clear();
			break;
		case "mobskill":
			MobSkillManager.reloadDataByCommand(paramCommandSender);
			break;
		case "chunk":
			sendChunkInfo(target);
			break;
		case "save":
			PlayerIODataManager.saveAsZip();
			break;
		case "status":
			sendPlayerStatus(target);
			break;
		case "addEffect":
			addEffect(target);
			break;
		case "version":
			paramCommandSender.sendMessage("1.1");
			break;
		case "zipworld":
			File file = new File(Main.dataFolder);
			File parentFile = file.getParentFile().getParentFile();
			File file2 = new File(parentFile, "world");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HHmmss");
			String format = sdf.format(new Date());
			String fileName = "backup_world_" + format + ".zip";
			InOutputUtil.compressDirectory(Main.dataFolder + File.separator + "worldbackup" + File.separator + fileName + ".zip", file2.getAbsolutePath());
			break;
//		case "doragon":
//			makeDragon();
//		case "doragon1":
//			makeDragon1();
//			break;
		case "dungeon":
			makeDungeonGround((Player)paramCommandSender);
			break;
		case "buff":
			BuffData buffFromId = BuffDataFactory.getBuffFromId(paramArrayOfString[1]);
			if (buffFromId != null) {
				buffFromId.addBuff((Player) paramCommandSender);
			}
			break;
		case "reinc":
			new ReincarnationTypeMune().open((Player) paramCommandSender);
			break;
//		case "dungeonCheck":
//			makeDungeonGround((Player)paramCommandSender);
//			break;
			default :
				paramCommandSender.sendMessage("unknown param");
		}
		return true;

	}


	private void addEffect(Player target) {
		new PotionEffect(PotionEffectType.SPEED, 80, 1).apply(target);
		new PotionEffect(PotionEffectType.POISON, 80, 1).apply(target);
	}


	private void sendPlayerStatus(Player target) {
		Collection<PotionEffect> activePotionEffects = target.getActivePotionEffects();
		target.sendMessage("active potion effect:");
		for (PotionEffect potionEffect : activePotionEffects) {
			target.sendMessage("    " + potionEffect.getType() + ", " + (potionEffect.getDuration() / 20) + "s");
		}
	}


	private void makeDungeonGround(Player p) {
		Location location = p.getLocation();
		if (!location.getWorld().getName().equals("dungeon")) {
			return;
		}

			new BukkitRunnable() {
				int xx = location.getBlockX() + 150;
				int c = 0;

				int blockCount = 0;
				@Override
				public void run() {
					for (int z = location.getBlockZ() - 150; z < location.getBlockZ() + 150; z++) {
						if (!new Location(location.getWorld(), xx, 3, z).getBlock().getType().equals(Material.GRASS)) {
							continue;
						}

						if (location.getWorld().getHighestBlockYAt(location) != 4) {
							continue;
						}

						for (int y = 2; y<= 200; y++) {
							new Location(location.getWorld(), xx, y, z).getBlock().setType(Material.STONE);
							blockCount++;
						}
					}

					xx--;
					c++;

					if (xx % 10 == 0) {
						p.sendMessage(c * 100 / 300 + "%  完了 :" + blockCount);
						blockCount = 0;
					}

					if (xx < location.getBlockX() - 150) {
						p.sendMessage("100%  完了");
						cancel();
					}
				}
			}.runTaskTimer(Main.plugin, 0, 1);
	}


	void makeDragon1() {
		for (int i = 44; i >= 3; i--) {
			for (int j = 200; j <= 254 ; j++) {
				for (int l = 555; l <= 733; l++) {
					Location location = new Location(Bukkit.getWorld("world"), i, j, l);
					if (location.getBlock().getType() != Material.AIR) {
						create.put(l, new BlockData(location));
						location.getBlock().setType(Material.AIR);
					}
				}
			}
		}
	}


	HashMultimap<Integer, BlockData> create = HashMultimap.create();
	void makeDragon() {
//		Location start = new Location(Bukkit.getWorld("world"), 44, 200, 555);
		new BukkitRunnable() {
			int i = 555;
			@Override
			public void run() {
				Set<BlockData> set = create.get(i);
				for (BlockData blockData : set) {
					blockData.setBlock();
				}

				i++;
				if (i > 734) {
					cancel();
				}
			}
		}.runTaskTimer(Main.plugin, 0, 2);
//		Location end = new Location(Bukkit.getWorld("world"), 3, 254, 733);
	}

	class BlockData {
		@SuppressWarnings("deprecation")
		protected BlockData(Location loc) {
			Block block = loc.getBlock();
			this.m = block.getType();
			this.loc = loc;
			this.data = block.getData();
		}
		Material m;
		Location loc;
		byte data;

		@SuppressWarnings("deprecation")
		void setBlock() {
			loc.getBlock().setType(m);
			loc.getBlock().setData(data);
		}
	}


	private void sendChunkInfo(Player target) {
		target.sendMessage(ChatColor.GREEN + "=== CHUNK INFO ===");
		Chunk chunk = target.getLocation().getChunk();
		target.sendMessage("loaded:" + chunk.isLoaded());
		target.sendMessage("name:" + target.getName());
		target.sendMessage(chunk.toString());
		target.sendMessage("hashcode:" + chunk.hashCode());
		target.sendMessage("system loaded:" + MobSpawnerPointManager.loadedChunk.contains(new ChunkWrapper(chunk)));
		target.sendMessage(ChatColor.GREEN + "=== CHUNK INFO ===");
	}


	private void showLoadedChunk(CommandSender paramCommandSender) {
		List<World> worlds = Bukkit.getWorlds();
		for (World world : worlds) {
			for (Chunk c : world.getLoadedChunks()) {
				paramCommandSender.sendMessage(world.getName() + ":" + c.getX() + "@" + c.getZ());
			}
		}
	}

}
