package lbn.mob.mobskill.skillrunnable;

import org.bukkit.entity.LivingEntity;

import lbn.mob.mobskill.MobSkillRunnable;
import lbn.util.JavaUtil;
import lbn.util.explosion.NotMonsterDamageExplosion;

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
