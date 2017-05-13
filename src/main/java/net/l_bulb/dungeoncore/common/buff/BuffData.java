package net.l_bulb.dungeoncore.common.buff;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@Getter
@AllArgsConstructor
public class BuffData {
  private String id;
  private PotionEffectType effect;
  private int tick;
  private int level = 1;

  public void addBuff(LivingEntity e) {
    if (e.isValid()) {
      e.addPotionEffect(new PotionEffect(effect, tick, level), false);
    }
  }

}
