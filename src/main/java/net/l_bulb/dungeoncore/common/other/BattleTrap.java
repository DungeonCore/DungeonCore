package net.l_bulb.dungeoncore.common.other;

import java.util.function.Consumer;
import java.util.function.Predicate;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import net.l_bulb.dungeoncore.common.particle.CircleParticleData;
import net.l_bulb.dungeoncore.common.particle.ParticleData;
import net.l_bulb.dungeoncore.common.particle.ParticleType;
import net.l_bulb.dungeoncore.util.LbnRunnable;
import net.l_bulb.dungeoncore.util.TheLowValidates;

import lombok.Setter;

public class BattleTrap {
  // パーティクルの種類
  private CircleParticleData circleParticleData;

  // ターゲットならtrue
  Predicate<Entity> isTarget;

  // トラップにかかった時の処理
  @Setter
  Consumer<Entity> firing;

  // トラップが消滅したときの処理
  @Setter
  Consumer<Location> onRemove;

  // Trapの中心の置くアーマースタンド
  private ArmorStand spawnEntity;

  // Trapが自然消滅する時間
  @Setter
  private int deadline = 30;

  // トラップの半径
  private int radius;

  private TrapTimer timer;

  public BattleTrap(ParticleType type, int radius, Predicate<Entity> isTarget) {
    this.radius = radius;
    circleParticleData = new CircleParticleData(new ParticleData(type, 10).setDispersion(0.1, 0.1, 0.1), radius);
    this.isTarget = isTarget;
  }

  /**
   * トラップをセットする。もし前のトラップが残っているのにセットした場合エラーになります
   *
   * @param loc
   */
  public void setTrap(Location loc) {
    if (timer != null) {
      TheLowValidates.throwIllegalState("this trap is already running");
    }

    if (firing == null) {
      firing = e -> {};
    }
    if (onRemove == null) {
      onRemove = e -> {};
    }

    // アーマースタンドを作る
    spawnEntity = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
    spawnEntity.setGravity(false);
    spawnEntity.setVisible(false);

    // トラップのタイマー処理を開始する
    timer = new TrapTimer(loc);
    timer.start();
  }

  /**
   * トラップが現在動いているならTRUE
   *
   * @return
   */
  public boolean isRunning() {
    return timer != null;
  }

  /**
   * トラップを削除する
   */
  public void stopTrap() {
    if (timer == null) {
      timer.cancel();
    }
  }

  class TrapTimer extends LbnRunnable {

    // トラップの中心の座標
    private Location loc;

    public TrapTimer(Location loc) {
      this.loc = loc;
    }

    @Override
    public void run2() {
      // タイマーの存在を確認
      if (timer == null) {
        cancel();
        TheLowValidates.throwIllegalState("タイマーが存在しないのにも関わらずタイマー処理が動いています");
      }

      // 自然消滅の時間になったらアーマースタンドを消す
      if (getAgeTick() >= deadline * 20) {
        cancel();
        return;
      }

      // 0.25秒に一回パーティクルを発生
      if (getAgeTick() % 6 == 0) {
        circleParticleData.run(loc);
      }

      boolean isFire = false;

      // トラップが爆発したときの処理
      for (Entity e : spawnEntity.getNearbyEntities(radius, 0.5, radius)) {
        if (isTarget.test(e)) {
          firing.accept(e);
          isFire = true;
        }
      }

      // もしトラップが発動したなら終了する
      if (isFire) {
        cancel();
      }
    }

    @Override
    public synchronized void cancel() throws IllegalStateException {
      super.cancel();
      runIfServerEnd();
    }

    @Override
    protected void runIfServerEnd() {
      spawnEntity.remove();
      onRemove.accept(loc);
      timer = null;
    }

    /**
     * タイマーを開始する
     */
    public void start() {
      runTaskTimer(2);
    }

  }
}
