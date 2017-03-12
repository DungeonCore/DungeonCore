package lbn.npc.citizens;

import java.util.List;

import lbn.util.DungeonLogger;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.Trait;

import org.bukkit.entity.Entity;

public class RemoveNearNpcOnSpawnTrait extends Trait{

	public RemoveNearNpcOnSpawnTrait() {
		super("THELOW_NEAR_REMOVE_ONSPAWN");
	}

	@Override
	public void onSpawn() {
		removeNearMob(getNPC());
	}

	private void removeNearMob(NPC npc) {
		if (npc == null) {
			return;
		}

		Entity bukkitEntity = npc.getEntity();
		if (bukkitEntity == null) {
			return;
		}

		List<Entity> nearbyEntities = bukkitEntity.getNearbyEntities(10, 10, 10);
		for (Entity target : nearbyEntities) {
			if (bukkitEntity.getType() == target.getType() && npc.getName().equals(target.getName())) {
				if (!target.equals(bukkitEntity)) {
					NPC targetNpc = CitizensAPI.getNPCRegistry().getNPC(target);
					if (targetNpc != null) {
						//同じなら何もしない
						if (targetNpc.getId() != npc.getId()) {
							targetNpc.destroy();
							DungeonLogger.development("npc:" + npc.getName() + " is destoried(1)");
						}
					} else {
						target.remove();
						DungeonLogger.development("npc:" + npc.getName() + " is destoried(2)");
					}
				}
			}
		}
	}

}
