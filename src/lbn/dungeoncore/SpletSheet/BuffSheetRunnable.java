package lbn.dungeoncore.SpletSheet;

import java.util.concurrent.Future;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.potion.PotionEffectType;

import lbn.common.buff.BuffData;
import lbn.common.buff.BuffDataFactory;
import lbn.util.JavaUtil;

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
		return new String[]{"id", "effect", "second", "level"};
	}

	@Override
	public void onCallbackFunction(Future<String[][]> submit) throws Exception {
		BuffDataFactory.clear();
		super.onCallbackFunction(submit);
	}

	@Override
	protected void excuteOnerow(String[] row) {

		String id = row[0];
		PotionEffectType effect = PotionEffectType.getByName(row[1]);
		int second = JavaUtil.getInt(row[2], 0);
		int level = JavaUtil.getInt(row[3], 0);

		BuffData data = BuffDataFactory.create(id, effect, second, level);
		if(data != null) {
			BuffDataFactory.register(data);
		}else{
			sendMessage("不正なBuffパラメータ[id:" + id + ", effect:" + effect + ", second:" + second + ", level:" + level + "]");
		}

	}

	public static void allReload() {
		ConsoleCommandSender consoleSender = Bukkit.getConsoleSender();
		BuffSheetRunnable buffSheetRunnable = new BuffSheetRunnable(consoleSender);
		SpletSheetExecutor.onExecute(buffSheetRunnable);
	}

}
