package net.l_bulb.dungeoncore.common.other;

import java.util.function.Predicate;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import net.l_bulb.dungeoncore.common.particle.CircleParticleData;
import net.l_bulb.dungeoncore.common.particle.ParticleData;
import net.l_bulb.dungeoncore.common.particle.ParticleType;
import net.l_bulb.dungeoncore.util.LbnRunnable;

import lombok.Setter;

public abstract class BattleTrap {
  // パーティクルの種類
  private ParticleType type;
  private CircleParticleData circleParticleData;

  // ターゲットならtrue
  @Setter
  Predicate<Entity> isTarget;

  // Trapの中心の置くアーマースタンド
  private ArmorStand spawnEntity;

  // Trapが自然消滅する時間
  @Setter
  private int deadline = 30;

  public BattleTrap(ParticleType type, int radius) {
    circleParticleData = new CircleParticleData(new ParticleData(type, 5), radius);
    this.type = type;
  }

  /**
   * トラップをセットする
   *
   * @param loc
   */
  public void setStap(Location loc) {
    spawnEntity = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
    spawnEntity.setGravity(false);
    spawnEntity.setVisible(false);
  }

  class TrapTimer extends LbnRunnable {
    @Override
    public void run2() {
      // 自然消滅の時間になったらアーマースタンドを消す
      if (getAgeTick() >= deadline * 20) {
        runIfServerEnd();
      }
    }

    @Override
    protected void runIfServerEnd() {
      spawnEntity.remove();
    }

    /**
     * タイマーを開始する
     */
    public void start() {
      runTaskTimer(2);
    }

  }
}
