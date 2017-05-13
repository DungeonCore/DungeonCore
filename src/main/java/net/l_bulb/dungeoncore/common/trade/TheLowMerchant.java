package net.l_bulb.dungeoncore.common.trade;

import io.netty.buffer.Unpooled;

import java.util.List;

import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;

import net.l_bulb.dungeoncore.common.trade.nms.MerchantRecipeListImplemention;
import net.l_bulb.dungeoncore.util.JavaUtil;

import net.minecraft.server.v1_8_R1.EntityPlayer;
import net.minecraft.server.v1_8_R1.MerchantRecipeList;
import net.minecraft.server.v1_8_R1.PacketDataSerializer;
import net.minecraft.server.v1_8_R1.PacketPlayOutCustomPayload;

public abstract class TheLowMerchant {
  protected Player p;
  int containerCounter;

  public TheLowMerchant(Player p) {
    this.p = p;
    this.containerCounter = JavaUtil.getField(EntityPlayer.class, "containerCounter", ((CraftPlayer) p).getHandle());
  }

  public int getContainerCounter() {
    return containerCounter;
  }

  /**
   * Playerがアイテムを動かした時
   */
  public void onSetItem() {
    InventoryView openInventory = p.getOpenInventory();
    onSetItem(openInventory);
  }

  /**
   * Playerがアイテムを動かした時の処理
   * 
   * @param inv
   */
  protected abstract void onSetItem(InventoryView inv);

  public Player getPlayer() {
    return p;
  }

  /**
   * 表示される名前
   *
   * @return
   */
  abstract public String getName();

  /**
   * Result欄にアイテムが表示された時の処理。nullなら取引できない
   *
   * @param recipe
   * @return TODO
   */
  abstract public TheLowMerchantRecipe getShowResult(TheLowMerchantRecipe recipe);

  /**
   * レシピのパケットを送信する
   * 
   * @param recipeList
   */
  @SuppressWarnings("unchecked")
  protected void sendRecipeList(List<TheLowMerchantRecipe> recipeList) {
    // リストを作成
    MerchantRecipeList merchantrecipelist = new MerchantRecipeList();
    for (TheLowMerchantRecipe recipe : recipeList) {
      merchantrecipelist.add(recipe.toMerchantRecipe());
    }

    // パケットを送信する
    PacketDataSerializer packetdataserializer = new PacketDataSerializer(Unpooled.buffer());
    packetdataserializer.writeInt(getContainerCounter());
    merchantrecipelist.a(packetdataserializer);
    ((CraftPlayer) p).getHandle().playerConnection.sendPacket(new PacketPlayOutCustomPayload("MC|TrList", packetdataserializer));

    // 一旦全て削除し、入れ直す
    if (nowRecipeList != null) {
      nowRecipeList.clear();
    } else {
      nowRecipeList = new MerchantRecipeListImplemention(this);
    }
    for (TheLowMerchantRecipe theLowMerchantRecipe : recipeList) {
      nowRecipeList.addTheLowRecipe(theLowMerchantRecipe);
    }
  }

  MerchantRecipeListImplemention nowRecipeList = null;

  /**
   * 現在開いているレシピリストを取得
   * 
   * @return
   */
  public MerchantRecipeListImplemention getNowRecipeList() {
    if (nowRecipeList == null) {
      nowRecipeList = new MerchantRecipeListImplemention(this);
      for (TheLowMerchantRecipe recipe : getInitRecipes()) {
        nowRecipeList.addTheLowRecipe(recipe);
      }
    }
    return nowRecipeList;
  }

  /**
   * 一番最初に表示されるレシピを取得する
   * 
   * @return
   */
  abstract public List<TheLowMerchantRecipe> getInitRecipes();

  /**
   * 取引が終了した時
   */
  abstract public void onFinishTrade(TheLowMerchantRecipe recipe);
}
