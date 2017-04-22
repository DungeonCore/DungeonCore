package lbn.mob.minecraftEntity.ai;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import lbn.mob.SummonPlayerManager;
import net.minecraft.server.v1_8_R1.Entity;
import net.minecraft.server.v1_8_R1.EntityAnimal;
import net.minecraft.server.v1_8_R1.EntityCreature;
import net.minecraft.server.v1_8_R1.EntityHuman;
import net.minecraft.server.v1_8_R1.EntityLiving;
import net.minecraft.server.v1_8_R1.EntityPlayer;
import net.minecraft.server.v1_8_R1.PathfinderGoalTarget;

import org.bukkit.GameMode;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityTargetEvent;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

public class PathfinderGoalNearestAttackableTargetNotTargetSub extends PathfinderGoalTarget {
	protected final Class<EntityLiving> a;
	private final int g;
	protected final DistanceComparator b;
	protected Predicate<? super EntityLiving> c;
	protected EntityLiving d;
	protected boolean isSummon = false;

	public PathfinderGoalNearestAttackableTargetNotTargetSub(EntityCreature entitycreature) {
		this(entitycreature, EntityLiving.class, true, false);
	}

	public void setSummon(boolean isSummon) {
		this.isSummon = isSummon;
	}

	public PathfinderGoalNearestAttackableTargetNotTargetSub(EntityCreature entitycreature, Class<EntityLiving> oclass,
			boolean flag, boolean flag1) {
		this(entitycreature, oclass, 10, flag, flag1, null);
	}

	public PathfinderGoalNearestAttackableTargetNotTargetSub(EntityCreature entitycreature, Class<EntityLiving> oclass,
			int i, boolean flag, boolean flag1, final Predicate<? super EntityLiving> predicate) {
		super(entitycreature, flag, flag1);
		this.a = oclass;
		this.g = i;
		this.b = new DistanceComparator(entitycreature);
		a(1);
		this.c = new Predicate<EntityLiving>() {
			public boolean a(EntityLiving t0) {
				if ((predicate != null) && (!predicate.apply(t0))) {
					return false;
				}
				if ((t0 instanceof EntityHuman)) {
					double d0 = f();
					if (t0.isSneaking()) {
						d0 *= 0.800000011920929D;
					}
					if (t0.isInvisible()) {
						float f = ((EntityHuman) t0).bX();
						if (f < 0.1F) {
							f = 0.1F;
						}
						d0 *= 0.7F * f;
					}
					if (t0.g(PathfinderGoalNearestAttackableTargetNotTargetSub.this.e) > d0) {
						return false;
					}
				}
				return PathfinderGoalNearestAttackableTargetNotTargetSub.this.a(t0, false);
			}

			public boolean apply(EntityLiving object) {
				return a(object);
			}
		};
	}

	public boolean a() {
		if ((this.g > 0) && (this.e.bb().nextInt(this.g) != 0)) {
			return false;
		}
		// double d0 = f();
		double d0 = 16;
		@SuppressWarnings("unchecked")
		List<EntityLiving> list = this.e.world.a(this.a, this.e.getBoundingBox().grow(d0, 8.0D, d0),
				Predicates.and(this.c, predicateEntity));

		Collections.sort(list, this.b);
		if (list.isEmpty()) {
			return false;
		} else {
			for (Entity e : list) {
				if (e == null) {
					continue;
				}
				// 動物には攻撃しない
				if (e instanceof EntityAnimal) {
					continue;
				}

				CraftEntity bukkitEntity = e.getBukkitEntity();
				boolean targetIsSummon = SummonPlayerManager.isSummonMob(bukkitEntity);
				if (isSummon) {
					// ターゲットがプレイヤーでなくsummonでないなら
					if (!targetIsSummon && bukkitEntity.getType() != EntityType.PLAYER) {
						this.d = (EntityLiving) e;
						return true;
					}
				} else {
					// ターゲットがプレイヤーまたはsummonなら
					if (targetIsSummon || bukkitEntity.getType() == EntityType.PLAYER) {
						this.d = (EntityLiving) e;
						return true;
					}
				}
				// else if
				// (SummonPlayerManager.isSummonMob(e.getBukkitEntity())) {
				// if (isSummon) {
				// //ターゲットがSUMMONならスキップする
				// continue;
				// } else {
				// this.d = (EntityLiving) e;
				// return true;
				// }
				// } else if (e.getBukkitEntity().getType() ==
				// EntityType.PLAYER) {
				// //クエリに場合には何もしない
				// if (((Player)e.getBukkitEntity()).getGameMode() ==
				// GameMode.CREATIVE) {
				// continue;
				// }
				// if (isSummon) {
				// continue;
				// } else {
				// this.d = (EntityLiving) e;
				// return true;
				// }
				// } else {
				// if (isSummon) {
				// this.d = (EntityLiving) e;
				// return true;
				// } else {
				// continue;
				// }
				// }
			}
			return false;
		}
	}

	// IEntitySelector.d の代わり
	public final Predicate<EntityLiving> predicateEntity = new Predicate<EntityLiving>() {
		@Override
		public boolean apply(EntityLiving e) {
			return (!(e instanceof EntityHuman))
					|| (((EntityHuman) e).getBukkitEntity().getGameMode() != GameMode.SPECTATOR);
		}
	};

	public void c() {
		this.e.setGoalTarget(this.d, (this.d instanceof EntityPlayer) ? EntityTargetEvent.TargetReason.CLOSEST_PLAYER
				: EntityTargetEvent.TargetReason.CLOSEST_ENTITY, true);
		super.c();
	}

	public static class DistanceComparator implements Comparator<Entity> {
		private final Entity a;

		public DistanceComparator(Entity entity) {
			this.a = entity;
		}

		public int a(Entity entity, Entity entity1) {
			double d0 = this.a.h(entity);
			double d1 = this.a.h(entity1);

			return d0 > d1 ? 1 : d0 < d1 ? -1 : 0;
		}

		public int compare(Entity object, Entity object1) {
			return a(object, object1);
		}
	}
}
