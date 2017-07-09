package net.l_bulb.dungeoncore.chest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.item.ItemInterface;
import net.l_bulb.dungeoncore.item.ItemManager;
import net.l_bulb.dungeoncore.mob.AbstractMob;
import net.l_bulb.dungeoncore.mob.MobHolder;
import net.l_bulb.dungeoncore.mob.customMob.SpreadSheetBossMob;
import net.l_bulb.dungeoncore.player.customplayer.PlayerChestTpManager;
import net.l_bulb.dungeoncore.util.DungeonLogger;

import lombok.Getter;

@Getter
public abstract class SpletSheetChest extends AbstractCustomChest {

  Location contentLoc;
  int refuelTick;
  Location moveLoc;
  int minItemCount;
  int maxItemCount;
  int moveSec;
  boolean random;

  boolean isBossChestTemplate = false;

  public boolean isBossChestTemplate() {
    return isBossChestTemplate;
  }

  public void setBossChestTemplate(boolean isBossChestTemplate) {
    this.isBossChestTemplate = isBossChestTemplate;
  }

  boolean isTemplateChest = false;

  public SpletSheetChest(Location chestLoc, Location contentLoc, int refuelTick, Location moveLoc, int minItemCount,
      int maxItemCount, int moveSec, boolean random) {
    this.contentLoc = contentLoc;
    this.refuelTick = refuelTick;
    this.moveLoc = moveLoc;
    this.minItemCount = minItemCount;
    this.maxItemCount = maxItemCount;
    this.moveSec = moveSec;
    this.random = random;

    for (AbstractMob<?> mob : MobHolder.getAllMobs()) {
      if (mob instanceof SpreadSheetBossMob) {
        if (((SpreadSheetBossMob) mob).chestLocation() != null && ((SpreadSheetBossMob) mob).chestLocation().equals(chestLoc)) {
          isBossChestTemplate = true;
        }
      }
    }

    String name = ChestLocationManager.getName(chestLoc);
    if (name != null) {
      isTemplateChest = true;
    }
  }

  @Override
  public String getName() {
    return "bonus chest";
  }

  @Override
  public boolean canOpen(Player p, Block block, PlayerInteractEvent e) {
    if (isTemplateChest) {
      if (p.getGameMode() == GameMode.CREATIVE || p.isOp()) { return true; }
      Bukkit.broadcast(p.getName() + "が初期街下のテンプレートチェストを開いています。ハックの可能性があります。",
          "main.lbnDungeonUtil.command.mob.sendMessage");
      return false;
    }

    if (isBossChestTemplate) {
      if (p.getGameMode() == GameMode.CREATIVE || p.isOp()) { return true; }
      Bukkit.broadcastMessage(ChatColor.YELLOW + "やあジョニー、そんな浮かない顔してどうしたんだい?  " + ChatColor.GREEN + "なぁ、マイケル聞いてくれよ、"
          + p.getDisplayName() + "ったらブロックグリッチをしてチェストを開けようとしたんだ。");
      return false;
    }

    return true;
  }

  @Override
  public void removeChest(Location loc) {}

  protected void teleportPlayer(String name) {
    PlayerChestTpManager.teleport(name, moveLoc, moveSec * 20);
  }

  protected int getTeleportTime() {
    return moveSec;
  }

  @Override
  public void executeIfDebug(Player p, Block block, PlayerInteractEvent e) {
    if (contentLoc == null) {
      p.sendMessage("このチェストの中身の座標が存在しません。設定を確認してください。");
      return;
    }
    BlockState state = contentLoc.getBlock().getState();
    if (!(state instanceof Chest)) {
      p.sendMessage(contentLoc.toVector().toString() + "にチェストが存在しません。設定を確認してください。");
      return;
    }
    p.openInventory(((Chest) state).getBlockInventory());

    e.setCancelled(true);
  }

  /**
   * 新しいインベントリを作成
   *
   * @param p
   * @return
   */
  protected Inventory getNewInventory(Player p) {
    Block b = contentLoc.getBlock();
    if (b.getType() != Material.CHEST) {
      DungeonLogger.error(b.getType() + " is not chest:" + b.getLocation().toVector());
      return null;
    }
    // チェストのインベントリー
    Chest state = (Chest) b.getState();
    Inventory blockInventory = state.getBlockInventory();

    Random rnd = new Random();

    // チェストの中身のアイテムを全てリストに入れる
    ArrayList<ItemStack> list = new ArrayList<>();
    for (ItemStack itemStack : blockInventory) {
      if (itemStack == null || itemStack.getType() == Material.AIR) {
        continue;
      }
      list.add(itemStack);
    }

    // 中身をシャッフルする
    Collections.shuffle(list, rnd);

    // インデックスをシャッフルする
    Collections.shuffle(index, rnd);

    // タイトルを作成
    String title = null;
    if (p.getGameMode() == GameMode.CREATIVE) {
      // chest名を取得
      String name = ChestLocationManager.getName(contentLoc);
      if (name != null) {
        title = name;
      }
    }
    title = Optional.ofNullable(title).orElse("chest");

    // 実際にチェストを開けた時のインベントリを作成
    Inventory createInventory = Bukkit.createInventory(null, 9 * 3, ChatColor.WHITE + title);

    int itemCount = 0;
    if (maxItemCount == minItemCount) {
      itemCount = Math.min(list.size(), minItemCount);
    } else {
      itemCount = Math.min(list.size(), rnd.nextInt(Math.abs(maxItemCount - minItemCount) + 1) + minItemCount);
    }

    if (random) {
      while (itemCount > 0) {
        itemCount--;
        createInventory.setItem(index.get(itemCount), getContentsItem(list.get(itemCount)));
      }
    } else {
      for (int i = 0; i < blockInventory.getSize(); i++) {
        ItemStack item = blockInventory.getItem(i);
        if (item == null || item.getType() == Material.AIR) {
          continue;
        }
        createInventory.setItem(i, getContentsItem(item));
      }
    }
    return createInventory;
  }

  /**
   * チェストの中のアイテムを取得
   * ThelowItemなら生成する。thelowアイテムでないなら引数をそのまま返す
   *
   * @return
   */
  private static ItemStack getContentsItem(ItemStack item) {
    ItemInterface customItem = ItemManager.getCustomItem(item);
    ItemStack rtnItem = Optional.ofNullable(customItem).map(val -> val.getItem()).orElse(item);
    return rtnItem;
  }

  /**
   * チェストの中身を指定したものに置き換える
   *
   * @param chest
   */
  abstract public void setRefule(SpletSheetChest chest);

  static List<Integer> index = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
      21, 22, 23, 24, 25, 26);
}
