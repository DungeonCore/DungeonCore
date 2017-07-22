package net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.skillset1;

import java.util.HashMap;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import net.l_bulb.dungeoncore.common.event.player.CombatEntityEvent;
import net.l_bulb.dungeoncore.common.particle.ParticleData;
import net.l_bulb.dungeoncore.common.particle.ParticleType;
import net.l_bulb.dungeoncore.dungeoncore.Main;
import net.l_bulb.dungeoncore.item.customItem.attackitem.AbstractAttackItem;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.SpreadSheetWeaponSkill;
import net.l_bulb.dungeoncore.item.itemInterface.CombatItemable;

public class Kenbu extends SpreadSheetWeaponSkill {

  // 実行中のPlayerのマップ
  static HashMap<UUID, SoundPlayRunner> executePlayerMap = new HashMap<>();

  @Override
  public String getId() {
    return "wskill2";
  }

  Random rnd = new Random();

  @Override
  public boolean onClick(Player p, ItemStack item, AbstractAttackItem customItem) {
    // すでに実行されているならキャンセルする
    if (executePlayerMap.containsKey(p.getUniqueId())) {
      SoundPlayRunner remove = executePlayerMap.remove(p.getUniqueId());
      remove.cancel();
      return false;
    } else {
      // まだ実行されていないなら実行する
      SoundPlayRunner soundPlayRunner = new SoundPlayRunner(p);
      executePlayerMap.put(p.getUniqueId(), soundPlayRunner);
      soundPlayRunner.runTaskTimer(Main.plugin, 0, 20 * 1);
      return true;
    }
  }

  @Override
  public void offHeldThisItem(Player player, ItemStack item) {
    SoundPlayRunner remove = executePlayerMap.remove(player.getUniqueId());
    if (remove != null) {
      remove.cancel();
    }
  }

  /**
   * 実行中なら音がなったタイミングと同時であるか調べ、同じタイミングならダメージを増加させる
   */
  @Override
  public void onCombat(Player p, ItemStack item, CombatItemable customItem, LivingEntity livingEntity, CombatEntityEvent event) {
    // 通常攻撃でないなら何もしない
    if (!event.isNormalAttack()) { return; }
    // 実行中でないなら無視
    if (!executePlayerMap.containsKey(p.getUniqueId())) { return; }

    // 音が鳴ったのと同じタイミングでないなら無視
    SoundPlayRunner soundPlayRunner = executePlayerMap.get(p.getUniqueId());
    if (!soundPlayRunner.isSameTiming()) { return; }

    // キャプチャ―中なら成功を記録する
    if (successFlg.containsKey(p.getUniqueId())) {
      successFlg.put(p.getUniqueId(), successFlg.get(p.getUniqueId()) + 1);
    }
    // 貫通ダメージを与える
    CombatEntityEvent combatEntityEvent = new CombatEntityEvent(p, livingEntity.getMaxHealth() * getData(2) * 0.01, customItem, item, false,
        livingEntity).callEvent();
    combatEntityEvent.damageEntity(true);

    // 音を鳴らす
    p.getWorld().playSound(p.getLocation(), Sound.BLAZE_HIT, 1, 1);
  }

  // 監視中、このスキルの攻撃に成功し続けているかどうか
  static HashMap<UUID, Integer> successFlg = new HashMap<>();

  /**
   * 攻撃成功のキャプチャを開始する
   *
   * @param p
   */
  public static void startCaptuer(Player p) {
    successFlg.put(p.getUniqueId(), 0);
  }

  /**
   * 攻撃成功のキャプチャを終了する
   *
   * @param p
   */
  public static int endCaptuer(Player p) {
    return Optional.of(successFlg.remove(p.getUniqueId())).orElse(0);
  }

  /**
   * 音を鳴らすためのRunner。音を鳴らした時間も管理する
   *
   */
  class SoundPlayRunner extends BukkitRunnable {
    private Player p;

    public SoundPlayRunner(Player p) {
      this.p = p;
    }

    // 音が鳴った時間
    long playSoundMeleeTime;

    // 音が鳴る予定の時間
    long willPlaySoundMeleeTime;

    ParticleData noteParticle = new ParticleData(ParticleType.note, 20);

    /**
     * 音を鳴らす
     */
    void PlaySound() {
      p.playSound(p.getLocation(), Sound.NOTE_PIANO, 1, 1);
      noteParticle.run(p.getLocation());

      // 時間を記録する
      playSoundMeleeTime = System.currentTimeMillis();
    }

    // 指定された範囲内の数字を出すように変更
    Random rnd = new Random() {
      private static final long serialVersionUID = 1L;

      @Override
      public int nextInt() {
        return (int) (rnd.nextInt((int) (getData(1) - getData(0))) + getData(0));
      };
    };

    // 実行までの残り秒
    int remainingTime = rnd.nextInt();

    @Override
    public void run() {
      // プレイヤーがいないならMapから削除する
      if (!p.isOnline()) {
        cancel();
        executePlayerMap.remove(p.getUniqueId());
      }

      if (remainingTime == 0) {
        // 音を鳴らす
        PlaySound();
        // 次実行される時間を再設定する
        remainingTime = rnd.nextInt();
        // 次に音がなる時間を設定する
        willPlaySoundMeleeTime = playSoundMeleeTime + remainingTime * 1000;
      } else {
        remainingTime--;
      }
    }

    /**
     * 音を鳴らしたタイミングを同じならTRUE
     *
     * @return
     */
    public boolean isSameTiming() {
      return System.currentTimeMillis() - playSoundMeleeTime < getData(3) * 1000
          || willPlaySoundMeleeTime - System.currentTimeMillis() > getData(3) * 1000;
    }
  }
}
