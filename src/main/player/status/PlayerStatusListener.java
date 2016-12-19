package main.player.status;

import main.common.event.player.PlayerChangeStatusExpEvent;
import main.common.event.player.PlayerChangeStatusLevelEvent;
import main.common.other.SystemLog;
import main.mob.AbstractMob;
import main.mob.LastDamageManager;
import main.mob.MobHolder;
import main.mob.SummonPlayerManager;
import main.player.AttackType;
import main.player.status.bowStatus.BowStatusManager;
import main.player.status.magicStatus.MagicStatusManager;
import main.player.status.mainStatus.MainStatusManager;
import main.player.status.swordStatus.SwordStatusManager;

import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public class PlayerStatusListener implements Listener{
	public static IStatusManager[] managerList = {MainStatusManager.getInstance(), SwordStatusManager.getInstance(), MagicStatusManager.getInstance(), BowStatusManager.getInstance()};

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

		AbstractMob<?> mob = MobHolder.getMob(entity);
		mob.addExp(entity, type, p);
	}

	@EventHandler
	public void onChangeStatusLevel(PlayerChangeStatusLevelEvent e) {
		String join = StringUtils.join(new Object[]{e.getPlayer().getName(), "の", e.getManager().getManagerName(), "がレベル", e.getNowLevel(), "(" , e.getNowExp(), ")になりました。"});
		SystemLog.addLog(join);
	}

	@EventHandler
	public void onChangeStatusXp(PlayerChangeStatusExpEvent e) {
	}

}
