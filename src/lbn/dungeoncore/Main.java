package lbn.dungeoncore;

import java.util.Collection;

import lbn.InitManager;
import lbn.LimitedListener;
import lbn.RecipeRegister;
import lbn.SystemListener;
import lbn.chest.ChestListner;
import lbn.chest.wireless.WireLessChestManager;
import lbn.common.OtherCommonListener;
import lbn.common.other.HolographicDisplaysManager;
import lbn.common.other.SystemLog;
import lbn.dungeon.contents.ContentsListner;
import lbn.item.ItemListener;
import lbn.item.SetItemListner;
import lbn.mob.MobListener;
import lbn.mob.customEntity1_7.CustomEnderman;
import lbn.mob.customEntity1_7.CustomGiant;
import lbn.mob.customEntity1_7.CustomGuardian;
import lbn.mob.customEntity1_7.CustomPig;
import lbn.mob.customEntity1_7.CustomPigZombie;
import lbn.mob.customEntity1_7.CustomSkeleton;
import lbn.mob.customEntity1_7.CustomSpider;
import lbn.mob.customEntity1_7.CustomVillager;
import lbn.mob.customEntity1_7.CustomWitch;
import lbn.mob.customEntity1_7.CustomZombie;
import lbn.mobspawn.point.MobSpawnerPointManager;
import lbn.money.MoneyListener;
import lbn.player.PlayerListener;
import lbn.player.appendix.PlayerAppendixListener;
import lbn.player.playerIO.PlayerIODataManager;
import lbn.player.status.PlayerStatusListener;
import lbn.quest.QuestListener;
import lbn.util.DungeonLogger;
import lbn.util.LbnRunnable;
import lbn.util.NMSUtils;
import net.minecraft.server.v1_8_R1.EntityEnderman;
import net.minecraft.server.v1_8_R1.EntityGiantZombie;
import net.minecraft.server.v1_8_R1.EntityGuardian;
import net.minecraft.server.v1_8_R1.EntityPig;
import net.minecraft.server.v1_8_R1.EntityPigZombie;
import net.minecraft.server.v1_8_R1.EntitySkeleton;
import net.minecraft.server.v1_8_R1.EntitySpider;
import net.minecraft.server.v1_8_R1.EntityVillager;
import net.minecraft.server.v1_8_R1.EntityWitch;
import net.minecraft.server.v1_8_R1.EntityZombie;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Main extends JavaPlugin {

	public static String dataFolder;

	public static JavaPlugin plugin;

	private static boolean debugging = false;

	public static boolean isDebugging() {
		return debugging;
	}

	@Override
	public void onEnable() {
		plugin = this;
		try {
			dataFolder = getDataFolder().getAbsolutePath();
			NMSUtils.registerEntity("Skeleton", 51, EntitySkeleton.class, CustomSkeleton.class);
			NMSUtils.registerEntity("Zombie", 54, EntityZombie.class, CustomZombie.class);
			NMSUtils.registerEntity("Spider", 52, EntitySpider.class, CustomSpider.class);
			NMSUtils.registerEntity("Pig", 90, EntityPig.class, CustomPig.class);
			NMSUtils.registerEntity("Enderman", 58, EntityEnderman.class, CustomEnderman.class);
			NMSUtils.registerEntity("Witch", 66, EntityWitch.class, CustomWitch.class);
			NMSUtils.registerEntity("Villager", 120, EntityVillager.class, CustomVillager.class);
			NMSUtils.registerEntity("PigZombie", 57, EntityPigZombie.class, CustomPigZombie.class);
			NMSUtils.registerEntity("Giant", 53, EntityGiantZombie.class, CustomGiant.class);
			NMSUtils.registerEntity("Guardian", 68, EntityGuardian.class, CustomGuardian.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			registLintener();
			CommandRegister.registCommand();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			RecipeRegister.registerRecipe();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			PlayerIODataManager.saveAsZip();
		} catch (Exception e) {
			e.printStackTrace();
		}

		new InitManager().init();

		getConfig().options().copyDefaults(true);
		saveDefaultConfig();

		boolean isDebug = Boolean.parseBoolean(getConfig().getString("debug"));
		MobSpawnerPointManager.ignoreSpawnWorld = getConfig().getString("ignore-spawn-world");

		Main.debugging = isDebug;
		DungeonLogger.info("Debug:" + isDebug);

		WireLessChestManager.getInstance().loadManageData();
		// 60分に一回のルーチンスタート
		startRutinePerHour();

		DungeonLogger.info("Start complate!!");
	}

	public static Collection<? extends Player> getOnlinePlayer() {
		return plugin.getServer().getOnlinePlayers();
	}



	public void registLintener() {
		getServer().getPluginManager().registerEvents(new SystemListener(), this);
		getServer().getPluginManager().registerEvents(new LimitedListener(), this);
		getServer().getPluginManager().registerEvents(new MobListener(), this);
		getServer().getPluginManager().registerEvents(new ChestListner(), this);
		getServer().getPluginManager().registerEvents(new ItemListener(), this);
		getServer().getPluginManager().registerEvents(new SetItemListner(), this);
		getServer().getPluginManager().registerEvents(new PlayerStatusListener(), this);
		getServer().getPluginManager().registerEvents(new PlayerAppendixListener(), this);
		getServer().getPluginManager().registerEvents(new OtherCommonListener(), this);
		getServer().getPluginManager().registerEvents(new PlayerListener(), this);
		getServer().getPluginManager().registerEvents(new QuestListener(), this);
		getServer().getPluginManager().registerEvents(new ContentsListner(), this);
		getServer().getPluginManager().registerEvents(new MoneyListener(), this);
	}

	/**
	 * 一時間に一回のルーチンワーク
	 */
	public void startRutinePerHour() {

		// 60分に一回ルーチンを行う
		new BukkitRunnable() {
			int i = 0;

			@Override
			public void run() {
				if (i == 0) {
					// 一時間に一回セーブする
					save(false);
				} else if (i == 1) {
				} else if (i == 2) {
					// TODO 何か処理
					i = -1;
				} else {
					i = -1;
				}
				i++;
			}
		}.runTaskTimer(this, 20 * 60 * 20, 20 * 60 * 20);
	}

	@Override
	public void onDisable() {
		save(true);
		// WireLessChestManager.getInstance().saveManageData();
		SystemLog.outPutSystemIn();
		SystemLog.outPut();

		LbnRunnable.allCancel();

		HolographicDisplaysManager.removeAllHologram();

		// 全てのプレイヤーのインベントリを閉じる
		for (Player p : Bukkit.getOnlinePlayers()) {
			p.closeInventory();
		}
	}

	public static void save(boolean instant) {
		DungeonLogger.info("[dungeon core]saveします。");

		// 時間がかかる処理は後で実行
		if (instant) {
			PlayerIODataManager.allSave(true);
		} else {
			// 1tick後に実行
			new BukkitRunnable() {
				int i = 0;

				@Override
				public void run() {
					switch (i) {
					case 9:
						PlayerIODataManager.allSave(false);
						break;
					}
					i++;
					if (i >= 10) {
						cancel();
					}
				}
			}.runTaskTimer(plugin, 0, 1);
		}
	}
}