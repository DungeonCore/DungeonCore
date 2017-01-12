package lbn.money.galion;

import lbn.util.Message;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public enum GalionEditReason {
	mob_drop(true),
	penalty(true),
	consume_shop(true),
	consume_strength(true),
	command(true),
	get_money_item(true),
	quest_reword(true),
	system(false);

	boolean isPrintMessageLog;

	private GalionEditReason(boolean isPrintMessageLog) {
		this.isPrintMessageLog = isPrintMessageLog;
	}

	public boolean isPrintMessageLog() {
		return isPrintMessageLog;
	}

	/**
	 * お金の取得・消費のメッセージをPlayerに表示する
	 * @param p
	 * @param galions
	 */
	public void sendMessageLog(Player p, int galions) {
		if (galions == 0) {
			return;
		}

		if (!isPrintMessageLog()) {
			return;
		}

		boolean isMinus = (galions < 0);
		if (isMinus) {
			if (this == consume_shop || this == consume_strength) {
				Message.sendMessage(p, ChatColor.YELLOW + "{0} Galions 消費した", galions);
			} else {
				Message.sendMessage(p, ChatColor.YELLOW + "{0} Galions 失った", galions);
			}
		} else {
			Message.sendMessage(p,  ChatColor.YELLOW + "{0} Galions 入手した", galions);
		}

	}
}
