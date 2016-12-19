package main.item.repair.repairTable;

import java.util.ArrayList;

import org.bukkit.inventory.ItemStack;

import main.item.attackitem.AttackItemStack;
import main.item.slot.SlotInterface;
import main.item.slot.SlotType;
import main.item.slot.slot.EmptySlot;
import main.item.slot.slot.UnavailableSlot;
import main.lbn.LbnRuntimeException;

public class RepairSetOperator {
	protected RepairSetOperator(AttackItemStack attackItem,
			SlotInterface magicStone) {
		this.attackItem = attackItem;
		this.magicStone = magicStone;
	}

	AttackItemStack attackItem;
	SlotInterface magicStone;

	public void setSlot() {
		SlotType slotType = magicStone.getSlotType();
		if (slotType == SlotType.NORMAL) {
			//空のスロットを削除し、魔法石をセットする
			attackItem.removeSlot(new EmptySlot());
			attackItem.addSlot(magicStone);
		} else if (slotType == SlotType.ADD_EMPTY) {
			//空のスロットを追加
			attackItem.addSlot(new EmptySlot());
		} else if (slotType == SlotType.REMOVE_UNAVAILABLE) {
			//使用不可のスロットを削除する
			attackItem.removeSlot(new UnavailableSlot());
		}
		attackItem.updateItem();
	}

	public String check() {
		ArrayList<SlotInterface> useSlot = attackItem.getUseSlot();

		int emptyNum = 0;
		int unavailableNum = 0;
		for (SlotInterface slotInterface : useSlot) {
			//空のスロットを調べる
			if (slotInterface.isSame(new EmptySlot())) {
				emptyNum++;
			//使用不可のスロットを調べる
			} else if (slotInterface.isSame(new UnavailableSlot())) {
				unavailableNum++;
			}
		}

		 SlotType type = magicStone.getSlotType();

		if (type == SlotType.NORMAL) {
			//空のスロットがないなら何もしない
			if (emptyNum <= 0) {
				return "空きスロットが存在しません。";
			}
			//同じ魔法石は付けれない
			if (useSlot.contains(magicStone)) {
				return "同じ魔法石はセットできません。";
			}
		} else if (type == SlotType.ADD_EMPTY) {
			if (attackItem.getItemInterface().getMaxSlotCount() - useSlot.size() <= 0) {
				return "これ以上、このアイテムにスロットを追加できません。";
			}
		} else if (type== SlotType.REMOVE_UNAVAILABLE) {
			if (unavailableNum <= 0) {
				return "使用不可のスロットが存在しません。";
			}
		} else {
			new LbnRuntimeException("invalid magic stone").printStackTrace();
			return "その魔法石は使えません";
		}
		return null;
	}

	public int getSuccessRate() {
		return ((int)magicStone.getLevel().getSucessPer());
	}

	public void rollback(ItemStack cursor) {
		AttackItemStack instance = AttackItemStack.getInstance(cursor);
		//nullの可能性がある
		if (instance == null) {
			new LbnRuntimeException("magic stone is null").printStackTrace();;
			return;
		}

		if (magicStone.getSlotType() == SlotType.NORMAL) {
			instance.removeSlot(magicStone);
			instance.addSlot(new UnavailableSlot());
		} else if (magicStone.getSlotType() == SlotType.ADD_EMPTY) {
			instance.removeSlot(new EmptySlot());
			instance.addSlot(new UnavailableSlot());
		} else if (magicStone.getSlotType() == SlotType.REMOVE_UNAVAILABLE) {
			instance.addSlot(new UnavailableSlot());
		}
		instance.updateItem();
	}

	public String getScuessComment() {
		 SlotType type = magicStone.getSlotType();

			if (type == SlotType.NORMAL) {
				return "魔法石の装着に成功しました。";
			} else if (type == SlotType.ADD_EMPTY) {
				return "空のスロットの追加に成功しました。";
			} else if (type== SlotType.REMOVE_UNAVAILABLE) {
				return "使用不可のスロットを取り除きました。";
			}
			return "成功しました";
	}

	public String getFailureComment() {
		SlotType type = magicStone.getSlotType();

		if (type == SlotType.NORMAL) {
			return "魔法石の装着に成功しました。";
		} else if (type == SlotType.ADD_EMPTY) {
			return "空のスロットの追加に失敗しました。";
		} else if (type== SlotType.REMOVE_UNAVAILABLE) {
			return "使用不可のスロットを取り除くのに失敗しました。";
		}
		return "失敗しました。";
	}

}
