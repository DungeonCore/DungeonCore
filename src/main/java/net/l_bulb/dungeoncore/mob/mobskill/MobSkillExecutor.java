package net.l_bulb.dungeoncore.mob.mobskill;

import java.util.Random;

import net.l_bulb.dungeoncore.common.other.Stun;
import net.l_bulb.dungeoncore.dungeoncore.Main;

import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

public class MobSkillExecutor {
  static protected Random rnd = new Random();

  /**
   * mobスキルを実行する
   * 
   * @param mob
   * @param target
   * @param skill
   */
  public static void executor(Entity mob, Entity target, MobSkillInterface skill) {
    // 発動タイミングを調べる
    if (!skill.getTiming().canExecute(mob)) { return; }

    // 確立を調べる
    if (rnd.nextInt(100) + 1 > skill.excutePercent()) { return; }

    // 時間ずらして実行する
    if (skill.getLaterTick() == 0) {
      executePrivate(mob, target, skill);
    } else {
      new BukkitRunnable() {
        @Override
        public void run() {
          executePrivate(mob, target, skill);
        }
      }.runTaskLater(Main.plugin, skill.getLaterTick());
    }

  }

  public static void executePrivate(Entity mob, Entity target, MobSkillInterface skill) {
    // スタンなら発動しない
    if (!skill.canUseWhenStun() && Stun.isStun(mob)) { return; }
    skill.execute(target, mob);
  }
}
