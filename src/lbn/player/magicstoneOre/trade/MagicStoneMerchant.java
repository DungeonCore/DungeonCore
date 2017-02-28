package lbn.player.magicstoneOre.trade;

import net.minecraft.server.v1_8_R1.ChatMessage;
import net.minecraft.server.v1_8_R1.EntityHuman;
import net.minecraft.server.v1_8_R1.IChatBaseComponent;
import net.minecraft.server.v1_8_R1.IMerchant;
import net.minecraft.server.v1_8_R1.ItemStack;
import net.minecraft.server.v1_8_R1.MerchantRecipe;
import net.minecraft.server.v1_8_R1.MerchantRecipeList;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;

public class MagicStoneMerchant implements IMerchant{
	EntityHuman e;
	@Override
	public void a(MerchantRecipe paramMerchantRecipe) {
		System.out.println("a_:" + "完了");
	}

	@Override
	public void a_(EntityHuman paramEntityHuman) {
		e = paramEntityHuman;
	}

	@SuppressWarnings("unchecked")
	@Override
	public MerchantRecipeList getOffers(EntityHuman paramEntityHuman) {
//		MerchantRecipe merchantRecipe = new MerchantRecipe(CraftItemStack.asNMSCopy(new ItemStack(Material.APPLE)),
//				CraftItemStack.asNMSCopy(new ItemStack(Material.STONE)),
//				CraftItemStack.asNMSCopy(new ItemStack(Material.ANVIL)), 0, 5);
//		MerchantRecipe merchantRecipe2 = new MerchantRecipe(CraftItemStack.asNMSCopy(new ItemStack(Material.APPLE)),
//				CraftItemStack.asNMSCopy(new ItemStack(Material.ARMOR_STAND)),
//				CraftItemStack.asNMSCopy(new ItemStack(Material.ANVIL)), 0, 5);

		MerchantRecipeList merchantRecipeList = new MagicStoneMerchantRecipeList();
//		merchantRecipeList.add(merchantRecipe);
//		merchantRecipeList.add(merchantRecipe2);
		return merchantRecipeList;
	}

	@Override
	public IChatBaseComponent getScoreboardDisplayName() {
		return new ChatMessage("Magic Stone Factory", new Object[0]);
	}

	@Override
	public EntityHuman u_() {
		return e;
	}

	@Override
	public void a_(net.minecraft.server.v1_8_R1.ItemStack paramItemStack) {
		if (paramItemStack == null || paramItemStack.getItem() == null) {
			System.out.println("アイテムなし");
		} else {
			System.out.println("a_:" + paramItemStack.getItem().getName());
		}

		Player p = (Player) e.getBukkitEntity();
		InventoryView openInventory = p.getOpenInventory();
		System.out.println(openInventory.getType());
	}
}

class MagicStoneMerchantRecipeList extends MerchantRecipeList{
	private static final long serialVersionUID = 2598462579436583041L;

	@Override
	public MerchantRecipe a(ItemStack paramItemStack1, ItemStack paramItemStack2, int paramInt) {
		MerchantRecipe merchantRecipe = new MerchantRecipe(paramItemStack1, paramItemStack2, CraftItemStack.asNMSCopy(new org.bukkit.inventory.ItemStack(Material.MAP)));
		return merchantRecipe;
	}
}
