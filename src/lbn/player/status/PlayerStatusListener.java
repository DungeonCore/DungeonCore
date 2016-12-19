package lbn.player.status;

import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import lbn.common.event.player.PlayerChangeStatusExpEvent;
import lbn.common.event.player.PlayerChangeStatusLevelEvent;
import lbn.common.other.SystemLog;
import lbn.mob.AbstractMob;
import lbn.mob.LastDamageManager;
import lbn.mob.MobHolder;
import lbn.mob.SummonPlayerManager;
import lbn.player.AttackType;
import lbn.player.status.bowStatus.BowStatusManager;
import lbn.player.status.magicStatus.MagicStatusManager;
import lbn.player.status.mainStatus.MainStatusManager;
import lbn.player.status.swordStatus.SwordStatusManager;

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
