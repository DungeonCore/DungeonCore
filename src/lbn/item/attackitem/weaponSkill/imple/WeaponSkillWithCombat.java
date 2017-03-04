package lbn.item.attackitem.weaponSkill.imple;

import java.util.HashSet;
import java.util.UUID;

import lbn.common.event.player.PlayerCombatEntityEvent;
import lbn.dungeoncore.Main;
import lbn.item.attackitem.AbstractAttackItem;
import lbn.player.ItemType;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class WeaponSkillWithCombat extends WeaponSkillForOneType{
	public WeaponSkillWithCombat(ItemType type) {
		super(type);
	}

	HashSet<UUID> executePlayer = new HashSet<UUID>();

	@Override
	public boolean onClick(Player p, ItemStack item, AbstractAttackItem customItem) {
		//クリックしたPlayerを保存する
		UUID uniqueId = p.getUniqueId();
		executePlayer.add(uniqueId);
		//５秒後に削除する
		new BukkitRunnable() {
			@Override
			public void run() {
				executePlayer.remove(uniqueId);
			}
		}.runTaskLater(Main.plugin, 20 * 5);

		return true;
	}

	@Override
	public boolean onCombat(Player p, ItemStack item, AbstractAttackItem customItem, LivingEntity livingEntity, PlayerCombatEntityEvent event) {
		if (executePlayer.contains(p.getUniqueId())) {
			onCombat2(p, item, customItem, livingEntity, event);
			executePlayer.remove(p.getUniqueId());
		}
		return false;
	}

	/**
	 * 効果発動条件を満たして攻撃を行うときの処理
	 * @param p
	 * @param item
	 * @param customItem
	 * @param livingEntity
	 * @param e TODO
	 */
	abstract protected void onCombat2(Player p, ItemStack item, AbstractAttackItem customItem, LivingEntity livingEntity, PlayerCombatEntityEvent e);
}
