package net.l_bulb.dungeoncore.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minecraft.server.v1_8_R1.BiomeBase;
import net.minecraft.server.v1_8_R1.BiomeMeta;
import net.minecraft.server.v1_8_R1.EntityInsentient;
import net.minecraft.server.v1_8_R1.EntityTypes;
import net.minecraft.server.v1_8_R1.ItemStack;
import net.minecraft.server.v1_8_R1.MerchantRecipe;

public class NMSUtils {
  /**
   * オリジナルのEntityを登録する
   * 
   * @param name
   * @param id
   * @param nmsClass
   * @param customClass
   */
  public static void registerEntity(String name, int id, Class<? extends EntityInsentient> nmsClass, Class<? extends EntityInsentient> customClass) {
    try {
      List<Map<?, ?>> dataMaps = new ArrayList<Map<?, ?>>();
      for (Field f : EntityTypes.class.getDeclaredFields()) {
        if (f.getType().getSimpleName().equals(Map.class.getSimpleName())) {
          f.setAccessible(true);
          dataMaps.add((Map<?, ?>) f.get(null));
        }
      }
      // もしidが競合していれば取り除く
      if (dataMaps.get(2).containsKey(id)) {
        dataMaps.get(0).remove(name);
        dataMaps.get(2).remove(id);
      }

      Method method = EntityTypes.class.getDeclaredMethod("a", Class.class, String.class, int.class);
      method.setAccessible(true);
      method.invoke(null, customClass, name, id);

      // 全てのバイオームにmodを登録する
      for (Field f : BiomeBase.class.getDeclaredFields()) {
        if (f.getType().getSimpleName()
            .equals(BiomeBase.class.getSimpleName())) {
          if (f.get(null) != null) {
            for (Field list : BiomeBase.class.getDeclaredFields()) {
              if (list.getType().getSimpleName().equals(List.class.getSimpleName())) {
                list.setAccessible(true);
                @SuppressWarnings("unchecked")
                List<BiomeMeta> metaList = (List<BiomeMeta>) list.get(f.get(null));
                for (BiomeMeta meta : metaList) {
                  Field clazz = BiomeMeta.class.getDeclaredFields()[0];
                  if (clazz.get(meta).equals(nmsClass)) {
                    clazz.set(meta, customClass);
                  }
                }
              }
            }

          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * MerchantRecipeのPrivateフィールドにItemをセットする。<br />
   * index = 0 : buy1 <br />
   * index = 1 : buy2 <br />
   * index = 2 : result <br />
   * 
   * @param recipe
   * @param item
   */
  public static void setMerchantRecipe(MerchantRecipe recipe, ItemStack item, int index) {
    String fieldName = null;
    switch (index) {
      case 0:
        fieldName = "buyingItem1";
        break;
      case 1:
        fieldName = "buyingItem2";
        break;
      case 2:
        fieldName = "sellingItem";
        break;
      default:
        break;
    }
    if (fieldName != null) {
      JavaUtil.setPrivateField(recipe, fieldName, item);
    } else {
      throw new RuntimeException("index is invaild : " + index);
    }
  }
}
