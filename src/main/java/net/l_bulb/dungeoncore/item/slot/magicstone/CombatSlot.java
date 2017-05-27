package net.l_bulb.dungeoncore.item.slot.magicstone;

import org.bukkit.entity.Player;

import net.l_bulb.dungeoncore.common.dropingEntity.CombatEntityEvent;
import net.l_bulb.dungeoncore.item.slot.AbstractSlot;

public abstract class CombatSlot extends AbstractSlot {
  abstract public void onCombat(CombatEntityEvent e, Player p);
}
