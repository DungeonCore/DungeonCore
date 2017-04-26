package lbn.npc.citizens;

import java.util.List;

import lbn.dungeoncore.Main;
import lbn.npc.NpcManager;
import lbn.npc.villagerNpc.VillagerNpc;
import lbn.npc.villagerNpc.VillagerNpcManager;
import lbn.util.DungeonLogger;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.Trait;

import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

public class RemoveNearNpcOnSpawnTrait extends Trait {

  public RemoveNearNpcOnSpawnTrait() {
    super("THELOW_NEAR_REMOVE_ONSPAWN");
  }

  @Override
  public void onSpawn() {
    removeNearMob(getNPC());
  }

  private void removeNearMob(NPC npc) {
    if (npc == null) { return; }

    Entity bukkitEntity = npc.getEntity();
    if (bukkitEntity == null) { return; }

    String id = NpcManager.getId(npc);
    if (id == null) { return; }

    new BukkitRunnable() {
      @Override
      public void run() {
        VillagerNpc thisNpc = VillagerNpcManager.getVillagerNpc(npc.getEntity());
        if (thisNpc == null) { return; }

        List<Entity> nearbyEntities = bukkitEntity.getNearbyEntities(100, 50, 100);
        for (Entity target : nearbyEntities) {
          // 名前・Typeが異なるなら削除
          if (bukkitEntity.getType() != target.getType() || !npc.getName().equals(target.getName())) {
            continue;
          }
          if (!target.equals(bukkitEntity)) {
            NPC targetNpc = CitizensAPI.getNPCRegistry().getNPC(target);
            if (targetNpc != null) {
              // NPCが同じなら何もしない
              if (targetNpc.getId() == npc.getId()) {
                continue;
              }
              String id2 = NpcManager.getId(targetNpc);
              // IDが同じなら削除
              if (id2 != null && id2.equals(thisNpc.getId())) {
                targetNpc.destroy();
                DungeonLogger.development("npc:" + id2 + " is destoried(1) by " + thisNpc.getId());
              }
            }
          }
        }
      }
    }.runTaskLater(Main.plugin, 20 * 10);
  }
}
