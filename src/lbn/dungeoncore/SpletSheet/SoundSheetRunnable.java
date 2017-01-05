package lbn.dungeoncore.SpletSheet;

import java.util.concurrent.Future;

import org.bukkit.Sound;
import org.bukkit.command.CommandSender;

import lbn.common.sound.SoundData;
import lbn.common.sound.SoundManager;
import lbn.util.JavaUtil;

public class SoundSheetRunnable extends AbstractSheetRunable{

	public SoundSheetRunnable(CommandSender p) {
		super(p);
	}

	@Override
	protected String getQuery() {
		return null;
	}

	@Override
	String getSheetName() {
		return "sound";
	}

	@Override
	public String[] getTag() {
		return new String[]{"id", "sound", "volume", "pitch"};
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
		if (valueOf == null) {
			return;
		}

		float vol = JavaUtil.getFloat(row[2], 1);
		float pitch = JavaUtil.getFloat(row[3], 1);

		SoundData soundData = new SoundData(id, valueOf, vol, pitch);

		SoundManager.regist(soundData);
	}

}
