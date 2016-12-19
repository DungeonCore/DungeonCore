package main.player;

import java.util.HashMap;

import org.bukkit.entity.Player;

import main.util.LbnRunnable;

public class MagicPointManager {

	public static void addMagicPoint(Player p, int val) {
		int maxMagicPoint = getMaxMagicPoint(p);
		int level = p.getLevel();
		p.setLevel(Math.min(maxMagicPoint, level + val));
	}

	protected static void startHealMagicPoint(Player p) {
		//magicポイントを回復させる
		MagicPointRunnable runable = new MagicPointRunnable(p);
		if (!runable.isRun()) {
			runable.startTask();
		}
	}

	public static void consumeMagicPoint(Player p, int val) {
		addMagicPoint(p, -1 * val);

		if (val > 0) {
			startHealMagicPoint(p);
		}
	}

	public static int getNowMagicPoint(Player p) {
		return p.getLevel();
	}

	public static int getMaxMagicPoint(Player p) {
		return 100;
	}

	public static void onJoinServer(Player p) {
		if (getMaxMagicPoint(p) > getNowMagicPoint(p)) {
			startHealMagicPoint(p);
		}
	}
}

class MagicPointRunnable extends LbnRunnable {
	static HashMap<Player, MagicPointRunnable> hashSet = new HashMap<Player,MagicPointRunnable>();

	Player p;
	public MagicPointRunnable(Player p) {
		this.p = p;
	}

	public boolean isRun() {
		return hashSet.containsKey(p);
	}

	@Override
	public void run2() {
		MagicPointManager.addMagicPoint(p, 1);
		if (MagicPointManager.getNowMagicPoint(p) >= MagicPointManager.getMaxMagicPoint(p)) {
			cancel();
			return;
		}

		if (!p.isOnline()) {
			cancel();
			return;
		}
	}

	public void startTask() {
		if (!isRun()) {
			hashSet.put(p, this);
			super.runTaskTimer(15);
		}
	}

	@Override
	public String getName() {
		return "mp heal";
	}

	@Override
	public synchronized void cancel() throws IllegalStateException {
		super.cancel();
		hashSet.remove(p);
	}
}

