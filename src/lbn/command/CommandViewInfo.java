package lbn.command;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lbn.common.buff.BuffData;
import lbn.common.buff.BuffDataFactory;
import lbn.common.cooltime.CooltimeManager;
import lbn.common.particle.ParticleData;
import lbn.common.particle.ParticleManager;
import lbn.dungeoncore.Main;
import lbn.item.ItemManager;
import lbn.item.customItem.armoritem.ArmorBase;
import lbn.item.itemInterface.ArmorItemable;
import lbn.item.slot.table.SlotSetTableOperation;
import lbn.item.system.craft.CraftItemSelectViewer;
import lbn.mob.AbstractMob;
import lbn.mob.MobHolder;
import lbn.mob.customMob.SpreadSheetMob;
import lbn.mobspawn.ChunkWrapper;
import lbn.mobspawn.point.MobSpawnerPointManager;
import lbn.npc.NpcManager;
import lbn.player.playerIO.PlayerIODataManager;
import lbn.util.InOutputUtil;
import lbn.util.LivingEntityUtil;

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
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
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

//		if (!((Player)paramCommandSender).isOp()) {
//			return false;
//		}

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
		case "chunk":
			sendChunkInfo(target);
			break;
		case "stop":
			try {
				Thread.sleep(1000 * 20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			break;
		case "save":
			PlayerIODataManager.saveAsZip();
			break;
		case "craft":
			Block block = new Location(Bukkit.getWorld("thelow"), -23, 70, 0).getBlock();
			Inventory inventory = CraftItemSelectViewer.getInventory(block);
			CraftItemSelectViewer.open((Player)paramCommandSender, Arrays.asList(inventory), 0);
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
		case "mobskill":
			showMobSkill(paramArrayOfString[1], paramCommandSender);
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
		case "npc":
			NpcManager.onTest();
			break;
		case "buff":
			BuffData buffFromId = BuffDataFactory.getBuffFromId(paramArrayOfString[1]);
			if (buffFromId != null) {
				buffFromId.addBuff((Player) paramCommandSender);
			}
			break;
		case "xyz":
			String command = "tellraw " + target.getName() +" {\"text\":\"座標取得\",\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"" + target.getLocation().getBlockX() + " " + target.getLocation().getBlockY()+ " " + target.getLocation().getBlockZ() + "\"}}";
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
			break;
		case "xyz2":
			String command2 = "tellraw " + target.getName() +" {\"text\":\"座標取得\",\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"" + target.getWorld().getName() + ":" + target.getLocation().getBlockX() + "," + target.getLocation().getBlockY()+ "," + target.getLocation().getBlockZ() + "\"}}";
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command2);
			break;
		case "particle":
			ParticleData particle = ParticleManager.getParticleData(paramArrayOfString[1]);
			if (particle != null) {
				particle.run(((Player)paramCommandSender).getLocation());
			}
			break;
		case "kaminari":
			LivingEntityUtil.strikeLightningEffect(((Player)paramCommandSender).getLocation());
			break;
		case "armor":
			sendArmorData((Player)paramCommandSender);
			break;
			default :
				paramCommandSender.sendMessage("unknown param");
		}
		return true;

	}


	private void sendArmorData(Player p) {
		double normalMobArmorPoint = 0;
		double bossArmorPoint = 0;

		ItemStack[] armorContents = p.getEquipment().getArmorContents();
		for (ItemStack itemStack : armorContents) {
			ArmorItemable customItem = ItemManager.getCustomItem(ArmorItemable.class, itemStack);
			if (customItem == null) {
				continue;
			}
			normalMobArmorPoint += customItem.getArmorPointForNormalMob();
			bossArmorPoint += customItem.getArmorPointForBossMob();
		}

		p.sendMessage(ChatColor.GOLD + "=========================");
		p.sendMessage(ChatColor.GREEN + "合計防具ポイント");
		p.sendMessage("    通常モンスター：" + normalMobArmorPoint);
		p.sendMessage("    ボスモンスター：" + bossArmorPoint);
		p.sendMessage(ChatColor.GREEN + "ダメージカット率");
		p.sendMessage("    通常モンスター：" + (100 - ArmorBase.getDamageCutParcent(normalMobArmorPoint) * 100) + "%カット");
		p.sendMessage("    ボスモンスター：" + (100 - ArmorBase.getDamageCutParcent(bossArmorPoint) * 100) + "%カット");
		p.sendMessage(ChatColor.GOLD + "=========================");
	}


	private void showMobSkill(String string, CommandSender paramCommandSender) {
		AbstractMob<?> mob = MobHolder.getMob(string);
		if (mob == null) {
			return;
		}

		if (mob instanceof SpreadSheetMob) {
			HashSet<String> skillIdList = ((SpreadSheetMob) mob).getSkillIdList();
			for (String skill : skillIdList) {
				paramCommandSender.sendMessage(skill);
			}
		}
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
