package lbn.dungeon.contents;

import lbn.chest.CustomChestManager;
import lbn.dungeon.contents.chest.AlphaWhiteListChest;
import lbn.dungeon.contents.item.click.JumpBoost;
import lbn.dungeon.contents.item.click.magicbook.AbstractMagicBook;
import lbn.dungeon.contents.item.key.impl.AllDriedKey;
import lbn.dungeon.contents.item.key.impl.Fragment;
import lbn.dungeon.contents.item.key.impl.KalgrusFinishKey;
import lbn.dungeon.contents.item.key.impl.KalgrusKey;
import lbn.dungeon.contents.item.key.impl.MaratKey;
import lbn.dungeon.contents.item.key.impl.NativeUnderground;
import lbn.dungeon.contents.item.key.impl.SuikaCastle;
import lbn.dungeon.contents.item.magic.normalItems.NormalMagicItem;
import lbn.dungeon.contents.item.other.strengthBase.StrengthBaseJade;
import lbn.dungeon.contents.item.questItem.Serum;
import lbn.dungeon.contents.item.setItem.crystal.SetItemHealthCrystal;
import lbn.dungeon.contents.item.shootbow.BowOfExplosion;
import lbn.dungeon.contents.item.shootbow.DebugBow;
import lbn.dungeon.contents.item.shootbow.LaserBow;
import lbn.dungeon.contents.slotStone.OneMobDamageUpSlot;
import lbn.dungeon.contents.slotStone.level1.FireAspect1;
import lbn.dungeon.contents.slotStone.level1.KillEffectSlotStone1;
import lbn.dungeon.contents.slotStone.level1.KillEffectSlotStone2;
import lbn.dungeon.contents.slotStone.level2.FireAspect2;
import lbn.dungeon.contents.slotStone.level3.FireAspect3;
import lbn.dungeon.contents.slotStone.level3.HealSlotStone;
import lbn.dungeon.contents.slotStone.level4.CombatLightningStone;
import lbn.dungeon.contents.slotStone.level4.HealSlotStone2;
import lbn.dungeon.contents.slotStone.other.KillEffectTutorial;
import lbn.dungeon.contents.slotStone.other.MagicHealMagicStone;
import lbn.dungeon.contents.slotStone.other.MagicStoneJade;
import lbn.item.ItemManager;
import lbn.item.setItem.SetItemManager;

public class ItemRegister {
	public static void registItem() {
		SetItemManager.regist(new SetItemHealthCrystal());

		ItemManager.registItem(SuikaCastle.getAllKey());
		ItemManager.registItem(AllDriedKey.getAllKey());
		ItemManager.registItem(Fragment.getAllFragment());
		ItemManager.registItem(new KalgrusKey());
		ItemManager.registItem(new KalgrusFinishKey());
		ItemManager.registItem(new MaratKey());
		ItemManager.registItem(new NativeUnderground());

		// rightClick
		ItemManager.registItem(AbstractMagicBook.getAllItem());
		ItemManager.registItem(new JumpBoost());

		// questitem
		ItemManager.registItem(new Serum());

		// 弓
		ItemManager.registItem(new LaserBow());
		ItemManager.registItem(new BowOfExplosion());
		ItemManager.registItem(new DebugBow());

		// 魔法
		ItemManager.registItem(NormalMagicItem.getAllItem());

		// SLOT
		ItemManager.registItem(new KillEffectTutorial());
		ItemManager.registItem(new KillEffectSlotStone1());
		ItemManager.registItem(new KillEffectSlotStone2());
		ItemManager.registItem(new CombatLightningStone());
		ItemManager.registItem(new HealSlotStone());
		ItemManager.registItem(new HealSlotStone2());
		ItemManager.registItem(new FireAspect1());
		ItemManager.registItem(new FireAspect2());
		ItemManager.registItem(new FireAspect3());
		ItemManager.registItem(new MagicStoneJade());
		ItemManager.registItem(MagicHealMagicStone.getAllItem());
		OneMobDamageUpSlot.regist();

		// OTHER
		ItemManager.registItem(new StrengthBaseJade());

		// chest
		AlphaWhiteListChest alphaWhiteListChest = new AlphaWhiteListChest();
		CustomChestManager.registChest(alphaWhiteListChest.getChestLocation(), alphaWhiteListChest);
	}
}
