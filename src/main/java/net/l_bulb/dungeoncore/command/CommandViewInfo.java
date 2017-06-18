package net.l_bulb.dungeoncore.command;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import net.l_bulb.dungeoncore.api.player.TheLowPlayer;
import net.l_bulb.dungeoncore.api.player.TheLowPlayerManager;
import net.l_bulb.dungeoncore.common.buff.BuffData;
import net.l_bulb.dungeoncore.common.buff.BuffDataFactory;
import net.l_bulb.dungeoncore.common.cooltime.CooltimeManager;
import net.l_bulb.dungeoncore.common.menu.MenuSelectorInterface;
import net.l_bulb.dungeoncore.common.menu.MenuSelectorManager;
import net.l_bulb.dungeoncore.common.particle.ParticleData;
import net.l_bulb.dungeoncore.common.particle.ParticleManager;
import net.l_bulb.dungeoncore.item.ItemManager;
import net.l_bulb.dungeoncore.item.customItem.armoritem.ArmorBase;
import net.l_bulb.dungeoncore.item.itemInterface.ArmorItemable;
import net.l_bulb.dungeoncore.item.nbttag.ItemStackNbttagAccessor;
import net.l_bulb.dungeoncore.item.slot.table.SlotMerchant;
import net.l_bulb.dungeoncore.item.system.craft.CraftItemSelectViewer;
import net.l_bulb.dungeoncore.mobspawn.chunk.ChunkGroup;
import net.l_bulb.dungeoncore.npc.NpcManager;
import net.l_bulb.dungeoncore.player.playerIO.PlayerIODataManager;
import net.l_bulb.dungeoncore.util.ItemStackUtil;
import net.l_bulb.dungeoncore.util.JavaUtil;

public class CommandViewInfo implements CommandExecutor {
  @Override
  public boolean onCommand(CommandSender paramCommandSender, Command paramCommand, String paramString, String[] paramArrayOfString) {
    if (paramArrayOfString.length == 0) {
      new CommandMenu().open((Player) paramCommandSender);
      return true;
    }

    if (!(paramCommandSender instanceof Player)) { return false; }

    // if (!((Player)paramCommandSender).isOp()) {
    // return false;
    // }

    String param = paramArrayOfString[0];
    if (param == null) {
      paramCommandSender.sendMessage("param is null");
      return false;
    }

    Player target = (Player) paramCommandSender;
    ;
    if (paramArrayOfString.length == 2) {
      target = Bukkit.getPlayer(paramArrayOfString[1]);
    }

    switch (param) {
      case "slot":
        new SlotMerchant(target).open();
        break;
      case "cooltime":
        CooltimeManager.clear();
        break;
      case "stop":
        try {
          Thread.sleep(1000 * 20);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        break;
      case "save":
        PlayerIODataManager.saveAsZip();
        break;
      case "craft":
        Block block = new Location(Bukkit.getWorld("thelow"), -23, 70, 0).getBlock();
        Inventory inventory = CraftItemSelectViewer.getInventory(block);
        CraftItemSelectViewer.open((Player) paramCommandSender, Arrays.asList(inventory), 0);
        break;
      case "status":
        sendPlayerStatus(target);
        break;
      case "version":
        paramCommandSender.sendMessage("1.1");
        break;
      case "npc":
        NpcManager.onTest();
        break;
      case "buff":
        BuffData buffFromId = BuffDataFactory.getBuffFromId(paramArrayOfString[1]);
        if (buffFromId != null) {
          buffFromId.addBuff((Player) paramCommandSender);
        }
        break;
      case "xyz":
        String command = "tellraw " + target.getName() + " {\"text\":\"座標取得\",\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\""
            + target.getLocation().getBlockX() + " " + target.getLocation().getBlockY() + " " + target.getLocation().getBlockZ() + "\"}}";
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        break;
      case "xyz2":
        String command2 = "tellraw " + target.getName() + " {\"text\":\"座標取得\",\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\""
            + target.getWorld().getName() + ":" + target.getLocation().getBlockX() + "," + target.getLocation().getBlockY() + ","
            + target.getLocation().getBlockZ() + "\"}}";
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command2);
        break;
      case "score":
        TheLowPlayer theLowPlayer = TheLowPlayerManager.getTheLowPlayer(target);
        theLowPlayer.setShowSideBar(!theLowPlayer.isShowSideBar());
        break;
      case "particle":
        ParticleData particle = ParticleManager.getParticleData(paramArrayOfString[1]);
        if (particle != null) {
          particle.run(((Player) paramCommandSender).getLocation());
        }
        break;
      case "armor":
        sendArmorData((Player) paramCommandSender);
        break;
      case "heal":
        target.setHealth(target.getMaxHealth());
        break;
      case "test":
        break;
      case "chunk":
        sendChunkData(target);
        break;
      default:
        paramCommandSender.sendMessage("unknown param");
    }
    return true;

  }

