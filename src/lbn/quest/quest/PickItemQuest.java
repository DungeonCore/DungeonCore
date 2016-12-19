package lbn.quest.quest;

import java.util.Set;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.HashMultimap;

import lbn.common.event.quest.ComplateQuestEvent;
import lbn.item.ItemInterface;
import lbn.quest.QuestData;
import lbn.quest.QuestManager;

public abstract class PickItemQuest extends AbstractVillagerQuest{
	static HashMultimap<ItemInterface, PickItemQuest> needItemMap = HashMultimap.create();

	public static Set<PickItemQuest> getQuest(ItemInterface item) {
		return needItemMap.get(item);
	}

	public PickItemQuest() {
		init();
	}

	protected void init() {
		needItemMap.put(getNeedItem(), this);
	}

	abstract public ItemInterface getNeedItem();

	abstract public int needCount();

	public void onPickUp(PlayerPickupItemEvent e) {
		ItemStack itemStack = e.getItem().getItemStack();
		//実行中でないなら何もしない
		if (!QuestManager.isDoingQuest(this, e.getPlayer())) {
			e.setCancelled(true);
			return;
		}

		if (itemStack == null || !getNeedItem().isThisItem(itemStack) || itemStack.getAmount() == 0) {
			return;
		}

		int nowCount = QuestData.getData(this, e.getPlayer()) + itemStack.getAmount();
		//クエスト完了の時
		if (nowCount >= needCount()) {
			onPickItem(e.getPlayer(), needCount());
			QuestManager.complateQuest(this, e.getPlayer());
			QuestData.remove(this, e.getPlayer());
		} else {
			QuestData.setData(this, e.getPlayer(), nowCount);
			onPickItem(e.getPlayer(), nowCount);
		}

		if (nowCount <= needCount()) {
			itemStack.setAmount(0);
			e.getItem().setItemStack(itemStack);
			e.getItem().remove();
			e.setCancelled(true);
		} else if (nowCount > needCount()) {
			itemStack.setAmount(nowCount - needCount());
			e.getItem().setItemStack(itemStack);
			e.setCancelled(true);
		}
	}

	protected void onPickItem(Player player, int nowCount) {
		sendProgressMessage(player, needCount(), nowCount);
		player.playSound(player.getLocation(), Sound.ITEM_PICKUP, 1, 1);
	}

	@Override
	public String getCurrentInfo(Player p) {
		int data = QuestData.getData(this, p);
		return "達成度(" + data + "/" + needCount() + ")";
	}

	@Override
	public void onComplate(ComplateQuestEvent e) {
		Player player = e.getPlayer();
		sendQuestClearMessage(player);
	}

}
