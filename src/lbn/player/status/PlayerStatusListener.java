package lbn.player.status;

import lbn.common.event.player.PlayerChangeStatusLevelEvent;
import lbn.common.other.SystemLog;
import lbn.mob.AbstractMob;
import lbn.mob.LastDamageManager;
import lbn.mob.MobHolder;
import lbn.mob.SummonPlayerManager;
import lbn.player.AttackType;
import lbn.player.TheLowPlayer;
import lbn.player.TheLowPlayerManager;

import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public class PlayerStatusListener implements Listener{

	@EventHandler
	public void onDeath(EntityDeathEvent e) {
		LivingEntity entity = e.getEntity();


		Player p = LastDamageManager.getLastDamagePlayer(entity);
		AttackType type = LastDamageManager.getLastDamageAttackType(entity);

		if (SummonPlayerManager.isSummonMob(entity)) {
			return;
		}

		if (p == null || type == null) {
			EntityDamageEvent event = entity.getLastDamageCause();
			if (event instanceof EntityDamageByEntityEvent) {
				LastDamageManager.setLastDamageStatic((EntityDamageByEntityEvent) event);
				p = LastDamageManager.getLastDamagePlayer(entity);
				type = LastDamageManager.getLastDamageAttackType(entity);
			}
			if (p == null || type == null) {
				return;
			}
		}
		TheLowPlayer theLowPlayer = TheLowPlayerManager.getTheLowPlayer(p);
		if (theLowPlayer == null) {
			return;
		}

		AbstractMob<?> mob = MobHolder.getMob(entity);
		mob.addExp(entity, type, theLowPlayer);
	}

	@EventHandler
	public void onChangeStatusLevel(PlayerChangeStatusLevelEvent e) {
		String join = StringUtils.join(new Object[]{e.getOfflinePlayer().getName(), "の", e.getLevelType().getName(), "がレベル", e.getLevel(), "(" , e.getNowExp(), ")になりました。"});
		SystemLog.addLog(join);
	}
}
