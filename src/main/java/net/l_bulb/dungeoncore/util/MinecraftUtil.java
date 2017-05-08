package net.l_bulb.dungeoncore.util;

import net.minecraft.server.v1_8_R1.PacketPlayOutNamedSoundEffect;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R1.CraftSound;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class MinecraftUtil {

  /**
   *
   //http://wiki.vg/Protocol#Sound_Effect
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

}
