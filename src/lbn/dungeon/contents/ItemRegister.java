package lbn.dungeon.contents;

import lbn.chest.CustomChestManager;
import lbn.dungeon.contents.chest.AlphaWhiteListChest;
import lbn.dungeon.contents.item.armor.BeneCommonAromor;
import lbn.dungeon.contents.item.armor.CommonArmor;
import lbn.dungeon.contents.item.armor.old.ChainArmor;
import lbn.dungeon.contents.item.armor.old.DiamondArmor;
import lbn.dungeon.contents.item.armor.old.GoldArmor;
import lbn.dungeon.contents.item.armor.old.IronArmor;
import lbn.dungeon.contents.item.armor.old.LeatherArmor;
import lbn.dungeon.contents.item.click.DungeonDirector;
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
import lbn.dungeon.contents.item.material.ArmorMaterialItem;
import lbn.dungeon.contents.item.material.CaveStone;
import lbn.dungeon.contents.item.other.strengthBase.StrengthBaseJade;
import lbn.dungeon.contents.item.questItem.Serum;
import lbn.dungeon.contents.item.setItem.crystal.SetItemHealthCrystal;
import lbn.dungeon.contents.item.shootbow.BowOfExplosion;
import lbn.dungeon.contents.item.shootbow.DebugBow;
import lbn.dungeon.contents.item.shootbow.LaserBow;
import lbn.dungeon.contents.item.shootbow.NormalBowWrapper;
import lbn.dungeon.contents.item.sword.DebugSword;
import lbn.dungeon.contents.item.sword.NormalAttackItemSelector;
import lbn.dungeon.contents.item.sword.NormalSwordWrapper;
import lbn.dungeon.contents.item.sword.TutorialSword;
import lbn.dungeon.contents.item.sword.special.AttributeLeafSword;
import lbn.dungeon.contents.item.sword.special.SpecialSwordAquatempest;
import lbn.dungeon.contents.item.sword.special.SpecialSwordIceAspect;
import lbn.dungeon.contents.item.sword.special.SpecialSwordMebiusSword;
import lbn.dungeon.contents.item.sword.special.SpecialSwordSoulSword;
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
import lbn.item.attackitem.old.SpecialAttackItemSelectorOld;
import lbn.item.setItem.SetItemManager;
import lbn.player.ItemType;

public class ItemRegister {
	public static void registItem() {
		SetItemManager.regist(new SetItemHealthCrystal());

		//防具
		ItemManager.registItem(LeatherArmor.getAllArmor());
		GoldArmor.regist();
		IronArmor.regist();
		ChainArmor.regist();
		DiamondArmor.regist();
		ItemManager.registItem(CommonArmor.getAllArmor());
		ItemManager.registItem(BeneCommonAromor.getAllArmor());

		ItemManager.registItem(SuikaCastle.getAllKey());
		ItemManager.registItem(AllDriedKey.getAllKey());
		ItemManager.registItem(Fragment.getAllFragment());
		ItemManager.registItem(new KalgrusKey());
		ItemManager.registItem(new KalgrusFinishKey());
		ItemManager.registItem(new MaratKey());
		ItemManager.registItem(new NativeUnderground());

		//rightClick
		ItemManager.registItem(AbstractMagicBook.getAllItem());
		ItemManager.registItem(new JumpBoost());
		ItemManager.registItem(DungeonDirector.getItemList());

		//material
		ItemManager.registItem(new CaveStone());
		ItemManager.registItem(ArmorMaterialItem.getAllItems());

		//questitem
		ItemManager.registItem(new Serum());

		//剣
		ItemManager.registItem(NormalSwordWrapper.getAllNormalItem());
		ItemManager.registItem(new DebugSword());
		ItemManager.registItem(new SpecialSwordIceAspect().getAllItem());
		ItemManager.registItem(new SpecialAttackItemSelectorOld(new SpecialSwordIceAspect()));
		ItemManager.registItem(new SpecialSwordMebiusSword().getAllItem());
		ItemManager.registItem(new SpecialAttackItemSelectorOld(new SpecialSwordMebiusSword()));
		ItemManager.registItem(new SpecialSwordAquatempest().getAllItem());
		ItemManager.registItem(new SpecialAttackItemSelectorOld(new SpecialSwordAquatempest()));
		ItemManager.registItem(new SpecialSwordSoulSword().getAllItem());
		ItemManager.registItem(new SpecialAttackItemSelectorOld(new SpecialSwordSoulSword()));
		ItemManager.registItem(new AttributeLeafSword().getAllItem());
		ItemManager.registItem(new SpecialAttackItemSelectorOld(new AttributeLeafSword()));
		ItemManager.registItem(new NormalAttackItemSelector(ItemType.SWORD));
		ItemManager.registItem(new TutorialSword());

		//弓
		ItemManager.registItem(NormalBowWrapper.getAllNormalItem());
		ItemManager.registItem(new LaserBow());
		ItemManager.registItem(new BowOfExplosion());
		ItemManager.registItem(new DebugBow());
		ItemManager.registItem(new NormalAttackItemSelector(ItemType.BOW));

		//魔法
//		ItemManager.registItem(new Level0MagicItem());
//		ItemManager.registItem(new Level10MagicItem());
//		ItemManager.registItem(new MagicStick());
		ItemManager.registItem(NormalMagicItem.getAllItem());
		ItemManager.registItem(new NormalAttackItemSelector(ItemType.MAGIC));

		//SLOT
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

		//OTHER
		ItemManager.registItem(new StrengthBaseJade());


		//chest
		AlphaWhiteListChest alphaWhiteListChest = new AlphaWhiteListChest();
		CustomChestManager.registChest(alphaWhiteListChest.getChestLocation(), alphaWhiteListChest);
	}
}
