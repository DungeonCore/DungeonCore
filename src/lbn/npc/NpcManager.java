package lbn.npc;

import java.util.HashMap;
import java.util.List;

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
import org.bukkit.metadata.MetadataValue;

public class NpcManager {
	static HashMap<String, VillagerNpc> registedNpcIdMap = new HashMap<String, VillagerNpc>();

	static HashMap<String, NPC> spawnedNPCIDMap = new HashMap<String, NPC>();

	public static VillagerNpc getVillagerNpcById(String name) {
		return registedNpcIdMap.get(name);
	}

	public static void regist(VillagerNpc villagerNpc) {
		registedNpcIdMap.put(villagerNpc.getId(), villagerNpc);
		//NPCをセットする
		villagerNpc.setNpc(spawnedNPCIDMap.get(villagerNpc.getId()));
	}

	public static boolean isNpc(Entity entity) {
		return entity.hasMetadata("NPC");
	}

	public static VillagerNpc getVillagerNpc(Entity entity) {
		if (!isNpc(entity)) {
			return null;
		}

		String id = getId(entity);
		return getVillagerNpcById(id);
	}

	/**
	 * VillagerIdを取得
	 * @param entity
	 * @return
	 */
	public static String getId(Entity entity) {
		List<MetadataValue> metadata = entity.getMetadata("thelow_villager_id");
		if (metadata.size() > 0) {
			return metadata.get(0).asString();
		}
		return null;
	}

	public static void onNPCRightClickEvent(NPCRightClickEvent e) {
		String id = getId(e.getNPC().getEntity());
		VillagerNpc villagerNpc = registedNpcIdMap.get(id);
		if (villagerNpc != null) {
			villagerNpc.onNPCRightClickEvent(e);
		}
	}

	public static void onNPCLeftClickEvent(NPCLeftClickEvent e) {
		String id = getId(e.getNPC().getEntity());
		VillagerNpc villagerNpc = registedNpcIdMap.get(id);
		if (villagerNpc != null) {
			villagerNpc.onNPCLeftClickEvent(e);
		}
	}

	public static void onNPCDamageEvent(NPCDamageEvent e) {
		String id = getId(e.getNPC().getEntity());
		VillagerNpc villagerNpc = registedNpcIdMap.get(id);
		if (villagerNpc != null) {
			villagerNpc.onNPCDamageEvent(e);
		}
	}

	public static void onNPCSpawnEvent(NPCSpawnEvent e) {
		String id = getId(e.getNPC().getEntity());
		//スポーン済みにセットする
		spawnedNPCIDMap.put(id, e.getNPC());
		VillagerNpc villagerNpc = registedNpcIdMap.get(id);
		if (villagerNpc != null) {
			villagerNpc.onSpawn(e);
		}
	}

	public static void onNPCDespawnEvent(NPCDespawnEvent e) {
		String id = getId(e.getNPC().getEntity());
		//もしスポーン済み情報がセットされてるなら削除する
		if (e.getNPC().equals(spawnedNPCIDMap.get(id))) {
			spawnedNPCIDMap.remove(id);
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
