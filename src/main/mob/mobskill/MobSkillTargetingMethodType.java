package main.mob.mobskill;

public enum MobSkillTargetingMethodType {
	DEPEND_ON_CONDTION("発動条件に当てはまるプレイヤー"),
	FALLING_BLOCK("ブロックを飛ばして当たったプレイヤー(ブロックのIDは隣のセルで指定)", 49),
	DROPING_ITEM("アイテムを飛ばして当たったプレイヤー(ブロックのIDは隣のセルで指定)",49),
	RANGE_BY_MOB("モブの周囲のプレイヤー(範囲は隣のセルで指定)", 5),
	RANGE_BY_CONDTION_TARGET("発動条件に当てはまるプレイヤーとその周囲のプレイヤー(範囲は隣のセルで指定)", 5);

	String detail;
	double initData = 0;
	private MobSkillTargetingMethodType(String detail) {
		this.detail = detail;
	}

	private MobSkillTargetingMethodType(String detail, double initData) {
		this.detail = detail;
		this.initData = initData;
	}

	public String getDetail() {
		return detail;
	}

	public double getInit() {
		return initData;
	}

	public static MobSkillTargetingMethodType getInstance(String detail) {
		if (detail == null) {
			return null;
		}
		for (MobSkillTargetingMethodType val : values()) {
			if (detail.equals(val.getDetail())) {
				return val;
			}
		}
		return null;
	}
}
