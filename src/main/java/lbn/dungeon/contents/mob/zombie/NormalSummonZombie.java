package lbn.dungeon.contents.mob.zombie;

import java.util.List;

import lbn.common.event.player.PlayerCustomMobSpawnEvent;
import lbn.dungeoncore.Main;
import lbn.player.ItemType;
import lbn.util.LivingEntityUtil;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

public class NormalSummonZombie extends AbstractSummonZombie {
  public NormalSummonZombie(int level, int strengthLevel) {
    this.level = level;
    this.strengthLevel = strengthLevel;
  }

  @Override
  public String getName() {
    return "SUMMON'S ZOMBIE";
  }

  int level;
  int strengthLevel;

  @Override
  protected Zombie spawnPrivate(Location loc) {
    Zombie zombie = super.spawnPrivate(loc);
    zombie.setMetadata("availableLevel", new FixedMetadataValue(Main.plugin, level));
    zombie.setMetadata("strengthLevel", new FixedMetadataValue(Main.plugin, strengthLevel));
    zombie.setBaby(false);
    return zombie;
  }

  @Override
  public void onSpawn(PlayerCustomMobSpawnEvent event) {
    LivingEntity e = event.getEntity();

    List<MetadataValue> metadata = event.getEntity().getMetadata("availableLevel");
    int level = 0;
    if (metadata != null && metadata.size() != 0) {
      level = metadata.get(0).asInt();
    }

    super.onSpawn(event);

    switch (level) {
      case 0:
        LivingEntityUtil.setEquipment(e, new ItemStack(Material.PUMPKIN), null, null, null, 0);
        break;
      case 10:
        LivingEntityUtil.setEquipment(e, new ItemStack(Material.PUMPKIN), null, null, null, 0);
        LivingEntityUtil.setItemInHand(e, new ItemStack(Material.WOOD_SWORD), 0);
        break;
      case 20:
        LivingEntityUtil.setEquipment(e, new ItemStack(Material.PUMPKIN), new ItemStack(Material.LEATHER_CHESTPLATE), null, null, 0);
        LivingEntityUtil.setItemInHand(e, new ItemStack(Material.WOOD_SWORD), 0);
        break;
      case 30:
        LivingEntityUtil.setEquipment(e, new ItemStack(Material.PUMPKIN), new ItemStack(Material.LEATHER_CHESTPLATE),
            new ItemStack(Material.LEATHER_LEGGINGS), new ItemStack(Material.LEATHER_BOOTS), 0);
        LivingEntityUtil.setItemInHand(e, new ItemStack(Material.WOOD_SWORD), 0);
        break;
      case 40:
        LivingEntityUtil.setEquipment(e, new ItemStack(Material.PUMPKIN), new ItemStack(Material.LEATHER_CHESTPLATE),
            new ItemStack(Material.LEATHER_LEGGINGS), new ItemStack(Material.LEATHER_BOOTS), 0);
        LivingEntityUtil.setItemInHand(e, new ItemStack(Material.STONE_SWORD), 0);
        break;
      case 50:
        LivingEntityUtil.setEquipment(e, new ItemStack(Material.PUMPKIN), new ItemStack(Material.CHAINMAIL_CHESTPLATE),
            new ItemStack(Material.CHAINMAIL_LEGGINGS), new ItemStack(Material.CHAINMAIL_BOOTS), 0);
        LivingEntityUtil.setItemInHand(e, new ItemStack(Material.STONE_SWORD), 0);
        break;
      case 60:
        LivingEntityUtil.setEquipment(e, new ItemStack(Material.PUMPKIN), new ItemStack(Material.IRON_CHESTPLATE),
            new ItemStack(Material.IRON_LEGGINGS), new ItemStack(Material.IRON_BOOTS), 0);
        LivingEntityUtil.setItemInHand(e, new ItemStack(Material.IRON_SWORD), 0);
        break;
      case 70:
        LivingEntityUtil.setEquipment(e, new ItemStack(Material.PUMPKIN), new ItemStack(Material.DIAMOND_CHESTPLATE),
            new ItemStack(Material.IRON_LEGGINGS), new ItemStack(Material.IRON_BOOTS), 0);
        LivingEntityUtil.setItemInHand(e, new ItemStack(Material.IRON_SWORD), 0);
        break;
      default:
        break;
    }

    if (level > 70) {
      LivingEntityUtil.setEquipment(e, new ItemStack(Material.PUMPKIN), new ItemStack(Material.DIAMOND_CHESTPLATE),
          new ItemStack(Material.IRON_LEGGINGS), new ItemStack(Material.IRON_BOOTS), 0);
      LivingEntityUtil.setItemInHand(e, new ItemStack(Material.IRON_SWORD), 0);
    }
  }

  @Override
  public ItemType getUseItemType() {
    return ItemType.MAGIC;
  }
}
