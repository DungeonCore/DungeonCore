package main.player.status.swordStatus;

import main.player.status.AbstractNormalStatusManager;
import main.player.status.IStatusDetail;
import main.player.status.bowStatus.BowStatusManager;
import main.player.status.magicStatus.MagicStatusManager;

public class SwordStatusManager extends AbstractNormalStatusManager	{

	private static SwordStatusManager manager = null;

	public static SwordStatusManager getInstance() {
		if (manager == null) {
			manager = new SwordStatusManager();
		}
		return manager;
	}

	private SwordStatusManager() {
	}

	@Override
	public String getManagerName() {
		return "剣レベル";
	}

	static SwordStatusDetail detail = new SwordStatusDetail();

	@Override
	public IStatusDetail getDetail() {
		return detail;
	}

	AbstractNormalStatusManager[] otherList = null;

	@Override
	protected AbstractNormalStatusManager[] getOtherMamager() {
		if (otherList == null) {
			otherList = new AbstractNormalStatusManager[]{BowStatusManager.getInstance(), MagicStatusManager.getInstance()};
		}
		return otherList;
	}

}
