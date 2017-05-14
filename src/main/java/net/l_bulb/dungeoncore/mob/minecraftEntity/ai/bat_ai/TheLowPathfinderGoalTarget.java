package net.l_bulb.dungeoncore.mob.minecraftEntity.ai.bat_ai;

import org.apache.commons.lang3.StringUtils;

import net.minecraft.server.v1_8_R1.AttributeInstance;
import net.minecraft.server.v1_8_R1.EntityHuman;
import net.minecraft.server.v1_8_R1.EntityInsentient;
import net.minecraft.server.v1_8_R1.EntityLiving;
import net.minecraft.server.v1_8_R1.EntityOwnable;
import net.minecraft.server.v1_8_R1.GenericAttributes;
import net.minecraft.server.v1_8_R1.MathHelper;
import net.minecraft.server.v1_8_R1.PathEntity;
import net.minecraft.server.v1_8_R1.PathPoint;
import net.minecraft.server.v1_8_R1.PathfinderGoal;
import net.minecraft.server.v1_8_R1.ScoreboardTeamBase;

public abstract class TheLowPathfinderGoalTarget extends PathfinderGoal {
  protected final EntityInsentient e;
  protected boolean f;
  private boolean a;
  private int b;
  private int c;
  private int d;

  public TheLowPathfinderGoalTarget(EntityInsentient paramEntityCreature, boolean paramBoolean) {
    this(paramEntityCreature, paramBoolean, false);
  }

  public TheLowPathfinderGoalTarget(EntityInsentient paramEntityCreature, boolean paramBoolean1, boolean paramBoolean2) {
    this.e = paramEntityCreature;
    this.f = paramBoolean1;
    this.a = paramBoolean2;
  }

  @Override
  public boolean b() {
    EntityLiving localEntityLiving = this.e.getGoalTarget();
    if (localEntityLiving == null) { return false; }
    if (!localEntityLiving.isAlive()) { return false; }
    ScoreboardTeamBase localScoreboardTeamBase1 = this.e.getScoreboardTeam();
    ScoreboardTeamBase localScoreboardTeamBase2 = localEntityLiving.getScoreboardTeam();
    if ((localScoreboardTeamBase1 != null) && (localScoreboardTeamBase2 == localScoreboardTeamBase1)) { return false; }
    double d1 = f();
    if (this.e.h(localEntityLiving) > d1 * d1) { return false; }
    if (this.f) {
      if (this.e.getEntitySenses().a(localEntityLiving)) {
        this.d = 0;
      } else if (++this.d > 60) { return false; }
    }
    if (((localEntityLiving instanceof EntityHuman)) && (((EntityHuman) localEntityLiving).abilities.isInvulnerable)) { return false; }
    return true;
  }

  protected double f() {
    AttributeInstance localAttributeInstance = this.e.getAttributeInstance(GenericAttributes.b);
    return localAttributeInstance == null ? 16.0D : localAttributeInstance.getValue();
  }

  @Override
  public void c() {
    this.b = 0;
    this.c = 0;
    this.d = 0;
  }

  @Override
  public void d() {
    this.e.setGoalTarget(null);
  }

  public static boolean a(EntityInsentient paramEntityInsentient, EntityLiving paramEntityLiving, boolean paramBoolean1, boolean paramBoolean2) {
    if (paramEntityLiving == null) { return false; }
    if (paramEntityLiving == paramEntityInsentient) { return false; }
    if (!paramEntityLiving.isAlive()) { return false; }
    if (!paramEntityInsentient.a(paramEntityLiving.getClass())) { return false; }
    ScoreboardTeamBase localScoreboardTeamBase1 = paramEntityInsentient.getScoreboardTeam();
    ScoreboardTeamBase localScoreboardTeamBase2 = paramEntityLiving.getScoreboardTeam();
    if ((localScoreboardTeamBase1 != null) && (localScoreboardTeamBase2 == localScoreboardTeamBase1)) { return false; }
    if (((paramEntityInsentient instanceof EntityOwnable)) && (StringUtils.isNotEmpty(((EntityOwnable) paramEntityInsentient).getOwnerUUID()))) {
      if (((paramEntityLiving instanceof EntityOwnable))
          && (((EntityOwnable) paramEntityInsentient).getOwnerUUID().equals(((EntityOwnable) paramEntityLiving).getOwnerUUID()))) { return false; }
      if (paramEntityLiving == ((EntityOwnable) paramEntityInsentient).getOwner()) { return false; }
    } else if (((paramEntityLiving instanceof EntityHuman)) && (!paramBoolean1)
        && (((EntityHuman) paramEntityLiving).abilities.isInvulnerable)) { return false; }
    if ((paramBoolean2) && (!paramEntityInsentient.getEntitySenses().a(paramEntityLiving))) { return false; }
    return true;
  }

  protected boolean a(EntityLiving paramEntityLiving, boolean paramBoolean) {
    if (!a(this.e, paramEntityLiving, paramBoolean, this.f)) { return false; }
    // if (!this.e.d(new BlockPosition(paramEntityLiving))) {
    // return false;
    // }
    if (this.a) {
      if (--this.c <= 0) {
        this.b = 0;
      }
      if (this.b == 0) {
        this.b = (a(paramEntityLiving) ? 1 : 2);
      }
      if (this.b == 2) { return false; }
    }
    return true;
  }

  private boolean a(EntityLiving paramEntityLiving) {
    this.c = (10 + this.e.bb().nextInt(5));
    PathEntity localPathEntity = this.e.getNavigation().a(paramEntityLiving);
    if (localPathEntity == null) { return false; }
    PathPoint localPathPoint = localPathEntity.c();
    if (localPathPoint == null) { return false; }
    int i = localPathPoint.a - MathHelper.floor(paramEntityLiving.locX);
    int j = localPathPoint.c - MathHelper.floor(paramEntityLiving.locZ);
    return i * i + j * j <= 2.25D;
  }

}
