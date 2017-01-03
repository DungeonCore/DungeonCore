package lbn.mob.mob.abstractmob.villager;

import java.util.Arrays;
import java.util.List;

import lbn.common.menu.MenuSelectorManager;
import lbn.money.BuyerShopSelector;
import lbn.quest.viewer.QuestSelectorViewer;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public class SpletSheetVillager extends AbstractVillager{

	VillagerData data;

	public SpletSheetVillager(VillagerData data) {
		this.data = data;
	}

	public String getData() {
		return getVillageData().getData();
	}

	@Override
	protected List<String> getMessage(Player p, LivingEntity mob) {
		return Arrays.asList(data.getTexts());
	}

	@Override
	public String getName() {
		return data.getName();
	}

	@Override
	public void onDamage(LivingEntity mob, Entity damager,
			EntityDamageByEntityEvent e) {
		e.setCancelled(true);
		if (damager.getType() != EntityType.PLAYER) {
			return;
		}
		super.onDamage(mob, damager, e);
		if (!isExecuteOnDamage) {
			return;
		}

		if (data.getType() == VillagerType.NORMAL) {
			QuestSelectorViewer.openSelector(this, (Player)damager);
		} else if (data.getType() == VillagerType.SHOP) {
			BuyerShopSelector.onOpen((Player) damager, getName());
		} if (data.getType() == VillagerType.BLACKSMITH) {
			MenuSelectorManager.open((Player) damager, "blacksmith menu");
		}
	}

	@Override
	public Location getLocation() {
		return data.getLocation();
	}

	@Override
	public void onAttack(LivingEntity mob, LivingEntity target,
			EntityDamageByEntityEvent e) {
	}

	@Override
	public void onDeathPrivate(EntityDeathEvent e) {
	}
}
