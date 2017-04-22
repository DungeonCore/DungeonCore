package lbn.mob.mobskill;

public class MobSkillData {
	protected MobSkillTargetingMethodType targetingMethod;
	protected String targetingDeta = null;
	protected double damage;
	protected int fireTick;
	protected MobSkillRunnable runnable;
	private MobSkillExcuteTimingType timing;
	private MobSkillExcuteConditionType condition;
	private String id;
	private int percent;
	int laterTick;
	String particleId;
	ParticleLocationType particleLocType;
	String chainId;
	String skillTalk;
	String soundId;
	boolean isSoundTargetOnePlayer;

	String buffId1;
	boolean isTargetMobBuff1 = true;

	public MobSkillTargetingMethodType getTargetingMethod() {
		return targetingMethod;
	}

	public void setTargetingMethod(MobSkillTargetingMethodType targetingMethod) {
		this.targetingMethod = targetingMethod;
	}

	public String getTargetingDeta() {
		return targetingDeta;
	}

	public void setTargetingDeta(String targetingDeta) {
		this.targetingDeta = targetingDeta;
	}

	public double getDamage() {
		return damage;
	}

	public void setDamage(double damage) {
		this.damage = damage;
	}

	public int getFireTick() {
		return fireTick;
	}

	public void setFireTick(int fireTick) {
		this.fireTick = fireTick;
	}

	public MobSkillRunnable getRunnable() {
		return runnable;
	}

	public void setRunnable(MobSkillRunnable runnable) {
		this.runnable = runnable;
	}

	public MobSkillExcuteTimingType getTiming() {
		return timing;
	}

	public void setTiming(MobSkillExcuteTimingType timing) {
		this.timing = timing;
	}

	public MobSkillExcuteConditionType getCondition() {
		return condition;
	}

	public void setCondition(MobSkillExcuteConditionType condition) {
		this.condition = condition;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getPercent() {
		return percent;
	}

	public void setPercent(int percent) {
		this.percent = percent;
	}

	public int getLaterTick() {
		return laterTick;
	}

	public void setLaterTick(int laterTick) {
		this.laterTick = laterTick;
	}

	public String getParticleId() {
		return particleId;
	}

	public void setParticleId(String particleId) {
		this.particleId = particleId;
	}

	public ParticleLocationType getParticleLocType() {
		return particleLocType;
	}

	public void setParticleLocType(ParticleLocationType particleLocType) {
		this.particleLocType = particleLocType;
	}

	public String getChainId() {
		return chainId;
	}

	public void setChainId(String chainId) {
		this.chainId = chainId;
	}

	public String getSkillTalk() {
		return skillTalk;
	}

	public void setSkillTalk(String skillTalk) {
		this.skillTalk = skillTalk;
	}

	public String getSoundId() {
		return soundId;
	}

	public void setSoundId(String soundId) {
		this.soundId = soundId;
	}

	public boolean isSoundTargetOnePlayer() {
		return isSoundTargetOnePlayer;
	}

	public void setSoundTargetOnePlayer(boolean isSoundTargetOnePlayer) {
		this.isSoundTargetOnePlayer = isSoundTargetOnePlayer;
	}

	public String getBuffId1() {
		return buffId1;
	}

	public void setBuffId1(String buffId1) {
		this.buffId1 = buffId1;
	}

	public boolean isTargetMobBuff1() {
		return isTargetMobBuff1;
	}

	public void setTargetMobBuff1(boolean isTargetMobBuff1) {
		this.isTargetMobBuff1 = isTargetMobBuff1;
	}

}
