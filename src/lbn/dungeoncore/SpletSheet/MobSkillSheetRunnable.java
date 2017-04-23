package lbn.dungeoncore.SpletSheet;

import lbn.mob.mobskill.MobSkillExcuteConditionType;
import lbn.mob.mobskill.MobSkillExcuteTimingType;
import lbn.mob.mobskill.MobSkillManager;
import lbn.mob.mobskill.MobSkillRunnable;
import lbn.mob.mobskill.MobSkillTargetingMethodType;
import lbn.mob.mobskill.OldNormalMobSkill;
import lbn.mob.mobskill.ParticleLocationType;
import lbn.mob.mobskill.skillrunnable.MobSkillExplosion;
import lbn.mob.mobskill.skillrunnable.MobSkillExplosionNoParticle;
import lbn.mob.mobskill.skillrunnable.MobSkillHealMob;
import lbn.mob.mobskill.skillrunnable.MobSkillKeepAwayTarget;
import lbn.mob.mobskill.skillrunnable.MobSkillLightningEffect;
import lbn.mob.mobskill.skillrunnable.MobSkillLightningEffect3;
import lbn.mob.mobskill.skillrunnable.MobSkillMobJump;
import lbn.mob.mobskill.skillrunnable.MobSkillNothing;
import lbn.mob.mobskill.skillrunnable.MobSkillSetRedstoneBlock;
import lbn.mob.mobskill.skillrunnable.MobSkillSpawnMob;
import lbn.mob.mobskill.skillrunnable.MobSkillSpawnMob2;
import lbn.mob.mobskill.skillrunnable.MobSkillTpToMob;
import lbn.mob.mobskill.skillrunnable.MobSkillTpToTarget;
import lbn.mob.mobskill.skillrunnable.MobSkillUpperTarget;
import lbn.mob.mobskill.skillrunnable.MobSkillUpperTargetHight;
import lbn.util.JavaUtil;

import org.bukkit.command.CommandSender;

public class MobSkillSheetRunnable extends AbstractSheetRunable{

	public MobSkillSheetRunnable(CommandSender p) {
		super(p);
	}

	@Override
	protected String getQuery() {
		return null;
	}

	@Override
	public String getSheetName() {
		return "mobskill";
	}

	@Override
	public String[] getTag() {
		return new String[]{"id", "skill", "damage", "condition", "timing", "rate", "firescound", "buffid1", "bufftarget1",
				"dummy3", "data", "particleId", "dummy1", "dummy2", "particlelocation", "targeting",
				"targetingdata", "latersecond", "chain", "skilltalk", "soundid", "soundtarget"};
	}

	@Override
	protected void excuteOnerow(String[] row) {
		String id = row[0];
		//skill
		MobSkillRunnable skill = getMobSkillByDetail(row[1], row[10]);
		if (skill == null) {
			sendMessage(id + "のskillが無効ですスキップされました。:" + row[1]);
			return;
		}

		double damage = getDouble(row[2]);

		MobSkillExcuteConditionType condition = MobSkillExcuteConditionType.getInstance(row[3]);
		if (condition == null) {
			sendMessage(id + "のconditionが無効ですスキップされました。:" + row[3]);
			return;
		}

		MobSkillExcuteTimingType timing = MobSkillExcuteTimingType.getInstance(row[4]);
		if (timing == null) {
			timing = MobSkillExcuteTimingType.ALWAYS;
		}


		int rate = (int)getDouble(row[5]);
		if (rate == 0) {
			rate = 100;
		}

		int fireTick = (int) (getDouble(row[6]) * 20);


		String buffId = row[7];
		boolean isMobBuffTerget = "モンスター".equals(row[8]);

		//パーティクルの設置
//		ParticleType type = ParticleType.getType(row[11]);
//		ParticleData data = null;
//		double particleData = JavaUtil.getDouble(row[13], 0);
		ParticleLocationType particleLocationType = ParticleLocationType.getValue(row[14]);
		if (particleLocationType == null) {
			particleLocationType = ParticleLocationType.MONSTER_BODY;
		}

//		if (type != null) {
//			data = getParticleData(row, type, particleData);
//		} else {
//			data = null;
//		}

		MobSkillTargetingMethodType targetingMethod = MobSkillTargetingMethodType.getInstance(row[15]);
		if (targetingMethod == null) {
			targetingMethod = MobSkillTargetingMethodType.DEPEND_ON_CONDTION;
		}

		int laterTick = (int) (JavaUtil.getDouble(row[17], 0) * 20);

		boolean isOnePlayerSoundTarget = false;
		if ("対象のみ".equals(row[21])) {
			isOnePlayerSoundTarget = true;
		}

		String soundId = "".equals(row[20]) ? null : row[20];

		OldNormalMobSkill normalMobSkill = new OldNormalMobSkill(damage, fireTick, skill, timing, condition,
				id, rate, row[11], particleLocationType, targetingMethod, row[16], laterTick, row[18], row[19],
				soundId, isOnePlayerSoundTarget, buffId, isMobBuffTerget);

		MobSkillManager.registSkill(normalMobSkill);
	}

//	protected ParticleData getParticleData(String[] row, ParticleType type,
//			double particleData) {
//		ParticleData data;
//		if ("円".equals(row[12])) {
//			data = new CircleParticleData(new ParticleData(type, 1), 3);
//		} else if ("バネ状".equals(row[12])) {
//			data = new SpringParticleData(new ParticleData(type, 3), 3, 6, 1.5, 10);
//		} else {
//			data = new ParticleData(type, 30);
//		}
//		data.setLastArgument(particleData);
//
//		return data;
//	}

	public double getDouble(String deta) {
		if (deta == null || deta.isEmpty()) {
			return 0;
		}

		try {
			return Double.parseDouble(deta);
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	public boolean isEmpty(String[] row, int index) {
		if (row[index] == null || row[index].isEmpty()) {
			return true;
		}
		return false;
	}

	private MobSkillRunnable getMobSkillByDetail(String detail, String data) {
		if (detail == null) {
			return null;
		}
		switch (detail) {
		case "対象をモンスターから遠ざける":
			return new MobSkillKeepAwayTarget(data);
		case "対象の場所に雷のエフェクトを落とす":
			return new MobSkillLightningEffect(data);
		case "対象の場所に雷のエフェクトを指定した回数落とす":
			return new MobSkillLightningEffect3(data);
		case "追加効果なし":
			return new MobSkillNothing(data);
		case "対象を上へ打ち上げる":
			return new MobSkillUpperTarget(data);
		case "対象を上へ高く打ち上げる":
			return new MobSkillUpperTargetHight(data);
		case "モンスターを一体召喚する":
			return new MobSkillSpawnMob(data);
		case "モンスターを三体召喚する":
			return new MobSkillSpawnMob2(data);
		case "モンスターを二秒間止めて指定した%回復させる":
			return new MobSkillHealMob(data);
		case "対象の場所で爆発を起こす":
			return new MobSkillExplosion(data);
		case "対象の場所で爆発を起こす(パーティクルなし)":
			return new MobSkillExplosionNoParticle(data);
		case "モンスターを対象の場所にTPさせる":
			return new MobSkillTpToTarget(data);
		case "対象をモンスターの場所にTPさせる":
			return new MobSkillTpToMob(data);
		case "指定した場所に一瞬だけレッドストーンブロックを置く":
			return new MobSkillSetRedstoneBlock(data);
		case "モンスターを上にとばす":
			return new MobSkillMobJump(data);
		default:
			return null;
		}
	}
}
