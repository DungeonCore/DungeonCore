package net.l_bulb.dungeoncore.npc;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import net.l_bulb.dungeoncore.npc.followNpc.FollowerNpcManager;

import net.citizensnpcs.api.event.NPCDamageByBlockEvent;
import net.citizensnpcs.api.event.NPCDamageByEntityEvent;
import net.citizensnpcs.api.event.NPCDamageEntityEvent;
import net.citizensnpcs.api.event.NPCDespawnEvent;
import net.citizensnpcs.api.event.NPCLeftClickEvent;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.event.NPCSpawnEvent;

public class NpcListener implements Listener {
  @EventHandler
  public void onNPCRightClickEvent(NPCRightClickEvent e) {
    NpcManager.onNPCRightClickEvent(e);
  }

  @EventHandler
  public void onNPCLeftClickEvent(NPCLeftClickEvent e) {
    NpcManager.onNPCLeftClickEvent(e);
  }

  @EventHandler
  public void onNPCDamageEvent(NPCDamageEntityEvent e) {
    NpcManager.onNPCDamageEvent(e);
    e.setCancelled(true);
  }

  @EventHandler
  public void onNPCDamageEvent(NPCDamageByBlockEvent e) {
    NpcManager.onNPCDamageEvent(e);
    e.setCancelled(true);
  }

  @EventHandler
  public void onNPCDamageEvent(NPCDamageByEntityEvent e) {
    NpcManager.onNPCDamageEvent(e);
    e.setCancelled(true);
  }

  @EventHandler(priority = EventPriority.MONITOR)
  public void onDamageEvent(EntityDamageEvent e) {
    if (NpcManager.isNpc(e.getEntity())) {
      e.setCancelled(true);
    }
  }

  @EventHandler
  public void onNPCSpawnEvent(NPCSpawnEvent e) {
    NpcManager.onNPCSpawnEvent(e);
  }

  @EventHandler
  public void onNPCDespawnEvent(NPCDespawnEvent e) {
    NpcManager.onNPCDespawnEvent(e);
  }

  @EventHandler
  public void onPlayerLogin(PlayerLoginEvent e) {
    FollowerNpcManager.hideAllFollowerNpc(e.getPlayer());
  }

  @EventHandler
  public void onTeleportWorld(PlayerChangedWorldEvent e) {
    FollowerNpcManager.despawn(e.getPlayer());
  }

  @EventHandler
  public void onShiftPlayer(PlayerToggleSneakEvent e) {
    FollowerNpcManager.onToggleEvent(e);
  }
}
