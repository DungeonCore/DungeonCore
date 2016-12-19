package main.lbn;

import java.util.Collection;

import main.Announce;
import main.InitManager;
import main.LimitedListener;
import main.RecipeRegistor;
import main.SystemListener;
import main.chest.ChestListner;
import main.chest.wireless.WireLessChestManager;
import main.command.CommandChest;
import main.command.CommandEquipPlayer;
import main.command.CommandExecuteLockByTimeCommand;
import main.command.CommandExtraMob;
import main.command.CommandGiveItem;
import main.command.CommandGiveSetItem;
import main.command.CommandItem;
import main.command.CommandMobSkill;
import main.command.CommandQuestOperate;
import main.command.CommandStatusView;
import main.command.CommandTpOtherWorld;
import main.command.CommandViewInfo;
import main.command.DeleteMonster;
import main.command.MobCommand;
import main.command.MoneyCommand;
import main.command.PlayerStatusCommand;
import main.command.QuestCommand;
import main.command.SetDungeonCommand;
import main.command.SetSpawnPointCommand;
import main.command.ShopCommand;
import main.command.StrengthItemCommand;
import main.command.TpCutCommand;
import main.command.VillagerCommand;
import main.command.util_command.ChangeBiomeCommand;
import main.command.util_command.CommandSavePlayer;
import main.command.util_command.CommandSpecialSign;
import main.command.util_command.DelayCommand;
import main.command.util_command.RemoveMobCommand;
import main.command.util_command.SearchPathCommand;
import main.command.util_command.SequenceCommand;
import main.command.util_command.SequenceSetBlockCommand;
import main.command.util_command.ShopItemCommand;
import main.command.util_command.SimplySetSpawnPointCommand;
import main.command.util_command.SoundPlayCommand;
import main.command.util_command.TimerExcuteCommand;
import main.command.util_command.TmCommand;
import main.command.util_command.ToggleSetBlockCommand;
import main.command.util_command.UsageCommandable;
import main.common.OtherCommonListener;
import main.common.other.HolographicDisplaysManager;
import main.common.other.SystemLog;
import main.dungeon.contents.ContentsListner;
import main.item.ItemListener;
import main.item.SetItemListner;
import main.mob.MobListener;
import main.mob.customEntity1_7.CustomEnderman;
import main.mob.customEntity1_7.CustomPig;
import main.mob.customEntity1_7.CustomPigZombie;
import main.mob.customEntity1_7.CustomSkeleton;
import main.mob.customEntity1_7.CustomSpider;
import main.mob.customEntity1_7.CustomVillager;
import main.mob.customEntity1_7.CustomWitch;
import main.mob.customEntity1_7.CustomZombie;
import main.mobspawn.point.MobSpawnerPointManager;
import main.money.MoneyListener;
import main.player.PlayerListener;
import main.player.appendix.PlayerAppendixListener;
import main.player.playerIO.PlayerIODataManager;
import main.player.status.PlayerStatusListener;
import main.quest.QuestListener;
import main.util.DungeonLog;
import main.util.LbnRunnable;
import main.util.NMSUtils;
import net.minecraft.server.v1_8_R1.EntityEnderman;
import net.minecraft.server.v1_8_R1.EntityPig;
import net.minecraft.server.v1_8_R1.EntityPigZombie;
import net.minecraft.server.v1_8_R1.EntitySkeleton;
import net.minecraft.server.v1_8_R1.EntitySpider;
import net.minecraft.server.v1_8_R1.EntityVillager;
import net.minecraft.server.v1_8_R1.EntityWitch;
import net.minecraft.server.v1_8_R1.EntityZombie;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;


public class Main extends JavaPlugin {
	public static String dataFolder;

	public static JavaPlugin plugin;

