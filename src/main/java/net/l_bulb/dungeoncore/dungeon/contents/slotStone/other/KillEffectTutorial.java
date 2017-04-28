package net.l_bulb.dungeoncore.dungeon.contents.slotStone.other;

import org.bukkit.Location;
import org.bukkit.Sound;

import net.l_bulb.dungeoncore.common.particle.ParticleData;
import net.l_bulb.dungeoncore.common.particle.ParticleType;
import net.l_bulb.dungeoncore.dungeon.contents.slotStone.level1.AbstractKillEffectSlotStone;
import net.l_bulb.dungeoncore.item.slot.SlotLevel;

public class KillEffectTutorial extends AbstractKillEffectSlotStone {

  ParticleData particleData = new ParticleData(ParticleType.flame, 50);
  {
    particleData.setDispersion(0.7, 0.7, 0.7);
  }

  @Override
  protected String getEffectName() {
    return "Tutorial kill Effect";
  }

  @Override
  protected String getEffectId() {
    return "turorial_1";
  }

  @Override
  protected void playSound(Location location) {
    location.getWorld().playSound(location, Sound.VILLAGER_DEATH, (float) 0.75, 1);
  }

  @Override
  protected ParticleData getParticleData() {
    return particleData;
  }

  @Override
  public SlotLevel getLevel() {
    return SlotLevel.TUTORIAL;
  }
}
