package net.l_bulb.dungeoncore.player;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import net.l_bulb.dungeoncore.api.LevelType;
import net.l_bulb.dungeoncore.api.player.TheLowPlayer;
import net.l_bulb.dungeoncore.api.player.TheLowPlayer.CheckIntegrityLevel;
import net.l_bulb.dungeoncore.api.player.TheLowPlayerManager;
import net.l_bulb.dungeoncore.command.TpCutCommand;
import net.l_bulb.dungeoncore.common.event.player.PlayerBreakMagicOreEvent;
import net.l_bulb.dungeoncore.common.event.player.PlayerChangeGalionsEvent;
import net.l_bulb.dungeoncore.common.event.player.PlayerChangeStatusExpEvent;
import net.l_bulb.dungeoncore.common.event.player.PlayerChangeStatusLevelEvent;
import net.l_bulb.dungeoncore.common.event.player.PlayerCompleteReincarnationEvent;
import net.l_bulb.dungeoncore.common.event.player.PlayerCraftCustomItemEvent;
import net.l_bulb.dungeoncore.common.event.player.PlayerJoinDungeonGameEvent;
import net.l_bulb.dungeoncore.common.event.player.PlayerLevelUpEvent;
import net.l_bulb.dungeoncore.common.event.player.PlayerLoadedDataEvent;
import net.l_bulb.dungeoncore.common.event.player.PlayerStrengthFinishEvent;
import net.l_bulb.dungeoncore.common.other.SystemLog;
import net.l_bulb.dungeoncore.common.particle.ParticleData;
import net.l_bulb.dungeoncore.common.particle.ParticleType;
import net.l_bulb.dungeoncore.dungeoncore.Main;
import net.l_bulb.dungeoncore.item.customItem.other.MagicStoneOre;
import net.l_bulb.dungeoncore.mob.AbstractMob;
import net.l_bulb.dungeoncore.mob.LastDamageManager;
import net.l_bulb.dungeoncore.mob.LastDamageMethodType;
import net.l_bulb.dungeoncore.mob.MobHolder;
import net.l_bulb.dungeoncore.mob.SummonPlayerManager;
import net.l_bulb.dungeoncore.money.GalionEditReason;
import net.l_bulb.dungeoncore.player.ability.impl.LevelUpAbility;
import net.l_bulb.dungeoncore.player.customplayer.MagicPointManager;
import net.l_bulb.dungeoncore.player.customplayer.PlayerChestTpManager;
import net.l_bulb.dungeoncore.player.magicstoneOre.MagicStoneFactor;
import net.l_bulb.dungeoncore.player.magicstoneOre.MagicStoneOreType;
import net.l_bulb.dungeoncore.util.ItemStackUtil;
import net.l_bulb.dungeoncore.util.LbnRunnable;
import net.l_bulb.dungeoncore.util.LivingEntityUtil;
import net.l_bulb.dungeoncore.util.Message;
import net.l_bulb.dungeoncore.util.TitleSender;

import com.connorlinfoot.actionbarapi.ActionBarAPI;

public class PlayerListener implements Listener {

  /**
   * ダメージを受けた時、LastDamageを登録する
   *
   * @param e
   */
  @EventHandler(priority = EventPriority.LOWEST)
  public void onDamage(EntityDamageByEntityEvent e) {
    LastDamageManager.registLastDamageByEvent(e);
  }

  /**
   * 爆発によるダメージを軽減する
   *
   * @param e
   */
  @EventHandler(priority = EventPriority.LOWEST)
  public void onDamage(EntityDamageEvent e) {
    DamageCause cause = e.getCause();
    if (cause == DamageCause.ENTITY_EXPLOSION || cause == DamageCause.ENTITY_EXPLOSION) {
      e.setDamage(e.getDamage() * 0.6);
    }
  }

