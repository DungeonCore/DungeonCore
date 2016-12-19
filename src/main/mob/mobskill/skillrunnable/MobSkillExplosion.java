package main.mob.mobskill.skillrunnable;

import org.bukkit.entity.LivingEntity;

import main.mob.mobskill.MobSkillRunnable;
import main.util.JavaUtil;
import main.util.explosion.NotMonsterDamageExplosion;

public class MobSkillExplosion extends MobSkillRunnable{

	int explosionSize;
	public MobSkillExplosion(String data) {
		super(data);
		explosionSize = JavaUtil.getInt(data, 2);
	}

	@Override
	public void execute(LivingEntity target, LivingEntity mob) {
		new NotMonsterDamageExplosion(target.getLocation(), explosionSize).runExplosion();
	}

}
