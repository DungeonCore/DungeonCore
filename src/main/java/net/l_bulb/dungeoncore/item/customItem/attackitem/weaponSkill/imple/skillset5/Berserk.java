package net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.skillset5;

import java.util.Map;

import org.apache.commons.collections.map.SingletonMap;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.l_bulb.dungeoncore.api.PlayerStatusType;
import net.l_bulb.dungeoncore.api.player.TheLowPlayer;
import net.l_bulb.dungeoncore.api.player.TheLowPlayerManager;
import net.l_bulb.dungeoncore.common.event.player.CombatEntityEvent;
import net.l_bulb.dungeoncore.item.customItem.attackitem.AbstractAttackItem;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.imple.WeaponSkillWithTimeLimit;
import net.l_bulb.dungeoncore.item.itemInterface.CombatItemable;
import net.l_bulb.dungeoncore.player.ability.AbstractTimeLimitAbility;
import net.l_bulb.dungeoncore.util.TheLowExecutor;

public class Berserk extends WeaponSkillWithTimeLimit {

  private static final String SKILL_ID = "wskill20";

  @Override
  public String getId() {
    return SKILL_ID;
  }

  @Override
  public boolean onClick(Player p, ItemStack item, AbstractAttackItem customItem) {
    TheLowPlayer theLowPlayer = TheLowPlayerManager.getTheLowPlayer(p);
    if (theLowPlayer == null) {
      TheLowPlayerManager.sendLoingingMessage(p);
      return false;
    }
    // HPを固定する
    AbilltyImplemention ablity = new AbilltyImplemention();
    theLowPlayer.addAbility(ablity);
    TheLowExecutor.executeLater(getData(1), () -> theLowPlayer.removeAbility(ablity));

    // 無敵にする
    p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, (int) (getData(1) * 20), 10, true));

    return super.onClick(p, item, customItem);
  }

  @Override
  public void onCombat(Player p, ItemStack item, CombatItemable customItem, LivingEntity livingEntity, CombatEntityEvent event) {
    if (isActive(p, item)) {
      event.setDamage(event.getDamage() * getData(2));
    }
  }

  class AbilltyImplemention extends AbstractTimeLimitAbility {
    @Override
    public String getId() {
      return SKILL_ID;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<PlayerStatusType, Double> getAbilityMap() {
      return new SingletonMap(PlayerStatusType.SET_MAX_HP, getData(0) * 2);
    }

    @Override
    public double getLimitTimeSec() {
      return getData(1);
    }
  }

  @Override
  protected void offActive(Player p, ItemStack item, AbstractAttackItem customItem) {}
}
