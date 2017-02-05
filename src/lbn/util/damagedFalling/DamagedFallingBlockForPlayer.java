package lbn.util.damagedFalling;

import lbn.common.event.player.PlayerCombatEntityEvent;
import lbn.common.event.player.PlayerKillEntityEvent;
import lbn.mob.LastDamageManager;
import lbn.mob.LastDamageMethodType;
import lbn.util.LivingEntityUtil;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

public abstract class DamagedFallingBlockForPlayer extends AbstractDamageFallingblock{

	protected ItemStack item;
	protected Player p;
	protected double damage;

	public DamagedFallingBlockForPlayer(Player p, Material m, ItemStack item, double damage) {
		this(p.getLocation().getDirection(), p.getLocation().add(0, 1, 0), m, item, p, damage, (byte) 0);
	}
	public DamagedFallingBlockForPlayer(Player p, Material m, ItemStack item, double damage, byte data) {
		this(p.getLocation().getDirection(), p.getLocation().add(0, 1.2, 0), m, item, p, damage, data);
	}
	public DamagedFallingBlockForPlayer(Vector direction, Location loc, Material m, ItemStack item, Player p, double damage, byte data) {
		super(direction, loc, m, data);
		this.item = item;
		this.p = p;
		this.damage = damage;
	}

	@Override
	public synchronized BukkitTask runTaskTimer() throws IllegalArgumentException, IllegalStateException {
		startEntityRutine(p);
		return super.runTaskTimer();
	}

	protected boolean damageLivingentity(Entity entity) {
		if (LivingEntityUtil.isEnemy(entity)) {
			//すでに死んでいたら何もしない
			if (entity.isDead()) {
				return false;
			}

			//LastDamageを登録
			LastDamageManager.onDamage(((LivingEntity)entity), p, getAttackType());

			//eventを実行
			PlayerCombatEntityEvent playerCombatEntityEvent = new PlayerCombatEntityEvent(p, (LivingEntity) entity, item, damage);
			playerCombatEntityEvent.callEvent();
			//ダメージを与える
			((LivingEntity)entity).damage(playerCombatEntityEvent.getDamage(), p);

			//もしEntityが死んでいたら今の攻撃で死んだと判断しeventを実行
			if (entity.isDead()) {
				PlayerKillEntityEvent playerKillEntityEvent = new PlayerKillEntityEvent(p, (LivingEntity) entity, item);
				playerKillEntityEvent.callEvent();
			}
			return true;
		}
		return false;
	}

	protected LastDamageMethodType getAttackType() {
		return LastDamageMethodType.MAGIC;
	}

	double[] range = new double[]{1, 1, 1};

	protected double[] getDamageRange() {
		return range;
	}

	abstract public void tickRutine(int count);
	abstract public void removedRutine(FallingBlock spawnEntity);
	abstract public void damagedEntityRutine(Entity target);
	abstract public void startEntityRutine(Player p);
}
