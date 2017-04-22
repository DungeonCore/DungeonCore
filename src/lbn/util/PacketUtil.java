package lbn.util;

import lbn.dungeoncore.Main;
import net.minecraft.server.v1_8_R1.EntityLiving;
import net.minecraft.server.v1_8_R1.PacketPlayOutAnimation;
import net.minecraft.server.v1_8_R1.WorldServer;

import org.bukkit.craftbukkit.v1_8_R1.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;

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
}
