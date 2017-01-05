package lbn.player.appendix;

import java.util.Random;

import lbn.common.other.Stun;
import lbn.dungeoncore.Main;
import lbn.player.appendix.appendixObject.AbstractPlayerAppendix;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerAppendixListener implements Listener{
	@EventHandler
	public void login(final PlayerJoinEvent e) {
		//他の処理を行う可能性があるので少し後に実行
		new BukkitRunnable() {
			@Override
			public void run() {
				PlayerAppendixManager.updatePlayerStatus(e.getPlayer());
			}
		}.runTaskLater(Main.plugin, 2);
	}

	Random rnd = new Random();

	@EventHandler
	public void EntityDamageByEntityEvent(EntityDamageByEntityEvent e) {

		Entity damager = e.getDamager();
		//プレイヤーがダメージを与えた場合
		if (damager instanceof Player && e.getEntity() instanceof LivingEntity) {
			e.setDamage(e.getDamage() + PlayerAppendixManager.getPlayerAppendix((Player) damager).getAddedAtackPoint());

			if (rnd.nextInt(100) < PlayerAppendixManager.getPlayerAppendix((Player) damager).getAddedStunRate()) {
				Stun.addStun((LivingEntity) e.getEntity(), 3 * 20);
			}
		}

		//プレイヤーがダメージを受けた場合
		if (e.getEntity() instanceof Player) {
			AbstractPlayerAppendix playerAppendix = PlayerAppendixManager.getPlayerAppendix((Player) e.getEntity());
			switch (e.getCause()) {
			case FIRE:
			case FIRE_TICK:
			case LAVA:
				invalidFireDamage(e, playerAppendix);
				break;
			case ENTITY_ATTACK:
			case CONTACT:
				contactDamage(e, playerAppendix);
				break;
			case PROJECTILE:
				projetileDamage(e, playerAppendix);
				break;
			default:
				break;
			}
		}
	}

	protected void invalidFireDamage(EntityDamageByEntityEvent e, AbstractPlayerAppendix playerAppendix) {
		double fireDamagePassageRate = playerAppendix.getFireDamagePassageRate();
		e.setDamage(e.getDamage() * fireDamagePassageRate);
	}

	protected void contactDamage(EntityDamageByEntityEvent e,  AbstractPlayerAppendix playerAppendix) {
//		e.setDamage(e.getDamage() * (((110 - playerAppendix.getAddedContactDefenceRate()) / 100)));
	}

	protected void projetileDamage(EntityDamageByEntityEvent e,  AbstractPlayerAppendix playerAppendix) {
//		e.setDamage(e.getDamage() * (((110 - playerAppendix.getAddedProjetileDefenceRate()) / 100)));
	}
}
