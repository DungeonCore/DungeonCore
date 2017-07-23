package net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.skillset2;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.item.customItem.attackitem.AbstractAttackItem;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.WeaponSkillFactory;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.SpreadSheetWeaponSkill;
import net.l_bulb.dungeoncore.player.PlayerListener;

import net.md_5.bungee.api.ChatColor;

public class Kaiho extends SpreadSheetWeaponSkill {

  private static final String YOCHO_ID = new Yochou().getId();
  /**
   * スキルの実行時間とPlayerのMap
   */
  static HashMap<UUID, Long> executeLog = new HashMap<>();

  static {
    PlayerListener.registerLogoutEvent(p -> executeLog.remove(p.getUniqueId()));
  }

  @Override
  public String getId() {
    return "wskill7";
  }

  @Override
  public boolean onClick(Player p, ItemStack item, AbstractAttackItem customItem) {
    Long executeSkillMillisTile = Yochou.getExecuteSkillMillisTile(p);

    // 予兆を実行してから指定時間経っているかしらべる
    if (executeSkillMillisTile == -1 || executeSkillMillisTile + getData(0) * 1000 > System.currentTimeMillis()) {
      p.sendMessage(ChatColor.RED + "このスキルはまだ実行できません。" + WeaponSkillFactory.getWeaponSkill(YOCHO_ID) + "を実行してから" + getData(0) + "秒以上経過する必要があります。");
      return false;
    }

    executeLog.put(p.getUniqueId(), System.currentTimeMillis());
    p.damage(getData(1) * 2);
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
