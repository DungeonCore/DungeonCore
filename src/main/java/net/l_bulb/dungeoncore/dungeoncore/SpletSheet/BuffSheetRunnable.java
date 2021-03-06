package net.l_bulb.dungeoncore.dungeoncore.SpletSheet;

import java.util.concurrent.Future;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.potion.PotionEffectType;

import net.l_bulb.dungeoncore.common.buff.BuffData;
import net.l_bulb.dungeoncore.common.buff.BuffDataFactory;
import net.l_bulb.dungeoncore.common.buff.BuffType;
import net.l_bulb.dungeoncore.util.JavaUtil;

public class BuffSheetRunnable extends AbstractSheetRunable {

  public BuffSheetRunnable(CommandSender p) {
    super(p);
  }

  @Override
  protected String getQuery() {
    return null;
  }

  @Override
  public String getSheetName() {
    return "buff";
  }

  @Override
  public String[] getTag() {
    return new String[] { "id", "effect", "second", "level" };
  }

  @Override
  public void onCallbackFunction(Future<String[][]> submit) throws Exception {
    BuffDataFactory.clear();
    super.onCallbackFunction(submit);
  }

  @Override
  protected void excuteOnerow(String[] row) {

    String id = row[0];

    BuffType debuffType = BuffType.getDebuffType(row[1]);
    if (debuffType == null) {
      sendMessage("不正なBuffパラメータ[id:" + id + ", effect:" + row[1] + "]");
      return;
    }

    PotionEffectType effect = debuffType.getType();
    double second = JavaUtil.getDouble(row[2], 0.0);
    int level = JavaUtil.getInt(row[3], 0);

    BuffData data = BuffDataFactory.create(id, effect, second, level);
    if (data != null) {
      BuffDataFactory.register(data);
    } else {
      sendMessage("不正なBuffパラメータ[id:" + id + ", effect:" + effect + ", second:" + second + ", level:" + level + "]");
    }

  }

  public static void allReload() {
    ConsoleCommandSender consoleSender = Bukkit.getConsoleSender();
    BuffSheetRunnable buffSheetRunnable = new BuffSheetRunnable(consoleSender);
    SpletSheetExecutor.onExecute(buffSheetRunnable);
  }

}
