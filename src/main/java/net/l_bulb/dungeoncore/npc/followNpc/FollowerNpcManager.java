package net.l_bulb.dungeoncore.npc.followNpc;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.scheduler.BukkitRunnable;

import net.l_bulb.dungeoncore.api.player.TheLowPlayer;
import net.l_bulb.dungeoncore.api.player.TheLowPlayerManager;
import net.l_bulb.dungeoncore.dungeoncore.Main;
import net.l_bulb.dungeoncore.npc.NpcManager;

import net.citizensnpcs.api.npc.NPC;

public class FollowerNpcManager {
  static HashMap<TheLowPlayer, FollowerNpc> map = new HashMap<>();

  /**
   * プラグインが開始するときのEvent
   */
  public static void onEnable() {

  }

  /**
   * プラグインインが終了するときのEvent
   */
  public static void onDisable() {
    // すべて削除する
    for (FollowerNpc npcFollowerNpc : map.values()) {
      NPC npc = npcFollowerNpc.getNpc();
      if (npc != null) {
        npc.destroy();
      }
    }
  }

  /**
   * NPCを削除する
   * 
   * @param p
   */
  public static void remove(Player p) {
    TheLowPlayer theLowPlayer = TheLowPlayerManager.getTheLowPlayer(p);
    if (theLowPlayer == null) {
      TheLowPlayerManager.sendLoingingMessage(p);
      return;
    }
    // デスポーンさせる
    FollowerNpc followerNpc = map.get(theLowPlayer);
    NPC npc = followerNpc.getNpc();
    if (npc != null) {
      npc.destroy();
    }

    // Mapから削除する
    map.remove(followerNpc);
  }

  /**
   * FollowerNpcを作成しスポーンする
   * 
   * @param p
   * @return
   */
  public static FollowerNpc createNpc(Player p) {
    TheLowPlayer theLowPlayer = TheLowPlayerManager.getTheLowPlayer(p);
    if (theLowPlayer == null) {
      TheLowPlayerManager.sendLoingingMessage(p);
      return null;
    }

    // NPCをスポーンさせる
    FollowerNpc followerNpc = new FollowerNpc(theLowPlayer);
    map.put(theLowPlayer, followerNpc);

    NpcManager.regist(followerNpc);
    return followerNpc;
  }

  /**
   * FollowNpcを取得する
   * 
   * @param p
   * @return
   */
  public static FollowerNpc getNpc(Player p) {
    TheLowPlayer theLowPlayer = TheLowPlayerManager.getTheLowPlayer(p);
    if (theLowPlayer == null) {
      TheLowPlayerManager.sendLoingingMessage(p);
      return null;
    }
    FollowerNpc followerNpc = map.get(theLowPlayer);
    return followerNpc;
  }

  /**
   * 自分のNPC以外を削除する
   * 
   * @param p
   */
  public static void hideAllFollowerNpc(Player p) {
    new BukkitRunnable() {
      @Override
      public void run() {
        for (Entry<TheLowPlayer, FollowerNpc> entry : map.entrySet()) {
          FollowerNpc value = entry.getValue();
          if (!value.getOwner().equalsPlayer(p)) {
            if (value.getNpc() != null) {
              p.hidePlayer((Player) value.getNpc().getEntity());
            }
          }
        }
      }
    }.runTaskLater(Main.plugin, 2);
  }

  /**
   * 指定されたPlayerに対応したNpcを削除する
   */
  public static void despawn(Player p) {
    TheLowPlayer theLowPlayer = TheLowPlayerManager.getTheLowPlayer(p);
    if (theLowPlayer == null) {
      TheLowPlayerManager.sendLoingingMessage(p);
      return;
    }

    // NPCを削除する
    FollowerNpc removedNpc = map.remove(theLowPlayer);
    if (removedNpc != null) {
      NPC npc = removedNpc.getNpc();
      if (npc != null) {
        npc.destroy();
      }
    }
  }

  /**
   * Playerがシフトした時のイベント
   * 
   * @param e
   */
  public static void onToggleEvent(PlayerToggleSneakEvent e) {
    Player player = e.getPlayer();
    TheLowPlayer theLowPlayer = TheLowPlayerManager.getTheLowPlayer(player);
    // PlayerがロードされていたらNPCもシフトする
    if (theLowPlayer != null) {
      // NPC取得
      FollowerNpc followerNpc = map.get(theLowPlayer);
      if (followerNpc != null) {
        // NPCをシフトさせる
        Entity entity = followerNpc.getNpc().getEntity();
        if (entity.getType() == EntityType.PLAYER) {
          // 5マス以内ならシフトさせる
          if (entity.getLocation().distance(player.getLocation()) < 5) {
            ((Player) entity).setSneaking(e.isSneaking());
          }
        }
      }
    }
  }
}
