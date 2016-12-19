package lbn.dungeon.contents.slotStone.level1;

import org.bukkit.Location;
import org.bukkit.Sound;

import lbn.util.particle.ParticleData;
import lbn.util.particle.ParticleType;

public class KillEffectSlotStone2 extends AbstractKillEffectSlotStone{

	@Override
	protected String getEffectName() {
		return "Blood";
	}

	@Override
	protected String getEffectId() {
		return "2";
	}

	@Override
	protected void playSound(Location location) {
		location.getWorld().playSound(location, Sound.BAT_DEATH, (float) 0.75, (float) 0.5);
	}

	@Override
	protected ParticleData getParticleData() {
		return new ParticleData(ParticleType.reddust, 100).setDispersion(0.7, 0.7, 0.7);
	}
}
