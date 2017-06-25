package net.l_bulb.dungeoncore.dungeon.contents.item.click.magicbook;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.common.event.player.CombatEntityEvent;
import net.l_bulb.dungeoncore.item.ItemInterface;
import net.l_bulb.dungeoncore.item.customItem.AbstractItem;
import net.l_bulb.dungeoncore.item.itemInterface.RightClickItemable;
import net.l_bulb.dungeoncore.util.LivingEntityUtil;

public abstract class AbstractMagicBook extends AbstractItem implements RightClickItemable {

  @Override
  public boolean excuteOnRightClick(PlayerInteractEvent e) {
    Player player = e.getPlayer();
    List<Entity> nearbyEntities = getNearEntitys(player);
    for (Entity entity : nearbyEntities) {
      if (LivingEntityUtil.isEnemy(entity)) {
        onDamage(player, (LivingEntity) entity, e.getItem());
      }
    }
    return true;
  }

  abstract protected List<Entity> getNearEntitys(Player player);

  /**
   * 効果を発動したときのダメージを与える
   *
   * @param player
   * @param entity
   * @param item
   */
  protected void onDamage(Player player, LivingEntity entity, ItemStack item) {
    CombatEntityEvent entityEvent = new CombatEntityEvent(player, getDamageVal(), this, item, false, entity);
    // イベントを呼ぶ
    entityEvent.callEvent();
    // ダメージを与える
    entityEvent.damageEntity();
  }

  abstract protected double getDamageVal();

  @Override
  public int getBuyPrice(ItemStack item) {
    return 400;
  }

  public static List<ItemInterface> getAllItem() {
    ArrayList<ItemInterface> arrayList = new ArrayList<>();
    arrayList.add(new MagicBookLevel1Flame());
    arrayList.add(new MagicBookFireLevel2());
    arrayList.add(new MagicBookFireLevel3());
    return arrayList;
  }

  @Override
  public boolean isConsumeWhenRightClick(PlayerInteractEvent event) {
    return true;
  }
}
