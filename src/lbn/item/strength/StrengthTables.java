package lbn.item.strength;

import lbn.common.trade.TheLowTrades;

import org.bukkit.entity.Player;

public class StrengthTables {
	public static void  openStrengthTable(Player p) {
		TheLowTrades.open(new StrengthMarchant(p), p);
	}
}
