package lbn.player;

import java.util.HashMap;

import lbn.command.TpCutCommand;
import lbn.util.LbnRunnable;
import lbn.util.Message;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PlayerChestTpManager {
	static HashMap<String, TpLaterRunnable> tpDataMap = new HashMap<String, TpLaterRunnable>();

	public static void executeIfNotTeleported(Player p) {
		TpLaterRunnable tpLaterRunnable = tpDataMap.get(p.getName());
		if (tpLaterRunnable == null) {
			return;
		}

		//まだテレポートしていなければテレポートする
		if (tpLaterRunnable.isCantTeleported()) {
			tpLaterRunnable.runIfServerEnd();
			tpDataMap.remove(p.getName());
			tpLaterRunnable.cancel();
		}
	}

	public static void teleport(String name, Location loc, int tick) {
		TpLaterRunnable tpNow = new TpLaterRunnable(name, tick, loc);

		//他のテレポートが実行されていないならそのまま実行
		if (!tpDataMap.containsKey(name)) {
			tpDataMap.put(name, tpNow);
			tpNow.exec();
			return;
		}

		TpLaterRunnable tpBefore = tpDataMap.get(name);
		//前に設定されたテレポートされる時刻
		int whenTpBefore = tpBefore.getWhenTp();
		//今設定されたテレポートされる時刻
		int whenTpNow = tpNow.getWhenTp();

		//今設定したほうがテレポートされる時刻があとなら前のテレポートをキャンセルし、今設定したものを開始する
		if (whenTpBefore < whenTpNow) {
			tpBefore.cancel();
			tpDataMap.put(name, tpNow);
			tpNow.exec();
		}
	}
}

class TpLaterRunnable extends LbnRunnable{
	String playerName;
	int tick;
	Location loc;
	int tpTick = 0;

	public TpLaterRunnable(String name, int tick, Location loc) {
		this.playerName = name;
		this.tick = tick;
		this.loc = loc;

		Player player = Bukkit.getPlayerExact(name);
		if (player != null) {
			tpTick = player.getTicksLived() + tick;
		}
	}

	boolean cantTeleported = false;

	public boolean isCantTeleported() {
		return cantTeleported;
	}

	@Override
	public void run2() {
		try {
			int remainedTime = (int) (tick / 20.0 - getRunCount());

			if (remainedTime <= -10) {
				Message.sendMessage(Bukkit.getPlayerExact(playerName), ChatColor.YELLOW + "エラーが発生したためテレポートできませんでした。");
				cancel();
				return;
			}

			if (TpCutCommand.isCancel(playerName)) {
				Message.sendMessage(Bukkit.getPlayerExact(playerName), ChatColor.YELLOW + "TPをキャンセルしました。");
				cancel();
				return;
			}

			boolean sendMessage = false;
			if (remainedTime == tick * 20) {
				sendMessage = true;
			} else if (remainedTime >= 10 && remainedTime % 10 == 0) {
				sendMessage = true;
			} else if (remainedTime == 7) {
				sendMessage = true;
			} else if (remainedTime <= 5 && remainedTime > 0) {
				sendMessage = true;
			} else if (getRunCount() == 0) {
				sendMessage = true;
			}

			//テレポートする
			if (remainedTime <= 0) {
				cantTeleported = !teleport();
				Message.sendMessage(Bukkit.getPlayerExact(playerName), ChatColor.YELLOW + "テレポートしました。");
				cancel();
				return;
			}

			if (sendMessage) {
				Message.sendMessage(Bukkit.getPlayerExact(playerName), ChatColor.YELLOW + "{0}秒後にテレポートします。", remainedTime);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected boolean teleport() {
		Player player = Bukkit.getPlayerExact(playerName);
		if (player == null || !player.isOnline()) {
			return false;
		}

		//playerがオンラインのときはテレポートする
		player.teleport(loc);
		//MAPから削除する
		PlayerChestTpManager.tpDataMap.remove(player.getName());
		return true;
	}

	@Override
	protected void runIfServerEnd() {
		cantTeleported = !teleport();
		cancel();
	}

	public int getWhenTp() {
		return tpTick;
	}

	public void exec() {
		runTaskTimer(20);
	}

	public Location direction() {
		return loc;
	}
}
