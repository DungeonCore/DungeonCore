package lbn.player.status.magicStatus;

import lbn.player.status.AbstractNormalStatusManager;
import lbn.player.status.IStatusDetail;
import lbn.player.status.bowStatus.BowStatusManager;
import lbn.player.status.swordStatus.SwordStatusManager;

public class MagicStatusManager extends AbstractNormalStatusManager	{

	private static MagicStatusManager manager = null;

	public static MagicStatusManager getInstance() {
		if (manager == null) {
			manager = new MagicStatusManager();
		}
		return manager;
	}

	private MagicStatusManager() {
	}

	@Override
	public String getManagerName() {
		return "魔術レベル";
	}

	static MagicStatusDetail detail = new MagicStatusDetail();

	@Override
	public IStatusDetail getDetail() {
		return detail;
	}


	AbstractNormalStatusManager[] otherList = null;

	@Override
	protected AbstractNormalStatusManager[] getOtherMamager() {
		if (otherList == null) {
			otherList = new AbstractNormalStatusManager[]{SwordStatusManager.getInstance(), BowStatusManager.getInstance()};
		}
		return otherList;
	}
}
