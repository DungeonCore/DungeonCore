package lbn.command;

import java.util.List;

import lbn.api.LevelType;
import lbn.api.player.TheLowPlayer;
import lbn.api.player.TheLowPlayerManager;
import lbn.item.customItem.armoritem.TestArmorItem;
import lbn.item.customItem.attackitem.SpreadSheetWeaponData;
import lbn.item.customItem.attackitem.test.TestBowWeaponItem;
import lbn.item.customItem.attackitem.test.TestMagicWeaponItem;
import lbn.item.customItem.attackitem.test.TestSwordWeaponItem;
import lbn.util.JavaUtil;
import lbn.util.LivingEntityUtil;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

import com.google.common.collect.ImmutableList;

public class CommandEquipPlayer implements CommandExecutor, TabCompleter {

  @Override
  public boolean onCommand(CommandSender paramCommandSender,
      Command paramCommand, String paramString,
      String[] paramArrayOfString) {

    if (paramArrayOfString.length == 0) { return false; }

    int level = JavaUtil.getInt(paramArrayOfString[0], -1);
    if (level == -1) { return false; }

    Player p = (Player) paramCommandSender;
    updatePlayerLevel(p, 0);
    p.getInventory().clear();

    boolean isMobTest = false;

    if (paramArrayOfString.length == 2) {
      if (paramArrayOfString[1].equals("mob")) {
        isMobTest = true;
      }
    }

    LivingEntityUtil.setEquipment(p, new TestArmorItem(level, Material.LEATHER_HELMET).getItem(),
        new TestArmorItem(level, Material.LEATHER_CHESTPLATE).getItem(),
        new TestArmorItem(level, Material.LEATHER_LEGGINGS).getItem(),
        new TestArmorItem(level, Material.LEATHER_BOOTS).getItem(),
        0f);

    int weaponLevel = isMobTest ? 9 : level;

    ItemStack sword = new TestSwordWeaponItem(getWeaponData(weaponLevel, "SWORD", Material.IRON_SWORD)).getItem();
    ItemStack magic = new TestBowWeaponItem(getWeaponData(weaponLevel, "BOW", Material.BOW)).getItem();
    ItemStack bow = new TestMagicWeaponItem(getWeaponData(weaponLevel, "MAGIC", Material.IRON_HOE)).getItem();

    PlayerInventory inventory = p.getInventory();
    inventory.addItem(sword,
        magic,
        bow,
        new ItemStack(Material.ARROW, 64),
        new ItemStack(Material.BREAD, 64));

    Potion potion = new Potion(PotionType.INSTANT_HEAL, 2);
    potion.setSplash(true);

    for (int i = 0; i < 27; i++) {
      inventory.addItem(potion.toItemStack(1));
    }

    Potion potionS = new Potion(PotionType.SPEED, 1);
    inventory.addItem(potionS.toItemStack(1));
    inventory.addItem(potionS.toItemStack(1));

    ItemStack itemStack = new ItemStack(Material.GOLDEN_APPLE, 15);
    inventory.addItem(itemStack);

    updatePlayerLevel(p, Math.max(weaponLevel, level));

    p.updateInventory();
    return true;
  }

  public SpreadSheetWeaponData getWeaponData(int level, String itemType, Material m) {
    SpreadSheetWeaponData data = new SpreadSheetWeaponData();
    data.setAvailableLevel(String.valueOf(level));
    data.setDefaultSlot("5");
    data.setMaxSlot("5");
    data.setName("テストアイテム：" + itemType);
    data.setId("test_magic_" + itemType.toLowerCase() + "_" + level);
    data.setItemType(itemType);
    data.setMaxDurability("10000");
    data.setRank("10");
    data.setItemMaterial(m.toString(), Bukkit.getConsoleSender());
    data.setSkillLevel("6");
    return data;
  }

  protected void updatePlayerLevel(Player p, int level) {
    TheLowPlayer theLowPlayer = TheLowPlayerManager.getTheLowPlayer(p);
    if (theLowPlayer != null) {
      theLowPlayer.setLevel(LevelType.MAIN, level);
    }
  }

  @Override
  public List<String> onTabComplete(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
    return ImmutableList.of();
  }

}