  /**
   * mobを倒した際のお金の加算を行う
   *
   * @param e
   */
  @EventHandler
  public void onDeathAddGalions(EntityDeathEvent e) {
    // お金の計算
    LivingEntity entity = e.getEntity();
    // コウモリの場合はお金を加算しない
    if (entity.getType() == EntityType.BAT) { return; }

    // 最後に攻撃をしたPlayerを取得
    Player p = LastDamageManager.getLastDamagePlayer(entity);
    AbstractMob<?> mob = MobHolder.getMob(entity);
    if (mob == null || p == null) { return; }
    int dropGalions = mob.getDropGalions();

    TheLowPlayer theLowPlayer = TheLowPlayerManager.getTheLowPlayer(p);
    if (theLowPlayer != null) {
      theLowPlayer.addGalions(dropGalions, GalionEditReason.mob_drop);
    }
  }

  /**
   * Exp取得時のメッセージを表示
   *
   * @param event
   */
  @EventHandler(priority = EventPriority.MONITOR)
  public void onGetExp(PlayerChangeStatusExpEvent event) {
    OfflinePlayer player = event.getPlayer();
    if (!(player instanceof Player)) { return; }

    if (event.getReason().isPrintMessageLog()) {
      Message.sendMessage((Player) player, ChatColor.AQUA + "{0} + {1}exp", event.getLevelType().getName(),
          event.getAddExp());
    }
  }

  /**
   * レベル変化時のメッセージをLogに残す
   *
   * @param e
   */
  @EventHandler(priority = EventPriority.MONITOR)
  public void onChangeStatusLevel(PlayerChangeStatusLevelEvent e) {
    String join = StringUtils.join(new Object[] { e.getOfflinePlayer().getName(), "の", e.getLeveltype().getName(),
        "がレベル", e.getLeveltype(), "(", e.getNowExp(), ")になりました。" });
    SystemLog.addLog(join);
  }

  /**
   * レベル変化時のメッセージを表示
   *
   * @param e
   */
  @EventHandler()
  public void onLevelUp(PlayerChangeStatusLevelEvent e) {
    if (e.isOnline()) {
      updateSidebar(e.getPlayer());
    }
  }

  /**
   * お金取得時のメッセージを表示、Logに記録を行う
   *
   * @param event
   */
  @EventHandler(priority = EventPriority.MONITOR)
  public void onMoneyExp(PlayerChangeGalionsEvent event) {
    // メッセージを表示
    event.getReason().sendMessageLog(event.getPlayer(), event.getGalions());

    // Logを残す
    if (event.getReason() != GalionEditReason.mob_drop) {
      final String format = "%s get %d galions by %s, (total %d galions)";
      final String message = String.format(format, event.getTheLowPlayer().getName(), event.getGalions(),
          event.getReason().toString(), event.getTheLowPlayer().getGalions());
      SystemLog.addLog(message);
    }

    // まだこの時点では確定してないのでタイミングをずらす
    new BukkitRunnable() {
      @Override
      public void run() {
        updateSidebar(event.getPlayer());
      }
    }.runTaskLater(Main.plugin, 1);
  }

  @EventHandler
  public void onJoin(PlayerJoinEvent e) {
    MagicPointManager.onJoinServer(e.getPlayer());
    updateSidebar(e.getPlayer());

    // チームに配属する
    PlayerTeamManager.setTeam(e.getPlayer());

    // Abilityを適応
    TheLowPlayer theLowPlayer = TheLowPlayerManager.getTheLowPlayer(e.getPlayer());
    if (theLowPlayer != null) {
      theLowPlayer.fixIntegrity(CheckIntegrityLevel.LEVEL1);
    }
  }

  @EventHandler
  public void onLoad(PlayerLoadedDataEvent e) {
    if (e.isSuccess()) {
      // ロード成功時
      Player player = e.getPlayer();
      if (player != null) {
        MagicPointManager.onJoinServer(e.getPlayer());
        updateSidebar(e.getPlayer());
      }
      // Abilityを適応
      TheLowPlayer theLowPlayer = e.getTheLowPlayer();
      theLowPlayer.fixIntegrity(CheckIntegrityLevel.LEVEL2);
    } else {
      // ロード失敗
      // PlayerがオンラインならKickする
      Player player = e.getPlayer();
      if (player != null) {
        player.kickPlayer("貴方のPlayerデータのロードに失敗しました。TwitterID : @namiken1993または公式Discordまで至急連絡をください。");
      }
    }
  }

