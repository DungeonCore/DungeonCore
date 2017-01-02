package lbn.mob;

import java.text.MessageFormat;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import lbn.mob.mob.NullMob;

public class VillagerManager {
	static HashMap<String, AbstractMob<?>> villagerNameMap = new HashMap<String, AbstractMob<?>>();

	static HashMap<String, Location> villagerLocMap = new HashMap<String, Location>();

	public static void registVillager(AbstractMob<?> mob) {
		villagerNameMap.put(mob.getName(), mob);
	}

	public static void spawnVillager(AbstractMob<?> mob, Location loc) {
		villagerLocMap.put(mob.getName(), loc);
	}

	public static void despawnVillager(AbstractMob<?> mob) {
		villagerLocMap.remove(mob.getName());
	}


	public static String getVillagerName(String name, Player p, String firstText, String endText) {
		String type = null;
		String loc = null;

		AbstractMob<?> abstractMob = villagerNameMap.get(name);
		//村人のTypeを設定
		if (abstractMob != null && abstractMob.getEntityType() != null) {
			EntityType entityType = abstractMob.getEntityType();
			type = entityType.toString();
		}

		Location location = villagerLocMap.get(name);
		if (location != null) {
			loc = MessageFormat.format("{0}, {1}, {2}", location.getBlockX(), location.getBlockY(), location.getBlockZ());
		}

		ConsoleCommandSender consoleSender = Bukkit.getConsoleSender();
		if (type == null) {
			Bukkit.dispatchCommand(consoleSender,
					MessageFormat.format("/tellraw {0} [\"\",{\"text\":\"{1}\"},{\"text\":\"{2}\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"Type : {3}    , Location : {4}\"}},{\"text\":\"{5}\"}]",
							p.getName(),
							firstText,
							name,
							"村人が登録されていません",
							"none",
							endText));
		} else {
			if (loc == null) {
				loc = "村人が今は存在しません";
			}
			Bukkit.dispatchCommand(consoleSender,
					MessageFormat.format("/tellraw {0} [\"\",{\"text\":\"{1}\"},{\"text\":\"{2}\",\"hoverEvent\":{\"action\":\"show_text\",\"value\":\"Type : {3}    , Location : {4}\"}},{\"text\":\"{5}\"}]",
							p.getName(),
							firstText,
							name,
							type,
							loc,
							endText));
		}

		return name;
	}

	public static AbstractMob<?> getVillager(String name) {
		if (villagerNameMap.containsKey(name)) {
			return villagerNameMap.get(name);
		}
		return new NullMob(name);
	}
}
