package lbn.npc;

import java.util.HashMap;

import lbn.dungeoncore.SpletSheet.SpletSheetExecutor;
import lbn.dungeoncore.SpletSheet.VillagerSheetRunnable;
import net.citizensnpcs.api.event.NPCDamageEvent;
import net.citizensnpcs.api.event.NPCDespawnEvent;
import net.citizensnpcs.api.event.NPCLeftClickEvent;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.event.NPCSpawnEvent;
import net.citizensnpcs.api.npc.NPC;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;

public class NpcManager {
	static HashMap<String, VillagerNpc> registedNpc = new HashMap<String, VillagerNpc>();

	static HashMap<String, NPC> spawned = new HashMap<String, NPC>();

	public static VillagerNpc getVillagerNpc(String name) {
		return registedNpc.get(name);
	}

	public static void regist(VillagerNpc villagerNpc) {
		registedNpc.put(villagerNpc.getName(), villagerNpc);
		//NPCをセットする
		villagerNpc.setNpc(spawned.get(villagerNpc.getName()));
	}

	public static boolean isNpc(Entity entity) {
		return entity.hasMetadata("NPC");
	}

	public static VillagerNpc getVillagerNpc(Entity entity) {
		if (!isNpc(entity)) {
			return null;
		}

		String customName = entity.getCustomName();
		return getVillagerNpc(customName);
	}

	public static void onNPCRightClickEvent(NPCRightClickEvent e) {
		String name = e.getNPC().getName();
		VillagerNpc villagerNpc = registedNpc.get(name);
		if (villagerNpc != null) {
			villagerNpc.onNPCRightClickEvent(e);
		}
	}

	public static void onNPCLeftClickEvent(NPCLeftClickEvent e) {
		String name = e.getNPC().getName();
		VillagerNpc villagerNpc = registedNpc.get(name);
		if (villagerNpc != null) {
			villagerNpc.onNPCLeftClickEvent(e);
		}
	}

	public static void onNPCDamageEvent(NPCDamageEvent e) {
		String name = e.getNPC().getName();
		VillagerNpc villagerNpc = registedNpc.get(name);
		if (villagerNpc != null) {
			villagerNpc.onNPCDamageEvent(e);
		}
	}

	public static void onNPCSpawnEvent(NPCSpawnEvent e) {
		String name = e.getNPC().getName();
		//スポーン済みにセットする
		spawned.put(name, e.getNPC());
		VillagerNpc villagerNpc = registedNpc.get(name);
		if (villagerNpc != null) {
			villagerNpc.onSpawn(e);
		}
	}

	public static void onNPCDespawnEvent(NPCDespawnEvent e) {
		String name = e.getNPC().getName();
		//もしスポーン済み情報がセットされてるなら削除する
		if (e.getNPC().equals(spawned.get(name))) {
			spawned.remove(name);
		}
	}

	public static void allReload(CommandSender sender) {
		if (sender == null) {
			sender = Bukkit.getConsoleSender();
		}

		VillagerSheetRunnable villagerSheetRunnable = new VillagerSheetRunnable(sender);
		villagerSheetRunnable.getData(null);
		SpletSheetExecutor.onExecute(villagerSheetRunnable);
	}
}
