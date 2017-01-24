package lbn.mob.customEntity1_7.ai;

import net.minecraft.server.v1_8_R1.EntityInsentient;
import net.minecraft.server.v1_8_R1.IRangedEntity;
import net.minecraft.server.v1_8_R1.ItemStack;
import net.minecraft.server.v1_8_R1.Items;

public class TheLoWPathfinderGoalArrowAttackForSkelton extends TheLoWPathfinderGoalArrowAttack {


    public TheLoWPathfinderGoalArrowAttackForSkelton(
			IRangedEntity irangedentity, double d0, int i, float f) {
		super(irangedentity, d0, i, f);
	}

	public boolean a() {
		EntityInsentient me = this.a;
		ItemStack bz = me.bz();

		boolean isBow = false;
		if (bz != null && bz.getItem().equals(Items.BOW)) {
			isBow = true;
		}

		//弓を打つ時
		if (super.a()) {
			if (!isBow) {
				me.setEquipment(0, new ItemStack(Items.BOW));
			}
			return true;
		} else {
		//近接攻撃
			if (isBow) {
				me.setEquipment(0, new ItemStack(Items.STONE_SWORD));
			}
			return false;
		}
    }

}

