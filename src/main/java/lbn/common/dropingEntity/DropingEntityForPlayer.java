package lbn.common.dropingEntity;

import lbn.util.ItemStackUtil;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.util.Vector;

public class DropingEntityForPlayer extends AbstractDamageFallingblock {
  public DropingEntityForPlayer(Vector direction, Location start, Material m, byte data) {
    super(getDropItem(direction, start, m, data));
  }

  public static Item getDropItem(Vector direction, Location start, Material m, byte data) {
    Item dropItem = start.getWorld().dropItem(start, ItemStackUtil.getItem("", m, data));
    dropItem.setVelocity(direction);
    dropItem.setPickupDelay(20 * 30000);
    return dropItem;
  }

  @Override
  protected boolean damageLivingentity(Entity entity) {
    // mobとは接触しない
    return false;
  }

  boolean onGround = false;

  @Override
  public void tickRutine(int count) {
    if (!onGround && spawnEntity.isOnGround()) {
      onGround(spawnEntity);
      onGround = true;
    }
  }

  /**
   * 地面についたときの処理
   * 
   * @param spawnEntity
   */
  public void onGround(Entity spawnEntity) {

  }

  @Override
  public void removedRutine(Entity spawnEntity) {}

  @Override
  public void onHitDamagedEntity(Entity target) {}

}