  public void sendChunkData(Player target) {
    target.sendMessage(ChatColor.GREEN + "======= chunk data =======");
    Chunk chunk = target.getLocation().getChunk();
    target.sendMessage("chunkId(x:" + chunk.getX() + ", z:" + chunk.getZ() + ")");
    ChunkGroup chunkGroup = new ChunkGroup(target.getLocation());
    target.sendMessage("chunk group id(x:" + chunkGroup.getX() + ", y:" + chunkGroup.getY() + ", z:" + chunkGroup.getZ() + ")");

    HashMap<String, Integer> nearMob = new HashMap<>();
    HashMap<String, Integer> farMob = new HashMap<>();
    chunkGroup.consumeChunk(e -> true, (e, c) -> {
      String key = JavaUtil.getNull(e.getCustomName(), e.getType().toString());
      if (c.isNear()) {
        nearMob.put(key, nearMob.getOrDefault(key, 0) + 1);
      } else {
        farMob.put(key, nearMob.getOrDefault(key, 0) + 1);
      }
    });

    nearMob.entrySet().stream().forEach(e -> {
      target.sendMessage(e.getKey() + ":" + (e.getValue() + farMob.getOrDefault(e.getKey(), 0) + "(近く：" + e.getValue() + ")"));
      farMob.remove(e.getKey());
    });
    farMob.entrySet().stream().forEach(e -> target.sendMessage(e.getKey() + ":" + e.getValue() + "(近く：0)"));
  }

  private void sendArmorData(Player p) {
    double normalMobArmorPoint = 0;
    double bossArmorPoint = 0;

    ItemStack[] armorContents = p.getEquipment().getArmorContents();
    for (ItemStack itemStack : armorContents) {
      ArmorItemable customItem = ItemManager.getCustomItem(ArmorItemable.class, itemStack);
      if (customItem == null) {
        continue;
      }

      ItemStackNbttagAccessor accessor = new ItemStackNbttagAccessor(itemStack);

      normalMobArmorPoint += accessor.getNormalArmorPoint();
      bossArmorPoint += accessor.getBossArmorPoint();
    }

    p.sendMessage(ChatColor.GOLD + "=========================");
    p.sendMessage(ChatColor.GREEN + "合計防具ポイント");
    p.sendMessage("    通常モンスター：" + normalMobArmorPoint);
    p.sendMessage("    ボスモンスター：" + bossArmorPoint);
    p.sendMessage(ChatColor.GREEN + "ダメージカット率");
    p.sendMessage("    通常モンスター：" + (100 - ArmorBase.getDamageCutParcent(normalMobArmorPoint) * 100) + "%カット");
    p.sendMessage("    ボスモンスター：" + (100 - ArmorBase.getDamageCutParcent(bossArmorPoint) * 100) + "%カット");
    p.sendMessage(ChatColor.GOLD + "=========================");
  }

  private void sendPlayerStatus(Player target) {
    Collection<PotionEffect> activePotionEffects = target.getActivePotionEffects();
    target.sendMessage("active potion effect:");
    for (PotionEffect potionEffect : activePotionEffects) {
      target.sendMessage("    " + potionEffect.getType() + ", " + (potionEffect.getDuration() / 20) + "s");
    }
  }
}

class CommandMenu implements MenuSelectorInterface {
  static {
    MenuSelectorManager.regist(new CommandMenu());
  }

  @Override
  public void open(Player p) {
    Inventory createInventory = Bukkit.createInventory(null, 9 * 1, getTitle());
    createInventory.addItem(getItem("座標取得", "tl xyz", Material.COMPASS, 0, "/tl xyzを実行", "x y z形式で座標を取得"));
    createInventory.addItem(getItem("座標取得2", "tl xyz2", Material.COMPASS, 0, "/tl xyz2を実行", "world:x,y,z形式で座標を取得"));
    createInventory.addItem(getItem("クールタイム初期化", "tl cooltime", Material.CACTUS, 0, "/tl cooltimeを実行", "全てのPlayerのクールタイムをリセット"));
    createInventory.addItem(getItem("スコアボード", "tl score", Material.MAP, 0, "/tl scoreを実行", "スコアボードの表示/非表示切り替え"));
    createInventory.addItem(getItem("装備", "tl armor", Material.DIAMOND_CHESTPLATE, 0, "/tl armorを実行", "現在装備中の防具情報を取得"));
    createInventory.addItem(getItem("回復", "tl heal", Material.GHAST_TEAR, 0, "/tl healを実行", "体力を全回復する"));
    p.openInventory(createInventory);
  }

  ItemStack getItem(String name, String command, Material m, int data, String... line) {
    List<String> collect = Arrays.stream(line).map(val -> ChatColor.WHITE.toString() + val).collect(Collectors.toList());
    ItemStack item = ItemStackUtil.getItem(ChatColor.GREEN + name, m, (byte) data, collect.toArray(new String[collect.size()]));
    ItemStackUtil.setNBTTag(item, "thelow_command", command);

    return item;
  }

  @Override
  public void onSelectItem(Player p, ItemStack item, InventoryClickEvent e) {
    if (item == null) { return; }

    String nbtTag = ItemStackUtil.getNBTTag(item, "thelow_command");
    if (nbtTag != null && !nbtTag.isEmpty()) {
      Bukkit.dispatchCommand(p, nbtTag);
      p.sendMessage(ChatColor.YELLOW + "コマンド:/" + nbtTag + "を実行しました。");
    }
  }

  @Override
  public String getTitle() {
    return "view info table";
  }

}