  @EventHandler
  public void onPluginEnable(PluginEnableEvent e) {
    Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
    for (Player player : onlinePlayers) {
      MagicPointManager.onJoinServer(player);
      updateSidebar(player);
    }
  }

  @EventHandler
  public void onDeathPlayer(PlayerDeathEvent e) {
    // 最後に死んだ時間をセット
    TheLowPlayer theLowPlayer = TheLowPlayerManager.getTheLowPlayer(e.getEntity());
    if (theLowPlayer != null) {
      theLowPlayer.setLastDeathTimeMillis(System.currentTimeMillis());
    }
    // TPをキャンセルする
    TpCutCommand.setTpCancel(e.getEntity());

    // レベルを存続させる
    e.setKeepLevel(true);

    // ポーション効果を消す
    new BukkitRunnable() {
      @Override
      public void run() {
        for (PotionEffect potionEffect : e.getEntity().getActivePotionEffects()) {
          e.getEntity().removePotionEffect(potionEffect.getType());
        }
      }
    }.runTaskLater(Main.plugin, 20);
  }

  @EventHandler
  public void removeBottle(PlayerItemConsumeEvent e) {
    new BukkitRunnable() {
      @Override
      public void run() {
        ItemStack item = e.getItem();
        switch (item.getType()) {
          case POTION:
            e.getPlayer().getInventory().remove(new ItemStack(Material.GLASS_BOTTLE));
            break;
          case MILK_BUCKET:
            e.getPlayer().getInventory().remove(new ItemStack(Material.BUCKET));
            break;
          case MUSHROOM_SOUP:
            e.getPlayer().getInventory().remove(new ItemStack(Material.BOWL));
            break;
          default:
            break;
        }
      }
    }.runTaskLater(Main.plugin, 1);
  }

  /**
   * +2以上のインスタンスヒールの効果をつける
   *
   * @param e
   */
  @EventHandler
  public void onAddInstantHealPot(PlayerItemConsumeEvent e) {
    ItemStack item = e.getItem();
    if (item.getType() == Material.POTION) {
      PotionMeta itemMeta = (PotionMeta) item.getItemMeta();
      List<PotionEffect> customEffects = itemMeta.getCustomEffects();
      for (PotionEffect potionEffect : customEffects) {
        if (!potionEffect.getType().equals(PotionEffectType.HEAL)) {
          continue;
        }

        int amplifier = potionEffect.getAmplifier();
        if (amplifier >= 2) {
          Player player = e.getPlayer();
          LivingEntityUtil.addHealth(player, (amplifier + 1) * 4);
        }
      }
    }
  }

  @EventHandler
  public void onStart(PlayerJoinDungeonGameEvent e) {
    // まだTPしてなければTPする
    PlayerChestTpManager.executeIfNotTeleported(e.getPlayer());

    if (Main.plugin.getServer().getPluginManager().isPluginEnabled("ActionBarAPI")) {
      ActionBarAPI.sendActionBar(e.getPlayer(), ChatColor.AQUA + "Welcome to THE LoW", 20 * 15);
    }
  }

  /**
   * レベルアップ時の処理を行う
   *
   * @param event
   */
  @EventHandler
  public void onLevelUp(PlayerLevelUpEvent event) {
    Message.sendMessage(event, ChatColor.GREEN + " === LEVEL UP === ");
    Message.sendMessage(event, MessageFormat.format("{0} === {1}  {2}レベル === ", ChatColor.YELLOW,
        event.getLevelType().getName(), event.getNewLevel()));
    Message.sendMessage(event, ChatColor.GREEN + " === LEVEL UP === ");

    Player player = event.getTheLowPlayer().getOnlinePlayer();
    if (player != null) {
      // タイトルを表示
      TitleSender titleSender = new TitleSender();
      titleSender.setTitle("== LEVEL UP ==", ChatColor.GREEN, true);
      titleSender.setSubTitle(MessageFormat.format("{0} {1}  {2}レベル", ChatColor.YELLOW, event.getLevelType().getName(),
          event.getNewLevel()), ChatColor.YELLOW, false);
      titleSender.execute(player);

      // 音を鳴らす
      player.playSound(player.getLocation(), Sound.LEVEL_UP, 1f, 0.1f);
    }

    // abilityを追加する
    LevelType levelType = event.getLevelType();
    if (levelType == LevelType.MAIN) {
      event.getTheLowPlayer().addAbility(new LevelUpAbility(event.getNewLevel()));
    }
  }

