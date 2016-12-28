package lbn.quest;

import java.util.HashMap;
import java.util.Set;

import lbn.common.event.quest.ComplateQuestEvent;
import lbn.common.event.quest.DestructionQuestEvent;
import lbn.common.event.quest.QuestEvent;
import lbn.common.event.quest.StartQuestEvent;
import lbn.player.status.mainStatus.MainStatusManager;
import lbn.quest.questData.PlayerQuestSession;
import lbn.quest.questData.PlayerQuestSessionManager;
import lbn.util.JavaUtil;
import lbn.util.Message;
import lbn.util.TitleSender;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;


public class QuestManager {
	private static HashMap<String, Quest> allQuestByName = new HashMap<String, Quest>();

	private static HashMap<String, Quest> allQuestById = new HashMap<String, Quest>();


	public static Quest getQuestByName(String name) {
		return allQuestByName.get(ChatColor.stripColor(name).toUpperCase());
	}

	public static Quest getQuestById(String id) {
		if (id == null) {
			return null;
		}
		return allQuestById.get(ChatColor.stripColor(id).toUpperCase());
	}

	public static Set<String> getQuestName() {
		return allQuestByName.keySet();
	}

	public static Set<String> getQuestId() {
		return allQuestById.keySet();
	}

	public static void registQuest(Quest q) {
		if (allQuestById.containsKey(q.getId())) {
//			new LbnRuntimeException("quest id is deplicated!! : " + q.getId()).printStackTrace();
			return;
		}
		allQuestByName.put(ChatColor.stripColor(q.getName()).toUpperCase(), q);
		allQuestById.put(ChatColor.stripColor(q.getId()).toUpperCase(), q);
	}

	public static QuestStartStatus getStartQuestStatus(Quest q, Player p) {
		PlayerQuestSession session = PlayerQuestSessionManager.getQuestSession(p);

		//現在実行中
		if (session.isDoing(q)) {
			Message.sendMessage(p, Message.QUEST_DOING_NOW, q.getName());
			return QuestStartStatus.DOING_NOW;
		}

		//クエスト上限
		if (session.getNowQuestSize() >= getMaxQuestCount(p)) {
			Message.sendMessage(p, Message.QUEST_OVER_COUNT);
			return QuestStartStatus.RECEIVE_COUNT_MAXIMUM;
		}

		//クエストのクールタイム
		long complateDate = session.getComplateDate(q);
		if (complateDate + q.getCoolTimeSecound() * 60 * 1000 >= JavaUtil.getJapanTimeInMillis()) {
			Message.sendMessage(p, "このクエストはまだ受けられません(時間)");
			return QuestStartStatus.REMAIND_COOL_TIME;
		}

		//利用可能レベル
		int mainLevel = q.getAvailableMainLevel();
		if (mainLevel > MainStatusManager.getInstance().getLevel(p)) {
			return QuestStartStatus.LACK_AVAILAVLE_MAIN_LEVEL;
		}

		//重複不可の時
		if (!q.isStartOverlap()) {
			//完了回数がでないときは利用不可とする
			if (session.getComplateCount(q) != 0) {
				return QuestStartStatus.CANNT_OVERLAP;
			}
		}

		//前提クエストの確認
		Set<Quest> beforeQuest = q.getBeforeQuest();
		for (Quest quest : beforeQuest) {
			if (!session.isComplate(quest)) {
				Message.sendMessage(p, "このクエストはまだ受けられません(前提クエスト)");
				return QuestStartStatus.LACK_BEFORE_QUEST;
			}
		}

		return QuestStartStatus.CAN_START;
	}

	/**
	 * クエストを開始する
	 * @param q
	 * @param p
	 * @return
	 */
	public static boolean startQuest(Quest q, Player p, boolean force, QuestStartStatus status) {
		PlayerQuestSession session = PlayerQuestSessionManager.getQuestSession(p);

		if (!status.canStart()) {
			//強制実行でないときは実行しない
			if (!force) {
				return false;
			} else {
				//強制実行だがそれでも実行出来ないときは実行しない
				if (!status.canStartIfForce()) {
					return false;
				}
			}
		}

		StartQuestEvent event = new StartQuestEvent(p, q);
		Bukkit.getServer().getPluginManager().callEvent(event);

		if (event.isCancelled()) {
			return false;
		}

		//title表示
		if (q.isShowTitle()) {
			TitleSender.getInstance().sendTitle(p, Message.getMessage(ChatColor.GOLD + "[Quest] クエスト開始"), ChatColor.GOLD + q.getName());
		}

		//実行中にする
		session.startQuest(q);
		return true;
	}

