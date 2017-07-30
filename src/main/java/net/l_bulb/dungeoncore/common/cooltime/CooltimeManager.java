package net.l_bulb.dungeoncore.common.cooltime;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.util.Message;

import lombok.Data;
import net.md_5.bungee.api.ChatColor;

public class CooltimeManager {

  public static void clear() {
    create.clear();
  }

  public CooltimeManager(PlayerInteractEvent e, Cooltimable cooltime) {
    this(e.getPlayer(), cooltime, e.getItem());
  }

  public CooltimeManager(Player p, Cooltimable cooltime, ItemStack item) {
    this.p = p;
    this.cooltime = cooltime;
    this.item = item;
  }

  static HashMap<CooltimeKeyClass, Long> create = new HashMap<>();

  Player p;
  Cooltimable cooltime;
  ItemStack item;

  /**
   * cooltimeを開始する
   */
  public void setCoolTime() {
    Cooltimes.setCoolTime(p.getUniqueId(), cooltime, item);
  }

  /**
   * クールタイムが終了しているならTRUE
   * 
   * @return
   */
  public boolean canUse() {
    return Cooltimes.canUse(p.getUniqueId(), cooltime);
  }

  public int getRemainTick() {
    CooltimeKeyClass cooltimeKeyClass = new CooltimeKeyClass(p, cooltime);
    if (create.containsKey(cooltimeKeyClass)) {
      long canUseMilliesTime = create.get(cooltimeKeyClass);
      return (int) Math.max(0, (canUseMilliesTime - System.currentTimeMillis()) / 50.0);
    } else {
      return 0;
    }
  }

  /**
   * プレイヤーに残りクールタイムのメッセージを送る
   *
   * @param p
   */
  public void sendCooltimeMessage(Player p) {
    Message.sendMessage(p, ChatColor.GREEN + "[システム]" + ChatColor.RED + "クールタイム中です。(残り{0}秒)", getRemainTick() / 20);
  }
}

@Data
class CooltimeKeyClass {
  public CooltimeKeyClass(Player p, Cooltimable cooltime) {
    this.cooltimeName = cooltime.getId();
    this.playerUUID = p.getUniqueId();
  }

  UUID playerUUID;
  String cooltimeName;
}
