package lbn.dungeoncore.SpletSheet;

import java.util.concurrent.Future;

import lbn.common.sound.SoundData;
import lbn.common.sound.SoundManager;
import lbn.util.JavaUtil;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

public class SoundSheetRunnable extends AbstractSheetRunable {

  public SoundSheetRunnable(CommandSender p) {
    super(p);
  }

  @Override
  protected String getQuery() {
    return null;
  }

  @Override
  public String getSheetName() {
    return "sound";
  }

  @Override
  public String[] getTag() {
    return new String[] { "id", "sound", "volume", "pitch" };
  }

  @Override
  public void onCallbackFunction(Future<String[][]> submit) throws Exception {
    SoundManager.clear();
    super.onCallbackFunction(submit);
  }

  @Override
  protected void excuteOnerow(String[] row) {
    String id = row[0];

    Sound valueOf = null;
    try {
      valueOf = Sound.valueOf(row[1]);
    } catch (Exception e) {
      sendMessage("soundが不正です, id：" + id + ", sound：" + row[1]);
    }
    if (valueOf == null) { return; }

    float vol = JavaUtil.getFloat(row[2], 1);
    float pitch = JavaUtil.getFloat(row[3], 1);

    SoundData soundData = new SoundData(valueOf, vol, pitch);

    SoundManager.regist(id, soundData);
  }

  public static void allReload() {
    ConsoleCommandSender consoleSender = Bukkit.getConsoleSender();
    SoundSheetRunnable soundSheetRunnable = new SoundSheetRunnable(consoleSender);
    SpletSheetExecutor.onExecute(soundSheetRunnable);
  }

}
