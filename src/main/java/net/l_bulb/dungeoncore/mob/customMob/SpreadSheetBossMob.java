package net.l_bulb.dungeoncore.mob.customMob;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

import net.l_bulb.dungeoncore.api.LevelType;
import net.l_bulb.dungeoncore.api.player.TheLowPlayer;
import net.l_bulb.dungeoncore.api.player.TheLowPlayerManager;
import net.l_bulb.dungeoncore.chest.AbstractCustomChest;
import net.l_bulb.dungeoncore.chest.BossChest;
import net.l_bulb.dungeoncore.chest.CustomChestManager;
import net.l_bulb.dungeoncore.chest.SpletSheetChest;
import net.l_bulb.dungeoncore.common.event.player.CombatEntityEvent;
import net.l_bulb.dungeoncore.common.event.player.PlayerCustomMobSpawnEvent;
import net.l_bulb.dungeoncore.dungeoncore.LbnRuntimeException;
import net.l_bulb.dungeoncore.dungeoncore.Main;
import net.l_bulb.dungeoncore.mob.AbstractMob;
import net.l_bulb.dungeoncore.mob.LastDamageManager;
import net.l_bulb.dungeoncore.mob.LastDamageMethodType;
import net.l_bulb.dungeoncore.mob.MobHolder;
import net.l_bulb.dungeoncore.mob.MobSpawnerFromCommand;
import net.l_bulb.dungeoncore.mob.mobskill.MobSkillExcuteConditionType;
import net.l_bulb.dungeoncore.player.status.StatusAddReason;
import net.l_bulb.dungeoncore.util.LivingEntityUtil;
import net.l_bulb.dungeoncore.util.TheLowExecutor;

import com.connorlinfoot.actionbarapi.ActionBarAPI;
import com.google.common.collect.HashBasedTable;

public class SpreadSheetBossMob extends SpreadSheetMob implements BossMobable {

  protected SpreadSheetBossMob(LbnMobTag nbtTag, String[] command, String name) {
    super(nbtTag, command, name);
  }

  long spawnTimeMill = -1;

  public static SpreadSheetBossMob getInstance(String[] command, String name, CommandSender sender, Location locationByString) {
    LbnMobTag nbtTag = MobSpawnerFromCommand.getNBTTagByCommand(command, sender);

    if (nbtTag == null) { return null; }

    if (locationByString == null) { return null; }

    SpreadSheetBossMob commandableMob = new SpreadSheetBossMob(nbtTag, command, name);
    commandableMob.chestloc = locationByString;

    AbstractMob<?> mob2 = MobHolder.getMob(name);
    if (mob2 != null && !mob2.isNullMob() && !(mob2 instanceof SpreadSheetMob)) {
      commandableMob.mob = mob2;
    }

    if (locationByString != null) {
      AbstractCustomChest customChest = CustomChestManager.getCustomChest(locationByString);
      if (customChest != null && customChest instanceof SpletSheetChest) {
        ((SpletSheetChest) customChest).setBossChestTemplate(true);
      }
    }

    nbtTag.setBoss(true);

    return commandableMob;
  }

  Location chestloc = null;

  private LivingEntity e;

  HashMap<MobSkillExcuteConditionType, BukkitRunnable> runtineMap = new HashMap<>();

  @Override
  public void setEntity(LivingEntity e) {
    this.e = e;

    // すでにキャンセルされてる可能性もあるのエラーを無視
    try {
      for (BukkitRunnable rutineRun : runtineMap.values()) {
        rutineRun.cancel();
      }
    } catch (Exception ex) {}

    // mobskillをスタート
    for (MobSkillExcuteConditionType condtion : Arrays.asList(MobSkillExcuteConditionType.RUNTINE_10SEC, MobSkillExcuteConditionType.RUNTINE_30SEC,
        MobSkillExcuteConditionType.RUNTINE_60SEC)) {
      RuntineRunnable run = new RuntineRunnable(condtion);
      run.runtask();
      runtineMap.put(condtion, run);
    }
  }

