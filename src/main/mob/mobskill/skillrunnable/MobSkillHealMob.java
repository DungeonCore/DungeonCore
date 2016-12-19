package main.mob.mobskill.skillrunnable;

import main.common.other.Stun;
import main.mob.mobskill.MobSkillRunnable;
import main.util.LivingEntityUtil;

import org.bukkit.entity.Damageable;
import org.bukkit.entity.LivingEntity;

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
