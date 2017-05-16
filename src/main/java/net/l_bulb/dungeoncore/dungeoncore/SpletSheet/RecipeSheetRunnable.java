package net.l_bulb.dungeoncore.dungeoncore.SpletSheet;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.bukkit.command.CommandSender;

import net.l_bulb.dungeoncore.item.system.craft.CraftItemRecipeFactory;
import net.l_bulb.dungeoncore.item.system.craft.TheLowCraftRecipeInterface;
import net.l_bulb.dungeoncore.item.system.craft.TheLowCraftRecipeWithMainItem;
import net.l_bulb.dungeoncore.item.system.craft.TheLowCraftRecipeWithMaterial;

public class RecipeSheetRunnable extends AbstractSheetRunable {

  public RecipeSheetRunnable(CommandSender sender) {
    super(sender);
  }

  @Override
  protected String getQuery() {
    return null;
  }

  @Override
  public String getSheetName() {
    return "craftrecipe";
  }

  @Override
  public String[] getTag() {
    return new String[] { "id", "craftitemid", "maincraftmaterial", "craftmatrial1", "craftcount1", "craftmatrial2", "craftcount2", "craftmatrial3",// 7
        "craftcount3", "successitemid", "greateitemid" };
  }

  @Override
  protected void excuteOnerow(String[] row) {
    try {
      String mainMaterialItem = row[2];

      // レシピを取得
      TheLowCraftRecipeInterface recipe;
      if (StringUtils.isEmpty(mainMaterialItem)) {
        recipe = new TheLowCraftRecipeWithMaterial();
      } else {
        recipe = new TheLowCraftRecipeWithMainItem(mainMaterialItem);
      }

      recipe.setId(row[0]);

      // not null
      Validate.notNull(row[1]);
      // クラフト後のアイテムを取得
      recipe.setCraftItemId(row[1]);
      recipe.setSuccessItemId(row[9]);
      recipe.setGreateSuccessItemId(row[10]);

      // 素材を登録
      if (row[3] != null) {
        recipe.addMaterial(row[3], Integer.parseInt(row[4]));
      }
      if (row[5] != null) {
        recipe.addMaterial(row[5], Integer.parseInt(row[6]));
      }
      if (row[7] != null) {
        recipe.addMaterial(row[7], Integer.parseInt(row[8]));
      }

      CraftItemRecipeFactory.addRecipe(row[1], recipe);
    } catch (Exception e) {
      sendMessage("レシピが不正です：" + Arrays.toString(row));
    }
  }

}
