package lbn.mob.customEntity1_7.ai;

import net.minecraft.server.v1_8_R1.EntityCreature;
import net.minecraft.server.v1_8_R1.PathfinderGoal;

public class TheLoWPathfinderGoalThrowPot extends PathfinderGoal {
	public TheLoWPathfinderGoalThrowPot(EntityCreature entityLiving) {
		this.entityLiving = entityLiving;
	}

	EntityCreature entityLiving;


	int throwPot = 0;
	@Override
	public boolean a() {
		if (entityLiving.getHealth() < entityLiving.getMaxHealth() - 7) {
			System.out.println("ThrowPot:true");
			return true;
		}
		if (throwPot != 0) {
			System.out.println("ThrowPot:true");
			return true;
		}
		System.out.println("ThrowPot:true false");
		return false;
	}

	@Override
	public void d() {
		throwPot = 0;
	}

	@Override
	public void e() {
//		if (entityLiving.getHealth() > entityLiving.getMaxHealth() - 3) {
//			throwPot = 0;
//			System.out.println("hpMax");
//			return;
//		}
//		//下にpotを投げる
////		LivingEntity bukkitEntity = (LivingEntity) entityLiving.getBukkitEntity();
////		ThrownPotion spawnEntity = (ThrownPotion) bukkitEntity.getWorld().spawnEntity(bukkitEntity.getLocation(), EntityType.SPLASH_POTION);
////		Potion potion = new Potion(PotionType.INSTANT_HEAL);
////		potion.setSplash(true);
////		potion.setLevel(2);
////		spawnEntity.setItem(potion.toItemStack(1));
////		spawnEntity.setVelocity(new Vector(0, -0.4, 0));
//		throwPot++;
//
//		EntityLiving goalTarget = entityLiving.getGoalTarget();
//		if (goalTarget == null) {
//			return;
//		}
//
//        Vec3D vec3d = RandomPositionGenerator.b(this.entityLiving, 16, 7, new Vec3D(goalTarget.locX, goalTarget.locY, goalTarget.locZ));
////        System.out.println("aaaaaaaaaaaaaaaaaaaaa@" + entityLiving.getMaxHealth());
//        if (vec3d == null) {
////        	System.out.println("null");
//        } else {
////        	System.out.println(vec3d);
////        	System.out.println(Math.sqrt(entityLiving.e(vec3d.a, vec3d.b, vec3d.c)));
//        }
	}

}
