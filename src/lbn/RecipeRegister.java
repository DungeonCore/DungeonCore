package lbn;

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
    for (Material material : Arrays.asList(Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE,
        Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS)) {
      shapedRecipeLeather.addIngredient(material);
      server.addRecipe(shapedRecipeLeather);
    }
    
    // Gold
    final ShapelessRecipe shapedRecipeGold = new ShapelessRecipe(new ArmorMaterialItem(ArmorMaterial.GOLD).getItem());
    for (Material material : Arrays.asList(Material.GOLD_HELMET, Material.GOLD_CHESTPLATE, Material.GOLD_LEGGINGS,
        Material.GOLD_BOOTS)) {
      shapedRecipeGold.addIngredient(material);
      server.addRecipe(shapedRecipeGold);
    }
    
    // Chainmail
    final ShapelessRecipe shapedRecipeChainmail = new ShapelessRecipe(
        new ArmorMaterialItem(ArmorMaterial.CHAINMAIL).getItem());
    for (Material material : Arrays.asList(Material.CHAINMAIL_HELMET, Material.CHAINMAIL_CHESTPLATE,
        Material.CHAINMAIL_LEGGINGS, Material.CHAINMAIL_BOOTS)) {
      shapedRecipeChainmail.addIngredient(material);
      server.addRecipe(shapedRecipeChainmail);
    }
    
    // Iron
    final ShapelessRecipe shapedRecipeIron = new ShapelessRecipe(new ArmorMaterialItem(ArmorMaterial.IRON).getItem());
    for (Material material : Arrays.asList(Material.IRON_HELMET, Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS,
        Material.IRON_BOOTS)) {
      shapedRecipeIron.addIngredient(material);
      server.addRecipe(shapedRecipeIron);
    }
    
    // Diamond
    final ShapelessRecipe shapedRecipeDiamond = new ShapelessRecipe(
        new ArmorMaterialItem(ArmorMaterial.DIAMOND).getItem());
    for (Material material : Arrays.asList(Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE,
        Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS)) {
      shapedRecipeDiamond.addIngredient(material);
      server.addRecipe(shapedRecipeDiamond);
    }
  }
}
