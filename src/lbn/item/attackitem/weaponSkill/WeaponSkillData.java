package lbn.item.attackitem.weaponSkill;

import org.bukkit.Material;

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

	Material material = Material.STONE;

	byte materialdata = 0;

	public void setMaterial(int materialId) {
		@SuppressWarnings("deprecation")
		Material material2 = Material.getMaterial(materialId);
		if (material2 != null) {
			material = material2;
		}
	}

	public void setMaterialdata(byte materialdata) {
		this.materialdata = materialdata;
	}

	public Material getMaterial() {
		return material;
	}

	public byte getMaterialdata() {
		return materialdata;
	}

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
