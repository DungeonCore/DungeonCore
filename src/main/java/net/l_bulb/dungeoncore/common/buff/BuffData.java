package net.l_bulb.dungeoncore.common.buff;

import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import lombok.Getter;

@Getter
public class BuffData {
  public BuffData(String id, PotionEffectType effect, double second, int level) {
    this.id = id;
    this.effect = effect;
    this.tick = (int) (second * 20);
    if (level < 1) {
      this.level = 1;
    } else {
      this.level = level;
    }
    this.level--;
  }

  private String id;
  private PotionEffectType effect;
  private int tick;
  private int level = 1;

  /**
   * バフ効果を適当させる。上書きしない
   * 
   * @param e
   */
  public void addBuff(LivingEntity e) {
    addBuff(e, false);
  }

  /**
   * バフ効果を適当させる。
   * 
   * @param e
   * @param force 上書きするならTRUE
   */
  public void addBuff(LivingEntity e, boolean force) {
    if (e.isValid()) {
      e.addPotionEffect(new PotionEffect(effect, tick, level), force);
    }
  }

}
