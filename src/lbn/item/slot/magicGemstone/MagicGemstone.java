package lbn.item.slot.magicGemstone;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import lbn.item.AbstractItem;
import lbn.item.SlotManager;
import lbn.item.itemInterface.RightClickItemable;
import lbn.item.slot.AbstractSlot;
import lbn.item.slot.SlotInterface;
import lbn.item.slot.SlotLevel;
import lbn.util.ItemStackUtil;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public abstract class MagicGemstone extends AbstractItem implements RightClickItemable{

	@Override
	public String getItemName() {
		return "魔法石の原石";
	}

	@Override
	public String getId() {
		return "m_gem_" + getSlotLevel();
	}

	abstract public SlotLevel getSlotLevel();

	@Override
	public int getBuyPrice(ItemStack item) {
		return getSlotLevel().getPrice();
	}

	@Override
	public String[] getDetail() {
		return new String[]{"かまどで精錬することで", MessageFormat.format("レア度：{0}の魔法石になります。", getSlotLevel().getStar())};
	}

	Random random = new Random();

	@Override
	public void excuteOnRightClick(PlayerInteractEvent e) {
		Player player = e.getPlayer();
		if (!player.isOp()) {
			return;
		}

		AbstractSlot slot = getRandomSlot();
		if (slot != null) {
			player.getInventory().addItem(slot.getItem());
			ItemStackUtil.consumeItemInHand(player);
		}
	}

	public AbstractSlot getRandomSlot() {
		AbstractSlot slot = null;
		Collection<SlotInterface> slotListByLevel = SlotManager.getSlotListByLevel(getSlotLevel());
		if (slotListByLevel.isEmpty()) {
			return null;
		}

		ArrayList<AbstractSlot> arrayList = new ArrayList<AbstractSlot>();
		for (SlotInterface slotInterface : slotListByLevel) {
			if (slotInterface instanceof AbstractSlot) {
				arrayList.add((AbstractSlot) slotInterface);
			}
		}
		if (arrayList.isEmpty()) {
			return null;
		}

		int nextInt = random.nextInt(arrayList.size());
		slot = arrayList.get(nextInt);
		return slot;
	}
}
