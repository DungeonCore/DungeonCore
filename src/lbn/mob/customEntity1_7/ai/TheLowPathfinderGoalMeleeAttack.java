package lbn.mob.customEntity1_7.ai;

import net.minecraft.server.v1_8_R1.BlockPosition;
import net.minecraft.server.v1_8_R1.Entity;
import net.minecraft.server.v1_8_R1.EntityCreature;
import net.minecraft.server.v1_8_R1.EntityLiving;
import net.minecraft.server.v1_8_R1.PathEntity;
import net.minecraft.server.v1_8_R1.PathfinderGoal;
import net.minecraft.server.v1_8_R1.World;

public class TheLowPathfinderGoalMeleeAttack extends PathfinderGoal {

    World a;
    protected EntityCreature b;
    int c;
    double d;
    boolean e;
    PathEntity f;
    Class<?> g;
    private int h;
    private double i;
    private double j;
    private double k;

    protected int attackTerm = 20;

    protected double attackRange = -1;

    protected boolean isJump = false;



    public TheLowPathfinderGoalMeleeAttack(EntityCreature entitycreature, Class<?> oclass, double d0, boolean flag) {
        this(entitycreature, d0, flag);
        this.g = oclass;
    }

    public TheLowPathfinderGoalMeleeAttack(EntityCreature entitycreature, double d0, boolean flag) {
        this.b = entitycreature;
        this.a = entitycreature.world;
        this.d = d0;
        this.e = flag;
        this.a(3);
    }

    public boolean a() {
        EntityLiving entityliving = this.b.getGoalTarget();

        if (entityliving == null) {
            return false;
        } else if (!entityliving.isAlive()) {
            return false;
        } else if (this.g != null && !this.g.isAssignableFrom(entityliving.getClass())) {
            return false;
        } else {
            this.f = this.b.getNavigation().a((Entity) entityliving);
            return this.f != null;
        }
    }

    public boolean b() {
        EntityLiving entityliving = this.b.getGoalTarget();

        return entityliving == null ? false : (!entityliving.isAlive() ? false : (!this.e ? !this.b.getNavigation().m() : this.b.d(new BlockPosition(entityliving))));
    }

    public void c() {
        this.b.getNavigation().a(this.f, this.d);
        this.h = 0;
    }

    public void d() {
        this.b.getNavigation().n();
    }

    public void e() {
        EntityLiving entityliving = this.b.getGoalTarget();

        this.b.getControllerLook().a(entityliving, 30.0F, 30.0F);
        double d0 = this.b.e(entityliving.locX, entityliving.getBoundingBox().b, entityliving.locZ);
        double d1 = attackRange + entityliving.width;
        if (attackRange < 0) {
        	d1 = this.a(entityliving); //腕の長さ
        }

        --this.h;

        if (isJump && d0 <= d1 + 1) { //ジャンプ斬り
        	this.b.getControllerJump().a();
        } else
        	if ((this.e || this.b.getEntitySenses().a(entityliving)) && this.h <= 0 && (this.i == 0.0D && this.j == 0.0D && this.k == 0.0D || entityliving.e(this.i, this.j, this.k) >= 1.0D || this.b.bb().nextFloat() < 0.05F)) {
            this.i = entityliving.locX;
            this.j = entityliving.getBoundingBox().b;
            this.k = entityliving.locZ;
            this.h = 5 + this.b.bb().nextInt(7);
            if (d0 > 1024.0D) {
                this.h += 10;
            } else if (d0 > 256.0D) {
                this.h += 5;
            }

            if (!this.b.getNavigation().a((Entity) entityliving, this.d)) {
                this.h += 15;
            }
        }

        //実際の攻撃処理
        this.c = Math.max(this.c - 1, 0);
        if (d0 <= d1 && this.c <= 0) {
            this.c = attackTerm; //攻撃頻度
            if (this.b.bz() != null) {
                this.b.bv();
            }

            this.b.r(entityliving);
        }

    }

    protected double a(EntityLiving entityliving) {
        return (double) (this.b.width * 2.0F * this.b.width * 2.0F + entityliving.width);
    }

    public static void main(String[] args) {
		System.out.println((int)(20/11));
	}

    /**
     * 秒間いくつか20 / x = y → 20/y = x
     * @param clickPerSec
     */
	public void setAttackTerm(double clickPerSec) {
		this.attackTerm = (int)(20.0 / clickPerSec) - 1;
	}

	public void setAttackRange(double attackRange) {
		this.attackRange = attackRange;
	}

	public void setJump(boolean isJump) {
		this.isJump = isJump;
	}
}
