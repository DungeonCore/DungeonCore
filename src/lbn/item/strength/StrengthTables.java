package lbn.item.strength;

import lbn.api.player.TheLowPlayer;
import lbn.api.player.TheLowPlayerManager;
import lbn.common.trade.TheLowTrades;

import org.bukkit.entity.Player;

public class StrengthTables {
	public static void  openStrengthTable(Player p) {
		TheLowPlayer theLowPlayer = TheLowPlayerManager.getTheLowPlayer(p);
		if (theLowPlayer != null) {
			TheLowTrades.open(new StrengthMarchant(p,theLowPlayer), p);
		} else {
			TheLowPlayerManager.sendLoingingMessage(p);
		}
	}
}
