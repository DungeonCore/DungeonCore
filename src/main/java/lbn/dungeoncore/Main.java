package lbn.dungeoncore;

import java.util.Collection;

import lbn.InitManager;
import lbn.LimitedListener;
import lbn.RecipeRegister;
import lbn.SystemListener;
import lbn.chest.ChestListner;
import lbn.chest.wireless.WireLessChestManager;
import lbn.common.OtherCommonListener;
import lbn.common.other.SystemLog;
import lbn.common.place.HolographicDisplaysManager;
import lbn.item.ItemListener;
import lbn.item.SetItemListner;
import lbn.mob.MobListener;
import lbn.mob.minecraftEntity.CustomBat;
import lbn.mob.minecraftEntity.CustomChicken;
import lbn.mob.minecraftEntity.CustomCow;
import lbn.mob.minecraftEntity.CustomEnderman;
import lbn.mob.minecraftEntity.CustomGiant;
import lbn.mob.minecraftEntity.CustomGuardian;
import lbn.mob.minecraftEntity.CustomPig;
import lbn.mob.minecraftEntity.CustomPigZombie;
import lbn.mob.minecraftEntity.CustomSkeleton;
import lbn.mob.minecraftEntity.CustomSlime;
import lbn.mob.minecraftEntity.CustomSpider;
import lbn.mob.minecraftEntity.CustomVillager;
import lbn.mob.minecraftEntity.CustomWitch;
import lbn.mob.minecraftEntity.CustomZombie;
import lbn.mobspawn.point.MobSpawnerPointManager;
import lbn.money.MoneyListener;
import lbn.npc.NpcListener;
import lbn.npc.followNpc.FollowerNpcManager;
import lbn.player.PlayerListener;
import lbn.player.PlayerTeamManager;
import lbn.player.ability.PlayerAbilityListener;
import lbn.player.playerIO.PlayerIODataManager;
import lbn.quest.QuestListener;
import lbn.util.DungeonLogger;
import lbn.util.LbnRunnable;
import lbn.util.NMSUtils;
import net.minecraft.server.v1_8_R1.AttributeRanged;
import net.minecraft.server.v1_8_R1.EntityBat;
import net.minecraft.server.v1_8_R1.EntityChicken;
import net.minecraft.server.v1_8_R1.EntityCow;
import net.minecraft.server.v1_8_R1.EntityEnderman;
import net.minecraft.server.v1_8_R1.EntityGiantZombie;
import net.minecraft.server.v1_8_R1.EntityGuardian;
import net.minecraft.server.v1_8_R1.EntityPig;
import net.minecraft.server.v1_8_R1.EntityPigZombie;
import net.minecraft.server.v1_8_R1.EntitySkeleton;
import net.minecraft.server.v1_8_R1.EntitySlime;
import net.minecraft.server.v1_8_R1.EntitySpider;
import net.minecraft.server.v1_8_R1.EntityVillager;
import net.minecraft.server.v1_8_R1.EntityWitch;
import net.minecraft.server.v1_8_R1.EntityZombie;
import net.minecraft.server.v1_8_R1.GenericAttributes;

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
      boolean isDebug = Boolean.parseBoolean(getConfig().getString("debug"));
      MobSpawnerPointManager.ignoreSpawnWorld = getConfig().getString("ignore-spawn-world");

      Main.debugging = isDebug;
      DungeonLogger.info("Debug:" + isDebug);
    } catch (Exception p) {
      p.printStackTrace();
    }
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
      NMSUtils.registerEntity("Slime", 55, EntitySlime.class, CustomSlime.class);
      NMSUtils.registerEntity("Bat", 65, EntityBat.class, CustomBat.class);
      NMSUtils.registerEntity("Cow", 92, EntityCow.class, CustomCow.class);
      NMSUtils.registerEntity("Chicken", 93, EntityChicken.class, CustomChicken.class);
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
      // 最大攻撃力書き換え
      ((AttributeRanged) GenericAttributes.maxHealth).b = Double.MAX_VALUE;
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

    WireLessChestManager.getInstance().loadManageData();
    // 60分に一回のルーチンスタート
    startRutinePerHour();

    DungeonLogger.info("Start complate!!");

    try {
      FollowerNpcManager.onEnable();

      // チームに配属する
      PlayerTeamManager.setTeamAllPlayer();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static Collection<? extends Player> getOnlinePlayer() {
    return plugin.getServer().getOnlinePlayers();
  }

  public void registLintener() {
    getServer().getPluginManager().registerEvents(new LimitedListener(), this);
    getServer().getPluginManager().registerEvents(new SystemListener(), this);
    getServer().getPluginManager().registerEvents(new MobListener(), this);
    getServer().getPluginManager().registerEvents(new ChestListner(), this);
    getServer().getPluginManager().registerEvents(new ItemListener(), this);
    getServer().getPluginManager().registerEvents(new SetItemListner(), this);
    getServer().getPluginManager().registerEvents(new PlayerAbilityListener(), this);
    getServer().getPluginManager().registerEvents(new OtherCommonListener(), this);
    getServer().getPluginManager().registerEvents(new PlayerListener(), this);
    getServer().getPluginManager().registerEvents(new QuestListener(), this);
    getServer().getPluginManager().registerEvents(new MoneyListener(), this);
    getServer().getPluginManager().registerEvents(new MoneyListener(), this);
    getServer().getPluginManager().registerEvents(new NpcListener(), this);
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
          save();
        } else if (i == 1) {} else if (i == 2) {
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
    save();
    // WireLessChestManager.getInstance().saveManageData();
    SystemLog.outPutSystemIn();
    SystemLog.outPut();

    LbnRunnable.allCancel();

    HolographicDisplaysManager.removeAllHologram();

    // NpcManager.allRemove();

    // 全てのプレイヤーのインベントリを閉じる
    for (Player p : Bukkit.getOnlinePlayers()) {
      p.closeInventory();
    }

    FollowerNpcManager.onDisable();
  }

  public static void save() {
    DungeonLogger.info("[dungeon core]saveします。");
    PlayerIODataManager.allSave();
  }
}