package net.l_bulb.dungeoncore.item.itemInterface;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

public interface ArmorItemable extends EquipItemable, Strengthenable, CraftItemable {
  /**
   * このアイテムを装備しているときの通常モンスターに対するガードポイント
   * 
   * @return
   */
  double getArmorPointForNormalMob();

  /**
   * このアイテムを装備しているときのボスモンスターに対するガードポイント
   * 
   * @return
   */
  double getArmorPointForBossMob();

  /**
   * ダメージを受けたときの特別処理があればここで修正し、修正後のダメージを返す
   * 
   * @param damage 実際のダメージ値
   * @param me 装備しているPlayer
   * @param e Event
   * @param isBoss ダメージを与えたものが生き物でかつ、BossならTrue
   * @param mob ダメージを与えたものが生き物(もし生き物以外ならNull)
   * @return 防具ポイント
   */
  double getOtherArmorPoint(double damage, Player me, EntityDamageEvent e, boolean isBoss, LivingEntity mob);
}