	private static int getMaxQuestCount(Player p) {
		return 25;
	}

	/**
	 * クエストを完了する
	 * @param q
	 * @param p
	 */
	public static void complateQuest(Quest q, Player p) {
		if (!q.canFinish(p)) {
			return;
		}

		ComplateQuestEvent event = new ComplateQuestEvent(p, q);
		Bukkit.getServer().getPluginManager().callEvent(event);
		if (!event.isCancelled()) {
			return;
		}

		q.giveRewardItem(p);

		//報酬を渡す
		PlayerQuestSession questSession = PlayerQuestSessionManager.getQuestSession(p);
		questSession.complateQuest(q);

		//次のクエストを開始する
		Quest autoExecuteNextQuest = q.getAutoExecuteNextQuest();
		if (autoExecuteNextQuest != null) {
			startQuest(autoExecuteNextQuest, p, true, getStartQuestStatus(autoExecuteNextQuest, p));
		}

	}

	/**
	 * クエストを破棄する
	 * @param q
	 * @param p
	 */
	public static boolean removeQuest(Quest q, Player p) {
		if (q.isMainQuest()) {
			p.sendMessage(ChatColor.RED + "メインクエストは破棄できません。");
			return false;
		}

		if (q.canDestory()) {
			p.sendMessage(ChatColor.RED + "このクエストは破棄できません。");
			return false;
		}
		QuestEvent event = new DestructionQuestEvent(p, q);
		Bukkit.getServer().getPluginManager().callEvent(event);

		PlayerQuestSession session = PlayerQuestSessionManager.getQuestSession(p);
		session.removeQuest(q);

		Message.sendMessage(p, Message.QUEST_REMOVE_MESSAGE, q.getName());
		return true;
	}

	//Tellrow使わないかもしれないので一旦コメントアウト
//	static HashMultimap<Quest, Player> canStartQuestByTellrowMap = HashMultimap.create();
//	public static String getStartTellrowCommand(final Player p, final Quest quest) {
//		canStartQuestByTellrowMap.put(quest, p);
//		new BukkitRunnable() {
//			@Override
//			public void run() {
//				canStartQuestByTellrowMap.remove(quest, p);
//			}
//		}.runTaskLater(Main.plugin, 20 * 10);
//		return StringUtils.join(new Object[]{"tellraw ", p.getName(), " {\"text\":\"クエスト受諾(10秒以内にクリック)\",\"bold\":false,\"underlined\":true,\"color\":\"aqua\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/quest start ", quest.getId(),"\"}}"});
//	}
//	public static boolean canStartQuestByTellrow(Quest q, Player p) {
//		return canStartQuestByTellrowMap.containsEntry(q, p);
//	}

	/**
	 * クエストを受けることが出来るかどうかを判断するためにのENUM
	 * @author kensuke
	 *
	 */
	public static enum QuestStartStatus {
		CAN_START(true, true),
		DOING_NOW("同じクエストを同時に受けることはできません", true, false),
		RECEIVE_COUNT_MAXIMUM("クエスト数が上限に達しました。どれかを破棄してください。", false, true),
		REMAIND_COOL_TIME("現在このクエストを受注できません(時間制限)", false, true),
		LACK_AVAILAVLE_MAIN_LEVEL("メインレベルが足りません", false, true),
		CANNT_OVERLAP("このクエストを再度受けることは出来ません", false, true),
		LACK_BEFORE_QUEST("前提クエストを完了していません", false, true);

		String errorMessage = null;
		boolean canStart;
		boolean canStartIfForce;

		private QuestStartStatus(boolean canStart, boolean canStartIfForce) {
			this.canStart = canStart;
			this.canStartIfForce = canStartIfForce;
		}

		private QuestStartStatus(String errorMessage, boolean canStart, boolean canStartIfForce) {
			this.canStart = canStart;
			this.errorMessage = errorMessage;
			this.canStartIfForce = canStartIfForce;
		}

		public boolean canStart() {
			return canStart;
		}

		public boolean canStartIfForce() {
			return canStartIfForce;
		}

		public String canntMessage() {
			return errorMessage;
		}
	}
}

