package main.mob.mobskill.skillrunnable;

import main.mob.AbstractMob;
import main.mob.MobHolder;
import main.mob.SummonPlayerManager;
import main.mob.mob.BossMobable;
import main.mob.mobskill.MobSkillRunnable;
import main.util.LivingEntityUtil;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

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
