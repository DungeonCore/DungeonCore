package lbn.command.util;

import org.apache.commons.lang3.RandomStringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import lbn.money.shop.CustomShopItem;
import lbn.util.JavaUtil;

public class ShopItemCommand implements CommandExecutor {

  @Override
  public boolean onCommand(CommandSender paramCommandSender,
      Command paramCommand, String paramString,
      String[] paramArrayOfString) {
    if (paramArrayOfString.length == 0) { return false; }

    int price = JavaUtil.getInt(paramArrayOfString[0], -1);
    if (price <= 0) {
      paramCommandSender.sendMessage("値段が不正です。");
      return true;
    }

    Player p = (Player) paramCommandSender;
    ItemStack itemInHand = p.getItemInHand();
    CustomShopItem customShopItem = new CustomShopItem(itemInHand, price, itemInHand.getAmount(), RandomStringUtils.randomAlphanumeric(5));
    p.setItemInHand(customShopItem.getCustomShopItemTemplate());
    p.updateInventory();
    return true;
  }

}
