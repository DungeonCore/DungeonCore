package net.l_bulb.dungeoncore.player.magicstoneOre.trade;

import net.minecraft.server.v1_8_R1.EntityPlayer;

import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class MagicStoneTrade {
  /**
   * 魔法鉱石の交換画面を開く
   */
  public static void open(Player player) {
    EntityPlayer p = ((CraftPlayer) player).getHandle();
    MagicStoneMerchant imerchant = new MagicStoneMerchant();
    imerchant.a_(p);
    p.openTrade(imerchant);
  }
}
