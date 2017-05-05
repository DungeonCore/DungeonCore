package lbn.mob.mobskill;

import java.util.List;

import org.bukkit.entity.Entity;

public class MobSkillInplemention implements MobSkillInterface{
	MobSkillData data;

	public MobSkillInplemention(MobSkillData data) {
		this.data = data;
	}
	@Override
	public MobSkillExcuteTimingType getTiming() {
		return data.getTiming();
	}
	@Override
	public MobSkillExcuteConditionType getCondtion() {
		return data.getCondition();
	}
	@Override
	public MobSkillTargetingMethodType getTargetingMethod() {
		return data.getTargetingMethod();
	}
	@Override
	public String getName() {
		return data.getId();
	}
	@Override
	public int excutePercent() {
		return data.getPercent();
	}

	@Override
	public void execute(Entity target, Entity mob) {
		//ターゲットを選択する
		if (getTargetingMethod() == MobSkillTargetingMethodType.FALLING_BLOCK) {

		}
	}

	protected void executeMobSkill(List<Entity> target, Entity mob) {

	}

	/**
	 * スキル発動時に一回だけ実行
	 * @param target
	 * @param mob
	 */
	protected void executeBefore(Entity target, Entity mob) {

	}
	@Override
	public int getLaterTick() {
		return data.getLaterTick();
	}

//	DamageFallingblockForMonsterSkill damageFallingblockForMonsterSkill = new DamageFallingblockForMonsterSkill(mob, condtionTarget.getLocation(), getMaterialById(blockId), (byte)data, speed){
//		ArrayList<Entity> damagedList = new ArrayList<>();
//		@Override
//		protected void executeDamage(LivingEntity target, LivingEntity mob) {
//			executeOneTarget(target, mob);
//			damagedList.add(target);
//		}
//		@Override
//		public synchronized void cancel() throws IllegalStateException {
//			super.cancel();
//
//			if (damagedList.size() != 0) {
//				executeOneTime(damagedList, mob);
//			}
//		}
//	};
}
