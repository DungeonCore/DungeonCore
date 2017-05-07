package net.l_bulb.dungeoncore.item.system.strength;

import org.bukkit.entity.Player;

import net.l_bulb.dungeoncore.api.player.TheLowPlayer;
import net.l_bulb.dungeoncore.api.player.TheLowPlayerManager;
import net.l_bulb.dungeoncore.common.trade.TheLowTrades;

public class StrengthTables {
  public static void openStrengthTable(Player p) {
    TheLowPlayer theLowPlayer = TheLowPlayerManager.getTheLowPlayer(p);
    if (theLowPlayer != null) {
      TheLowTrades.open(new StrengthMerchant(p, theLowPlayer), p);
    } else {
      TheLowPlayerManager.sendLoingingMessage(p);
    }
  }
}
