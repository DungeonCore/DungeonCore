package lbn.item.SpreadSheetItem;

import java.text.MessageFormat;
import java.util.Arrays;

import lbn.api.LevelType;
import lbn.api.player.TheLowPlayer;
import lbn.api.player.TheLowPlayerManager;
import lbn.common.buff.BuffData;
import lbn.common.buff.BuffDataFactory;
import lbn.common.sound.SoundData;
import lbn.common.sound.SoundManager;
import lbn.item.ItemLoreToken;
import lbn.item.itemAbstract.FoodItem;
import lbn.player.status.StatusAddReason;
import lbn.util.ItemStackUtil;
import lbn.util.particle.ParticleData;
import lbn.util.particle.ParticleManager;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

public class SpreadSheetFoodItem extends FoodItem{

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

		//バフ効果を与える
		BuffData buff1 = BuffDataFactory.getBuffFromId(buffId1);
		if (buff1 != null) {
			buff1.addBuff(player);
		}
		BuffData buff2 = BuffDataFactory.getBuffFromId(buffId2);
		if (buff2 != null) {
			buff2.addBuff(player);
		}
		BuffData buff3 = BuffDataFactory.getBuffFromId(buffId3);
		if (buff3 != null) {
			buff3.addBuff(player);
		}

		//パーティクル
		String particleID = data.getParticle();
		ParticleData particleData = ParticleManager.getParticleData(particleID);
		if (particleData != null) {
			particleData.run(player.getLocation());
		}

		//音
		String soundId = data.getSound();
		SoundData soundData = SoundManager.fromId(soundId);
		if (soundData != null) {
			soundData.playSoundAllPlayer(player.getLocation());
		}

		//EXP付与
		TheLowPlayer theLowPlayer = TheLowPlayerManager.getTheLowPlayer(player);
		if (theLowPlayer != null) {
			for (LevelType levelType : Arrays.asList(LevelType.SWORD, LevelType.BOW, LevelType.MAGIC)) {
				int exp = data.getExp(levelType);
				if (exp != 0) {
					theLowPlayer.addExp(levelType, exp, StatusAddReason.food_eat);
				}
			}
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
	public ItemLoreToken getStandardLoreToken() {
		ItemLoreToken loreToken = super.getStandardLoreToken();

		String buffId1 = data.getBuff1();
		String buffId2 = data.getBuff2();
		String buffId3 = data.getBuff3();

		BuffData buff1 = BuffDataFactory.getBuffFromId(buffId1);
		if (buff1 != null && (int)(buff1.getTick() / 20.0) > 0) {
			loreToken.addLore(MessageFormat.format("{0}(レベル{1})を{2}秒付与", buff1.getPotionEffectType().getName(), (buff1.getLevel() + 1), (int)(buff1.getTick() / 20.0)));
		}
		BuffData buff2 = BuffDataFactory.getBuffFromId(buffId2);
		if (buff2 != null && (int)(buff2.getTick() / 20.0) > 0) {
			loreToken.addLore(MessageFormat.format("{0}(レベル{1})を{2}秒付与", buff2.getPotionEffectType().getName(), (buff2.getLevel() + 1), (int)(buff2.getTick() / 20.0)));
		}
		BuffData buff3 = BuffDataFactory.getBuffFromId(buffId3);
		if (buff3 != null && (int)(buff3.getTick() / 20.0) > 0) {
			loreToken.addLore(MessageFormat.format("{0}(レベル{1})を{2}秒付与", buff3.getPotionEffectType().getName(), (buff3.getLevel() + 1), (int)(buff3.getTick() / 20.0)));
		}
		return loreToken;
	}

}