  @EventHandler
  public void onReincarnation(PlayerCompleteReincarnationEvent e) {
    Player player = e.getPlayer();
    if (player == null) { return; }

    // 花火を表示
    Firework spawn = player.getWorld().spawn(player.getLocation().add(0, 2, 0), Firework.class);
    FireworkMeta fireworkMeta = spawn.getFireworkMeta();
    fireworkMeta.setPower(10);
    // パーティクル
    new ParticleData(ParticleType.lava, 30).run(player.getLocation());
    // 音をだす
    new LbnRunnable() {
      @Override
      public void run2() {
        player.getWorld().playSound(player.getLocation(), Sound.FIREWORK_BLAST, 1, 10);
        if (getRunCount() >= 2) {
          cancel();
        }
      }
    }.runTaskTimer(30);

    // Level Type名
    String levelTypeName = e.getLevelType().getName();

    TitleSender titleSender = new TitleSender();
    titleSender.setTitle("さらなる高みへ", ChatColor.GREEN, true);
    titleSender.setSubTitle(levelTypeName + "を転生完了", ChatColor.YELLOW, false);
    titleSender.execute(player);

    player.sendMessage(MessageFormat.format("{0}{1}の転生が完了しました。{2}が{3}レベルになった代わりに選択した特殊効果を得ます。", ChatColor.GREEN,
        levelTypeName, levelTypeName, e.getTheLowPlayer().getLevel(e.getLevelType())));
  }

  /**
   * Playerがモンスターを倒した時、Expを追加する
   *
   * @param e
   */
  @EventHandler
  public void onDeath(EntityDeathEvent e) {
    LivingEntity entity = e.getEntity();

    // 最後に倒したPlayerと攻撃方法を取得
    Player p = LastDamageManager.getLastDamagePlayer(entity);
    LastDamageMethodType type = LastDamageManager.getLastDamageAttackType(entity);

    // 倒したのがSummonの時はExpを与えない
    if (SummonPlayerManager.isSummonMob(entity)) { return; }

    // 攻撃方法が不明な時はEventから取得
    if (p == null || type == null) {
      EntityDamageEvent event = entity.getLastDamageCause();
      if (event instanceof EntityDamageByEntityEvent) {
        LastDamageManager.registLastDamageByEvent((EntityDamageByEntityEvent) event);
        p = LastDamageManager.getLastDamagePlayer(entity);
        type = LastDamageManager.getLastDamageAttackType(entity);
      }
      // ここでも取得出来ない場合は無視する
      if (p == null || type == null) { return; }
      // Logを出しておく
      new RuntimeException(
          MessageFormat.format("type:{0} is not registed last damege(player:{1})", type, p.getCustomName()))
              .printStackTrace();
    }

    // データがロードされていないなら無視する
    TheLowPlayer theLowPlayer = TheLowPlayerManager.getTheLowPlayer(p);
    if (theLowPlayer == null) { return; }

    // mobを取得
    AbstractMob<?> mob = MobHolder.getMob(entity);
    mob.addExp(entity, type, theLowPlayer);
  }

