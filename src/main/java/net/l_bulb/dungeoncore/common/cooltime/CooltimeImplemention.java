package net.l_bulb.dungeoncore.common.cooltime;

import org.bukkit.inventory.ItemStack;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CooltimeImplemention implements Cooltimable {
  /** クールタイム時間(tick) */
  int cooltimeTick;

  /** id */
  String id;

  @Override
  public int getCooltimeTick(ItemStack item) {
    return cooltimeTick;
  }

  @Override
  public String getId() {
    return id;
  }

}
