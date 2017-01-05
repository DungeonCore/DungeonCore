package lbn.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lbn.player.status.IStatusManager;
import lbn.player.status.PlayerStatus;
import lbn.player.status.PlayerStatusListener;
import lbn.player.status.StatusAddReason;
import lbn.player.status.bowStatus.BowStatusManager;
import lbn.player.status.magicStatus.MagicStatusManager;
import lbn.player.status.mainStatus.MainStatusManager;
import lbn.player.status.swordStatus.SwordStatusManager;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class PlayerStatusCommand implements CommandExecutor, TabCompleter{

	public static ArrayList<String> managerList = null;

	public static String[] oprateName = {"ADD", "SET", "SHOW"};

	public PlayerStatusCommand() {
		//managerの初期化する
		if (managerList == null) {
			managerList = new ArrayList<String>();
			for (IStatusManager manager : PlayerStatusListener.managerList) {
				managerList.add(manager.getManagerName().replace(" ", "_"));
			}
		}
	}

	@Override
	public boolean onCommand(CommandSender paramCommandSender, Command paramCommand, String paramString, String[] params) {
		if (params.length < 3) {
			return false;
		}

		if (!(paramCommandSender instanceof Player)) {
			paramCommandSender.sendMessage("コンソールから実行は出来ません。");
			return true;
		}
		Player sender = (Player) paramCommandSender;
		OfflinePlayer target = sender;
		if (params.length == 4) {
			@SuppressWarnings("deprecation")
			OfflinePlayer player = Bukkit.getOfflinePlayer(params[3]);
			if (player == null) {
				paramCommandSender.sendMessage(params[3] + "というプレイヤーは存在しません。");
				return true;
			}
			target = player;
		}

		//操作を変換
		String type = params[1].toUpperCase();


		//数値に変換
		int value = 0;
		try {
			if (params.length == 1) {
				value = 0;
			} else {
				value = Integer.parseInt(params[2]);
			}
		} catch (Exception e) {
			sender.sendMessage(params[2] + "を整数に変換できません。");
			return true;
		}

		//managerに変換
		String managerName = params[0].toUpperCase().replace("_", " ");
		IStatusManager manager = getManager(managerName);
		if (manager == null) {
			sender.sendMessage(params[0] + "をstatusに変換できません。");
		} else if (manager.equals(MainStatusManager.getInstance())) {
			Bukkit.dispatchCommand(paramCommandSender, StringUtils.join(new Object[]{paramString, SwordStatusManager.getInstance().getManagerName(), type, value, target.getName()},  " "));
			Bukkit.dispatchCommand(paramCommandSender, StringUtils.join(new Object[]{paramString, BowStatusManager.getInstance().getManagerName(), type, value, target.getName()},  " "));
			Bukkit.dispatchCommand(paramCommandSender, StringUtils.join(new Object[]{paramString, MagicStatusManager.getInstance().getManagerName(), type, value, target.getName()},  " "));
			return true;
		}

		switch (type) {
		case "ADD":
			addMehtod(manager, value, target, sender);
			break;
		case "SET":
			setMehtod(manager, value, target, sender);
			break;
		case "SHOW":
			showMehtod(manager, value, target, sender);
			break;
		default:
			break;
		}

		return true;
	}


	protected void showMehtod(IStatusManager manager, int value, OfflinePlayer target, Player sender) {
		PlayerStatus status = manager.getStatus(target);
		sender.sendMessage(manager.getManagerName() + "は" + status.getLevel() + "(" + status.getExp() + "xp)です。");
	}

	protected void setMehtod(IStatusManager manager, int value, OfflinePlayer target, Player sender) {
		manager.setLevel(target, value);
		PlayerStatus status = manager.getStatus(target);
		sender.sendMessage(manager.getManagerName() + "が" + status.getLevel() + "(" + status.getExp() + "xp)になりました。");
	}

	protected void addMehtod(IStatusManager manager, int value, OfflinePlayer target, Player sender) {
		manager.addExp(target, value, StatusAddReason.commad);
		PlayerStatus status = manager.getStatus(target);
		sender.sendMessage(manager.getManagerName() + "が" + status.getLevel() + "(" + status.getExp() + "xp)になりました。");
	}

	protected IStatusManager getManager(String name) {
		if (name == null) {
			return null;
		}
		for (IStatusManager manager : PlayerStatusListener.managerList) {
			if (manager.getManagerName().equalsIgnoreCase(name)) {
				return manager;
			}
		}
		return null;
	}

	@Override
	public List<String> onTabComplete(CommandSender paramCommandSender, Command paramCommand, String paramString, String[] paramArrayOfString) {
		if (paramArrayOfString.length == 1) {
			return managerList;
		} else if (paramArrayOfString.length == 2) {
			return Arrays.asList(oprateName);
		}
		return null;
	}

}
