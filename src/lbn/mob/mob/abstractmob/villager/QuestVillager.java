package lbn.mob.mob.abstractmob.villager;

import java.util.ArrayList;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import lbn.quest.Quest;
import lbn.quest.QuestManager;
import lbn.quest.quest.TouchVillagerQuest;

public abstract class QuestVillager extends AbstractVillager{
	@Override
	public void onAttack(LivingEntity mob, LivingEntity target,
			EntityDamageByEntityEvent e) {
	}

	@Override
	public void onDamage(LivingEntity mob, Entity damager,
			EntityDamageByEntityEvent e) {
		super.onDamage(mob, damager, e);
		if (damager.getType() == EntityType.PLAYER && mob.getType() == EntityType.VILLAGER) {
			onQuestExcute((Player)damager, (Villager)mob);
		}
	}

	public Quest getAvailableQuest(Player p) {
		if (getHaveQuest() == null || getHaveQuest().length == 0) {
			return null;
		}

		Set<Quest> complateQuest = QuestManager.getComplateQuest(p);
		for (Quest quest : getHaveQuest()) {
			//もし前のクエストが完了していないならスキップする
			if (quest.getBeforeQuest() != null && !complateQuest.containsAll(quest.getBeforeQuest())) {
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
		if (quest.isDoing(p)) {
			return StringUtils.join(new Object[]{ChatColor.GOLD , getName() , ChatColor.WHITE , "「" , quest.getQuestDetail() , "」", ChatColor.RED, quest.getCurrentInfo(p)});
		} else {
			return StringUtils.join(new Object[]{ChatColor.GOLD , getName() , ChatColor.WHITE , "「" , quest.getQuestDetail() , "」"});
		}
	}

	protected void onQuestExcute(Player player, Villager villager) {
		//この村人をタッチするクエストで進行中のクエストがあったらクエストは開始しない
		boolean isDoingTouchQuest = false;
		Set<TouchVillagerQuest> questForVillager = TouchVillagerQuest.getQuestByTargetVillager(villager);
		for (TouchVillagerQuest touchVillagerQuest : questForVillager) {
			if (QuestManager.isDoingQuest(touchVillagerQuest, player)) {
				touchVillagerQuest.onTouchVillager(player, villager);
				isDoingTouchQuest = true;
			}
		}
		if (isDoingTouchQuest) {
			return;
		}

		ArrayList<String> messageList = new ArrayList<String>();
		//クエストを持っていないなら何もしない
		if (getHaveQuest() == null || getHaveQuest().length == 0) {
			return;
		}
		//次受注するであろうクエストの説明を出す
		Quest quest = getAvailableQuest(player);
		if (quest == null) {
			return;
		}
		//クエストの詳細を表示する
		messageList.add(getDetail(quest, player));

		player.sendMessage(messageList.toArray(new String[0]));
		//もしクエストを実行していないならクエストを実行するためのコマンドを出す
		if (!QuestManager.isDoingQuest(quest, player)) {
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), QuestManager.getStartTellrowCommand(player, quest));
		}
	}


	@Override
	public void onDeathPrivate(EntityDeathEvent e) {
	}

	abstract public Quest[] getHaveQuest();
}
