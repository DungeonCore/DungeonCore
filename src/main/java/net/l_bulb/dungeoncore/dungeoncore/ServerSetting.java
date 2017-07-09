package net.l_bulb.dungeoncore.dungeoncore;

import org.bukkit.Bukkit;

import net.l_bulb.dungeoncore.util.DungeonLogger;
import net.l_bulb.dungeoncore.util.TheLowExecutor;

import net.minecraft.server.v1_8_R1.AttributeRanged;
import net.minecraft.server.v1_8_R1.GenericAttributes;

public class ServerSetting {
  public static void setting() {
    // 最大体力を設定
    TheLowExecutor.executeIgnoreException(
        () -> ((AttributeRanged) GenericAttributes.maxHealth).b = Double.MAX_VALUE,
        e -> DungeonLogger.error("最大体力を設定できません。"));

    // 体力の自然回復を無しにする
    TheLowExecutor.executeLater(20 * 1, () -> Bukkit.getWorlds().stream().forEach(w -> w.setGameRuleValue("naturalRegeneration", "false")));
    // // thelowワールドだけありにする
    // TheLowExecutor.executeLater(20 * 6, () -> Bukkit.getWorld("thelow").setGameRuleValue("naturalRegeneration", "true"));
  }
}
