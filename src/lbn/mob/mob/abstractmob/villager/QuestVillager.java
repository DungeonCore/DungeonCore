package lbn.mob.mob.abstractmob.villager;

import java.util.ArrayList;
import java.util.Collection;

import lbn.quest.Quest;
import lbn.quest.questData.PlayerQuestSession;
import lbn.quest.questData.PlayerQuestSessionManager;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public abstract class QuestVillager extends AbstractVillager{
	@Override
	public void onAttack(LivingEntity mob, LivingEntity target,
			EntityDamageByEntityEvent e) {
	}

	@Override
	public void onDamage(LivingEntity mob, Entity damager,
			EntityDamageByEntityEvent e) {
		super.onDamage(mob, damager, e);
		onQuestExcute((Player)damager, mob);
	}

	public Quest getAvailableQuest(Player p) {
		if (getHaveQuest() == null || getHaveQuest().length == 0) {
			return null;
		}

		PlayerQuestSession questSession = PlayerQuestSessionManager.getQuestSession(p);

		Collection<Quest> complateQuest = questSession.getComplateQuestList();
		for (Quest quest : getHaveQuest()) {
			//もし前のクエストが完了していないならスキップする
			if (quest.getBeforeQuest() != null) {
				continue;
			}

			if (!complateQuest.containsAll(quest.getBeforeQuest())) {
				continue;
			}

			//自身のクエストが完了している時
			if (complateQuest.contains(quest)) {
				//重複を許すならこのクエストを返す
				if (quest.isStartOverlap()) {
					return quest;
				} else {
					continue;
				}
			} else {
				return quest;
			}
		}
		return null;
	}

	protected String getDetail(Quest quest, Player p) {
		PlayerQuestSession session = PlayerQuestSessionManager.getQuestSession(p);
		if (session.isDoing(quest)) {
			return StringUtils.join(new Object[]{ChatColor.GOLD , getName() , ChatColor.WHITE , "「" , quest.getQuestDetail() , "」", ChatColor.RED, quest.getCurrentInfo(p)});
		} else {
			return StringUtils.join(new Object[]{ChatColor.GOLD , getName() , ChatColor.WHITE , "「" , quest.getQuestDetail() , "」"});
		}
	}

	protected void onQuestExcute(Player player, LivingEntity villager) {
		ArrayList<String> messageList = new ArrayList<String>();
		//クエストを持っていないなら何もしない
		if (getHaveQuest() == null || getHaveQuest().length == 0) {
			return;
		}
		PlayerQuestSession session = PlayerQuestSessionManager.getQuestSession(player);
		//次受注するであろうクエストの説明を出す
		Quest quest = getAvailableQuest(player);
		if (quest == null) {
			return;
		}
		//クエストの詳細を表示する
		messageList.add(getDetail(quest, player));

		player.sendMessage(messageList.toArray(new String[0]));
		//もしクエストを実行していないならクエストを実行するためのコマンドを出す
		if (!session.isDoing(quest)) {
			//TODO Quest Selecetor追加
//			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), QuestManager.getStartTellrowCommand(player, quest));
		}
	}


	@Override
	public void onDeathPrivate(EntityDeathEvent e) {
	}

	abstract public Quest[] getHaveQuest();
}
