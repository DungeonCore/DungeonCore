package lbn.command;

import lbn.dungeoncore.SpletSheet.MobSheetRunnable;
import lbn.dungeoncore.SpletSheet.SpletSheetExecutor;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class MobCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender paramCommandSender,
			Command paramCommand, String paramString,
			String[] paramArrayOfString) {

		if (paramArrayOfString.length < 1) {
			return false;
		}

		String operate = paramArrayOfString[0];
		if (operate.equalsIgnoreCase("reload")) {
			if (paramArrayOfString.length == 1) {
				reloadAllMob(paramCommandSender);
				return true;
			}

			MobSheetRunnable mobSheetRunnable = new MobSheetRunnable(paramCommandSender, getMobName(paramArrayOfString));
			if (mobSheetRunnable.isTransaction()) {
				paramCommandSender.sendMessage("現在他の人が実行中です。");
				return true;
			}
			SpletSheetExecutor.onExecute(mobSheetRunnable);
			return true;
		}
		paramCommandSender.sendMessage("許可されていない操作です");
		return false;
	}

	protected String getMobName(String[] paramArrayOfString) {
		String name = "";
		int i = -1;
		for (String string : paramArrayOfString) {
			i++;
			if (i == 0) {
				continue;
			}
			name = string + " ";
		}
		return name.trim();
	}

	public static void reloadAllMob(CommandSender sender) {
		if (sender == null) {
			sender = Bukkit.getConsoleSender();
		}

		SpletSheetCommand.reloadSheet(null, "mob2");
		SpletSheetCommand.reloadSheet(null, "mob1");
		MobSheetRunnable mobSheetRunnable = new MobSheetRunnable(sender);
		SpletSheetExecutor.onExecute(mobSheetRunnable);
	}

}
