package net.l_bulb.dungeoncore.common.explosion;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R1.event.CraftEventFactory;

import com.google.common.collect.Lists;

import net.minecraft.server.v1_8_R1.AxisAlignedBB;
import net.minecraft.server.v1_8_R1.DamageSource;
import net.minecraft.server.v1_8_R1.EnchantmentProtection;
import net.minecraft.server.v1_8_R1.Entity;
import net.minecraft.server.v1_8_R1.EntityFallingBlock;
import net.minecraft.server.v1_8_R1.EntityHuman;
import net.minecraft.server.v1_8_R1.EntityPlayer;
import net.minecraft.server.v1_8_R1.EntityTNTPrimed;
import net.minecraft.server.v1_8_R1.Explosion;
import net.minecraft.server.v1_8_R1.MathHelper;
import net.minecraft.server.v1_8_R1.PacketPlayOutExplosion;
import net.minecraft.server.v1_8_R1.Vec3D;
import net.minecraft.server.v1_8_R1.World;

public abstract class AbstractNotDamageExplosion extends Explosion {

  public AbstractNotDamageExplosion(Location l, float f) {
    super(((CraftWorld) l.getWorld()).getHandle(), (Entity) null, l.getX(), l.getY(), l.getZ(), f, false, false);
    this.size = ((float) Math.max(f, 0.0D));
    this.world = ((CraftWorld) l.getWorld()).getHandle();
    this.posX = l.getX();
    this.posY = l.getY();
    this.posZ = l.getZ();

    @SuppressWarnings("rawtypes")
    Iterator iterator = world.players.iterator();
    while (iterator.hasNext()) {
      EntityHuman entityhuman = (EntityHuman) iterator.next();

      if (entityhuman.e(l.getX(), l.getY(), l.getZ()) < 4096.0D) {
        ((EntityPlayer) entityhuman).playerConnection.sendPacket(new PacketPlayOutExplosion(l.getX(), l.getY(), l.getZ(), f, Lists.newArrayList(),
            this.b().get(entityhuman)));
      }
    }
  }

  private final double posX;
  private final double posY;
  private final double posZ;

  float size = 0;
  protected World world;

  protected Map<EntityHuman, Vec3D> l = new HashMap<>();

  protected int i = 16;

  @Override
  public void a() {
    if (this.size < 0.1F) { return; }

    // ブロックを壊さないのでコメントアウト
    // HashSet<BlockPosition> hashset = Sets.newHashSet();
    // for (int i = 0; i < this.i; i++) {
    // for (int j = 0; j < this.i; j++) {
    // for (int k = 0; k < this.i; k++) {
    // if ((k == 0) || (k == 15) || (i == 0) || (i == 15)
    // || (j == 0) || (j == 15)) {
    // double d0 = k / 15.0F * 2.0F - 1.0F;
    // double d1 = i / 15.0F * 2.0F - 1.0F;
    // double d2 = j / 15.0F * 2.0F - 1.0F;
    // double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
    //
    // d0 /= d3;
    // d1 /= d3;
    // d2 /= d3;
    // float f = this.size
    // * (0.7F + this.world.random.nextFloat() * 0.6F);
    // double d4 = this.posX;
    // double d5 = this.posY;
    // double d6 = this.posZ;
    // for (; f > 0.0F; f -= 0.22500001F) {
    // BlockPosition blockposition = new BlockPosition(d4,
    // d5, d6);
    // IBlockData iblockdata = this.world
    // .getType(blockposition);
    // if (iblockdata.getBlock().getMaterial() != Material.AIR) {
    // float f2 = this.source != null ? this.source.a(
    // this, this.world, blockposition,
    // //別なメソッドかも
    // iblockdata) : iblockdata.getBlock().a((World)null);
    //
    // f -= (f2 + 0.3F) * 0.3F;
    // }
    // if ((f > 0.0F)
    // && ((this.source == null) || (this.source
    // .a(this, this.world, blockposition,
    // iblockdata, f)))
    // && (blockposition.getY() < 256)
    // && (blockposition.getY() >= 0)) {
    // hashset.add(blockposition);
    // }
    // d4 += d0 * 0.30000001192092896D;
    // d5 += d1 * 0.30000001192092896D;
    // d6 += d2 * 0.30000001192092896D;
    // }
    // }
    // }
    // }
    // }

    // ブロック壊さないのでコメントアウト
    // this.blocks.addAll(hashset);
    float f3 = this.size * 2.0F;

    int i = MathHelper.floor(this.posX - f3 - 1.0D);
    int j = MathHelper.floor(this.posX + f3 + 1.0D);
    int l = MathHelper.floor(this.posY - f3 - 1.0D);
    int i1 = MathHelper.floor(this.posY + f3 + 1.0D);
    int j1 = MathHelper.floor(this.posZ - f3 - 1.0D);
    int k1 = MathHelper.floor(this.posZ + f3 + 1.0D);
    @SuppressWarnings("unchecked")
    List<Entity> list = this.world.getEntities(this.source, new AxisAlignedBB(i, l,
        j1, j, i1, k1));
    Vec3D vec3d = new Vec3D(this.posX, this.posY, this.posZ);
    for (int l1 = 0; l1 < list.size(); l1++) {
      Entity entity = list.get(l1);
      if (!entity.aV()) {
        double d7 = entity.f(this.posX, this.posY, this.posZ) / f3;
        if (d7 <= 1.0D) {
          double d8 = entity.locX - this.posX;
          double d9 = entity.locY + entity.getHeadHeight()
              - this.posY;
          double d10 = entity.locZ - this.posZ;
          double d11 = MathHelper.sqrt(d8 * d8 + d9 * d9 + d10 * d10);
          if (d11 != 0.0D) {
            d8 /= d11;
            d9 /= d11;
            d10 /= d11;
            double d12 = this.world.a(vec3d,
                entity.getBoundingBox());
            double d13 = (1.0D - d7) * d12;

            CraftEventFactory.entityDamage = this.source;
            boolean wasDamaged = false;

            if (!isNotDamage(entity.getBukkitEntity())) {
              wasDamaged = entity.damageEntity(DamageSource.explosion(this), (int) ((d13 * d13 + d13) / 2.0D * 8.0D * f3 + 1.0D));
            }

            CraftEventFactory.entityDamage = null;
            if ((wasDamaged)
                || ((entity instanceof EntityTNTPrimed))
                || ((entity instanceof EntityFallingBlock))) {
              double d14 = EnchantmentProtection.a(entity, d13);

              entity.motX += d8 * d14;
              // 上への吹っ飛びを1/4にする
              entity.motY += d9 * d14 / 4.0;
              entity.motZ += d10 * d14;
              if (((entity instanceof EntityHuman))
                  && (!((EntityHuman) entity).abilities.isInvulnerable)) {
                this.l.put((EntityHuman) entity, new Vec3D(d8
                    * d13, d9 * d13, d10 * d13));
              }
            }
          }
        }
      }
    }
  }

  /**
   * 爆発のParticleありならTRUE
   *
   * @return
   */
  protected boolean isRunParticle() {
    return true;
  }

  public void damageEntity(org.bukkit.entity.Entity craftEntity, float d10) {
    ((CraftEntity) craftEntity).getHandle().damageEntity(DamageSource.explosion(this), d10);
  }

  abstract boolean isNotDamage(org.bukkit.entity.Entity bukkitEntity);

  @Override
  public Map<EntityHuman, Vec3D> b() {
    return l;
  }

  public Explosion runExplosion() {
    a();
    a(true);
    return this;
  }
}
