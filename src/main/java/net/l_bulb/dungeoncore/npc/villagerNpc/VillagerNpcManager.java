package net.l_bulb.dungeoncore.npc.villagerNpc;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.LookClose;
import net.l_bulb.dungeoncore.dungeoncore.Main;
import net.l_bulb.dungeoncore.npc.NpcManager;
import net.l_bulb.dungeoncore.npc.citizens.TheLowIdTrail;

public class VillagerNpcManager {
  static HashMap<String, VillagerNpc> registedVillagerNpcIdMap = new HashMap<String, VillagerNpc>();

  public static VillagerNpc getVillagerNpcById(String name) {
    return registedVillagerNpcIdMap.get(name);
  }

  public static Map<String, VillagerNpc> getNpcIdMap() {
    return registedVillagerNpcIdMap;
  }

  /**
   * NPCを登録する
   * 
   * @param villagerNpc
   */
  public static void regist(VillagerNpc villagerNpc) {
    String id = villagerNpc.getId();
    NPC spawnedNpc = NpcManager.getSpawnedNpc(id);
    if (spawnedNpc != null) {
      villagerNpc.setNpc(spawnedNpc);
    }
    registedVillagerNpcIdMap.put(id, villagerNpc);
  }

  /**
   * NPCをスポーンする
   * 
   * @param villagerNpc
   */
  public static void spawnNpc(VillagerNpc villagerNpc, Location loc) {
    try {
      // CitizenNPC作成
      NPC createNPC = CitizensAPI.getNPCRegistry().createNPC(villagerNpc.getEntityType(), villagerNpc.getName());
      createNPC.addTrait(TheLowIdTrail.fromId(villagerNpc.getId()));

      new BukkitRunnable() {
        @Override
        public void run() {
          // 振り向くようにする
          LookClose paramTrait = createNPC.hasTrait(LookClose.class) ? createNPC.getTrait(LookClose.class) : new LookClose();
          paramTrait.lookClose(true);
          createNPC.addTrait(paramTrait);
        }
      }.runTaskLater(Main.plugin, 20 * 1);

      if (loc != null && loc.getWorld() != null) {
        // チャンクがロードされてなければロードする
        if (!loc.getChunk().isLoaded()) {
          // チャンクをロードする
          loc.getChunk().load(true);
        }

        if (!createNPC.isSpawned()) {
          // スポーンさせる
          createNPC.spawn(loc);
        }
      }
      villagerNpc.setNpc(createNPC);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * EntityからVillagerNpcを取得
   * 
   * @param entity
   * @return
   */
  public static VillagerNpc getVillagerNpc(Entity entity) {
    if (entity == null) { return null; }
    if (!NpcManager.isNpc(entity)) { return null; }
    String id = NpcManager.getId(CitizensAPI.getNPCRegistry().getNPC(entity));
    return VillagerNpcManager.getVillagerNpcById(id);
  }
}
