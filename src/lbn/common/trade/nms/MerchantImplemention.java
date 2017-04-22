package lbn.common.trade.nms;

import lbn.common.trade.TheLowMerchant;
import lbn.common.trade.TheLowMerchantRecipe;
import net.minecraft.server.v1_8_R1.ChatMessage;
import net.minecraft.server.v1_8_R1.EntityHuman;
import net.minecraft.server.v1_8_R1.IChatBaseComponent;
import net.minecraft.server.v1_8_R1.IMerchant;
import net.minecraft.server.v1_8_R1.ItemStack;
import net.minecraft.server.v1_8_R1.MerchantRecipe;
import net.minecraft.server.v1_8_R1.MerchantRecipeList;

public class MerchantImplemention implements IMerchant {

	int containerCounter;
	TheLowMerchant merchant;

	public MerchantImplemention(TheLowMerchant merchant) {
		this.containerCounter = merchant.getContainerCounter();
		this.merchant = merchant;
	}

	public int getContainerCounter() {
		return containerCounter;
	}

	EntityHuman e;

	@Override
	public void a(MerchantRecipe paramMerchantRecipe) {
		merchant.onFinishTrade(new TheLowMerchantRecipe(paramMerchantRecipe));
	}

	@Override
	public void a_(EntityHuman paramEntityHuman) {
		e = paramEntityHuman;
	}

	@Override
	public void a_(ItemStack paramItemStack) {
		merchant.onSetItem();
	}

	@Override
	public MerchantRecipeList getOffers(EntityHuman paramEntityHuman) {
		return merchant.getNowRecipeList();
	}

	@Override
	public IChatBaseComponent getScoreboardDisplayName() {
		if (merchant.getName() == null) {
			return new ChatMessage("", new Object[0]);
		}
		return new ChatMessage(merchant.getName(), new Object[0]);
	}

	@Override
	public EntityHuman u_() {
		return e;
	}

}
