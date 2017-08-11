package net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill;

import static net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.WeaponSkillFactory.regist;
import static net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.WeaponSkillFactory.registTempData;

import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.skillset1.DeathDance;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.skillset1.Kenbu;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.skillset1.Parry;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.skillset2.Kaiho;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.skillset2.Kakusei;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.skillset2.Yochou;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.skillset3.Enkyaku;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.skillset3.HiNoMai;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.skillset3.Lavaness;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.skillset6.MeteoStrike;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.old.all.WeaponSkillBlastOff;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.old.all.WeaponSkillBlastOffLevel2;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.old.all.WeaponSkillCancel;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.old.bow.ArrowStorm;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.old.bow.BlindEye;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.old.bow.Finale;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.old.bow.IceArrow;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.old.bow.ReleaseAura;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.old.magic.Explosion;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.old.magic.GravityField;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.old.magic.HealRain;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.old.magic.LeafFlare;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.old.sword.BloodyHeal;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.old.sword.BurstFlame;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.old.sword.GrandSpike;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.old.sword.LightningOrder;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.old.sword.Lump;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.old.sword.ProtectionArmor;;

public class WeaponSkillRegister {

  public static void registWeaponSkill() {
    regist(new WeaponSkillBlastOff(), null, null);
    regist(new WeaponSkillBlastOffLevel2(), null, null);
    regist(new WeaponSkillCancel(), null, null);
    registTempData(new ArrowStorm());
    registTempData(new LightningOrder());
    registTempData(new BloodyHeal());
    registTempData(new MeteoStrike());
    registTempData(new IceArrow());
    registTempData(new HealRain());
    registTempData(new ProtectionArmor());
    registTempData(new Explosion());
    registTempData(new Lump());
    registTempData(new BlindEye());
    registTempData(new BurstFlame());
    registTempData(new GrandSpike());
    registTempData(new Finale());
    registTempData(new LeafFlare());
    registTempData(new ReleaseAura());
    registTempData(new GravityField());

    // skill set1
    registTempData(new DeathDance());
    registTempData(new Kenbu());
    registTempData(new Parry());

    // skill set2
    registTempData(new Kaiho());
    registTempData(new Kakusei());
    registTempData(new Yochou());

    // skill set3
    registTempData(new Enkyaku());
    registTempData(new HiNoMai());
    registTempData(new Lavaness());

  }
}
