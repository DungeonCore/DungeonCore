package lbn.mobspawn.gettter;

import java.util.Arrays;
import java.util.List;

import lbn.mob.AbstractMob;

public class OneMonsterGetter extends AbstractSpawnMobGetter{
	AbstractMob<?> mob;
	public OneMonsterGetter(AbstractMob<?> mob) {
		this.mob = mob;
	}

	@Override
	public String getName() {
		return mob.getName();
	}

	@Override
	public List<AbstractMob<?>> getAllMobList() {
		return Arrays.asList(mob);
	}

}
