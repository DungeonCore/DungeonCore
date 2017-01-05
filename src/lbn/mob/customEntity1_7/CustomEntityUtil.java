package lbn.mob.customEntity1_7;

import lbn.mob.customEntity.ICustomEntitySummonable;
import net.minecraft.server.v1_8_R1.Entity;
import net.minecraft.server.v1_8_R1.EntityLiving;

import org.bukkit.craftbukkit.v1_8_R1.entity.CraftLivingEntity;

public class CustomEntityUtil {
	public static EntityLiving getEntity(org.bukkit.entity.LivingEntity entity) {
		return ((CraftLivingEntity)entity).getHandle();
	}
//	public static void onFly(ChunkCoordinates h, EntityLiving e) {
//		Random rnd = e.aI();
//
//		if (h != null
//				&& (!e.world.isEmpty(h.x, h.y, h.z) || h.y < 1)) {
//			h = null;
//		}
//
//		if (h == null
//				|| rnd.nextInt(30) == 0
//				|| h.e((int) e.locX, (int) e.locY, (int) e.locZ) < 4.0F) {
//			h = new ChunkCoordinates((int) e.locX
//					+ rnd.nextInt(7) - rnd.nextInt(7),
//					(int) e.locY + rnd.nextInt(6) - 2,
//					(int) e.locZ + rnd.nextInt(7)
//							- rnd.nextInt(7));
//		}
//
//		double d0 = (double) h.x + 0.5D - e.locX;
//		double d1 = (double) h.y + 0.1D - e.locY + 3;
//		double d2 = (double) h.z + 0.5D - e.locZ;
//
//		e.motX += (Math.signum(d0) * 0.5D - e.motX) * 0.10000000149011612D;
//		e.motY += (Math.signum(d1) * 0.699999988079071D - e.motY) * 0.10000000149011612D;
//		e.motZ += (Math.signum(d2) * 0.5D - e.motZ) * 0.10000000149011612D;
////		float f = (float) (Math.atan2(e.motZ, e.motX) * 180.0D / 3.1415927410125732D) - 90.0F;
////		float f1 = MathHelper.g(f - e.yaw);
//
//		e.be = 0.5F;
////		e.yaw += f1;
//	}

	public static boolean isSummon(Entity entity) {
		if (entity == null) {
			return false;
		}
		return (entity instanceof ICustomEntitySummonable) && ((ICustomEntitySummonable)entity).isSummon();
	}
}
