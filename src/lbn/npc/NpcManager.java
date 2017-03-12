package lbn.npc;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import lbn.dungeoncore.SpletSheet.SpletSheetExecutor;
import lbn.dungeoncore.SpletSheet.VillagerSheetRunnable;
import lbn.npc.citizens.RemoveNearNpcOnSpawnTrait;
import lbn.npc.citizens.TheLowIdTrail;
import lbn.util.DungeonLogger;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.NPCDamageEvent;
import net.citizensnpcs.api.event.NPCDespawnEvent;
import net.citizensnpcs.api.event.NPCLeftClickEvent;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.event.NPCSpawnEvent;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.TraitInfo;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;

public class NpcManager {
	static HashMap<String, VillagerNpc> registedNpcIdMap = new HashMap<String, VillagerNpc>();

//	static HashMap<String, NPC> spawnedNPCIDMap = new HashMap<String, NPC>();

//	static CitizensNPCRegistry citizensNPCRegistry = new CitizensNPCRegistry(SimpleNPCDataStore.create(new YamlStorage(file)));
//	static NPCRegistry citizensNPCRegistry = CitizensAPI.createNamedNPCRegistry("TheLowNpc", SimpleNPCDataStore.create(new YamlStorage(new File(Main.dataFolder + File.separator + "npc.yml"))));

	public static VillagerNpc getVillagerNpcById(String name) {
		return registedNpcIdMap.get(name);
	}
//
	public static void onTest() {
		for (Entry<String, VillagerNpc> entry : registedNpcIdMap.entrySet()) {
			VillagerNpc value = entry.getValue();
			Location location = value.getLocation();
			if (location == null) {
				continue;
			}
			DungeonLogger.debug(value.getName() + " is spawned");
			spawnNpc(value);
		}
	}

	public static Map<String, VillagerNpc> getNpcIdMap() {
		return registedNpcIdMap;
	}

	/**
	 * NPCを登録する
	 * @param villagerNpc
	 */
	public static void regist(VillagerNpc villagerNpc) {
		registedNpcIdMap.put(villagerNpc.getId(), villagerNpc);
	}

	static Random random = new Random();
	/**
	 * NPCをスポーンする
	 * @param villagerNpc
	 */
	public static void spawnNpc(VillagerNpc villagerNpc) {
		try {
			//CitizenNPC作成
			NPC createNPC = CitizensAPI.getNPCRegistry().createNPC(villagerNpc.getEntityType(),villagerNpc.getName());
			createNPC.addTrait(TheLowIdTrail.fromId(villagerNpc.getId()));

			Location location = villagerNpc.getLocation();

			if (location != null && location.getWorld() != null) {
				//チャンクがロードされてなければロードする
				if (!location.getChunk().isLoaded()) {
					//チャンクをロードする
					location.getChunk().load(true);
				}

				if (!createNPC.isSpawned()) {
					//スポーンさせる
					createNPC.spawn(location);
					//IDをつける
				}
			}
			villagerNpc.setNpc(createNPC);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 指定されたエンチティがNPCならTRUE
	 * @param entity
	 * @return
	 */
	public static boolean isNpc(Entity entity) {
		return entity.hasMetadata("NPC");
	}

	/**
	 * EntityからVillagerNpcを取得
	 * @param entity
	 * @return
	 */
	public static VillagerNpc getVillagerNpc(Entity entity) {
		if (!isNpc(entity)) {
			return null;
		}
		String id = getId(CitizensAPI.getNPCRegistry().getNPC(entity));
		return getVillagerNpcById(id);
	}

	/**
	 * EntityからVillagerIdを取得
	 * @param npc
	 * @return
	 */
	public static String getId(NPC npc) {
		if (npc == null) {
			return null;
		}

		//IDを振り分ける
		TheLowIdTrail trait = npc.getTrait(TheLowIdTrail.class);
		if (trait != null) {
			return trait.getId();
		}
		return null;
	}

	/**
	 * NPCを右クリックをした時の処理
	 * @param e
	 */
	public static void onNPCRightClickEvent(NPCRightClickEvent e) {
		String id = getId(e.getNPC());
		VillagerNpc villagerNpc = registedNpcIdMap.get(id);
		if (villagerNpc != null) {
			villagerNpc.onNPCRightClickEvent(e);
		}
	}

	/**
	 * NPCを左クリックをした時の処理
	 * @param e
	 */
	public static void onNPCLeftClickEvent(NPCLeftClickEvent e) {
		String id = getId(e.getNPC());
		VillagerNpc villagerNpc = registedNpcIdMap.get(id);
		if (villagerNpc != null) {
			villagerNpc.onNPCLeftClickEvent(e);
		}
	}

	/**
	 * NPCがダメージを受けたときの処理
	 * @param e
	 */
	public static void onNPCDamageEvent(NPCDamageEvent e) {
		String id = getId(e.getNPC());
		VillagerNpc villagerNpc = registedNpcIdMap.get(id);
		if (villagerNpc != null) {
			villagerNpc.onNPCDamageEvent(e);
		}
	}

	/**
	 * NPCがスポーンした時の処理
	 * @param e
	 */
	public static void onNPCSpawnEvent(NPCSpawnEvent e) {
		String id = getId(e.getNPC());
		//スポーン済みにセットする
		VillagerNpc villagerNpc = registedNpcIdMap.get(id);
		if (villagerNpc != null) {
			villagerNpc.onSpawn(e);
		}
	}

	/**
	 * NPCがデスポーンした時の処理
	 * @param e
	 */
	public static void onNPCDespawnEvent(NPCDespawnEvent e) {
	}


	/**
	 * スプレットシートのデータを取得する
	 * @param sender
	 */
	public static void allReload(CommandSender sender) {
		if (sender == null) {
			sender = Bukkit.getConsoleSender();
		}
//		NpcManager.allRemove();

		VillagerSheetRunnable villagerSheetRunnable = new VillagerSheetRunnable(sender);
		villagerSheetRunnable.getData(null);
		SpletSheetExecutor.onExecute(villagerSheetRunnable);
	}

//	/**
//	 * 全てのNPCを削除する
//	 */
//	public static void allRemove() {
//		Iterator<NPC> iterator = CitizensAPI.getNPCRegistry().iterator();
//		while (iterator.hasNext()) {
//			NPC npc = iterator.next();
//			Entity entity = npc.getEntity();
//			if (entity != null && entity.getLocation() != null) {
//				JavaUtil.chunkLoadIfUnload(entity.getLocation().getChunk());
//				entity.remove();
//				iterator.remove();
//			}
//		}
//	}

	public static void init() {
		CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(TheLowIdTrail.class).withName(new TheLowIdTrail().getName()));
		CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(RemoveNearNpcOnSpawnTrait.class).asDefaultTrait().withName(new RemoveNearNpcOnSpawnTrait().getName()));
	}
}
