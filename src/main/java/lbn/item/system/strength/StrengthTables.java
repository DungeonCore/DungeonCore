package lbn.item.system.strength;

import org.bukkit.entity.Player;

import lbn.api.player.TheLowPlayer;
import lbn.api.player.TheLowPlayerManager;
import lbn.common.trade.TheLowTrades;

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
