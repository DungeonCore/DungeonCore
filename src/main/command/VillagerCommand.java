package main.command;

import java.util.HashMap;
import java.util.List;

import main.lbn.SpletSheet.AbstractComplexSheetRunable;
import main.lbn.SpletSheet.SpletSheetExecutor;
import main.lbn.SpletSheet.VillagerSheetRunnable;
import main.mob.AbstractMob;
import main.mob.MobHolder;
import main.mob.mob.abstractmob.villager.AbstractVillager;
import main.util.LivingEntityUtil;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;

public class VillagerCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		if (arg3.length == 0) {
			return false;
		}

		String targetName = null;
		if (arg3.length >= 2) {
			targetName = arg3[1];
		}

		if (arg3[0].equalsIgnoreCase("spawn")) {
			spawnVillager(arg0, targetName);
		} else if (arg3[0].equalsIgnoreCase("remove")) {
			removeVillager(arg0, targetName);
		} else if (arg3[0].equalsIgnoreCase("reload")) {
			reloadVillager(arg0, targetName);
		} else if (arg3[0].equalsIgnoreCase("reset")) {
			resetVillager(arg0, targetName);
		} else {
			arg0.sendMessage(arg3[0] + "は認められていません。[spawn, remove, reload, reset]のみ可能です。");
			return false;
		}
		return true;
	}

	protected void resetVillager(CommandSender arg0, String targetName) {
		Player p = (Player) arg0;
		List<Entity> nearbyEntities = p.getNearbyEntities(30, 10, 30);
		for (Entity entity : nearbyEntities) {
			if (entity.getType() != EntityType.VILLAGER) {
				continue;
			}

			if (!LivingEntityUtil.isCustomVillager(entity)) {
				entity.remove();
			}
		}
	}

	public static void reloadAllVillager(CommandSender arg0, boolean init) {
		try {
			VillagerSheetRunnable villagerSheetRunnable = new VillagerSheetRunnable(arg0);
			villagerSheetRunnable.setChunkLoadInit(init);
			villagerSheetRunnable.getData(null);
			SpletSheetExecutor.onExecute(villagerSheetRunnable);
		} catch (Exception e) {
			e.printStackTrace();
			arg0.sendMessage("エラーが発生しました。");
		}
	}

	protected void reloadVillager(CommandSender arg0, String targetName) {
		VillagerSheetRunnable villagerSheetRunnable = new VillagerSheetRunnable(arg0);
		if (villagerSheetRunnable.isTransaction()) {
			arg0.sendMessage("現在実行中です");
		}
		try {
			if (targetName == null || targetName.isEmpty()) {
				villagerSheetRunnable.getData(null);
			} else {
				villagerSheetRunnable.getData("name=" + targetName);
			}
			SpletSheetExecutor.onExecute(villagerSheetRunnable);
		} catch (Exception e) {
			e.printStackTrace();
			arg0.sendMessage("エラーが発生しました。");
		}
	}

	protected void removeVillager(CommandSender sender, String targetName) {
		//locationを更新
		VillagerSheetRunnable villagerSheetRunnable = new VillagerSheetRunnable(sender);
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("location", "");
		villagerSheetRunnable.updateData(map, "name=" + targetName);
		try {
			SpletSheetExecutor.onExecute(villagerSheetRunnable);
		} catch (Exception e) {
			e.printStackTrace();
			sender.sendMessage("エラーが発生しました。");
		}

		List<Entity> nearbyEntities = ((Player)sender).getNearbyEntities(1, 1, 1);
		if (targetName == null) {
			nearbyEntities = ((Player)sender).getNearbyEntities(5, 2, 5);
		} else {
			nearbyEntities = ((Player)sender).getNearbyEntities(1, 1, 1);
		}
		//名前が指定されている場合はその村人だけ削除する
		for (Entity entity : nearbyEntities) {
			if (entity instanceof Villager) {
				if (targetName == null) {
					entity.remove();
				} else {
					if (targetName.equalsIgnoreCase(((Villager) entity).getCustomName())) {
						entity.remove();
					}
				}
			}
		}
	}

	protected void spawnVillager(CommandSender arg0, String targetName) {
		if (targetName == null) {
			arg0.sendMessage("spawn対象の村人が選択されていません。\n /villager spawn 村人名");
			return;
		}

		AbstractMob<?> mob = MobHolder.getMob(targetName);
		if (mob == null || mob.getEntityType() != EntityType.VILLAGER) {
			arg0.sendMessage(targetName + "という名前の村人が存在しません。");
			return;
		}

		//前スポーンしたものを削除する
		if (((AbstractVillager)mob).getLocation() != null) {
			removeVillager(targetName, mob);
		}

		Location location = ((Player)arg0).getLocation();
		location.setX(location.getBlockX());
		location.setY(location.getBlockY());
		location.setZ(location.getBlockZ());
		//スポーンさせる
		mob.spawn(location);
		//スプレットシートに書きこむ
		VillagerSheetRunnable villagerSheetRunnable = new VillagerSheetRunnable(arg0);
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("location", AbstractComplexSheetRunable.getLocationString(((Player)arg0).getLocation()));
		villagerSheetRunnable.updateData(map, "name=" + targetName);
		try {
			SpletSheetExecutor.onExecute(villagerSheetRunnable);
		} catch (Exception e) {
			e.printStackTrace();
			arg0.sendMessage("エラーが発生しました。");
		}
	}

	protected void removeVillager(String targetName, AbstractMob<?> mob) {
		Chunk chunk = ((AbstractVillager)mob).getLocation().getChunk();
		Entity[] entities = chunk.getEntities();
		for (Entity entity : entities) {
			if (entity.getType() == EntityType.VILLAGER && ((LivingEntity)entity).getCustomName().equals(targetName)) {
				entity.remove();
			}
		}
	}
}
