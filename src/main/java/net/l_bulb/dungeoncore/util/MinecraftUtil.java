package net.l_bulb.dungeoncore.util;

import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.l_bulb.dungeoncore.util.BlockUtil.BlockData;

import net.minecraft.server.v1_8_R1.CommandAbstract;

public class MinecraftUtil {

  /**
   * 指定したPlayerに聞こえるようにだけ音を鳴らす
   *
   * @param p
   * @param sound
   * @param volume
   * @param pitch
   */
  public static void playSoundForPlayer(Player p, Sound sound, float volume, float pitch) {
    p.playSound(p.getLocation(), sound, volume, pitch);
  }

  /**
   * 周囲のPlayerに聞こえるように音を鳴らす
   *
   * @param location
   * @param sound
   * @param volume
   * @param pitch
   */
  public static void playSoundForAll(Location location, Sound sound, double volume, double pitch) {
    location.getWorld().playSound(location, sound, (float) volume, (float) pitch);
  }

  public static void chunkLoadIfUnload(Chunk c) {
    if (!c.isLoaded()) {
      c.load();
    }
  }

  public static Location getSenderLocation(CommandSender sender) {
    Location senderLoc = null;
    if ((sender instanceof BlockCommandSender)) {
      senderLoc = ((BlockCommandSender) sender).getBlock().getLocation();
    } else if (sender instanceof Player) {
      senderLoc = ((Player) sender).getLocation();
    }
    return senderLoc;
  }

  public static String getLocationString(Location loc) {
    return String.format("(%d, %d, %d)", loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
  }

  /**
   * blockid:dataからブロック情報を取得する
   *
   * @param blockString
   * @return
   */
  public static BlockData getBlockData(String blockString) {
    try {
      String id = blockString.substring(0, blockString.indexOf(":") == -1 ? blockString.length() : blockString.indexOf(":"));
      final String data;
      if (blockString.contains(":") && !blockString.endsWith(":")) {
        data = blockString.substring(blockString.indexOf(":") + 1);
      } else {
        data = "0";
      }

      if (!NumberUtils.isNumber(id) || !NumberUtils.isNumber(data)) { return null; }

      @SuppressWarnings("deprecation")
      final Material material = Material.getMaterial(Integer.parseInt(id));
      if (material == null) { return null; }

      BlockData blockData = new BlockData();
      blockData.setM(material);
      blockData.setB(Byte.parseByte(data));
      return blockData;
    } catch (Exception e) {
      return null;
    }
  }

  /**
   * コマンドの引数からLocationを取得する。
   *
   * @param sender コマンドのsender
   * @param paramX コマンド引数のx
   * @param paramY コマンド引数のy
   * @param paramZ コマンド引数のz
   * @param isCenterLoc +0.5をしてブロックの中心の座標を取得するならTRUE
   * @return
   */
  public static Location getLocationByCommandParam(CommandSender sender, String paramX, String paramY, String paramZ, boolean isCenterLoc) {
    Location senderLoc = getSenderLocation(sender);
    double x = CommandAbstract.b(senderLoc.getX(), paramX, -30000000, 30000000, isCenterLoc);
    double y = CommandAbstract.b(senderLoc.getY(), paramY, 0, 256, isCenterLoc);
    double z = CommandAbstract.b(senderLoc.getZ(), paramZ, -30000000, 30000000, isCenterLoc);
    return new Location(senderLoc.getWorld(), x, y, z);
  }

}
