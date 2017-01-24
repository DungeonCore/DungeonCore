package lbn.mob.customEntity1_7.ai;

import net.minecraft.server.v1_8_R1.Entity;
import net.minecraft.server.v1_8_R1.EntityInsentient;
import net.minecraft.server.v1_8_R1.EntityLiving;
import net.minecraft.server.v1_8_R1.IRangedEntity;
import net.minecraft.server.v1_8_R1.MathHelper;
import net.minecraft.server.v1_8_R1.PathfinderGoal;

public class TheLoWPathfinderGoalArrowAttack extends PathfinderGoal {
    protected final EntityInsentient a;
    protected final IRangedEntity b;
    protected EntityLiving c;
    protected int d;
    protected double e;
    protected int f;
    protected int g;
    protected int h;
    protected float i;
    protected float j;

    protected float nearAttackRange = 3 * 3;

    public TheLoWPathfinderGoalArrowAttack(IRangedEntity irangedentity, double d0, int i, float f) {
        this(irangedentity, d0, i, i, f);
    }

    public TheLoWPathfinderGoalArrowAttack(IRangedEntity irangedentity, double d0, int i, int j, float f) {
        this.d = -1;
        if (!(irangedentity instanceof EntityLiving)) {
            throw new IllegalArgumentException("ArrowAttackGoal requires Mob implements RangedAttackMob");
        } else {
            this.b = irangedentity;
            this.a = (EntityInsentient) irangedentity;
            this.e = d0;
            this.g = i;
            this.h = j;
            this.i = f;
            this.j = f * f;
            this.a(3);
        }
    }

    public void setNearAttackRange(float val) {
    	nearAttackRange = val * val;
    }

    public boolean a() {
        EntityLiving entityliving = this.a.getGoalTarget();

        if (entityliving == null) {
            return false;
        } else {
            this.c = entityliving;
            double d0 = this.a.e(this.c.locX, this.c.getBoundingBox().b, this.c.locZ);
            return d0 >= nearAttackRange;
        }
    }

    public boolean b() {
        return this.a() || !this.a.getNavigation().m();
    }

    public void d() {
        this.c = null;
        this.f = 0;
        this.d = -1;
    }

    public void e() {
        double d0 = this.a.e(this.c.locX, this.c.getBoundingBox().b, this.c.locZ); //対象との距離の2乗
        boolean flag = this.a.getEntitySenses().a(this.c);//対象が見えるかどうか

        if (flag) {
            ++this.f;
        } else {
            this.f = 0;
        }

        //近すぎる場合
        if (d0 < 9) {
        	this.a.getNavigation().n(); //ターゲットを消す
        } else  if (d0 <= (double) this.j && this.f >= 20) {
            this.a.getNavigation().n(); //ターゲットを消す
        } else {
            this.a.getNavigation().a((Entity) this.c, this.e);
        }

        this.a.getControllerLook().a(this.c, 30.0F, 30.0F);
        float f;

        if (--this.d == 0) {
            if (d0 > (double) this.j || !flag) {
                return;
            }

            f = MathHelper.sqrt(d0) / this.i;
            float f1 = MathHelper.a(f, 0.1F, 1.0F);

            this.b.a(this.c, f1);//発射
            this.d = MathHelper.d(f * (float) (this.h - this.g) + (float) this.g);
        } else if (this.d < 0) {
            f = MathHelper.sqrt(d0) / this.i;
            this.d = MathHelper.d(f * (float) (this.h - this.g) + (float) this.g);
        }

    }
}

