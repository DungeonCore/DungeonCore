package lbn.mob.mobskill.skillrunnable;

import org.bukkit.entity.Damageable;
import org.bukkit.entity.LivingEntity;

import lbn.common.other.Stun;
import lbn.mob.mobskill.MobSkillRunnable;
import lbn.util.LivingEntityUtil;

public class MobSkillHealMob extends MobSkillRunnable{

	public MobSkillHealMob(String data) {
		super(data);
	}

	@Override
	public void execute(LivingEntity target, LivingEntity mob) {
		Stun.addStun(mob, 20 * 2);

		double parsent = 20;
		try {
			parsent = Double.parseDouble(data);
		} catch (Exception e) {
		}

		LivingEntityUtil.addHealth(mob, ((Damageable)mob).getMaxHealth() * parsent * 0.01);
	}

}
