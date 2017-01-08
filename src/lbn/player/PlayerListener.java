package lbn.player;

import java.util.Collection;
import java.util.List;

import lbn.command.TpCutCommand;
import lbn.common.event.player.PlayerChangeGalionsEvent;
import lbn.common.event.player.PlayerChangeStatusExpEvent;
import lbn.common.event.player.PlayerChangeStatusLevelEvent;
import lbn.common.event.player.PlayerJoinDungeonGameEvent;
import lbn.dungeoncore.Main;
import lbn.mob.AbstractMob;
import lbn.mob.LastDamageManager;
import lbn.mob.MobHolder;
import lbn.money.galion.GalionEditReason;
import lbn.money.galion.GalionManager;
import lbn.util.LivingEntityUtil;
import lbn.util.Message;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.connorlinfoot.actionbarapi.ActionBarAPI;

public class PlayerListener implements Listener{

	@EventHandler(priority=EventPriority.LOWEST)
	public void onDamage(EntityDamageByEntityEvent e) {
		LastDamageManager.setLastDamageStatic(e);
	}

	@EventHandler(priority=EventPriority.LOWEST)
	public void onDamage(EntityDamageEvent e) {
		DamageCause cause = e.getCause();
		if (cause == DamageCause.ENTITY_EXPLOSION || cause == DamageCause.ENTITY_EXPLOSION) {
			e.setDamage(e.getDamage() * 0.6);
		}
	}

	@EventHandler
	public void onDeathAddGalions(EntityDeathEvent e) {
		//お金の計算
		LivingEntity entity = e.getEntity();
		//コウモリの場合はお金を加算しない
		if (entity.getType() == EntityType.BAT) {
			return;
		}

		Player p = LastDamageManager.getLastDamagePlayer(entity);
		AbstractMob<?> mob = MobHolder.getMob(entity);
		if (mob == null || p == null) {
			return;
		}
		int dropGalions = mob.getDropGalions();
		GalionManager.addGalion(p, dropGalions, GalionEditReason.mob_drop);
	}

	@EventHandler
	public void onDeath(EntityDeathEvent e) {
		//取得経験値は常にゼロ
		e.setDroppedExp(0);
	}

	@EventHandler(priority=EventPriority.MONITOR)
	public void onGetExp(PlayerChangeStatusExpEvent event) {
		OfflinePlayer player = event.getPlayer();
		if (!(player instanceof Player)) {
			return;
		}

		if (event.getReason().isPrintMessageLog()) {
			Message.sendMessage((Player)player, ChatColor.AQUA + "{0} + {1}xp", event.getManager().getManagerName(), event.getAddExp());
		}
	}

	@EventHandler()
	public void onLevelUp(PlayerChangeStatusLevelEvent e) {
		if (e.getPlayer().isOnline()) {
			updateSidebar((Player) e.getPlayer());
		}
	}

	@EventHandler(priority=EventPriority.MONITOR)
	public void onMoneyExp(PlayerChangeGalionsEvent event) {
		if (event.getReason().isPrintMessageLog()) {
			Message.sendMessage(event.getPlayer(), ChatColor.YELLOW + "{0} Galions 入手した", event.getGalions());
		}

		//まだこの時点では確定してないのでタイミングをずらす
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
		PlayerData.onDeath(e);
		//TPをキャンセルする
		TpCutCommand.setTpCancel(e.getEntity());

		//ポーション効果を消す
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
		//まだTPしてなければTPする
		PlayerChestTpManager.executeIfNotTeleported(e.getPlayer());

		if (Main.plugin.getServer().getPluginManager().isPluginEnabled("ActionBarAPI")) {
			ActionBarAPI.sendActionBar(e.getPlayer(), ChatColor.AQUA + "Welcome to THE LoW", 20 * 15);
		}
	}

	public static void updateSidebar(Player p) {
//		ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
//		Scoreboard newScoreboard = scoreboardManager.getNewScoreboard();
//		Objective registerNewObjective = newScoreboard.registerNewObjective("player_status", "dummy");
//		registerNewObjective.setDisplaySlot(DisplaySlot.SIDEBAR);
//		registerNewObjective.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "THE LoW  ");
//
//		Score scoreB = registerNewObjective.getScore(ChatColor.AQUA + "＝＝＝＝＝＝＝＝ ");
//		scoreB.setScore(8);
//
//		SwordStatusManager instance = SwordStatusManager.getInstance();
//		Score score = registerNewObjective.getScore("剣術 : " + instance.getLevel(p) + "レベル");
//		score.setScore(7);
//
//		BowStatusManager instance2 = BowStatusManager.getInstance();
//		Score score2 = registerNewObjective.getScore("弓術 : " + instance2.getLevel(p) + "レベル");
//		score2.setScore(6);
//
//		MagicStatusManager instance3 = MagicStatusManager.getInstance();
//		Score score3 = registerNewObjective.getScore("魔術 : " + instance3.getLevel(p) + "レベル");
//		score3.setScore(5);
//
//		Score score4 = registerNewObjective.getScore(ChatColor.GRAY + "   /statsで詳細確認");
//		score4.setScore(4);
//
//		Score score6 = registerNewObjective.getScore("");
//		score6.setScore(3);
//
//		Score score5 = registerNewObjective.getScore("お金 : " + GalionManager.getGalion(p) + " G");
//		score5.setScore(2);
//
//		Score scoreB1 = registerNewObjective.getScore(ChatColor.AQUA + "＝＝＝＝＝＝＝＝");
//		scoreB1.setScore(1);
//
//		Score scoreIp = registerNewObjective.getScore("play.l-bulb.net");
//		scoreIp.setScore(0);
//		p.setScoreboard(newScoreboard);
	}
}
