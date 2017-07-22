package net.l_bulb.dungeoncore.util;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R1.CraftSound;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import net.l_bulb.dungeoncore.dungeoncore.Main;

import net.minecraft.server.v1_8_R1.EntityLiving;
import net.minecraft.server.v1_8_R1.PacketPlayOutAnimation;
import net.minecraft.server.v1_8_R1.PacketPlayOutNamedSoundEffect;
import net.minecraft.server.v1_8_R1.WorldServer;

public class PacketUtil {
  /**
   * 攻撃のモーション
   *
   * @param e
   */
  public static void sendAttackMotionPacket(LivingEntity e) {
    EntityLiving handle = ((CraftLivingEntity) e).getHandle();
    new BukkitRunnable() {
      @Override
      public void run() {
        if ((handle.world instanceof WorldServer)) {
          ((WorldServer) handle.world).getTracker().a(handle, new PacketPlayOutAnimation(handle, 0));
        }
      }
    }.runTaskLater(Main.plugin, (long) (20 * 0.2));
  }

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
}
