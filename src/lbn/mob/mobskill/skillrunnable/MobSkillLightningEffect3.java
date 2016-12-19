package lbn.mob.mobskill.skillrunnable;

import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;

import lbn.dungeoncore.Main;
import lbn.mob.mobskill.MobSkillRunnable;
import lbn.util.JavaUtil;

public class MobSkillLightningEffect3 extends MobSkillRunnable{

	public MobSkillLightningEffect3(String data) {
		super(data);
	}

	@Override
	public void execute(LivingEntity target, LivingEntity mob) {
			int num = JavaUtil.getInt(data, 3);

		new BukkitRunnable() {
			int count = 0;
			@Override
			public void run() {
				MobSkillLightningEffect.strikeLightningEffect(target, mob);
				count++;
				if (count >= num) {
					cancel();
				}
			}
		}.runTaskTimer(Main.plugin, 0, 10);
	}

}
