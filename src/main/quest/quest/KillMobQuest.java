package main.quest.quest;

import java.util.HashSet;

import main.common.event.quest.ComplateQuestEvent;
import main.mob.AbstractMob;
import main.mob.LastDamageManager;
import main.mob.MobHolder;
import main.quest.Quest;
import main.quest.QuestData;
import main.quest.QuestManager;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;

public abstract class KillMobQuest extends AbstractVillagerQuest{
	static HashSet<AbstractMob<?>> mobList = new HashSet<AbstractMob<?>>();

	static HashSet<String> idList = new HashSet<>();

	public KillMobQuest() {
		init();
	}

	protected void init() {
		mobList.add(getTargetMob());
		idList.add(getId());
	}

	public static boolean containsTargetMob(AbstractMob<?> mob) {
		return mobList.contains(mob);
	}

	public static boolean isKillMobQuest(Quest q) {
		return idList.contains(q.getId());
	}

	public void onDeath(EntityDeathEvent e) {
		LivingEntity entity = e.getEntity();
		if (MobHolder.getMob(e).equals(getTargetMob())) {
			//最後に攻撃したプレイヤーがいない場合は無視する
			Player p = LastDamageManager.getLastDamagePlayer(entity);
			if (p == null) {
				return;
			}
			//カウントを加算する
			int data = QuestData.getData(this, p);

			//必要キル数未満の場合は加算する
			if (data < getNeedCount()) {
				QuestData.setData(this, p, data + 1);
				sendProgressMessage(p, getNeedCount(), data + 1);
			}

			//クエスト完了
			if (data + 1 >= getNeedCount()) {
				QuestManager.complateQuest(this, p);
				QuestData.remove(this, p);
			}
		}
	}

	@Override
	public String getCurrentInfo(Player p) {
		if (!QuestManager.isDoingQuest(this, p)) {
			return "";
		}

		int data = QuestData.getData(this, p);
		return "達成度(" + data + "/" + getNeedCount() + ")";
	}

	@Override
	public void onComplate(ComplateQuestEvent e) {
		Player player = e.getPlayer();
		sendQuestClearMessage(player);
	}

	abstract protected AbstractMob<?> getTargetMob();

	abstract protected int getNeedCount();

}
