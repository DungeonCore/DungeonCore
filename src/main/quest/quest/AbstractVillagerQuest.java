package main.quest.quest;

import main.quest.Quest;
import main.quest.QuestManager;
import main.util.Message;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public abstract class AbstractVillagerQuest implements Quest{
	@Override
	public int hashCode() {
		return getId().hashCode();
	}

	abstract protected void init();

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj instanceof Quest) {
			return ((Quest) obj).getName().equals(getName());
		}
		return false;
	}

	@Override
	public String toString() {
		return getName() + "(" + getId() + ")";
	}

	@Override
	public void playCompleteSound(Player p) {
		p.playSound(p.getLocation(), Sound.LEVEL_UP, 1, (float) 0.1);
	}

	@Override
	public void playDistructionSound(Player p) {
		p.playSound(p.getLocation(), Sound.VILLAGER_DEATH, 1, (float) 0.1);
	}

	@Override
	public void playStartSound(Player p) {
		p.playSound(p.getLocation(), Sound.NOTE_PLING, 1, (float) 0.1);
	}

	@Override
	public boolean isDoing(Player p) {
		return QuestManager.isDoingQuest(this, p);
	}

	@Override
	public boolean canDestory() {
		return !isMainQuest();
	}

	/**
	 * クエスト進展メッセージ
	 * @param player
	 */
	protected void sendProgressMessage(Player player, int needCount, int nowCount) {
		player.sendMessage(StringUtils.join(new Object[]{ChatColor.GOLD, "[Quest]", ChatColor.GREEN, getName(), "  (", nowCount, "/", needCount, ")"}));
	}

	/**
	 * クエストクリアメッセージ
	 * @param player
	 */
	protected void sendQuestClearMessage(Player player) {
		Message.sendMessage(player, StringUtils.join(new Object[]{ChatColor.GREEN, ChatColor.BOLD + Message.getMessage(player, "クエスト達成！！ 村人のところに戻ろう")}));
	}
	
}