  @Override
  public void onSpawn(PlayerCustomMobSpawnEvent e) {
    super.onSpawn(e);
    setEntity(e.getEntity());

    spawnTimeMill = System.currentTimeMillis();

    // 前のが動いていた場合はキャンセルする
    for (BukkitRunnable val : runtineMap.values()) {
      if (val == null) {
        continue;
      }
      // 念のため
      try {
        val.cancel();
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }

    // mobskillをスタート
    for (MobSkillExcuteConditionType condtion : Arrays.asList(MobSkillExcuteConditionType.RUNTINE_10SEC, MobSkillExcuteConditionType.RUNTINE_30SEC,
        MobSkillExcuteConditionType.RUNTINE_60SEC)) {
      RuntineRunnable run = new RuntineRunnable(condtion);
      run.runtask();
      runtineMap.put(condtion, run);
    }
  }

  public Location chestLocation() {
    return chestloc;
  }

  @Override
  public BossChest getBossChest() {
    AbstractCustomChest customChest = CustomChestManager.getCustomChest(chestloc.getBlock().getLocation());
    if (customChest != null || customChest instanceof SpletSheetChest) {
      return new BossChest((SpletSheetChest) customChest);
    } else {
      return null;
    }
  }

  @Override
  protected Entity spawnPrivate(Location loc) {
    Entity spawnPrivate = super.spawnPrivate(loc);
    return spawnPrivate;
  }

  @Override
  public LivingEntity getEntity() {
    return e;
  }

  private HashMap<TheLowPlayer, Long> combatPlayerSet = new HashMap<>();

  private HashBasedTable<TheLowPlayer, LevelType, Double> combatDamagePlayerMap = HashBasedTable.create();

  @Override
  public void onAttack(LivingEntity mob, LivingEntity target,
      EntityDamageByEntityEvent e) {
    super.onAttack(mob, target, e);

    // ボスモブとして認識されていないなら再セットする
    if ((this.e == null || !this.e.isValid()) && mob != this.e) {
      setEntity(mob);
    }
  }

  @Override
  public void onDamagePlayer(CombatEntityEvent e) {
    super.onDamagePlayer(e);

    LivingEntity mob = e.getEnemy();

    // 最後に攻撃したPlayerと攻撃方法を取得
    Player player = LastDamageManager.getLastDamagePlayer(mob);
    if (player == null) { return; }
    TheLowPlayer p = TheLowPlayerManager.getTheLowPlayer(player);
    LastDamageMethodType lastDamageType = LastDamageManager.getLastDamageAttackType(mob);

    // 攻撃方法を対応するステータスのTypeに変換
    LevelType type = lastDamageType.getLevelType();

    // 攻撃者がいる または 攻撃方法に対応するステータスが存在するならダメージを記録する
    if (p != null && type != null) {
      combatPlayerSet.put(p, System.currentTimeMillis());

      // ダメージを記録
      double doubleNowDamage = 0;
      if (combatDamagePlayerMap.contains(p, type)) {
        doubleNowDamage = combatDamagePlayerMap.get(p, type);
      }
      combatDamagePlayerMap.put(p, type, doubleNowDamage + e.getDamage());
    }

    // ボスモブとして認識されていないなら再セットする
    if ((this.e == null || !this.e.isValid()) && mob != this.e) {
      setEntity(mob);
    }
  }

  @Override
  public Set<TheLowPlayer> getCombatPlayer() {
    Iterator<Entry<TheLowPlayer, Long>> iterator = combatPlayerSet.entrySet().iterator();
    while (iterator.hasNext()) {
      Entry<TheLowPlayer, Long> entry = iterator.next();
      // Combatした時間よりも後に死んでいたらCombatを認めない
      if (entry.getKey().getLastDeathTimeMillis() > entry.getValue()) {
        iterator.remove();
      }
    }
    return combatPlayerSet.keySet();
  }

  @Override
  public void onDeathPrivate(EntityDeathEvent e) {
    super.onDeathPrivate(e);

    if (Main.plugin.getServer().getPluginManager().isPluginEnabled("ActionBarAPI")) {
      for (TheLowPlayer p : combatPlayerSet.keySet()) {
        if (p.isOnline()) {
          ActionBarAPI.sendActionBar(p.getOnlinePlayer(), "");
        }
      }
    }

    // 数チック後に削除する
    TheLowExecutor.executeLater(2, () -> {
      combatPlayerSet.clear();
      combatDamagePlayerMap.clear();
    });
  }

  @Override
  @EventHandler
  public void updateName(boolean islater) {
    if (isNullMob()) { return; }
    new UpdateNameLater().runTaskLater(Main.plugin, 1);
  }

  @Override
  public void onTarget(EntityTargetLivingEntityEvent event) {
    super.onTarget(event);
  }

  @Override
  public void addExp(LivingEntity entity, LastDamageMethodType type, TheLowPlayer p) {
    int exp = getExp(type);
    if (exp == -1) {
      exp = (int) (((Damageable) entity).getMaxHealth() * 1.3);
    }

    // totalダメージを取得
    double totalDamage = combatDamagePlayerMap.values().stream()
        .filter(d -> d != null)
        .mapToDouble(d -> d.doubleValue())
        .sum();

    // 経験値を分配する
    for (Entry<TheLowPlayer, Map<LevelType, Double>> entry : combatDamagePlayerMap.rowMap().entrySet()) {
      TheLowPlayer theLowPlayer = entry.getKey();
      for (Entry<LevelType, Double> typeEntry : entry.getValue().entrySet()) {
        if (typeEntry.getKey() != null) {
          theLowPlayer.addExp(typeEntry.getKey(), (int) (exp * typeEntry.getValue() / totalDamage), StatusAddReason.monster_drop);
        }
      }
    }
  }

  @Override
  public boolean isCombatPlayer(Player player) {
    // onlineでないならfalse
    if (player == null || !player.isOnline()) { return false; }

    TheLowPlayer theLowPlayer = TheLowPlayerManager.getTheLowPlayer(player);
    if (theLowPlayer == null) { return false; }

    if (combatPlayerSet.containsKey(theLowPlayer)) {
      // 攻撃をした時よりも後に死んだらコンバットと認めない
      return theLowPlayer.getLastDeathTimeMillis() < combatPlayerSet.get(theLowPlayer);
    }
    return false;
  }

  /**
   * ボススキルを発動するためのクラス
   *
   * @author KENSUKE
   *
   */
  class RuntineRunnable extends BukkitRunnable {
    MobSkillExcuteConditionType condtion;
    int term;

    public RuntineRunnable(MobSkillExcuteConditionType condtion) {
      this.term = condtion.getTerm();
      this.condtion = condtion;

      if (term <= 0) { throw new LbnRuntimeException("mob skill term is invalid!! :" + term); }
    }

    @Override
    public void run() {
      if (getEntity() == null || !getEntity().isValid()) {
        cancel();
        runtineMap.remove(condtion);
        return;
      }
      // ランダムで一番近くの友好モブに対してダメージを与える
      for (LivingEntity entity : LivingEntityUtil.getNearFrendly(getEntity(), 30, 20, 30)) {
        // Playerだがコンバットプレイヤーでないときは無視する
        if (entity.getType() == EntityType.PLAYER && !isCombatPlayer((Player) entity)) {
          continue;
        }
        executeMobSkill(getEntity(), entity, condtion);
        return;
      }
    }

    public void runtask() {
      runTaskTimer(Main.plugin, rnd.nextInt(term * 20), term * 20);
    }
  }

  class UpdateNameLater extends BukkitRunnable {
    @SuppressWarnings("unchecked")
    @Override
    public void run() {
      // ActionBarAPIがないなら無視する
      if (!Main.plugin.getServer().getPluginManager().isPluginEnabled("ActionBarAPI")) { return; }

      LivingEntity entity = getEntity();
      if (entity != null) {
        double maxHealth = ((Damageable) entity).getMaxHealth();
        double nowHealth = ((Damageable) entity).getHealth();
        String hpMessage = StringUtils.join(getName(), ChatColor.RED, " [", (int) nowHealth, "/", (int) maxHealth, "]");
        entity.setCustomName(hpMessage);

        // onlineのコンバットプレイヤーならメッセージを与える。
        combatPlayerSet.keySet().stream()
            .map(p -> p.getOnlinePlayer())
            .filter(SpreadSheetBossMob.this::isCombatPlayer)
            .forEach(p -> ActionBarAPI.sendActionBar(p, nowHealth <= 0 ? "" : hpMessage));
      }
    }
  }
}