	public static boolean isDebug = false;

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
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			registLintener();
			registCommand();
		} catch (Exception e) {
			e.printStackTrace();
		}

		Announce.AnnounceInfo("サーバーがreloadされました。Minecraft1.8をお使いの方はダンジョン名が表示されなくなります。これはサーバーに入り直すことで解決できます。");

		try {
			RecipeRegistor.addRecipe();
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

		Main.isDebug = isDebug;
		DungeonLog.println("Debug:" + isDebug);

		WireLessChestManager.getInstance().loadManageData();
		//60分に一回のルーチンスタート
		startRutinePerHour();

		DungeonLog.println("Start complate!!");
	}

	public static Collection<? extends Player> getOnlinePlayer() {
		return plugin.getServer().getOnlinePlayers();
	}

	public void registCommand() {
		registCommand(new CommandGiveItem(), "getItem");
		registCommand(new CommandGiveSetItem(), "getSetItem");
		registCommand(new CommandExtraMob(), "spawnmob");
		registCommand(new CommandViewInfo(), "viewinfo");
		registCommand(new CommandStatusView(), "statusview");
		registCommand(new PlayerStatusCommand(), "statusCommand");
		registCommand(new SetSpawnPointCommand(), "set_spawn_point");
		registCommand(new StrengthItemCommand(), "strength_item");
		registCommand(new CommandSpecialSign(), "set_special_sign");
		registCommand(new DelayCommand(), "DELAYCOMMAND");
		registCommand(new SequenceCommand(), "SEQUENCECOMMAND");
		registCommand(new SequenceSetBlockCommand(), "SEQUENCE_SETBLOCK");
		registCommand(new SoundPlayCommand(), "soundPlay");
		registCommand(new TimerExcuteCommand(), "timer_command");
		registCommand(new CommandQuestOperate(), "quest_operate");
		registCommand(new ChangeBiomeCommand(), "change_biome");
		registCommand(new QuestCommand(), "quest");
		registCommand(new TmCommand(), "tm");
		registCommand(new MoneyCommand(), "galion");
		registCommand(new ShopCommand(), "shop");
		registCommand(new DeleteMonster(), "deletemob");
		registCommand(new VillagerCommand(), "villager");
		registCommand(new CommandChest(), "chest");
		registCommand(new RemoveMobCommand(), "removemob");
		registCommand(new MobCommand(), "mob");
		registCommand(new SimplySetSpawnPointCommand(), "setSpawn");
		registCommand(new CommandMobSkill(), "mobskill");
		registCommand(new CommandItem(), "item");
		registCommand(new ShopItemCommand(), "shopitem");
		registCommand(new CommandSavePlayer(), "saveplayer");
		registCommand(new SearchPathCommand(), "searchPath");
		registCommand(new SetDungeonCommand(), "setdungeon");
		registCommand(new TpCutCommand(), "tpcut");
		registCommand(new CommandEquipPlayer(), "equip");
		registCommand(new CommandExecuteLockByTimeCommand(), "timelock");
		registCommand(new ToggleSetBlockCommand(), "toggleSetblock");
		registCommand(new CommandTpOtherWorld(), "tpworld");
	}

	private void registCommand(CommandExecutor instance, String name) {
		PluginCommand command = getCommand(name);
		command.setExecutor(instance);
		if (instance instanceof TabCompleter) {
			command.setTabCompleter((TabCompleter) instance);
		}
		if (instance instanceof UsageCommandable) {
			command.setUsage(((UsageCommandable) instance).getUsage());
			command.setDescription(((UsageCommandable) instance).getDescription());
		}
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

		//60分に一回ルーチンを行う
		new BukkitRunnable() {
			int i = 0;
			@Override
			public void run() {
				if (i == 0) {
					//一時間に一回セーブする
					save(false);
				} else if (i == 1) {
				} else if (i == 2) {
					//TODO 何か処理
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
//		WireLessChestManager.getInstance().saveManageData();
		SystemLog.outPutSystemIn();
		SystemLog.outPut();

		LbnRunnable.allCancel();

		HolographicDisplaysManager.removeAllHologram();

		//全てのプレイヤーのインベントリを閉じる
		for (Player p : Bukkit.getOnlinePlayers()) {
			p.closeInventory();
		}
	}

	public static void save(boolean instant) {
		DungeonLog.println("[dungeon core]saveします。");

		//時間がかかる処理は後で実行
		if (instant) {
			PlayerIODataManager.allSave(true);
		} else {
			//1tick後に実行
			new BukkitRunnable() {
				int i = 0;
				@Override
				public void run() {
					switch (i) {
					case 0:
						break;
					case 9:
						PlayerIODataManager.allSave(false);
						break;
					default:
						break;
					}
					i++;
					if (i == 10) {
						cancel();
					}
				}
			}.runTaskTimer(plugin,0, 1);
		}
	}
}
