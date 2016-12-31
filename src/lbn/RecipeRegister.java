package lbn;

import static org.bukkit.Material.*;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.inventory.ShapelessRecipe;

import lbn.dungeon.contents.item.material.ArmorMaterialItem;
import lbn.item.armoritem.ArmorMaterial;

public class RecipeRegister {

	public static void registerRecipe() {
		Server server = Bukkit.getServer();

		// Leather
		final ShapelessRecipe shapedRecipeLeather = new ShapelessRecipe(
				new ArmorMaterialItem(ArmorMaterial.LEATHER).getItem());
		for (Material material : Arrays.asList(
				LEATHER_HELMET,
				LEATHER_CHESTPLATE,
				LEATHER_LEGGINGS,
				LEATHER_BOOTS)) {
			shapedRecipeLeather.addIngredient(material);
			server.addRecipe(shapedRecipeLeather);
		}

		// Gold
		final ShapelessRecipe shapedRecipeGold = new ShapelessRecipe(
				new ArmorMaterialItem(ArmorMaterial.GOLD).getItem());
		for (Material material : Arrays.asList(
				GOLD_HELMET,
				GOLD_CHESTPLATE,
				GOLD_LEGGINGS,
				GOLD_BOOTS)) {
			shapedRecipeGold.addIngredient(material);
			server.addRecipe(shapedRecipeGold);
		}

		// Chainmail
		final ShapelessRecipe shapedRecipeChainmail = new ShapelessRecipe(
				new ArmorMaterialItem(ArmorMaterial.CHAINMAIL).getItem());
		for (Material material : Arrays.asList(
				CHAINMAIL_HELMET,
				CHAINMAIL_CHESTPLATE,
				CHAINMAIL_LEGGINGS,
				CHAINMAIL_BOOTS)) {
			shapedRecipeChainmail.addIngredient(material);
			server.addRecipe(shapedRecipeChainmail);
		}

		// Iron
		final ShapelessRecipe shapedRecipeIron = new ShapelessRecipe(
				new ArmorMaterialItem(ArmorMaterial.IRON).getItem());
		for (Material material : Arrays.asList(
				IRON_HELMET,
				IRON_CHESTPLATE,
				IRON_LEGGINGS,
				IRON_BOOTS)) {
			shapedRecipeIron.addIngredient(material);
			server.addRecipe(shapedRecipeIron);
		}

		// Diamond
		final ShapelessRecipe shapedRecipeDiamond = new ShapelessRecipe(
				new ArmorMaterialItem(ArmorMaterial.DIAMOND).getItem());
		for (Material material : Arrays.asList(
				DIAMOND_HELMET,
				DIAMOND_CHESTPLATE,
				DIAMOND_LEGGINGS,
				DIAMOND_BOOTS)) {
			shapedRecipeDiamond.addIngredient(material);
			server.addRecipe(shapedRecipeDiamond);
		}
	}
}
