package lbn.quest.quest;

import java.util.HashSet;

import lbn.common.event.quest.ComplateQuestEvent;
import lbn.common.event.quest.DestructionQuestEvent;
import lbn.common.event.quest.StartQuestEvent;
import lbn.mob.AbstractMob;
import lbn.mob.LastDamageManager;
import lbn.mob.MobHolder;
import lbn.quest.questData.PlayerQuestSession;
import lbn.quest.questData.PlayerQuestSessionManager;
import lbn.util.JavaUtil;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;

public class KillMobQuest extends AbstractQuest{
	static HashSet<String> mobNameList = new HashSet<>();

	private KillMobQuest(String id, String targetMobName, int needCount) {
		super(id);
		this.targetMobName = targetMobName;
		this.needCount = needCount;
		init();
	}

	public static KillMobQuest getInstance(String id, String data1, String data2) {
		if (data1 == null) {
			return null;
		}

		//討伐数チェック
		int count = JavaUtil.getInt(data2, -1);
		if (count <= 0) {
			return null;
		}
		return new KillMobQuest(id, data1, count);
	}

	protected void init() {
		mobNameList.add(targetMobName);
	}

	public static boolean containsTargetMob(AbstractMob<?> mob) {
		return mobNameList.contains(mob.getName());
	}

	public void onDeath(EntityDeathEvent e, PlayerQuestSession session) {
		LivingEntity entity = e.getEntity();
		if (MobHolder.getMob(e).equals(getTargetMob())) {
			//最後に攻撃したプレイヤーがいない場合は無視する
			Player p = LastDamageManager.getLastDamagePlayer(entity);
			if (p == null) {
				return;
			}
			//カウントを加算する
			int data = session.getQuestData(this);

			//必要キル数未満の場合は加算する
			if (data < getNeedCount()) {
				session.setQuestData(this, data + 1);
				sendProgressMessage(p, getNeedCount(), data + 1);
			}
		}
	}

	@Override
	public String getCurrentInfo(Player p) {
		PlayerQuestSession questSession = PlayerQuestSessionManager.getQuestSession(p);
		if (!questSession.isDoing(this)) {
			return "";
		}

		int data = questSession.getQuestData(this);
		return "達成度(" + data + "/" + getNeedCount() + ")";
	}

	@Override
	public void onComplate(ComplateQuestEvent e) {
		Player player = e.getPlayer();
		sendQuestComplateMessage(player);
	}

	String targetMobName;

	protected AbstractMob<?> getTargetMob() {
		return MobHolder.getMob(targetMobName);
	}

	int needCount = 0;
	protected int getNeedCount() {
		return needCount;
	}

	@Override
	public void onStart(StartQuestEvent e) {

	}

	@Override
	public void onDistruction(DestructionQuestEvent e) {

	}

	@Override
	public QuestType getQuestType() {
		return QuestType.KILL_MOB_QUEST;
	}

	@Override
	public boolean isComplate(int data) {
		return data >= needCount;
	}

}
