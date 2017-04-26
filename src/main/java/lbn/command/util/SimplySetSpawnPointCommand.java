package lbn.command.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lbn.dungeoncore.SpletSheet.SpawnPointSheetRunnable;
import lbn.dungeoncore.SpletSheet.SpletSheetExecutor;
import lbn.mob.AbstractMob;
import lbn.mob.MobHolder;
import lbn.mobspawn.SpawnLevel;
import lbn.mobspawn.gettter.SpawnMobGetterManager;
import lbn.mobspawn.gettter.SpletSheetSpawnMobGetter;
import lbn.mobspawn.point.MobSpawnerPoint;
import lbn.mobspawn.point.MobSpawnerPointManager;
import lbn.util.ItemStackUtil;
import lbn.util.LbnRunnable;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.StringUtil;
import org.bukkit.util.Vector;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;

public class SimplySetSpawnPointCommand implements CommandExecutor, TabCompleter {
  static HashMultimap<Player, MobLocation> create = HashMultimap.create();
  static HashMap<Player, MobLocation> lastSet = new HashMap<Player, MobLocation>();
  static HashSet<Player> begin = new HashSet<Player>();

  static boolean look_flg = false;

  @Override
  public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
    Player p = (Player) arg0;
    if (arg3.length == 0) { return false; }

