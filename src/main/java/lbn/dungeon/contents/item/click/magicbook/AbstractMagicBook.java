package lbn.dungeon.contents.item.click.magicbook;

import java.util.ArrayList;
import java.util.List;

import lbn.item.ItemInterface;
import lbn.item.customItem.itemAbstract.RightClickItem;
import lbn.mob.LastDamageManager;
import lbn.mob.LastDamageMethodType;
import lbn.util.LivingEntityUtil;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public abstract class AbstractMagicBook extends RightClickItem {

  @Override
  protected boolean excuteOnRightClick2(PlayerInteractEvent e) {
    Player player = e.getPlayer();
    List<Entity> nearbyEntities = getNearEntitys(player);
    for (Entity entity : nearbyEntities) {
      if (LivingEntityUtil.isEnemy(entity)) {
        onDamage(player, (LivingEntity) entity);
      }
    }
    return true;
  }

  abstract protected List<Entity> getNearEntitys(Player player);

  protected void onDamage(Player player, LivingEntity entity) {
    // ダメージを与える
    entity.damage(getDamageVal(), player);
    // LastDamageを登録
    LastDamageManager.onDamage(entity, player, LastDamageMethodType.USE_ITEM);
  }

  abstract protected double getDamageVal();

  @Override
  protected boolean isConsumeWhenUse() {
    return true;
  }

  @Override
  public int getBuyPrice(ItemStack item) {
    return 400;
  }

  public static List<ItemInterface> getAllItem() {
    ArrayList<ItemInterface> arrayList = new ArrayList<ItemInterface>();
    arrayList.add(new MagicBookLevel1Flame());
    arrayList.add(new MagicBookFireLevel2());
    arrayList.add(new MagicBookFireLevel3());
    return arrayList;
  }
}
