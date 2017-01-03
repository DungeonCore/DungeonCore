package lbn.mob.mob.abstractmob.villager;

import lbn.quest.viewer.QuestSelectorViewer;

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

	@Override
	public void onDeathPrivate(EntityDeathEvent e) {
	}

}
