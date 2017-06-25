package net.l_bulb.dungeoncore.util;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import net.l_bulb.dungeoncore.common.particle.ParticleData;
import net.l_bulb.dungeoncore.common.particle.ParticleType;
import net.l_bulb.dungeoncore.item.customItem.other.GalionItem;

public class TheLowUtil {

  static ParticleData particleData = new ParticleData(ParticleType.fireworksSpark, 100);

  public static void addBonusGold(Player p, Location l) {
    l.getWorld().dropItem(l, GalionItem.getInstance(20).getItem());
    particleData.run(l);
    l.getWorld().playSound(l, Sound.FIREWORK_BLAST, 1, 1);
  }
}
