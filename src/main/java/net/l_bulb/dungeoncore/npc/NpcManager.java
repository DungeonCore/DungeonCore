package net.l_bulb.dungeoncore.npc;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;

import net.l_bulb.dungeoncore.dungeoncore.SpletSheet.SpletSheetExecutor;
import net.l_bulb.dungeoncore.dungeoncore.SpletSheet.VillagerSheetRunnable;
import net.l_bulb.dungeoncore.npc.citizens.RemoveNearNpcOnSpawnTrait;
import net.l_bulb.dungeoncore.npc.citizens.TheLowIdTrail;
import net.l_bulb.dungeoncore.npc.villagerNpc.VillagerNpc;
import net.l_bulb.dungeoncore.npc.villagerNpc.VillagerNpcManager;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.NPCDamageEvent;
import net.citizensnpcs.api.event.NPCDespawnEvent;
import net.citizensnpcs.api.event.NPCLeftClickEvent;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.event.NPCSpawnEvent;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.TraitInfo;
import net.citizensnpcs.trait.LookClose;

public class NpcManager {
  static HashMap<String, NPC> spawnedNpcMap = new HashMap<>();

  static HashMap<String, CustomNpcInterface> registedNpcIdMap = new HashMap<>();

  // static HashMap<String, NPC> spawnedNPCIDMap = new HashMap<String, NPC>();

  // static CitizensNPCRegistry citizensNPCRegistry = new CitizensNPCRegistry(SimpleNPCDataStore.create(new YamlStorage(file)));
  // static NPCRegistry citizensNPCRegistry = CitizensAPI.createNamedNPCRegistry("TheLowNpc", SimpleNPCDataStore.create(new YamlStorage(new
  // File(Main.dataFolder + File.separator + "npc.yml"))));

  public static void onTest() {
    Iterator<NPC> it = CitizensAPI.getNPCRegistry().iterator();
    while (it.hasNext()) {
      NPC npc = it.next();
      TheLowIdTrail trait = npc.getTrait(TheLowIdTrail.class);
      if (trait != null) {
        String id = trait.getId();
        if (id == null || id.isEmpty()) {
          trait.setId(npc.getName());
          npc.addTrait(trait);
        }
      }
      LookClose trait2 = npc.getTrait(LookClose.class);
      trait2.lookClose(true);

      npc.addTrait(trait2);
    }
  }

  /**
   * NPCを登録する
   *
   * @param villagerNpc
   */
  public static void regist(CustomNpcInterface villagerNpc) {
    registedNpcIdMap.put(villagerNpc.getId(), villagerNpc);

    if (villagerNpc instanceof VillagerNpc) {
      VillagerNpcManager.regist((VillagerNpc) villagerNpc);
    }
  }

  static Random random = new Random();

  /**
   * 指定されたエンチティがNPCならTRUE
   *
   * @param entity
   * @return
   */
  public static boolean isNpc(Entity entity) {
    return entity.hasMetadata("NPC");
  }

  /**
   * EntityからVillagerIdを取得
   *
   * @param npc
   * @return
   */
  public static String getId(NPC npc) {
    if (npc == null) { return null; }

    String id = null;

    // IDを振り分ける
    TheLowIdTrail trait = npc.getTrait(TheLowIdTrail.class);
    if (trait != null) {
      id = trait.getId();
      if (id != null && !id.isEmpty()) { return id; }
    }
    return null;
  }

  /**
   * NPCを右クリックをした時の処理
   *
   * @param e
   */
  public static void onNPCRightClickEvent(NPCRightClickEvent e) {
    String id = getId(e.getNPC());
    CustomNpcInterface villagerNpc = registedNpcIdMap.get(id);
    if (villagerNpc != null) {
      villagerNpc.onNPCRightClickEvent(e);
    }
  }

  /**
   * NPCを左クリックをした時の処理
   *
   * @param e
   */
  public static void onNPCLeftClickEvent(NPCLeftClickEvent e) {
    String id = getId(e.getNPC());
    CustomNpcInterface villagerNpc = registedNpcIdMap.get(id);
    if (villagerNpc != null) {
      villagerNpc.onNPCLeftClickEvent(e);
    }
  }

  /**
   * NPCがダメージを受けたときの処理
   *
   * @param e
   */
  public static void onNPCDamageEvent(NPCDamageEvent e) {
    String id = getId(e.getNPC());
    CustomNpcInterface villagerNpc = registedNpcIdMap.get(id);
    if (villagerNpc != null) {
      villagerNpc.onNPCDamageEvent(e);
    }
  }

  /**
   * NPCがスポーンした時の処理
   *
   * @param e
   */
  public static void onNPCSpawnEvent(NPCSpawnEvent e) {
    String id = getId(e.getNPC());

    // スポーン済みにセットする
    if (id != null && !id.isEmpty()) {
      spawnedNpcMap.put(id, e.getNPC());
    }
  }

  /**
   * NPCがデスポーンした時の処理
   *
   * @param e
   */
  public static void onNPCDespawnEvent(NPCDespawnEvent e) {}

  /**
   * スプレットシートのデータを取得する
   *
   * @param sender
   */
  public static void allReload(CommandSender sender) {
    if (sender == null) {
      sender = Bukkit.getConsoleSender();
    }
    VillagerSheetRunnable villagerSheetRunnable = new VillagerSheetRunnable(sender);
    villagerSheetRunnable.getData(null);
    SpletSheetExecutor.onExecute(villagerSheetRunnable);
  }

  /**
   * IDからNPCを取得する
   *
   * @return
   */
  public static NPC getSpawnedNpc(String id) {
    return spawnedNpcMap.get(id);
  }

  /**
   * EntityからIDを取得する
   *
   * @param e
   * @return
   */
  public static String getId(Entity e) {
    // NPCを取得
    NPC npc = CitizensAPI.getNPCRegistry().getNPC(e);
    if (npc == null) { return null; }
    // IDを取得
    String id = NpcManager.getId(npc);
    if (id == null) { return null; }
    return id;
  }

  public static void init() {
    CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(TheLowIdTrail.class).withName(new TheLowIdTrail().getName()));
    CitizensAPI.getTraitFactory()
        .registerTrait(TraitInfo.create(RemoveNearNpcOnSpawnTrait.class).asDefaultTrait().withName(new RemoveNearNpcOnSpawnTrait().getName()));
  }
}
