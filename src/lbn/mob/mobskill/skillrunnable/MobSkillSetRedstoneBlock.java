package lbn.mob.mobskill.skillrunnable;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

import lbn.dungeoncore.SpletSheet.AbstractSheetRunable;
import lbn.mob.mobskill.MobSkillRunnable;
import lbn.util.BlockUtil;

public class MobSkillSetRedstoneBlock extends MobSkillRunnable{

	public MobSkillSetRedstoneBlock(String data) {
		super(data);
	}

	@Override
	public void execute(LivingEntity target, LivingEntity mob) {
		Location loc = AbstractSheetRunable.getLocationByString(data);
		if (loc != null) {
			BlockUtil.setRedstone(loc);
		}
	}

}
