package lbn.mob.mobskill.skillrunnable;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import lbn.mob.AbstractMob;
import lbn.mob.MobHolder;
import lbn.mob.SummonPlayerManager;
import lbn.mob.mob.BossMobable;
import lbn.mob.mobskill.MobSkillRunnable;
import lbn.util.LivingEntityUtil;

public class MobSkillLightningEffect extends MobSkillRunnable{

	public MobSkillLightningEffect(String data) {
		super(data);
	}

	@Override
	public void execute(LivingEntity target, LivingEntity mob) {
		strikeLightningEffect(target, mob);
	}

	public static void strikeLightningEffect(LivingEntity target, LivingEntity mob) {
		AbstractMob<?> mob2 = MobHolder.getMob(mob);
		if (mob2.isBoss()) {
			LivingEntityUtil.strikeLightningEffect(target.getLocation(), ((BossMobable)mob2).getCombatPlayer());
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
