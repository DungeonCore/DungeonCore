package lbn.item.attackitem.weaponSkill;

import lbn.player.ItemType;

public class WeaponSkillData {
	double[] data = new double[5];


	public WeaponSkillData(String name, ItemType type, String id) {
		this.name = name;
		this.type = type;
		this.id = id;
	}

	int skillLevel = 0;
	int cooltime = 0;
	int needMp = 0;

	String[] detail;

	String name;

	String id;

	ItemType type;

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setDetail(String detail) {
		this.detail = detail.split(",");
	}

	public String[] getDetail() {
		return detail;
	}

	public ItemType getType() {
		return type;
	}

	public int getSkillLevel() {
		return skillLevel;
	}

	public void setSkillLevel(int skillLevel) {
		this.skillLevel = skillLevel;
	}

	public int getCooltime() {
		return cooltime;
	}

	public void setCooltime(int cooltime) {
		this.cooltime = cooltime;
	}

	public int getNeedMp() {
		return needMp;
	}

	public void setNeedMp(int needMp) {
		this.needMp = needMp;
	}

	public double getData(int i) {
		return data[i];
	}

	public void setData(double data0, int index) {
		data[index] = data0;;
	}
}
