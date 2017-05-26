package net.l_bulb.dungeoncore.util;

import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R1.CraftSound;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.l_bulb.dungeoncore.util.BlockUtil.BlockData;

import net.minecraft.server.v1_8_R1.PacketPlayOutNamedSoundEffect;

public class MinecraftUtil {

  /**
   *
   * //http://wiki.vg/Protocol#Sound_Effect
   *
   * @param center
   * @param sound
   * @param volume
   * @param pitch
   * @param range
   */
  public static void sendSound(Location center, Sound sound, float volume, float pitch, double range) {
    // packetを送信
    for (Player p : center.getWorld().getPlayers()) {
      if (!p.isOnline()) {
        continue;
      }
      Location loc = p.getLocation();

      // 聞こえるが遠い場所
      if (loc.distance(center) <= range && loc.distance(center) > (range / 2)) {
        volume = volume * 0.7f;
      }

      PacketPlayOutNamedSoundEffect packet = new PacketPlayOutNamedSoundEffect(CraftSound.getSound(sound), center.getX(), center.getY(),
          center.getZ(), volume, pitch);
      ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
    }
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

}
