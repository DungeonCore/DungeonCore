package net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.skillset2;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.l_bulb.dungeoncore.item.customItem.attackitem.AbstractAttackItem;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.SpreadSheetWeaponSkill;
import net.l_bulb.dungeoncore.player.PlayerListener;

public class Yochou extends SpreadSheetWeaponSkill {

  /**
   * スキルの実行時間とPlayerのMap
   */
  static HashMap<UUID, Long> executeLog = new HashMap<>();

  static {
    PlayerListener.registerLogoutEvent(p -> executeLog.remove(p.getUniqueId()));
  }

  @Override
  public String getId() {
    return "wskill6";
  }

  @Override
  public boolean onClick(Player p, ItemStack item, AbstractAttackItem customItem) {
    final PotionEffect SLOW = new PotionEffect(PotionEffectType.SLOW, (int) (getData(0) * 20), 100);

    p.addPotionEffect(SLOW, true);
    executeLog.put(p.getUniqueId(), System.currentTimeMillis());
    return true;
  }

  /**
   * 指定したPlayerがスキルをいつ実行したか取得する
   *
   * @param p
   * @return 実行したミリ秒。 もし一回も実行してないなら-1を返す
   */
  public static Long getExecuteSkillMillisTile(Player p) {
    if (executeLog.containsKey(p.getUniqueId())) {
      return executeLog.get(p.getUniqueId());
    } else {
      return -1L;
    }
  }
}
