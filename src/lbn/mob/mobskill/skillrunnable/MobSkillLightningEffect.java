package lbn.mob.mobskill.skillrunnable;

import lbn.api.player.TheLowPlayer;
import lbn.mob.AbstractMob;
import lbn.mob.MobHolder;
import lbn.mob.SummonPlayerManager;
import lbn.mob.mob.BossMobable;
import lbn.mob.mobskill.MobSkillRunnable;
import lbn.util.LivingEntityUtil;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class MobSkillLightningEffect extends MobSkillRunnable{

	public MobSkillLightningEffect(String data) {
		super(data);
	}

	@Override
	public void execute(Entity target, Entity mob) {
		strikeLightningEffect(target, mob);
	}

	public static void strikeLightningEffect(Entity target, Entity mob) {
		AbstractMob<?> mob2 = MobHolder.getMob(mob);
		if (mob2.isBoss()) {
			//コンバットPlayerのみ雷を落とす
			for (TheLowPlayer player : ((BossMobable)mob2).getCombatPlayer()) {
				LivingEntityUtil.strikeLightningEffect(target.getLocation(), player.getOnlinePlayer());
			}
		} else {
			if (target.getType() == EntityType.PLAYER) {
				LivingEntityUtil.strikeLightningEffect(target.getLocation(), (Player)(target));
			}else {
				Player owner = SummonPlayerManager.getOwner(target);
				if (owner != null) {
					LivingEntityUtil.strikeLightningEffect(target.getLocation(), (Player)(owner));
				}
			}

		}
	}

}
