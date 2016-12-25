package lbn.quest;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import lbn.common.event.quest.ComplateQuestEvent;
import lbn.common.event.quest.DestructionQuestEvent;
import lbn.common.event.quest.QuestEvent;
import lbn.common.event.quest.StartQuestEvent;
import lbn.dungeoncore.Main;
import lbn.util.Message;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.collect.HashMultimap;


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
//			new LbnRuntimeException("quest id is deplicated!! : " + q.getId()).printStackTrace();;
			return;
		}
		allQuestByName.put(ChatColor.stripColor(q.getName()).toUpperCase(), q);
		allQuestById.put(ChatColor.stripColor(q.getId()).toUpperCase(), q);
	}

	/**
	 * 指定したクエストが進行中ならTRUE
	 * @param q
	 * @param p
	 * @return
	 */
	public static boolean isDoingQuest(Quest q, Player p) {
		return doingQuestList.containsEntry(p.getUniqueId(), q);
	}

	/**
	 * 指定したクエストが一回でも完了してるならTRUE
	 * @param q
	 * @param p
	 * @return
	 */
	public static boolean isComplateQuest(Quest q, Player p) {
		return complateQuestList.containsEntry(p.getUniqueId(), q);
	}

	public static Set<Quest> getDoingQuest(Player p) {
		return new HashSet<Quest>(doingQuestList.get(p.getUniqueId()));
	}

	public static HashMultimap<UUID, Quest> complateQuestList = HashMultimap.create();

	public static Set<Quest> getComplateQuest(Player p) {
		UUID uniqueId = p.getUniqueId();
		if (complateQuestList.containsKey(complateQuestList)) {
			return Collections.emptySet();
		}
		return complateQuestList.get(uniqueId);
	}

	public static HashMultimap<UUID, Quest> doingQuestList = HashMultimap.create();

	public static void remove(Player p) {
		complateQuestList.removeAll(p.getUniqueId());
		complateQuestList.removeAll(p.getUniqueId());
	}

	/**
	 * クエストを開始する
	 * @param q
	 * @param p
	 * @return
	 */
	public static boolean startQuest(Quest q, Player p, boolean force) {
		//現在実行中
		if (doingQuestList.containsEntry(p.getUniqueId(), q)) {
			Message.sendMessage(p, Message.QUEST_DOING_NOW, q.getName());
			return false;
		}

		if (!force) {
			//クエスト上限
			if (doingQuestList.get(p.getUniqueId()).size() >= getMaxQuestCount(p)) {
				Message.sendMessage(p, Message.QUEST_OVER_COUNT);
				return false;
			}
		}

		StartQuestEvent event = new StartQuestEvent(p, q);
		Bukkit.getServer().getPluginManager().callEvent(event);

		if (event.isCancelled()) {
			return false;
		}

		//クエストを開始する
		Message.sendTellraw(p, Message.QUEST_START_MESSAGE, q.getName());

		//実行中にする
		doingQuestList.put(p.getUniqueId(), q);
		//実行可能クエストから削除する
		canStartQuestByTellrowMap.remove(q, p);
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

		doingQuestList.remove(p.getUniqueId(), q);
		complateQuestList.put(p.getUniqueId(), q);

		QuestEvent event = new ComplateQuestEvent(p, q);
		Bukkit.getServer().getPluginManager().callEvent(event);
	}

	/**
	 * クエストを破棄する
	 * @param q
	 * @param p
	 */
	public static void removeQuest(Quest q, Player p) {
		if (q.isMainQuest()) {
			p.sendMessage(ChatColor.RED + "メインクエストは破棄できません。");
			return;
		}
		QuestEvent event = new DestructionQuestEvent(p, q);
		Bukkit.getServer().getPluginManager().callEvent(event);

		doingQuestList.remove(p.getUniqueId(), q);

		Message.sendMessage(p, Message.QUEST_REMOVE_MESSAGE, q.getName());
	}

	static HashMultimap<Quest, Player> canStartQuestByTellrowMap = HashMultimap.create();

	public static String getStartTellrowCommand(final Player p, final Quest quest) {
		canStartQuestByTellrowMap.put(quest, p);
		new BukkitRunnable() {
			@Override
			public void run() {
				canStartQuestByTellrowMap.remove(quest, p);
			}
		}.runTaskLater(Main.plugin, 20 * 10);
		return StringUtils.join(new Object[]{"tellraw ", p.getName(), " {\"text\":\"クエスト受諾(10秒以内にクリック)\",\"bold\":false,\"underlined\":true,\"color\":\"aqua\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/quest start ", quest.getId(),"\"}}"});
	}


	public static void load() {

	}

	public static void save() {

	}

	public static boolean canStartQuestByTellrow(Quest q, Player p) {
		return canStartQuestByTellrowMap.containsEntry(q, p);
	}
}
