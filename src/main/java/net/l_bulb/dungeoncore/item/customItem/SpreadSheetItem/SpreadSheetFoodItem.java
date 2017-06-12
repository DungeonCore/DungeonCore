package net.l_bulb.dungeoncore.item.customItem.SpreadSheetItem;

import java.text.MessageFormat;
import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.api.LevelType;
import net.l_bulb.dungeoncore.api.player.TheLowPlayer;
import net.l_bulb.dungeoncore.api.player.TheLowPlayerManager;
import net.l_bulb.dungeoncore.common.buff.BuffData;
import net.l_bulb.dungeoncore.common.buff.BuffDataFactory;
import net.l_bulb.dungeoncore.common.particle.ParticleData;
import net.l_bulb.dungeoncore.common.particle.ParticleManager;
import net.l_bulb.dungeoncore.common.sound.SoundData;
import net.l_bulb.dungeoncore.common.sound.SoundManager;
import net.l_bulb.dungeoncore.item.customItem.itemAbstract.FoodItem;
import net.l_bulb.dungeoncore.item.nbttag.ItemStackNbttagAccessor;
import net.l_bulb.dungeoncore.item.system.lore.ItemLoreToken;
import net.l_bulb.dungeoncore.player.customplayer.MagicPointManager;
import net.l_bulb.dungeoncore.player.status.StatusAddReason;
import net.l_bulb.dungeoncore.util.ItemStackUtil;
import net.l_bulb.dungeoncore.util.TheLowExecutor;

public class SpreadSheetFoodItem extends FoodItem {

  FoodItemData data;

  public SpreadSheetFoodItem(FoodItemData data) {
    this.data = data;
  }

  @Override
  public void onPlayerItemConsumeEvent(PlayerItemConsumeEvent event) {
    String buffId1 = data.getBuff1();
    String buffId2 = data.getBuff2();
    String buffId3 = data.getBuff3();

    Player player = event.getPlayer();

    // バフ効果を与える
    BuffData buff1 = BuffDataFactory.getBuffFromId(buffId1);
    if (buff1 != null) {
      TheLowExecutor.executeLater(data.getBuff1DelayTick(), () -> buff1.addBuff(player));
    }
    BuffData buff2 = BuffDataFactory.getBuffFromId(buffId2);
    if (buff2 != null) {
      TheLowExecutor.executeLater(data.getBuff2DelayTick(), () -> buff2.addBuff(player));
    }
    BuffData buff3 = BuffDataFactory.getBuffFromId(buffId3);
    if (buff3 != null) {
      TheLowExecutor.executeLater(data.getBuff3DelayTick(), () -> buff3.addBuff(player));
    }

    // パーティクル
    String particleID = data.getParticle();
    ParticleData particleData = ParticleManager.getParticleData(particleID);
    if (particleData != null) {
      particleData.run(player.getLocation());
    }

    // 音
    String soundId = data.getSound();
    SoundData soundData = SoundManager.fromId(soundId);
    if (soundData != null) {
      soundData.playSoundAllPlayer(player.getLocation());
    }

    // EXP付与
    TheLowPlayer theLowPlayer = TheLowPlayerManager.getTheLowPlayer(player);
    if (theLowPlayer != null) {
      for (LevelType levelType : Arrays.asList(LevelType.SWORD, LevelType.BOW, LevelType.MAGIC)) {
        int exp = data.getExp(levelType);
        if (exp != 0) {
          theLowPlayer.addExp(levelType, exp, StatusAddReason.food_eat);
        }
      }
    }

    // MPを回復させる
    if (data.getRecoveryMp() > 0) {
      MagicPointManager.addMagicPoint(player, data.getRecoveryMp());
    } else if (data.getRecoveryMp() < 0) {
      MagicPointManager.consumeMagicPoint(player, data.getRecoveryMp());
    }
  }

  @Override
  public String getItemName() {
    return data.getName();
  }

  @Override
  public String getId() {
    return data.getId();
  }

  @Override
  public int getBuyPrice(ItemStack item) {
    return data.getPrice();
  }

  ItemStack itemStackByCommand;

  @Override
  protected ItemStack getItemStackBase() {
    if (itemStackByCommand == null) {
      itemStackByCommand = ItemStackUtil.getItemStackByCommand(data.getCommand());
    }
    return itemStackByCommand.clone();
  }

  @Override
  protected Material getMaterial() {
    return getItemStackBase().getType();
  }

  @Override
  public String[] getDetail() {
    return data.getDetail();
  }

  @Override
  public ItemLoreToken getStandardLoreToken(ItemStackNbttagAccessor newParam) {
    ItemLoreToken loreToken = super.getStandardLoreToken(newParam);

    String buffId1 = data.getBuff1();
    String buffId2 = data.getBuff2();
    String buffId3 = data.getBuff3();

    BuffData buff1 = BuffDataFactory.getBuffFromId(buffId1);
    if (buff1 != null && (buff1.getTick() / 20.0) > 0) {
      loreToken.addLore(MessageFormat.format("{0}(レベル{1})を{2}秒付与", buff1.getEffect().getName(), (buff1.getLevel() + 1),
          (int) (buff1.getTick() / 20.0)));
    }
    BuffData buff2 = BuffDataFactory.getBuffFromId(buffId2);
    if (buff2 != null && (int) (buff2.getTick() / 20.0) > 0) {
      loreToken.addLore(MessageFormat.format("{0}(レベル{1})を{2}秒付与", buff2.getEffect().getName(), (buff2.getLevel() + 1),
          (int) (buff2.getTick() / 20.0)));
    }
    BuffData buff3 = BuffDataFactory.getBuffFromId(buffId3);
    if (buff3 != null && (int) (buff3.getTick() / 20.0) > 0) {
      loreToken.addLore(MessageFormat.format("{0}(レベル{1})を{2}秒付与", buff3.getEffect().getName(), (buff3.getLevel() + 1),
          (int) (buff3.getTick() / 20.0)));
    }

    // mp回復
    if (data.getRecoveryMp() != 0) {
      loreToken.addLore("MP + " + data.getRecoveryMp());
    }

    // exp追加
    for (LevelType levelType : Arrays.asList(LevelType.SWORD, LevelType.BOW, LevelType.MAGIC)) {
      int exp = data.getExp(levelType);
      if (exp != 0) {
        loreToken.addLore(MessageFormat.format("{0} + {1} exp", levelType.getName(), exp));
      }
    }

    return loreToken;
  }
}
