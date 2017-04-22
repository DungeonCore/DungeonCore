package lbn.mob.minecraftEntity.ai.bat_ai;

import java.util.Iterator;
import java.util.List;

import net.minecraft.server.v1_8_R1.AxisAlignedBB;
import net.minecraft.server.v1_8_R1.EntityInsentient;
import net.minecraft.server.v1_8_R1.EntityLiving;

import org.bukkit.event.entity.EntityTargetEvent;

public class TheLowPathfinderGoalHurtByTarget extends TheLowPathfinderGoalTarget {
	private boolean a;
	private int b;
	private final Class<?>[] c;

	public TheLowPathfinderGoalHurtByTarget(EntityInsentient entitycreature, boolean flag, Class<?>... aclass) {
		super(entitycreature, false);
		this.a = flag;
		this.c = aclass;
		a(1);
	}

	public boolean a() {
		int i = this.e.bd();

		return (i != this.b) && (a(this.e.getLastDamager(), false));
	}

	public void c() {
		this.e.setGoalTarget(this.e.getLastDamager(), EntityTargetEvent.TargetReason.TARGET_ATTACKED_ENTITY, true);
		this.b = this.e.bd();
		if (this.a) {
			double d0 = f();
			List<?> list = this.e.world.a(this.e.getClass(), new AxisAlignedBB(this.e.locX, this.e.locY, this.e.locZ,
					this.e.locX + 1.0D, this.e.locY + 1.0D, this.e.locZ + 1.0D).grow(d0, 10.0D, d0));
			Iterator<?> iterator = list.iterator();
			while (iterator.hasNext()) {
				EntityInsentient entitycreature = (EntityInsentient) iterator.next();
				if ((this.e != entitycreature) && (entitycreature.getGoalTarget() == null)
						&& (!entitycreature.c(this.e.getLastDamager()))) {
					boolean flag = false;
					Class<?>[] aclass = this.c;
					int i = aclass.length;
					for (int j = 0; j < i; j++) {
						Class<?> oclass = aclass[j];
						if (entitycreature.getClass() == oclass) {
							flag = true;
							break;
						}
					}
					if (!flag) {
						a(entitycreature, this.e.getLastDamager());
					}
				}
			}
		}
		super.c();
	}

	protected void a(EntityInsentient entitycreature, EntityLiving entityliving) {
		entitycreature.setGoalTarget(entityliving, EntityTargetEvent.TargetReason.TARGET_ATTACKED_NEARBY_ENTITY, true);
	}
}
