package net.l_bulb.dungeoncore.util;

import java.text.MessageFormat;
import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.libs.com.google.gson.JsonParseException;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_8_R1.ChatComponentUtils;
import net.minecraft.server.v1_8_R1.ChatSerializer;
import net.minecraft.server.v1_8_R1.CommandAbstract;
import net.minecraft.server.v1_8_R1.EntityPlayer;
import net.minecraft.server.v1_8_R1.EnumTitleAction;
import net.minecraft.server.v1_8_R1.IChatBaseComponent;
import net.minecraft.server.v1_8_R1.ICommandListener;
import net.minecraft.server.v1_8_R1.MinecraftServer;
import net.minecraft.server.v1_8_R1.PacketPlayOutTitle;

public class TitleSender {
  ArrayList<String[]> commandJobList = new ArrayList<>();

  public void setTitle(String text, ChatColor c, boolean isBold) {
    commandJobList.add(new String[] { null, "title",
        MessageFormat.format("{3}text:\"{0}\",color:\"{1}\",bold:\"{2}\"}", text, c.name().toLowerCase(), isBold, "{") });
  }

  public void setSubTitle(String text, ChatColor c, boolean isBold) {
    commandJobList.add(new String[] { null, "subtitle",
        MessageFormat.format("{3}text:\"{0}\",color:\"{1}\",bold:\"{2}\"}", text, c.name().toLowerCase(), isBold, "{") });
  }

  public void setTimes(int fadeIn, int stay, int fadeOut) {
    commandJobList.add(new String[] { null, "times", Integer.toString(fadeIn), Integer.toString(stay), Integer.toString(fadeOut) });
  }

  public void setReset() {
    commandJobList.add(new String[] { null, "reset" });
  }

  public void setClear() {
    commandJobList.add(new String[] { null, "clear" });
  }

  /**
   * Titileを実行
   *
   * @param p
   */
  public void execute(Player p) {
    MinecraftServer server = MinecraftServer.getServer();
    for (String[] param : commandJobList) {
      execute(server, p, param);
    }
  }

  private void execute(ICommandListener paramICommandListener, Player p, String[] paramArrayOfString) {
    EntityPlayer localEntityPlayer = ((CraftPlayer) p).getHandle();
    EnumTitleAction localEnumTitleAction = EnumTitleAction
        .a(paramArrayOfString[1]);
    if ((localEnumTitleAction == EnumTitleAction.CLEAR) || (localEnumTitleAction == EnumTitleAction.RESET)) {
      PacketPlayOutTitle localPacketPlayOutTitle1 = new PacketPlayOutTitle(localEnumTitleAction, null);
      localEntityPlayer.playerConnection.sendPacket(localPacketPlayOutTitle1);
      return;
    }
    if (localEnumTitleAction == EnumTitleAction.TIMES) {
      int i = Integer.parseInt(paramArrayOfString[2]);
      int j = Integer.parseInt(paramArrayOfString[3]);
      int k = Integer.parseInt(paramArrayOfString[4]);
      PacketPlayOutTitle localObject = new PacketPlayOutTitle(i, j, k);
      localEntityPlayer.playerConnection.sendPacket(localObject);
      return;
    }

    String str = CommandAbstract.a(paramArrayOfString, 2);
    IChatBaseComponent localIChatBaseComponent = null;
    try {
      localIChatBaseComponent = ChatSerializer.a(str);
    } catch (JsonParseException localJsonParseException) {
      localJsonParseException.printStackTrace();
      return;
    }
    PacketPlayOutTitle localPacketPlayOutTitle2 = new PacketPlayOutTitle(localEnumTitleAction, ChatComponentUtils.filterForDisplay(
        paramICommandListener, localIChatBaseComponent, localEntityPlayer));
    localEntityPlayer.playerConnection.sendPacket(localPacketPlayOutTitle2);
  }

}