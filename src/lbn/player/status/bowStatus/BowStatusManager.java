package lbn.player.status.bowStatus;

import lbn.player.status.AbstractNormalStatusManager;
import lbn.player.status.IStatusDetail;
import lbn.player.status.magicStatus.MagicStatusManager;
import lbn.player.status.swordStatus.SwordStatusManager;

public class BowStatusManager extends AbstractNormalStatusManager	{

	private static BowStatusManager manager = null;

	public static BowStatusManager getInstance() {
		if (manager == null) {
			manager = new BowStatusManager();
		}
		return manager;
	}

	private BowStatusManager() {
	}

	@Override
	public String getManagerName() {
		return "弓レベル";
	}

	static BowStatusDetail detail = new BowStatusDetail();

	@Override
	public IStatusDetail getDetail() {
		return detail;
	}

	AbstractNormalStatusManager[] otherList = null;

	@Override
	protected AbstractNormalStatusManager[] getOtherMamager() {
		if (otherList == null) {
			otherList = new AbstractNormalStatusManager[]{SwordStatusManager.getInstance(), MagicStatusManager.getInstance()};
		}
		return otherList;
	}

}
