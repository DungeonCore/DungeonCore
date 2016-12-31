package lbn.mob.mob.abstractmob.villager;

import java.util.Arrays;
import java.util.List;

import lbn.common.menu.MenuSelectorManager;
import lbn.money.BuyerShopSelector;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class SpletSheetVillager extends QuestVillager{

	VillagerData data;

	public SpletSheetVillager(VillagerData data) {
		this.data = data;
	}

	public String getData() {
		return getVillageData().getData();
	}

	@Override
	public String[] getHaveQuest() {
		return data.questList.toArray(new String[0]);
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

		if (data.getType() == VillagerType.NORMAL) {
			super.onDamage(mob, damager, e);
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
}
