package lbn.mob.mob.abstractmob.villager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import lbn.common.event.player.PlayerCustomMobSpawnEvent;
import lbn.mob.AbstractCustomMob;
import lbn.mob.customEntity1_7.CustomVillager;
import lbn.quest.quest.TouchVillagerQuest;
import lbn.quest.questData.PlayerQuestSession;
import lbn.quest.questData.PlayerQuestSessionManager;
import lbn.util.DungeonLog;
import lbn.util.JavaUtil;
import lbn.util.LivingEntityUtil;

public abstract class AbstractVillager extends AbstractCustomMob<CustomVillager, Villager>{
	@Override
	protected CustomVillager getInnerEntity(World w) {
		return new CustomVillager(w);
	}

	abstract public Location getLocation();

	@Override
	public void onSpawn(PlayerCustomMobSpawnEvent e) {
		try {
			Villager spawn = (Villager) e.getEntity();
			if (getVillageData() != null) {
				if (getVillageData().isAdult()) {
					spawn.setAdult();
				} else {
					spawn.setBaby();
				}
			} else {
				spawn.setAdult();
//			DungeonLog.errorln(getName() + " dont have villgaer data!!");
			}
			spawn.setCustomName(getName());
			spawn.setCustomNameVisible(true);
			spawn.setRemoveWhenFarAway(false);
			spawn.setMaxHealth(2000.0);
			spawn.setHealth(2000.0);
		} catch (Exception ex) {
			ex.printStackTrace();
			DungeonLog.errorln(e.getEntity() + "(" + getName() + ") ocure error when spawn villager!!");
		}
	}

	protected VillagerData getVillageData() {
		return VillagerData.getInstance(getName());
	}

	@Override
	public EntityType getEntityType() {
		return EntityType.VILLAGER;
	}

	@Override
	public void onDamage(LivingEntity mob, Entity damager, EntityDamageByEntityEvent e) {
		e.setCancelled(true);
	}

	abstract protected List<String> getMessage(Player p, LivingEntity mob);

	@Override
	public void onInteractEntity(PlayerInteractEntityEvent e) {
		e.setCancelled(true);

		Player p = e.getPlayer();
		LivingEntity entity = (LivingEntity) e.getRightClicked();
		//動く村人なら消す
		if (!LivingEntityUtil.isCustomVillager(entity)) {
			Entity rightClicked = e.getRightClicked();
			rightClicked.remove();
			if (p.getGameMode() != GameMode.CREATIVE) {
				JavaUtil.addBonusGold(p, rightClicked.getLocation());
			}
			return;
		}

		//この村人をタッチするクエストで進行中のクエストがあったら他の会話は開始しない
		boolean isDoingTouchQuest = false;
		Set<TouchVillagerQuest> questForVillager = TouchVillagerQuest.getQuestByTargetVillager(entity);

		//メッセージを取得
		List<String> message = new ArrayList<String>();

		//実行中のクエストを調べる
		PlayerQuestSession questSession = PlayerQuestSessionManager.getQuestSession(p);
		for (TouchVillagerQuest touchVillagerQuest : questForVillager) {
			if (questSession.isDoing(touchVillagerQuest)) {
				//クエスト処理
				touchVillagerQuest.onTouchVillager(p, entity, questSession);
				questSession.setQuestData(touchVillagerQuest, 1);
				if (touchVillagerQuest.canFinish(p)) {
					touchVillagerQuest.onSatisfyComplateCondtion(p);
				}
				//メッセージを格納
				message.addAll(Arrays.asList(touchVillagerQuest.talkOnTouch()));
				isDoingTouchQuest = true;
			}
		}

		//もしクエスト実行中でなければ通常の村人のメッセージを出力する
		if (!isDoingTouchQuest) {
			VillagerData villageData = getVillageData();
			if (villageData != null) {
				message = Arrays.asList(villageData.getTexts());
			}
		}
		if (!message.isEmpty()) {
			p.sendMessage("");
			for (String string : message) {
				p.sendMessage(ChatColor.GOLD + string);
			}
		}


	}
}