  /**
   * Playerが鉱石を壊した際にその鉱石がSpletSheetに登録されていたら 直接Inventoryに入れる。
   */
  @EventHandler(ignoreCancelled = true)
  public void onPlayerMagicStoneOreBreak(BlockBreakEvent event) {
    Block block = event.getBlock();
    Player player = event.getPlayer();
    // 破壊したブロックから鉱石タイプを取得
    MagicStoneOreType brokenType = MagicStoneOreType.FromMaterial(block.getType());

    // 登録されている鉱石ブロックの種類を取得
    MagicStoneOreType registedType = MagicStoneFactor.getMagicStoneByLocation(block.getLocation());

    // 登録されている鉱石の種類と、実際に破壊したブロックの鉱石の種類が同じなら鉱石を入手する
    if (brokenType != null && registedType != null && registedType == brokenType) {
      // 取得する鉱石
      ItemStack item = MagicStoneOre.getMagicStoneOre(brokenType).getItem();
      // イベントを呼び出す
      PlayerBreakMagicOreEvent e = new PlayerBreakMagicOreEvent(player, block.getLocation(), brokenType, item);
      Bukkit.getServer().getPluginManager().callEvent(e);

      // 変更する素材
      Material willChangeMaterial = block.getType();
      if (!e.isCancelled()) {
        item = e.getAcquisition();
        // 鉱石をインベントリに入れる
        player.getInventory().addItem(item);
        // 鉱石を丸いしに変える
        willChangeMaterial = Material.COBBLESTONE;
        // 音を鳴らす
        e.getPlayer().playSound(e.getLocation(), Sound.ORB_PICKUP, 1, 1);
      }

      // クリエの時うまく動かないので2tick後に置き換える
      final Material willChangeMaterialFinal = willChangeMaterial;
      new BukkitRunnable() {
        @Override
        public void run() {
          block.setType(willChangeMaterialFinal);
          // 鉱石を再配置する
          MagicStoneFactor.relocationOre(block.getLocation());
        }
      }.runTaskLater(Main.plugin, 2);
      e.setCancelled(true);
    } else {
      if (registedType != null) {
        // 管理者なら情報を送信する
        if (PlayerChecker.isNonNormalPlayer(player)) {
          MagicStoneFactor.sendOreSchedulerInfo(player, block.getLocation());
          event.setCancelled(true);
        }
      }
    }

  }

  @EventHandler
  public void onPlayerStrengthFinishEvent2(PlayerStrengthFinishEvent e) {
    // 成功
    Player p = e.getPlayer();
    if (e.isSuccess()) {
      // 成功の時は何もしない
      p.sendMessage(ChatColor.BLUE + "強化に成功しました");
      p.playSound(p.getLocation(), Sound.ANVIL_USE, 1, 1);
      // 失敗
    } else {
      p.sendMessage(ChatColor.RED + "強化に失敗しました");
      p.playSound(p.getLocation(), Sound.ITEM_BREAK, 1f, 1f);
    }
  }

  @EventHandler
  public void onPlayerCraftCustomItemEvent(PlayerCraftCustomItemEvent event) {
    event.getPlayer().sendMessage(MessageFormat.format("{0}アイテム{1}[{2}]{3}を作成しました", ChatColor.GREEN,
        ChatColor.LIGHT_PURPLE, ChatColor.stripColor(ItemStackUtil.getName(event.getCraftedItem())), ChatColor.GREEN));
    event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ANVIL_USE, (float) 0.8, 1);
  }

  public static void updateSidebar(Player p) {
    TheLowPlayer theLowPlayer = TheLowPlayerManager.getTheLowPlayer(p);
    if (theLowPlayer == null) { return; }

    Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
    Objective objective = board.registerNewObjective(ChatColor.GREEN + "player_status", "dummy");
    objective.setDisplayName(ChatColor.AQUA + "===== THE LoW =====");
    objective.setDisplaySlot(DisplaySlot.SIDEBAR);

    objective.getScore(ChatColor.DARK_GREEN + "剣術 : " + theLowPlayer.getLevel(LevelType.SWORD) + "レベル").setScore(8);
    objective.getScore(ChatColor.DARK_GREEN + "弓術 : " + theLowPlayer.getLevel(LevelType.BOW) + "レベル").setScore(7);
    objective.getScore(ChatColor.DARK_GREEN + "魔術 : " + theLowPlayer.getLevel(LevelType.MAGIC) + "レベル").setScore(6);
    objective.getScore(ChatColor.DARK_AQUA + "   /statsで詳細確認").setScore(5);
    objective.getScore("").setScore(4);
    objective.getScore(ChatColor.GOLD + "お金 : " + theLowPlayer.getGalions() + " G").setScore(3);
    objective.getScore(ChatColor.AQUA + "==================").setScore(2);
    objective.getScore(ChatColor.GOLD + "play.l-bulb.net").setScore(1);
    p.setScoreboard(board);
  }
}
