package net.l_bulb.dungeoncore.item.system.repair;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import net.l_bulb.dungeoncore.api.player.TheLowPlayer;
import net.l_bulb.dungeoncore.api.player.TheLowPlayerManager;
import net.l_bulb.dungeoncore.common.menu.MenuSelector;
import net.l_bulb.dungeoncore.common.menu.MenuSelectorManager;
import net.l_bulb.dungeoncore.common.menu.SelectRunnable;
import net.l_bulb.dungeoncore.item.nbttag.ItemStackNbttagSetter;
import net.l_bulb.dungeoncore.money.GalionEditReason;
import net.l_bulb.dungeoncore.util.ItemStackUtil;
import net.l_bulb.dungeoncore.util.Message;

import net.md_5.bungee.api.ChatColor;

public class RepairUi {
  {

  }

  public static void onOpenUi(Player p) {
    MenuSelector menuSelecor = new MenuSelector("Repair Menu");
    menuSelecor.addMenu(ItemStackUtil.getItem(Message.getMessage("全て修理"), Material.ANVIL,
        ChatColor.GREEN + Message.getMessage("全ての保持しているアイテムを修理する"), ChatColor.GREEN + Message.getMessage("必要金額: {0} Galions",
            getNeedGalion(p.getInventory().getContents()) + getNeedGalion(p.getInventory().getArmorContents()))),
        11,
        new SelectRunnable() {
          @Override
          public void run(Player p, ItemStack item) {
            repairItemAll(p);
            p.playSound(p.getLocation(), Sound.ANVIL_USE, 1, 1);
          }
        });

    menuSelecor.addMenu(ItemStackUtil.getItem(Message.getMessage("選択して修理(未実装)"), Material.DIAMOND_SPADE, Message.getMessage("修理するアイテムを選択して修理する")),
        15,
        new SelectRunnable() {
          @Override
          public void run(Player p, ItemStack item) {
            p.sendMessage("未実装です");
          }
        });
    menuSelecor.regist();
    MenuSelectorManager.open(p, "Repair Menu");
  }

  public static int getNeedGalion(ItemStack... item) {
    int repairDurability = 0;
    for (ItemStack itemStack : item) {
      // アイテムがないなら無視
      if (itemStack == null) {
        continue;
      }
      Material type = itemStack.getType();
      // 最大耐久が３０以下のアイテムは無視
      if (type.getMaxDurability() < 30) {
        continue;
      }

      double waight = 1;
      short durability = (short) (itemStack.getDurability() * waight);
      repairDurability += Math.min(durability, type.getMaxDurability());
    }
    return (repairDurability);
  }

  /**
   * プレイヤーの全てのアイテムを修理する
   *
   * @param p
   */
  public static void repairItemAll(Player p) {
    TheLowPlayer theLowPlayer = TheLowPlayerManager.getTheLowPlayer(p);
    if (theLowPlayer == null) {
      Message.sendMessage(p, ChatColor.RED + "現在Playerデータをロードしています。もう暫くお待ち下さい");
      return;
    }

    int galion = theLowPlayer.getGalions();

    PlayerInventory inventory = p.getInventory();
    int needGalion = getNeedGalion(inventory.getContents()) + getNeedGalion(inventory.getArmorContents());

    if (galion < needGalion) {
      Message.sendMessage(p, "お金が足りないので修理できません。");
      return;
    }

    // 防具以外のアイテムを修理
    for (int i = 0; i < inventory.getSize(); i++) {
      ItemStack item = inventory.getItem(i);
      if (repairItem(item)) {
        inventory.setItem(i, item);
      }
    }

    // 防具を修理
    ItemStack helmet = inventory.getHelmet();
    if (repairItem(helmet)) {
      inventory.setHelmet(helmet);
    }
    ItemStack chestplate = inventory.getChestplate();
    if (repairItem(chestplate)) {
      inventory.setChestplate(chestplate);
    }
    ItemStack leggings = inventory.getLeggings();
    if (repairItem(leggings)) {
      inventory.setLeggings(leggings);
    }
    ItemStack boots = inventory.getBoots();
    if (repairItem(boots)) {
      inventory.setBoots(boots);
    }

    theLowPlayer.addGalions(-needGalion, GalionEditReason.consume_strength);

    Message.sendMessage(p, "アイテムを修理しました。");
    return;
  }

  /**
   * 指定されたアイテムを修理する
   *
   * @param item
   * @return アイテムを修理したならTRUE
   */
  protected static boolean repairItem(ItemStack item) {
    if (item == null) { return false; }
    Material type = item.getType();
    if (type.getMaxDurability() < 30) { return false; }

    ItemStackNbttagSetter nbttagSetter = new ItemStackNbttagSetter(item);
    // アイテムの消費耐久を０にする
    item.setDurability((short) 0);
    nbttagSetter.setNowDurability(nbttagSetter.getMaxDurability());
    return true;
  }
}
