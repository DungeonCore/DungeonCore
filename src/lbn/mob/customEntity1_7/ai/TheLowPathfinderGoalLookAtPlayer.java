package lbn.mob.customEntity1_7.ai;

import net.minecraft.server.v1_8_R1.Entity;
import net.minecraft.server.v1_8_R1.EntityHuman;
import net.minecraft.server.v1_8_R1.EntityInsentient;
import net.minecraft.server.v1_8_R1.PathfinderGoal;

public class TheLowPathfinderGoalLookAtPlayer extends PathfinderGoal{
	protected EntityInsentient a;
	protected Entity b;
	protected float c;
	private int e;
	private float f;
	protected Class<?> d;

	public TheLowPathfinderGoalLookAtPlayer(EntityInsentient paramEntityInsentient, Class<?> paramClass, float paramFloat) {
		this.a = paramEntityInsentient;
		this.d = paramClass;
		this.c = paramFloat;
		this.f = 0.02F;
		a(2);
	}

	public TheLowPathfinderGoalLookAtPlayer(EntityInsentient paramEntityInsentient, Class<?> paramClass, float paramFloat1, float paramFloat2) {
		this.a = paramEntityInsentient;
		this.d = paramClass;
		this.c = paramFloat1;
		this.f = paramFloat2;
		a(2);
	}

	public boolean a() {
		if (this.a.bb().nextFloat() >= this.f) {
			return false;
		}
		if (this.a.getGoalTarget() != null) {
			this.b = this.a.getGoalTarget();
		}
		if (this.d == EntityHuman.class) {
			this.b = this.a.world.findNearbyPlayer(this.a, this.c);
		} else {
			this.b = this.a.world.a(this.d, this.a.getBoundingBox().grow(this.c, 3.0D, this.c), this.a);
		}
		return this.b != null;
	}

	public boolean b() {
		if (!this.b.isAlive()) {
			return false;
		}
		if (this.a.h(this.b) > this.c * this.c) {
			return false;
		}
		return this.e > 0;
	}

	public void c() {
		this.e = (40 + this.a.bb().nextInt(40));
	}

	public void d() {
		this.b = null;
	}

	public void e() {
		this.a.getControllerLook().a(this.b.locX, this.b.locY + this.b.getHeadHeight(), this.b.locZ, 10.0F, this.a.bP());
		this.e -= 1;
	}
}
