package net.l_bulb.dungeoncore.mob.minecraftEntity.ai;

import net.l_bulb.dungeoncore.mob.customMob.LbnMobTag;
import net.minecraft.server.v1_8_R1.EntityInsentient;
import net.minecraft.server.v1_8_R1.EntityLiving;
import net.minecraft.server.v1_8_R1.IRangedEntity;
import net.minecraft.server.v1_8_R1.MathHelper;
import net.minecraft.server.v1_8_R1.PathfinderGoal;

/**
 * 元の仕様：距離が引数のf以上離れていたら近づく、fより近かったら弓を打つ
 *
 * 近接攻撃範囲^2+5なら無視
 * 距離が16から22の場合は近づく
 * 
 * @author KENSUKE
 *
 */
public class TheLoWPathfinderGoalArrowAttackForShortLongAI extends PathfinderGoal {
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

  public TheLoWPathfinderGoalArrowAttackForShortLongAI(IRangedEntity irangedentity, double d0, int i, float f, LbnMobTag tag) {
    this(irangedentity, d0, i, i, f, tag);
  }

  public TheLoWPathfinderGoalArrowAttackForShortLongAI(IRangedEntity irangedentity, double d0, int i, int j, float f, LbnMobTag tag) {
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
      setNearAttackRange(tag.getAttackReach());
      setShotTerm(tag.getShotTarm());
      this.a(3);
    }
  }

  public void setNearAttackRange(float val) {
    nearAttackRange = val * val;
  }

  @Override
  public boolean a() {
    EntityLiving entityliving = this.a.getGoalTarget();

    if (entityliving == null) {
      return false;
    } else {
      this.c = entityliving;
      double d0 = this.a.e(this.c.locX, this.c.getBoundingBox().b, this.c.locZ);
      // 近距離攻撃^2+5より近い場合は無視する
      return d0 >= nearAttackRange + 5;
    }
  }

  @Override
  public boolean b() {
    return this.a() || !this.a.getNavigation().m();
  }

  @Override
  public void d() {
    this.c = null;
    this.f = 0;
    this.d = -1;
  }

  @Override
  public void e() {
    double d0 = this.a.e(this.c.locX, this.c.getBoundingBox().b, this.c.locZ); // 対象との距離の2乗
    boolean flag = this.a.getEntitySenses().a(this.c);// 対象が見えるかどうか

    if (flag) {
      ++this.f;
    } else {
      this.f = 0;
    }

    // 敵との距離が16マス以下の場合はその場にいる
    if (d0 <= 16 * 16) {
      this.a.getNavigation().n(); // ターゲットを消す
      // 距離が16 ~ 22の時は近づく
    } else if (d0 <= 22 * 22) {
      this.a.getNavigation().a(this.c, this.e);
    } else {
      this.a.getNavigation().n(); // ターゲットを消す
    }

    this.a.getControllerLook().a(this.c, 30.0F, 30.0F);
    float f;

    if (--this.d == 0) {
      // 一定距離以上離れていたら打たない
      if (d0 > (double) 22 * 22 || !flag) { return; }

      f = MathHelper.sqrt(d0) / this.i;
      float f1 = MathHelper.a(f, 0.1F, 1.0F);

      this.b.a(this.c, f1);// 発射
      this.d = shotParSecound;
    } else if (this.d < 0) {
      f = MathHelper.sqrt(d0) / this.i;
      this.d = shotParSecound;
    }

  }

  int shotParSecound = 20;

  /**
   * 一秒間に何発打つかセットする
   * 
   * @param val
   */
  public void setShotTerm(int val) {
    shotParSecound = (int) (20.0 / val);
  }
}
