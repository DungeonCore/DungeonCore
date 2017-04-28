package net.l_bulb.dungeoncore.dungeon.contents;

import net.l_bulb.dungeoncore.chest.CustomChestManager;
import net.l_bulb.dungeoncore.dungeon.contents.chest.AlphaWhiteListChest;
import net.l_bulb.dungeoncore.dungeon.contents.item.click.JumpBoost;
import net.l_bulb.dungeoncore.dungeon.contents.item.click.magicbook.AbstractMagicBook;
import net.l_bulb.dungeoncore.dungeon.contents.item.key.impl.AllDriedKey;
import net.l_bulb.dungeoncore.dungeon.contents.item.key.impl.Fragment;
import net.l_bulb.dungeoncore.dungeon.contents.item.key.impl.KalgrusFinishKey;
import net.l_bulb.dungeoncore.dungeon.contents.item.key.impl.KalgrusKey;
import net.l_bulb.dungeoncore.dungeon.contents.item.key.impl.MaratKey;
import net.l_bulb.dungeoncore.dungeon.contents.item.key.impl.NativeUnderground;
import net.l_bulb.dungeoncore.dungeon.contents.item.key.impl.SuikaCastle;
import net.l_bulb.dungeoncore.dungeon.contents.item.magic.normalItems.NormalMagicItem;
import net.l_bulb.dungeoncore.dungeon.contents.item.other.strengthBase.StrengthBaseJade;
import net.l_bulb.dungeoncore.dungeon.contents.item.questItem.Serum;
import net.l_bulb.dungeoncore.dungeon.contents.item.setItem.crystal.SetItemHealthCrystal;
import net.l_bulb.dungeoncore.dungeon.contents.item.shootbow.BowOfExplosion;
import net.l_bulb.dungeoncore.dungeon.contents.item.shootbow.DebugBow;
import net.l_bulb.dungeoncore.dungeon.contents.item.shootbow.LaserBow;
import net.l_bulb.dungeoncore.dungeon.contents.slotStone.OneMobDamageUpSlot;
import net.l_bulb.dungeoncore.dungeon.contents.slotStone.level1.FireAspect1;
import net.l_bulb.dungeoncore.dungeon.contents.slotStone.level1.KillEffectSlotStone1;
import net.l_bulb.dungeoncore.dungeon.contents.slotStone.level1.KillEffectSlotStone2;
import net.l_bulb.dungeoncore.dungeon.contents.slotStone.level2.FireAspect2;
import net.l_bulb.dungeoncore.dungeon.contents.slotStone.level3.FireAspect3;
import net.l_bulb.dungeoncore.dungeon.contents.slotStone.level3.HealSlotStone;
import net.l_bulb.dungeoncore.dungeon.contents.slotStone.level4.CombatLightningStone;
import net.l_bulb.dungeoncore.dungeon.contents.slotStone.level4.HealSlotStone2;
import net.l_bulb.dungeoncore.dungeon.contents.slotStone.other.KillEffectTutorial;
import net.l_bulb.dungeoncore.dungeon.contents.slotStone.other.MagicHealMagicStone;
import net.l_bulb.dungeoncore.dungeon.contents.slotStone.other.MagicStoneJade;
import net.l_bulb.dungeoncore.item.ItemManager;
import net.l_bulb.dungeoncore.item.setItem.SetItemManager;

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
