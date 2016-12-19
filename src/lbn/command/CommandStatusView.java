package lbn.command;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import lbn.player.playerIO.PlayerIOData;
import lbn.player.playerIO.PlayerIODataManager;
import lbn.player.playerIO.PlayerLastSaveType;
import lbn.player.status.StatusViewerInventory;

public class CommandStatusView implements CommandExecutor{
	@Override
	public boolean onCommand(CommandSender paramCommandSender, Command paramCommand, String paramString, String[] params) {
		if (!(paramCommandSender instanceof Player)) {
			paramCommandSender.sendMessage("コンソールから実行できません。");
			return true;
		}

		if (params.length == 0) {
			openStatus((Player)paramCommandSender, (Player)paramCommandSender);
		} else if (params.length == 1) {
			String name = params[0];
			if (name.equals("show")) {
				openStatus((Player)paramCommandSender, (Player)paramCommandSender);
				return true;
			}

			Player player = Bukkit.getPlayerExact(name);

			if (player == null) {
				showOfflineStats(paramCommandSender, name);
				return true;
			}
			openStatus((Player) paramCommandSender, player);
		}
		return true;
	}

	public void showOfflineStats(CommandSender paramCommandSender, String name) {
		if (!paramCommandSender.isOp()  && ((Player)paramCommandSender).getGameMode() == GameMode.CREATIVE) {
			paramCommandSender.sendMessage(name + "というPlayerが存在しません。");
			return;
		}

		@SuppressWarnings("deprecation")
		OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
		if (offlinePlayer == null) {
			paramCommandSender.sendMessage(name + "というPlayerが過去にこのサーバーに存在しません。");
			return;
		}
		UUID uniqueId = offlinePlayer.getUniqueId();
		PlayerIOData loadData = PlayerIODataManager.getLoadData(uniqueId, PlayerLastSaveType.getType(uniqueId));
		if (loadData == null) {
			paramCommandSender.sendMessage(name + "というPlayerが過去にこのサーバーに存在しません。");
			return;
		}
		paramCommandSender.sendMessage("剣レベル：" + loadData.swordLevel);
		paramCommandSender.sendMessage("弓レベル：" + loadData.bowLevel);
		paramCommandSender.sendMessage("魔法レベル：" + loadData.magicLevel);
		paramCommandSender.sendMessage("お金：" + loadData.galions);
	}

	private void openStatus(Player sender, Player target) {
		Inventory statusView = StatusViewerInventory.getStatusView(target);
		sender.openInventory(statusView);
	}

}
