package lbn.mob.customEntity1_7.ai;

import net.minecraft.server.v1_8_R1.EntityInsentient;
import net.minecraft.server.v1_8_R1.IRangedEntity;
import net.minecraft.server.v1_8_R1.ItemStack;
import net.minecraft.server.v1_8_R1.Items;

public class TheLoWPathfinderGoalArrowAttackForSkelton extends TheLoWPathfinderGoalArrowAttack {


	ItemStack bowItem = new ItemStack(Items.BOW);

	ItemStack meleeAttackItem = null;

    public TheLoWPathfinderGoalArrowAttackForSkelton(
			IRangedEntity irangedentity, double d0, int i, float f) {
		super(irangedentity, d0, i, f);
	}

	public boolean a() {
		EntityInsentient me = this.a;
		//手持ちのアイテム
		ItemStack bz = me.bz();

		//弓を持っているならTRUE
		boolean hasBow = false;
		//弓を持っているか確認
		if (bz != null && bz.getItem().equals(Items.BOW)) {
			hasBow = true;
		}

		//弓を持ってない時は最初に持っているアイテムを保存しておく
		if (!hasBow) {
			if (meleeAttackItem == null) {
				meleeAttackItem = bz;
			}
		}

		//弓を打つ時
		if (super.a()) {
			//弓を持っていないなら弓を装備
			if (!hasBow) {
				me.setEquipment(0, bowItem);
			}
			return true;
		} else {
		//近接攻撃
			//弓をもっているなら剣を装備
			if (hasBow) {
				if (meleeAttackItem != null) {
					me.setEquipment(0, meleeAttackItem);
				} else {
					me.setEquipment(0, new ItemStack(Items.STONE_SWORD));
				}
			}
			return false;
		}
	}

}

