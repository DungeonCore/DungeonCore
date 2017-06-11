package net.l_bulb.dungeoncore.mobspawn.spawnPointImpl;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.item.ItemInterface;
import net.l_bulb.dungeoncore.mobspawn.SpawnPointSpreadSheetData;

public class ItemSpawnPoint extends AbstractSpawnPoint {

  private ItemStack itemStack;
  private ItemInterface itemInterface;

  public ItemSpawnPoint(SpawnPointSpreadSheetData data, ItemInterface item) {
    super(data);
    this.itemInterface = item;
    itemStack = item.getItem();
  }

  @Override
  public Entity spawn() {
    return getLocation().getWorld().dropItem(getLocation(), itemStack);
  }

  @Override
  public boolean equalsEntity(Entity e) {
    return e.getType() == EntityType.DROPPED_ITEM && itemInterface.isThisItem(((Item) e).getItemStack());
  }

  @Override
  public String getSpawnTargetName() {
    return itemInterface.getItemName();
  }

}
