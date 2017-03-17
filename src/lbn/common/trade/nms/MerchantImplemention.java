package lbn.common.trade.nms;

import lbn.common.trade.TheLowMerchant;
import net.minecraft.server.v1_8_R1.ChatMessage;
import net.minecraft.server.v1_8_R1.EntityHuman;
import net.minecraft.server.v1_8_R1.IChatBaseComponent;
import net.minecraft.server.v1_8_R1.IMerchant;
import net.minecraft.server.v1_8_R1.ItemStack;
import net.minecraft.server.v1_8_R1.MerchantRecipe;
import net.minecraft.server.v1_8_R1.MerchantRecipeList;

public class MerchantImplemention implements IMerchant{

	int containerCounter;
	TheLowMerchant merchant;
	public MerchantImplemention(TheLowMerchant merchant) {
		this.containerCounter = merchant.getContainerCounter();
		this.merchant = merchant;
		this.recipeList = new MerchantRecipeListImplemention(merchant);
	}

	public int getContainerCounter() {
		return containerCounter;
	}

	EntityHuman e;
	@Override
	public void a(MerchantRecipe paramMerchantRecipe) {
		//取引完了
	}

	@Override
	public void a_(EntityHuman paramEntityHuman) {
		e = paramEntityHuman;
	}

	@Override
	public void a_(ItemStack paramItemStack) {
		merchant.onSetItem();
	}

	MerchantRecipeList recipeList;
	@Override
	public MerchantRecipeList getOffers(EntityHuman paramEntityHuman) {
		return recipeList;
	}

	@Override
	public IChatBaseComponent getScoreboardDisplayName() {
		return new ChatMessage(merchant.getName(), new Object[0]);
	}


	@Override
	public EntityHuman u_() {
		return e;
	}

}
