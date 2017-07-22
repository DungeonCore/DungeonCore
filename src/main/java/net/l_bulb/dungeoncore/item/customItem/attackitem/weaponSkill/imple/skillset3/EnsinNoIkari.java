package net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.skillset3;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class EnsinNoIkari {
  /**
   * パッシブスキルがアクティブならTRUE
   *
   * @return
   */
  public boolean isActive(Player p, Entity e) {
    return e.getFallDistance() > 0;
  }

  /**
   * パッシブスキルがアクティブ場合のダメージ率
   *
   * @return
   */
  public double getDamageRateWhenActive() {
    return 1;
  }
}
