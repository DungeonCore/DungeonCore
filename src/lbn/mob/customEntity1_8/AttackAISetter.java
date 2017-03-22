package lbn.mob.customEntity1_8;

import java.lang.reflect.Field;

import lbn.mob.customEntity1_8.ai.AvoidTargetPredicate;
import lbn.mob.customEntity1_8.ai.TheLoWPathfinderGoalArrowAttackForShortLongAI;
import lbn.mob.customEntity1_8.ai.TheLoWPathfinderGoalArrowAttackForSkelton;
import lbn.mob.customEntity1_8.ai.TheLowPathfinderGoalMeleeAttack;
import lbn.mob.mob.LbnMobTag;
import net.minecraft.server.v1_8_R1.EntityCreature;
import net.minecraft.server.v1_8_R1.EntityInsentient;
import net.minecraft.server.v1_8_R1.EntityLiving;
import net.minecraft.server.v1_8_R1.EntityMonster;
import net.minecraft.server.v1_8_R1.EntitySkeleton;
import net.minecraft.server.v1_8_R1.IRangedEntity;
import net.minecraft.server.v1_8_R1.PathfinderGoalArrowAttack;
import net.minecraft.server.v1_8_R1.PathfinderGoalAvoidTarget;
import net.minecraft.server.v1_8_R1.PathfinderGoalSelector;

import org.bukkit.entity.EntityType;

/**
 * 4,5,6
 * @author KENSUKE
 *
 */
public class AttackAISetter {
	public static void setAttackAI(EntityInsentient e, LbnMobTag tag) {
		switch (tag.getAiType()) {
		case NO_ATACK:
			break;
		case NORMAL:
			setNormalAI(e, tag);
			break;
		case SHORT_LONG:
			setShortLongAI(e, tag);
			break;
		case LONG1:
			setLongAI1(e, tag);
			break;
		case LONG2:
			setLongAI2(e, tag);
			break;
		default:
			setNormalAI(e, tag);
			break;
		}
	}

	/**
	 * 遠距離AI(強い)
	 * @param e
	 * @param tag
	 */
	private static void setLongAI2(EntityInsentient e, LbnMobTag tag) {
		//対象が7マスよりも近い場合は無視する
		e.goalSelector.a(4, new PathfinderGoalAvoidTarget((EntityCreature) e, new AvoidTargetPredicate(tag.isSummonMob()), 7.0F, 1.6D, 2D));
		//遠距離用のAI
		TheLoWPathfinderGoalArrowAttackForShortLongAI theLoWPathfinderGoalArrowAttack = new TheLoWPathfinderGoalArrowAttackForShortLongAI((IRangedEntity) e, 1.25D, 20, 20.0F, tag);
		theLoWPathfinderGoalArrowAttack.setNearAttackRange(7);
		e.goalSelector.a(5, theLoWPathfinderGoalArrowAttack);
	}

	/**
	 * 遠距離AI(弱い)
	 * @param e
	 * @param tag
	 */
	protected static void setLongAI1(EntityInsentient e, LbnMobTag tag) {
		//通常の遠距離AI
		PathfinderGoalArrowAttack b = new PathfinderGoalArrowAttack((IRangedEntity) e, 1.0D, 20, 60, 15.0F);
		e.goalSelector.a(5, b);
	}

	/**
	 * 遠距離中距離用のAIをセットする
	 * @param e
	 * @param tag
	 */
	private static void setShortLongAI(EntityInsentient e, LbnMobTag tag) {
		if (tag.getType() == EntityType.SKELETON) {
			//遠距離用のAI
			TheLoWPathfinderGoalArrowAttackForShortLongAI theLoWPathfinderGoalArrowAttack = new TheLoWPathfinderGoalArrowAttackForSkelton((IRangedEntity) e, 1.25D, 20, 20.0F, tag);
			e.goalSelector.a(5, theLoWPathfinderGoalArrowAttack);
			//近距離攻撃のAI
			TheLowPathfinderGoalMeleeAttack bq = new TheLowPathfinderGoalMeleeAttack((EntityCreature) e, getTargetEntityClass(tag.isSummonMob()), tag);
			bq.setKillAura(true);
			e.goalSelector.a(6, bq);
		} else {
			//遠距離用のAI
			TheLoWPathfinderGoalArrowAttackForShortLongAI theLoWPathfinderGoalArrowAttack = new TheLoWPathfinderGoalArrowAttackForShortLongAI((IRangedEntity) e, 1.25D, 20, 10.0F, tag);
			e.goalSelector.a(5, theLoWPathfinderGoalArrowAttack);
			//近距離攻撃のAI
			TheLowPathfinderGoalMeleeAttack bq = new TheLowPathfinderGoalMeleeAttack((EntityCreature) e, getTargetEntityClass(tag.isSummonMob()), tag);
			bq.setKillAura(true);
			e.goalSelector.a(6, bq);
		}
	}

	/**
	 * 通常時のAIをセットする
	 * @param e
	 * @param tag
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 */
	private static void setNormalAI(EntityInsentient e, LbnMobTag tag) {
		//スケルトンの処理
		if (tag.getType() == EntityType.SKELETON) {
			((EntitySkeleton)e).n();
		} else {
			e.goalSelector.a(6, new TheLowPathfinderGoalMeleeAttack((EntityCreature) e, getTargetEntityClass(tag.isSummonMob()), tag));
		}
	}

	/**
	 * 攻撃対象のクラスを取得
	 * @param isSummon summon　mobならTRUE
	 * @return
	 */
	public static Class<?> getTargetEntityClass(boolean isSummon) {
		if (isSummon) {
			return EntityMonster.class;
		} else {
			return EntityLiving.class;
		}
	}



	/**
	 * 全てのAIを取り除く
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public static void removeAllAi(EntityLiving e) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Field field = EntityInsentient.class.getDeclaredField("targetSelector");
		field.setAccessible(true);
		field.set(e, new PathfinderGoalSelector((e.world != null) && (e.world.methodProfiler != null) ? e.world.methodProfiler : null));

		//goalSelectorを初期化
		Field field2 = EntityInsentient.class.getDeclaredField("goalSelector");
		field2.setAccessible(true);
		field2.set(e, new PathfinderGoalSelector((e.world != null) && (e.world.methodProfiler != null) ? e.world.methodProfiler : null));

	}
}
