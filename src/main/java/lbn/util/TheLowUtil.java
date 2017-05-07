package lbn.util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import lbn.common.particle.ParticleData;
import lbn.common.particle.ParticleType;
import lbn.item.customItem.other.GalionItem;

public class TheLowUtil {

  static ParticleData particleData = new ParticleData(ParticleType.fireworksSpark, 100);

  public static void addBonusGold(Player p, Location l) {
    l.getWorld().dropItem(l, GalionItem.getInstance(20).getItem());
    particleData.run(l);
    l.getWorld().playSound(l, Sound.FIREWORK_BLAST, 1, 1);
  }

  public static boolean isSoulBound(ItemStack item) {
    if (item == null) { return false; }

    ItemMeta itemMeta = item.getItemMeta();

    if (itemMeta == null) { return false; }

    List<String> lore = itemMeta.getLore();

    if (lore == null) { return false; }

    for (String str : lore) {
      if (str.contains(TheLowUtil.SOUL_BOUND)) { return true; }
    }

    return false;
  }

  public static void addSoulBound(ItemStack item) {
    if (item == null) { return; }
    if (!isSoulBound(item)) {

      ItemMeta itemMeta = item.getItemMeta();
      List<String> lore = itemMeta.getLore();

      if (lore == null) {
        lore = new ArrayList<String>();
      }
      lore.add(TheLowUtil.SOUL_BOUND);
      itemMeta.setLore(lore);

      item.setItemMeta(itemMeta);
    }
  }

  public static final String SOUL_BOUND = "soulbound";

}
