package lbn.quest.abstractQuest;

import java.text.MessageFormat;
import java.util.HashSet;

import lbn.mob.AbstractMob;
import lbn.mob.LastDamageManager;
import lbn.mob.MobHolder;
import lbn.quest.QuestProcessingStatus;
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
		mobNameList.add(targetMobName);
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

	public static boolean containsTargetMob(AbstractMob<?> mob) {
		return mobNameList.contains(mob.getName());
	}

	public void onDeath(EntityDeathEvent e, PlayerQuestSession session, AbstractMob<?> killedMob) {
		LivingEntity entity = e.getEntity();

		if (allowAnyMob() || killedMob.getName().equals(targetMobName)) {
			//最後に攻撃したプレイヤーがいない場合は無視する
			Player p = LastDamageManager.getLastDamagePlayer(entity);
			if (p == null) {
				return;
			}
			if (session.getProcessingStatus(this) == QuestProcessingStatus.PROCESSING) {
				//カウントを加算する
				int data = session.getQuestData(this);
				session.setQuestData(this, data + 1);
				//メッセージを出力
				sendProgressMessage(p, getNeedCount(), data + 1);
			}
		}
	}

	/**
	 * どんなモンスターでもカウントを許可するならTRUE
	 * @return
	 */
	protected boolean allowAnyMob() {
		return targetMobName == null || targetMobName.isEmpty();
	}

	@Override
	public String getCurrentInfo(Player p) {
		PlayerQuestSession questSession = PlayerQuestSessionManager.getQuestSession(p);
		QuestProcessingStatus status = questSession.getProcessingStatus(this);
		if (status == QuestProcessingStatus.PROCESSING || status == QuestProcessingStatus.PROCESS_END) {
			int data = questSession.getQuestData(this);
			return "達成度(" + data + "/" + getNeedCount() + ")";
		}
		return "";
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
	public QuestType getQuestType() {
		return QuestType.KILL_MOB_QUEST;
	}

	@Override
	public boolean isComplate(int data) {
		return data >= needCount;
	}

	@Override
	public String getComplateCondition() {
		return MessageFormat.format("モンスター[{0}]を{1}体倒す", targetMobName, needCount);
	}
}