    String operate = arg3[0].toLowerCase();
    switch (operate) {
      case "set":
        executeSet(p, arg3);
        break;
      case "begin":
        executeBegin(p, arg3);
        break;
      case "commit":
        executeCommit(p, arg3);
        break;
      case "cancel":
        executeCancel(p, arg3);
        break;
      case "undo":
        executeUndo(p, arg3);
        break;
      case "list":
        executeList(p, arg3);
        break;
      case "look":
        executeLook(p, arg3);
        break;
      default:
        p.sendMessage("不正なコマンドです。");
        break;
    }
    return true;
  }

  private static void executeLook(Player p, String[] arg3) {
    Collection<MobSpawnerPoint> allSpawnerPointList = MobSpawnerPointManager.getAllSpawnerPointList();
    ArrayList<MobSpawnerPoint> allList = new ArrayList<>(allSpawnerPointList);

    new LbnRunnable() {
      int i = 0;

      @Override
      public void run2() {
        for (; i < allList.size() || (i != 0 && i % 2000 == 0); i++) {
          MobSpawnerPoint mobSpawnerPoint = allList.get(i);
          if (look_flg) {
            Chunk chunk = mobSpawnerPoint.getChunk();
            if (!chunk.isLoaded()) {
              chunk.load();
            }
            mobSpawnerPoint.getLocation().getBlock().setType(Material.SPONGE);
          } else {
            Chunk chunk = mobSpawnerPoint.getChunk();
            if (!chunk.isLoaded()) {
              chunk.load();
            }
            mobSpawnerPoint.getLocation().getBlock().setType(Material.AIR);
          }
        }

        if (i >= allList.size()) {
          if (!look_flg) {
            p.sendMessage("スポーン地点のスポンジを削除しました。");
          } else {
            p.sendMessage("スポーン地点にスポンジを設置しました。");
          }
          look_flg = !look_flg;
          cancel();
          return;
        }
      }

      protected void runIfServerEnd() {
        for (MobSpawnerPoint mobSpawnerPoint : allList) {
          Chunk chunk = mobSpawnerPoint.getChunk();
          if (!chunk.isLoaded()) {
            chunk.load();
          }
          mobSpawnerPoint.getLocation().getBlock().setType(Material.AIR);
        }
      }
    }.runTaskTimer(1);
  }

  private void executeList(Player p, String[] arg3) {
    if (!begin.contains(p)) {
      p.sendMessage("開始されていません。/setSpawn beginで開始してください。");
      return;
    }
    Set<MobLocation> set = create.get(p);
    p.sendMessage(ChatColor.GREEN + "====仮登録SpawnPointList====");
    for (MobLocation mobLocation : set) {
      p.sendMessage(mobLocation.locStr + "(" + mobLocation.names.toString() + ")");
    }
    p.sendMessage(ChatColor.GREEN + "====仮登録SpawnPointList====");
  }

  private void executeUndo(Player p, String[] arg3) {
    if (!begin.contains(p)) {
      p.sendMessage("開始されていません。/setSpawn beginで開始してください。");
      return;
    }
    MobLocation mobLocation = lastSet.get(p);
    if (mobLocation == null) {
      p.sendMessage("これ以上、UNDOできません。");
      return;
    }

    // ピンク羊毛を削除する
    mobLocation.setBlock(Material.AIR, 0);
    create.remove(p, mobLocation);
    lastSet.remove(p);
    p.sendMessage("一件undoしました。");
  }

  private void executeCancel(Player p, String[] arg3) {
    if (!begin.contains(p)) {
      p.sendMessage("開始されていません。/setSpawn beginで開始してください。");
      return;
    }
    clear(p);
    p.sendMessage("すべての仮登録を削除しました。");
  }

  private void executeCommit(Player p, String[] arg3) {
    if (!begin.contains(p)) {
      p.sendMessage("開始されていません。/setSpawn beginで開始してください。");
      return;
    }

    // スプレットシートに書き込む
    SpawnPointSheetRunnable spawnPointSheetRunnable = new SpawnPointSheetRunnable(p);
    String memo = p.getName();

    Set<MobLocation> set = create.get(p);
    for (MobLocation mobLocation : set) {
      // getterにセット
      SimpleMobSpawnGetter simpleMobSpawnGetter = new SimpleMobSpawnGetter(MobSpawnerPointManager.getNextId());
      simpleMobSpawnGetter.addMob(mobLocation.getMobList());

      MobSpawnerPoint spawnerPoint = simpleMobSpawnGetter.getMobSpawnerPoint(mobLocation.loc, 3, SpawnLevel.LEVEL3);
      MobSpawnerPointManager.addSpawnPoint(spawnerPoint);

      spawnPointSheetRunnable.addData(spawnerPoint, memo);
    }
    SpletSheetExecutor.onExecute(spawnPointSheetRunnable);

    clear(p);
  }

  protected void clear(Player p) {
    for (MobLocation spawnLoc : create.get(p)) {
      spawnLoc.setBlock(Material.AIR, 0);
    }
    create.removeAll(p);
    lastSet.remove(p);
    begin.remove(p);
  }

  private void executeBegin(Player p, String[] arg3) {
    if (begin.contains(p)) {
      p.sendMessage("すでに開始されています。登録する場合は/setSpawn commitを入力してください");
    } else {
      begin.add(p);
      p.sendMessage("開始されました。/setspawn setで登録したアイテムを持ってクリックしてください。");
    }
  }

  private void executeSet(Player p, String[] arg3) {
    p.sendMessage("アイテムをもってください");

    ArrayList<String> nameList = new ArrayList<String>();
    for (String name : arg3) {
      if (name.equalsIgnoreCase("set")) {
        continue;
      }

      String namereplace = name.replace("_", " ");
      AbstractMob<?> mob = MobHolder.getMobWithNormal(namereplace);
      if (mob == null) {
        p.sendMessage(namereplace + "というmobは存在しません");
        return;
      }
      nameList.add(namereplace);
    }

    ItemStack itemInHand = p.getItemInHand();
    ItemStackUtil.setDispName(itemInHand, ChatColor.GREEN + "SPAWN POINT SETTER");
    ItemStackUtil.setLore(itemInHand, nameList);
    ItemStackUtil.addLore(itemInHand,
        ChatColor.WHITE + "#コマンド /setSpawn beginで仮登録開始",
        ChatColor.WHITE + "#このアイテムをもって右クリックで仮登録",
        ChatColor.WHITE + "#コマンド /setSpawn commitで仮登録",
        ChatColor.WHITE + "#コマンド /setSpawn cancelで仮登録をすべて削除",
        ChatColor.WHITE + "#コマンド /setSpawn undoで最後に登録した仮登録を削除",
        ChatColor.WHITE + "#コマンド /setSpawn listで現在の仮登録を確認");
    p.updateInventory();

    p.sendMessage("モンスターを登録してください。/setSpawn beginで開始し、クリックしスポーンポイントをセットしてください。");
  }

  public static void onClick(PlayerInteractEvent e) {
    ItemStack item = e.getItem();
    String name = ItemStackUtil.getName(item);
    if (!name.contains("SPAWN POINT SETTER")) { return; }

    Player player = e.getPlayer();
    if (!begin.contains(player)) {
      player.sendMessage("開始されていません。/setSpawn beginで開始してください。");
      return;
    }

    Vector direction = player.getLocation().getDirection();
    Snowball spawn = player.getWorld().spawn(player.getLocation().add(0, 1, 0), Snowball.class);
    spawn.setVelocity(direction.multiply(3));
    spawn.setShooter((ProjectileSource) player);
  }

  public static void ProjectileHitEvent(ProjectileHitEvent e) {
    Projectile entity = e.getEntity();
    if (entity.getType() != EntityType.SNOWBALL) { return; }

    ProjectileSource shooter = entity.getShooter();
    if (!(shooter instanceof Player)) { return; }
    Player p = (Player) shooter;
    ItemStack itemInHand = p.getItemInHand();
    if (!ItemStackUtil.getName(itemInHand).contains("SPAWN POINT SETTER")) { return; }

    Location location = e.getEntity().getLocation();
    // 下のブロックを確認
    if (location.add(0, -1, 0).getBlock().isEmpty()) {
      // 一つ上の場所の可能性もあるので選択場所を１つ下にする
      location = location.add(0, -1, 0);
      if (location.add(0, -1, 0).getBlock().isEmpty()) {
        p.sendMessage(ChatColor.RED + "雪玉が着地した下のブロックが地面でないため仮登録できません(" + location.getBlockX() + ", " + location.getBlockY() + ", "
            + location.getBlockZ() + ")");
        return;
      }
    }

    if (!location.getBlock().isEmpty() && !!location.getBlock().isLiquid()) {
      p.sendMessage(ChatColor.RED + "雪玉が着地した地点にブロックが置かれているため仮登録できません");
      return;
    }

    MobLocation mobLocation = new MobLocation(e.getEntity().getLocation());
    // mobの存在チェック
    List<String> lore = ItemStackUtil.getLore(itemInHand);
    for (String name : lore) {
      if (name.contains("#")) {
        continue;
      }
      AbstractMob<?> mob = MobHolder.getMobWithNormal(name);
      if (mob == null) {
        p.sendMessage(name + "というmobは存在しません。");
        return;
      }
      mobLocation.addMob(mob);
    }

    create.put(p, mobLocation);
    // 仮登録した場所にはピンクの羊毛を置く
    mobLocation.setBlock(Material.WOOL, 6);
    int size = create.get(p).size();

    lastSet.put(p, mobLocation);
    p.sendMessage(ChatColor.GREEN + "仮登録しました。本登録するには/setSpawn commitと入力してください。(" + size + ")");
  }

  @Override
  public List<String> onTabComplete(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
    if (arg3.length > 1) {
      if ("set".equalsIgnoreCase(arg3[0])) {
        if (arg3.length == 2) { return (List<String>) StringUtil.copyPartialMatches(arg3[1].toUpperCase(), SpawnMobGetterManager.getNames(),
            new ArrayList<String>(SpawnMobGetterManager.getNames().size())); }
      }
    }
    return ImmutableList.of();
  }

}

class SimpleMobSpawnGetter extends SpletSheetSpawnMobGetter {

  public SimpleMobSpawnGetter(int id) {
    super(id);
  }

  @Override
  public String getName() {
    return "simpleCommand";
  }

  public void addMob(AbstractMob<?> mob) {
    moblist.add(mob);
  }

  public void addMob(List<AbstractMob<?>> mobList) {
    moblist.addAll(mobList);
  }
}

class MobLocation {
  List<AbstractMob<?>> mobList = new ArrayList<AbstractMob<?>>();
  Location loc;
  String locStr;

  StringBuilder names = new StringBuilder();

  public MobLocation(Location location) {
    loc = location;
    locStr = "[" + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ() + "]";
  }

  public void addMob(AbstractMob<?> mob) {
    if (mob.getName() == null || mob.getName().isEmpty()) {
      names.append(mob.getEntityType());
    } else {
      names.append(mob.getName());
    }
    mobList.add(mob);
  }

  public List<AbstractMob<?>> getMobList() {
    return mobList;
  }

  @SuppressWarnings("deprecation")
  public void setBlock(Material m, int data) {
    loc.getBlock().setType(m);
    loc.getBlock().setData((byte) data);
    ;
  }
}
