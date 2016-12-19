package main.dungeon.contents;

import main.chest.CustomChestManager;
import main.dungeon.contents.chest.AlphaWhiteListChest;
import main.dungeon.contents.item.armor.BeneCommonAromor;
import main.dungeon.contents.item.armor.CommonArmor;
import main.dungeon.contents.item.armor.old.ChainArmor;
import main.dungeon.contents.item.armor.old.DiamondArmor;
import main.dungeon.contents.item.armor.old.GoldArmor;
import main.dungeon.contents.item.armor.old.IronArmor;
import main.dungeon.contents.item.armor.old.LeatherArmor;
import main.dungeon.contents.item.click.DungeonDirector;
import main.dungeon.contents.item.click.JumpBoost;
import main.dungeon.contents.item.click.magicbook.AbstractMagicBook;
import main.dungeon.contents.item.key.impl.AllDriedKey;
import main.dungeon.contents.item.key.impl.Fragment;
import main.dungeon.contents.item.key.impl.KalgrusFinishKey;
import main.dungeon.contents.item.key.impl.KalgrusKey;
import main.dungeon.contents.item.key.impl.MaratKey;
import main.dungeon.contents.item.key.impl.NativeUnderground;
import main.dungeon.contents.item.key.impl.SuikaCastle;
import main.dungeon.contents.item.magic.normalItems.NormalMagicItem;
import main.dungeon.contents.item.material.ArmorMaterialItem;
import main.dungeon.contents.item.material.CaveStone;
import main.dungeon.contents.item.other.strengthBase.StrengthBaseJade;
import main.dungeon.contents.item.questItem.Serum;
import main.dungeon.contents.item.setItem.crystal.AttackCrystal;
import main.dungeon.contents.item.setItem.crystal.SetItemHealthCrystal;
import main.dungeon.contents.item.shootbow.BowOfExplosion;
import main.dungeon.contents.item.shootbow.DebugBow;
import main.dungeon.contents.item.shootbow.LaserBow;
import main.dungeon.contents.item.shootbow.NormalBowWrapper;
import main.dungeon.contents.item.sword.DebugSword;
import main.dungeon.contents.item.sword.NormalAttackItemSelector;
import main.dungeon.contents.item.sword.NormalSwordWrapper;
import main.dungeon.contents.item.sword.TutorialSword;
import main.dungeon.contents.item.sword.special.AttributeLeafSword;
import main.dungeon.contents.item.sword.special.SpecialSwordAquatempest;
import main.dungeon.contents.item.sword.special.SpecialSwordIceAspect;
import main.dungeon.contents.item.sword.special.SpecialSwordMebiusSword;
import main.dungeon.contents.item.sword.special.SpecialSwordSoulSword;
import main.dungeon.contents.slotStone.OneMobDamageUpSlot;
import main.dungeon.contents.slotStone.level1.FireAspect1;
import main.dungeon.contents.slotStone.level1.KillEffectSlotStone1;
import main.dungeon.contents.slotStone.level1.KillEffectSlotStone2;
import main.dungeon.contents.slotStone.level2.FireAspect2;
import main.dungeon.contents.slotStone.level3.FireAspect3;
import main.dungeon.contents.slotStone.level3.HealSlotStone;
import main.dungeon.contents.slotStone.level4.CombatLightningStone;
import main.dungeon.contents.slotStone.level4.HealSlotStone2;
import main.dungeon.contents.slotStone.other.KillEffectTutorial;
import main.dungeon.contents.slotStone.other.MagicHealMagicStone;
import main.dungeon.contents.slotStone.other.MagicStoneJade;
import main.item.ItemManager;
import main.item.attackitem.SpecialAttackItemSelector;
import main.item.setItem.SetItemManager;
import main.player.AttackType;

public class ItemRegister {
	public static void registItem() {
//		SetItemManager.regist(new SetItemAttacker());
		SetItemManager.regist(new SetItemHealthCrystal());
		SetItemManager.regist(new AttackCrystal());

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
		ItemManager.registItem(new SpecialAttackItemSelector(new SpecialSwordIceAspect()));
		ItemManager.registItem(new SpecialSwordMebiusSword().getAllItem());
		ItemManager.registItem(new SpecialAttackItemSelector(new SpecialSwordMebiusSword()));
		ItemManager.registItem(new SpecialSwordAquatempest().getAllItem());
		ItemManager.registItem(new SpecialAttackItemSelector(new SpecialSwordAquatempest()));
		ItemManager.registItem(new SpecialSwordSoulSword().getAllItem());
		ItemManager.registItem(new SpecialAttackItemSelector(new SpecialSwordSoulSword()));
		ItemManager.registItem(new AttributeLeafSword().getAllItem());
		ItemManager.registItem(new SpecialAttackItemSelector(new AttributeLeafSword()));
		ItemManager.registItem(new NormalAttackItemSelector(AttackType.SWORD));
		ItemManager.registItem(new TutorialSword());

		//弓
		ItemManager.registItem(NormalBowWrapper.getAllNormalItem());
		ItemManager.registItem(new LaserBow());
		ItemManager.registItem(new BowOfExplosion());
		ItemManager.registItem(new DebugBow());
		ItemManager.registItem(new NormalAttackItemSelector(AttackType.BOW));

		//魔法
//		ItemManager.registItem(new Level0MagicItem());
//		ItemManager.registItem(new Level10MagicItem());
//		ItemManager.registItem(new MagicStick());
		ItemManager.registItem(NormalMagicItem.getAllItem());
		ItemManager.registItem(new NormalAttackItemSelector(AttackType.MAGIC));

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
