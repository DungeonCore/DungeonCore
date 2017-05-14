package net.l_bulb.dungeoncore.common.cooltime;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.dungeoncore.LbnRuntimeException;
import net.l_bulb.dungeoncore.util.Message;

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

  static HashMap<CooltimeKeyClass, Integer> create = new HashMap<>();

  Player p;
  Cooltimable cooltime;
  ItemStack item;

  /**
   * cooltimeを開始する
   */
  public void setCoolTime() {
    if (cooltime.getCooltimeTick(item) <= 0) { return; }

    if (create.size() > 1000000) {
      new LbnRuntimeException("cool time size is too big!!").printStackTrace();
    }
    create.put(new CooltimeKeyClass(p, cooltime), p.getTicksLived() + cooltime.getCooltimeTick(item));
  }

  public boolean canUse() {
    CooltimeKeyClass cooltimeKeyClass = new CooltimeKeyClass(p, cooltime);
    if (create.containsKey(cooltimeKeyClass)) {
      int canUseTickLive = create.get(cooltimeKeyClass);
      boolean canUse = canUseTickLive < p.getTicksLived();
      if (canUse) {
        create.remove(cooltimeKeyClass);
      }
      return canUse;
    }
    return true;
  }

  public int getRemainTick() {
    CooltimeKeyClass cooltimeKeyClass = new CooltimeKeyClass(p, cooltime);
    if (create.containsKey(cooltimeKeyClass)) {
      int canUseTickLive = create.get(cooltimeKeyClass);
      return Math.max(0, canUseTickLive - p.getTicksLived());
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

class CooltimeKeyClass {
  public CooltimeKeyClass(Player p, Cooltimable cooltime) {
    this.cooltimeName = cooltime.getId();
    this.playerName = p.getName();
  }

  String playerName;
  String cooltimeName;

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result
        + ((cooltimeName == null) ? 0 : cooltimeName.hashCode());
    result = prime * result
        + ((playerName == null) ? 0 : playerName.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    CooltimeKeyClass other = (CooltimeKeyClass) obj;
    if (cooltimeName == null) {
      if (other.cooltimeName != null)
        return false;
    } else if (!cooltimeName.equals(other.cooltimeName))
      return false;
    if (playerName == null) {
      if (other.playerName != null)
        return false;
    } else if (!playerName.equals(other.playerName))
      return false;
    return true;
  };

}
