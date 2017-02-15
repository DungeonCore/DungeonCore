package lbn.mob.customEntity1_8;

import net.minecraft.server.v1_8_R1.EntityGuardian;
import net.minecraft.server.v1_8_R1.GenericAttributes;
import net.minecraft.server.v1_8_R1.World;

public class CustomGuardian extends EntityGuardian{

	public CustomGuardian(World world) {
		super(world);
	}

	@Override
	public void a(boolean flag) {
		a(4, flag);
		if (flag) {
			a(1.9975F, 1.9975F);
			getAttributeInstance(GenericAttributes.d).setValue(
					0.30000001192092896D);
			getAttributeInstance(GenericAttributes.e).setValue(8.0D);
			//HP固定しない
//			getAttributeInstance(GenericAttributes.maxHealth).setValue(80.0D);
			bW();
			this.bq.b(400);
		}
	}

	private void a(int i, boolean flag) {
		int j = this.datawatcher.getInt(16);
		if (flag) {
			this.datawatcher.watch(16, Integer.valueOf(j | i));
		} else {
			this.datawatcher.watch(16, Integer.valueOf(j & (i ^ 0xFFFFFFFF)));
		}
	}
}
