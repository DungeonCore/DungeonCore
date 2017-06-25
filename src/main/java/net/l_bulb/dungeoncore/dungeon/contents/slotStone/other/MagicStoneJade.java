package net.l_bulb.dungeoncore.dungeon.contents.slotStone.other;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import net.l_bulb.dungeoncore.common.event.player.CombatEntityEvent;
import net.l_bulb.dungeoncore.common.particle.ParticleData;
import net.l_bulb.dungeoncore.common.particle.ParticleType;
import net.l_bulb.dungeoncore.item.slot.SlotLevel;
import net.l_bulb.dungeoncore.item.slot.magicstone.CombatSlot;
import net.l_bulb.dungeoncore.util.JavaUtil;
import net.l_bulb.dungeoncore.util.LivingEntityUtil;

public class MagicStoneJade extends CombatSlot {

  @Override
  public String getSlotName() {
    return "ジェダイト";
  }

  @Override
  public String getSlotDetail() {
    return "攻撃速度上昇+25%,攻撃時一定確率で回復,攻撃力+20%";
  }

  @Override
  public String getId() {
    return "red_jade";
  }

  @Override
  public ChatColor getNameColor() {
    return ChatColor.RED;
  }

  @Override
  public SlotLevel getLevel() {
    return SlotLevel.LEGENT;
  }

  ParticleData particleData = new ParticleData(ParticleType.heart, 30);

  @Override
  public void onCombat(CombatEntityEvent e, Player p) {
    LivingEntityUtil.setNoDamageTick(e.getEnemy(), 15);

    if (JavaUtil.isRandomTrue(5)) {
      LivingEntityUtil.addHealth(p, Math.min(e.getDamage() * 0.2, 3));
      p.playSound(p.getLocation(), Sound.SILVERFISH_IDLE, 1, (float) 0.1);
      particleData.run(p.getLocation().add(0, 2, 0));
    }

    e.setDamage(e.getDamage() * 1.2);
    ;
  }

}
