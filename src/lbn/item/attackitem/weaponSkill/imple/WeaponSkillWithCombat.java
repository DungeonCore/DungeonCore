package lbn.item.attackitem.weaponSkill.imple;

import java.util.HashMap;
import java.util.UUID;

import lbn.common.event.player.PlayerCombatEntityEvent;
import lbn.item.attackitem.AbstractAttackItem;
import lbn.player.ItemType;
import lbn.util.LbnRunnable;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

public abstract class WeaponSkillWithCombat extends WeaponSkillForOneType{
	public WeaponSkillWithCombat(ItemType type) {
		super(type);
	}

	HashMap<UUID, BukkitTask> executePlayer = new HashMap<>();

	@Override
	public boolean onClick(Player p, ItemStack item, AbstractAttackItem customItem) {
		//クリックしたPlayerを保存する
		UUID uniqueId = p.getUniqueId();

		//5tickに一度、パーティクルを発生させる
		BukkitTask runTaskTimer = new LbnRunnable() {
			@Override
			public void run2() {
				long ageTick = getAgeTick();
				if (ageTick > 20 * 5) {
					cancel();
				}
				runWaitParticleData(p.getLocation().add(0, 1, 0), getRunCount());
			}
		}.runTaskTimer(5);
		executePlayer.put(uniqueId, runTaskTimer);

		return true;
	}

	/**
	 * 攻撃待機中のパーティクル
	 * @param loc
	 * @param i
	 */
	abstract protected void runWaitParticleData(Location loc, int i);

	@Override
	public boolean onCombat(Player p, ItemStack item, AbstractAttackItem customItem, LivingEntity livingEntity, PlayerCombatEntityEvent event) {
		if (executePlayer.containsKey(p.getUniqueId())) {
			onCombat2(p, item, customItem, livingEntity, event);
			BukkitTask remove = executePlayer.remove(p.getUniqueId());
			remove.cancel();
		}
		return false;
	}

	/**
	 * 効果発動条件を満たして攻撃を行うときの処理
	 * @param p
	 * @param item
	 * @param customItem
	 * @param livingEntity
	 * @param e
	 */
	abstract protected void onCombat2(Player p, ItemStack item, AbstractAttackItem customItem, LivingEntity livingEntity, PlayerCombatEntityEvent e);
}
