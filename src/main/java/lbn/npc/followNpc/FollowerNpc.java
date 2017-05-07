package lbn.npc.followNpc;

import java.util.Collection;

import net.aufdemrand.sentry.SentryInstance;
import net.aufdemrand.sentry.SentryTrait;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.event.DespawnReason;
import net.citizensnpcs.api.event.NPCDamageEvent;
import net.citizensnpcs.api.event.NPCLeftClickEvent;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.util.Util;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import lbn.api.player.TheLowPlayer;
import lbn.common.menu.MenuSelectorManager;
import lbn.dungeoncore.Main;
import lbn.npc.CustomNpcInterface;
import lbn.npc.citizens.TheLowIdTrail;
import lbn.util.PacketUtil;

public class FollowerNpc implements CustomNpcInterface {
  static {
    MenuSelectorManager.regist(new SilfiaNpcMenu());
  }
  private TheLowPlayer p;

  public FollowerNpc(TheLowPlayer p) {
    this.p = p;
    spawn();
  }

  public TheLowPlayer getOwner() {
    return p;
  }

  NPC npc;

  public NPC getNpc() {
    return npc;
  }

  @Override
  public void setNpc(NPC npc) {
    this.npc = npc;
  }

  /**
   * NPCをスポーンする
   */
  private void spawn() {
    // CitizenNPC作成
    NPC createNPC = CitizensAPI.getNPCRegistry().createNPC(getEntityType(), getName());
    npc = createNPC;

    createNPC.addTrait(TheLowIdTrail.fromId(getId()));

    // 戦闘Traitを追加
    SentryTrait sentryTrait = new SentryTrait();
    createNPC.addTrait(sentryTrait);

    createNPC.data().setPersistent("player-skin-name", getSkinName());
    createNPC.spawn(p.getOnlinePlayer().getLocation());

    SentryInstance instance = sentryTrait.getInstance();
    instance.setGuardTarget(p.getName(), true);

    if (createNPC.isSpawned()) {
      createNPC.despawn(DespawnReason.PENDING_RESPAWN);
      createNPC.spawn(createNPC.getStoredLocation());
    }

    hideNpcForOtherPlayer();
  }

  /**
   * 他のPlayerから見えないようにする
   */
  public void hideNpcForOtherPlayer() {
    if (npc == null) { return; }
    new BukkitRunnable() {
      @Override
      public void run() {
        Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
        for (Player player : onlinePlayers) {
          if (!p.equalsPlayer(player)) {
            player.hidePlayer(((Player) npc.getEntity()));
          }
        }
      }
    }.runTaskLater(Main.plugin, 2);
  }

  public String getSkinName() {
    return "shilfia";
  }

  @Override
  public void onNPCRightClickEvent(NPCRightClickEvent e) {
    if (e.getClicker().isSneaking() && getOwner().equalsPlayer(e.getClicker())) {
      SilfiaNpcMenu silfiaNpcMenu = new SilfiaNpcMenu();
      silfiaNpcMenu.open(e.getClicker());
    }
  }

  @Override
  public EntityType getEntityType() {
    return EntityType.PLAYER;
  }

  @Override
  public void onNPCLeftClickEvent(NPCLeftClickEvent e) {
    if (getOwner().equalsPlayer(e.getClicker())) {
      // 向く方向を設定
      Util.faceEntity(e.getNPC().getEntity(), e.getClicker());
      PacketUtil.sendAttackMotionPacket((LivingEntity) e.getNPC().getEntity());
    }
  }

  @Override
  public void onNPCDamageEvent(NPCDamageEvent e) {}

  @Override
  public String getId() {
    return p.getName() + "_" + getName() + "_follower";
  }

  @Override
  public String getName() {
    return "シルフィア";
  }

}
