package lbn.quest.quest;

import java.text.MessageFormat;

import lbn.common.event.quest.StartQuestEvent;
import lbn.item.ItemInterface;
import lbn.item.ItemManager;
import lbn.quest.QuestAnnouncement;
import lbn.quest.questData.PlayerQuestSession;
import lbn.util.ItemStackUtil;
import lbn.util.QuestUtil;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

public class TakeItemQuest extends TouchVillagerQuest{

	String itemId;
	String npcName;

	protected TakeItemQuest(String id, String itemId, String npcName) {
		super(id, npcName, new String[0]);
	}

	@Override
	public void onTouchVillager(Player p, Entity entity, PlayerQuestSession session) {
		//NPCが同じかチェック
		String name = entity.getCustomName();
		if (!name.equalsIgnoreCase(getTargetVillagerName())) {
			return;
		}

		//アイテムを持っているか確認
		PlayerInventory inventory = p.getInventory();
		if (!inventory.contains(getNeedItem().getItem())) {
			return;
		}
		//アイテムを削除する
		inventory.remove(getNeedItem().getItem());

		QuestUtil.sendMessageByVillager(p, new String[]{"届けてくれてありがとう"});

		session.setQuestData(this, 1);
	}

	@Override
	public QuestType getQuestType() {
		return QuestType.TAKE_ITEM_QUEST;
	}

	protected ItemInterface getNeedItem() {
		return ItemManager.getCustomItemById(itemId);
	}

	@Override
	public String getComplateCondition() {
		ItemInterface needItem = getNeedItem();
		if (needItem == null) {
			return MessageFormat.format("{0}のところへアイテム[{1}]を持っていく", npcName, itemId);
		} else {
			return MessageFormat.format("{0}のところへアイテム[{1}]を持っていく", npcName, needItem.getItemName());
		}
	}

	public static TakeItemQuest getInstance(String id, String deta1, String deta2) {
		return new TakeItemQuest(id, deta1, deta2);
	}

	@Override
	public void onStartQuestEvent(StartQuestEvent e) {
		super.onStartQuestEvent(e);
		ItemInterface needItem = getNeedItem();
		//アイテムが存在しなければ無視する
		if (needItem == null) {
			QuestAnnouncement.sendQuestError(e.getPlayer(), "現在このクエストを受けることが出来ません");
			e.setCancelled(true);
			return;
		}

		if (!ItemStackUtil.canGiveItem(e.getPlayer(), needItem.getItem())) {
			QuestAnnouncement.sendQuestError(e.getPlayer(), "インベントがいっぱいです。空きを作ってください。");
			e.setCancelled(true);
			return;
		}

		e.getPlayer().getInventory().addItem(needItem.getItem());
	}
}
