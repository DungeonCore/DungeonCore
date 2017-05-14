package net.l_bulb.dungeoncore.item.customItem.armoritem;

import org.bukkit.ChatColor;

import net.l_bulb.dungeoncore.api.LevelType;
import net.l_bulb.dungeoncore.dungeon.contents.strength_template.StrengthTemplate;
import net.l_bulb.dungeoncore.item.customItem.AbstractItem;
import net.l_bulb.dungeoncore.item.itemInterface.ArmorItemable;
import net.l_bulb.dungeoncore.item.itemInterface.StrengthChangeItemable;
import net.l_bulb.dungeoncore.item.system.lore.ItemLoreToken;
import net.l_bulb.dungeoncore.util.Message;

public abstract class AbstractArmorItem extends AbstractItem implements ArmorItemable, StrengthChangeItemable {
  @Override
  public LevelType getLevelType() {
    return LevelType.MAIN;
  }

  @Override
  public StrengthTemplate getStrengthTemplate() {
    return new ArmorStrengthTemplate();
  }

  @Override
  public int getMaxStrengthCount() {
    return 10;
  }

  /**
   * 加算される耐久力
   *
   * @param level
   * @return
   */
  public short getAddDurability(int level) {
    return (short) (level * 25);
  }

  @Override
  public ItemLoreToken getStandardLoreToken() {
    ItemLoreToken loreToken = super.getStandardLoreToken();
    // 使用可能レベル
    loreToken.addLore(Message.getMessage("使用可能 ： {2}{0}{1}以上", LevelType.MAIN.getName(), getAvailableLevel(), ChatColor.GOLD));
    // 防御ポイント
    loreToken.addLore(Message.getMessage("Mobに対する防具ポイント ： +{0}", getArmorPointForNormalMob()));
    loreToken.addLore(Message.getMessage("Bossに対する防具ポイント ： +{0}", getArmorPointForBossMob()));
    return loreToken;
  }

}
