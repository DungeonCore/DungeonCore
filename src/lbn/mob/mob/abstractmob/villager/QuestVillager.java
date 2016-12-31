package lbn.mob.mob.abstractmob.villager;

import lbn.quest.Quest;
import lbn.quest.questData.PlayerQuestSession;
import lbn.quest.questData.PlayerQuestSessionManager;
import lbn.quest.viewer.QuestSelectorViewer;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
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
	public void onDamage(LivingEntity mob, Entity damager, EntityDamageByEntityEvent e) {
		super.onDamage(mob, damager, e);
		if (damager.getType() == EntityType.PLAYER) {
			QuestSelectorViewer.openSelector(this, (Player)damager);
		}
	}

	protected String getDetail(Quest quest, Player p) {
		PlayerQuestSession session = PlayerQuestSessionManager.getQuestSession(p);
		if (session.isDoing(quest)) {
			return StringUtils.join(new Object[]{ChatColor.GOLD , getName() , ChatColor.WHITE , "「" , quest.getQuestDetail() , "」", ChatColor.RED, quest.getCurrentInfo(p)});
		} else {
			return StringUtils.join(new Object[]{ChatColor.GOLD , getName() , ChatColor.WHITE , "「" , quest.getQuestDetail() , "」"});
		}
	}

	@Override
	public void onDeathPrivate(EntityDeathEvent e) {
	}

	abstract public String[] getHaveQuest();
}
