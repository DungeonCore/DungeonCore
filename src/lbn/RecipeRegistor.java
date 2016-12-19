package lbn;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.inventory.ShapelessRecipe;

import lbn.dungeon.contents.item.material.ArmorMaterialItem;
import lbn.item.armoritem.ArmorMaterial;

public class RecipeRegistor {
	public static void addRecipe() {
		Server server = Bukkit.getServer();

		for (Material material : Arrays.asList(Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS)) {
			ShapelessRecipe shapedRecipe = new ShapelessRecipe(new ArmorMaterialItem(ArmorMaterial.LEATHER).getItem());
			shapedRecipe.addIngredient(material);
			server.addRecipe(shapedRecipe);
		}
		for (Material material : Arrays.asList(Material.GOLD_HELMET, Material.GOLD_CHESTPLATE, Material.GOLD_LEGGINGS, Material.GOLD_BOOTS)) {
			ShapelessRecipe shapedRecipe = new ShapelessRecipe(new ArmorMaterialItem(ArmorMaterial.GOLD).getItem());
			shapedRecipe.addIngredient(material);
			server.addRecipe(shapedRecipe);
		}
		for (Material material : Arrays.asList(Material.CHAINMAIL_HELMET, Material.CHAINMAIL_CHESTPLATE, Material.CHAINMAIL_LEGGINGS, Material.CHAINMAIL_BOOTS)) {
			ShapelessRecipe shapedRecipe = new ShapelessRecipe(new ArmorMaterialItem(ArmorMaterial.CHAINMAIL).getItem());
			shapedRecipe.addIngredient(material);
			server.addRecipe(shapedRecipe);
		}
		for (Material material : Arrays.asList(Material.IRON_HELMET, Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS, Material.IRON_BOOTS)) {
			ShapelessRecipe shapedRecipe = new ShapelessRecipe(new ArmorMaterialItem(ArmorMaterial.IRON).getItem());
			shapedRecipe.addIngredient(material);
			server.addRecipe(shapedRecipe);
		}
		for (Material material : Arrays.asList(Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS)) {
			ShapelessRecipe shapedRecipe = new ShapelessRecipe(new ArmorMaterialItem(ArmorMaterial.DIAMOND).getItem());
			shapedRecipe.addIngredient(material);
			server.addRecipe(shapedRecipe);
		}
	}
}
