package net.l_bulb.dungeoncore.dungeon.contents.slotStone.level1;

import net.l_bulb.dungeoncore.common.particle.ParticleData;
import net.l_bulb.dungeoncore.common.particle.ParticleType;

import org.bukkit.Location;
import org.bukkit.Sound;

public class KillEffectSlotStone1 extends AbstractKillEffectSlotStone {

  ParticleData particleData = new ParticleData(ParticleType.lava, 50);
  {
    particleData.setDispersion(0.7, 0.7, 0.7);
  }

  @Override
  protected String getEffectName() {
    return "Popun";
  }

  @Override
  protected String getEffectId() {
    return "1";
  }

  @Override
  protected void playSound(Location location) {
    location.getWorld().playSound(location, Sound.VILLAGER_DEATH, (float) 0.75, 1);
  }

  @Override
  protected ParticleData getParticleData() {
    return particleData;
  }
}
