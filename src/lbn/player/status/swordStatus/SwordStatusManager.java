package lbn.player.status.swordStatus;

import lbn.player.status.AbstractNormalStatusManager;
import lbn.player.status.IStatusDetail;
import lbn.player.status.bowStatus.BowStatusManager;
import lbn.player.status.magicStatus.MagicStatusManager;

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
