package net.l_bulb.dungeoncore.npc.villagerNpc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import net.l_bulb.dungeoncore.money.BuyerShopSelector;
import net.l_bulb.dungeoncore.npc.CustomNpcInterface;
import net.l_bulb.dungeoncore.npc.NpcManager;
import net.l_bulb.dungeoncore.npc.gui.StrengthMenu;
import net.l_bulb.dungeoncore.player.magicstoneOre.trade.MagicStoneTrade;
import net.l_bulb.dungeoncore.player.reincarnation.ReincarnationFactor;
import net.l_bulb.dungeoncore.quest.Quest;
import net.l_bulb.dungeoncore.quest.QuestManager;
import net.l_bulb.dungeoncore.quest.QuestProcessingStatus;
import net.l_bulb.dungeoncore.quest.abstractQuest.TakeItemQuest;
import net.l_bulb.dungeoncore.quest.abstractQuest.TouchVillagerQuest;
import net.l_bulb.dungeoncore.quest.questData.PlayerQuestSession;
import net.l_bulb.dungeoncore.quest.questData.PlayerQuestSessionManager;
import net.l_bulb.dungeoncore.quest.viewer.QuestSelectorViewer;
import net.l_bulb.dungeoncore.util.JavaUtil;
import net.l_bulb.dungeoncore.util.QuestUtil;

import net.citizensnpcs.api.event.DespawnReason;
import net.citizensnpcs.api.event.NPCDamageEvent;
import net.citizensnpcs.api.event.NPCLeftClickEvent;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.Age;
import net.citizensnpcs.trait.ZombieModifier;

public class VillagerNpc implements CustomNpcInterface {
  VillagerData data;

  public VillagerNpc(VillagerData data) {
    this.data = data;
  }

  public String getData() {
    return data.getData();
  }

  protected List<String> getMessage(Player p, Entity mob) {
    return Arrays.asList(data.getTexts());
  }

  NPC npc;

  @Override
  public void setNpc(NPC npc) {
    this.data.location = npc.getStoredLocation();
    this.npc = npc;
    // NPCを更新する
    updateNpc();
  }

  @Override
  public void onNPCRightClickEvent(NPCRightClickEvent e) {
    Player p = e.getClicker();

    Entity entity = e.getNPC().getEntity();
    Set<TouchVillagerQuest> questForVillager = TouchVillagerQuest.getQuestByTargetVillager(entity);

    // メッセージを取得
    List<String> message = new ArrayList<>();

    boolean isDoingTouchQuest = false;

    PlayerQuestSession questSession = PlayerQuestSessionManager.getQuestSession(p);
    for (TouchVillagerQuest touchVillagerQuest : questForVillager) {
      // クエスト実行中なら処理を行う
      if (questSession.getProcessingStatus(touchVillagerQuest) == QuestProcessingStatus.PROCESSING) {
        // 村人にタッチしたときの処理を実行
        touchVillagerQuest.onTouchVillager(p, entity, questSession);
        // データを記録
        questSession.setQuestData(touchVillagerQuest, 1);
        touchVillagerQuest.onSatisfyComplateCondtion(p);
        // メッセージを格納
        message.addAll(Arrays.asList(touchVillagerQuest.talkOnTouch()));
        isDoingTouchQuest = true;
      }
    }

    // もしクエスト実行中でなければ通常の村人のメッセージを出力する
    if (!isDoingTouchQuest) {
      message = getMessage(p, entity);
    }

    // メッセージを出力する
    if (!message.isEmpty()) {
      QuestUtil.sendMessageByVillager(p, message.toArray(new String[0]));
    }
  }

  // /**
  // * NPCをスポーンさせる
  // * @param loc
  // * @return
  // */
  // public NPC spawn(Location loc) {
  // NPC npc = CitizensAPI.getNPCRegistry().createNPC(getEntityType(), getName());
  // npc.spawn(loc);
  // if (npc != null) {
  // remove();
  // }
  // this.npc = npc;
  // updateNpc();
  // return npc;
  // }

  /**
   * デスポーンさせる
   */
  public void despawn() {
    if (npc == null) { return; }
    if (npc.isSpawned()) {
      npc.despawn(DespawnReason.PLUGIN);
    }
  }

  /**
   * NPCを削除する
   */
  public void remove() {
    despawn();
    if (npc != null) {
      npc.destroy();
    }
    npc = null;
  }

  @Override
  public EntityType getEntityType() {
    return data.getEntityType();
  }

  public void updateNpc() {
    if (npc == null || npc.getEntity() == null) { return; }
    VillagerData villagerData = getVillagerData();
    if (villagerData == null) { return; }
    // EntityTypeを変更する
    if (villagerData.getEntityType() != npc.getEntity().getType()) {
      npc.setBukkitEntityType(villagerData.getEntityType());
    }
    if (getEntityType() == EntityType.PLAYER && villagerData.getSkin() != null) {
      // skinの適用
      npc.data().setPersistent("player-skin-name", villagerData.getSkin());
    }

    // 子供にする
    if (!data.isAdult()) {
      // ageableの処理
      if (npc.getEntity() instanceof Ageable) {
        Age trait = npc.getTrait(Age.class);
        trait.setAge(-24000);
        npc.addTrait(trait);
      }

      // ZombieかPigManの処理
      if (npc.getEntity().getType() == EntityType.ZOMBIE || npc.getEntity().getType() == EntityType.PIG_ZOMBIE) {
        boolean toggleBaby = npc.getTrait(ZombieModifier.class).toggleBaby();
        if (!toggleBaby) {
          npc.getTrait(ZombieModifier.class).toggleBaby();
        }
      }
    }

    if (npc.isSpawned()) {
      npc.despawn(DespawnReason.PENDING_RESPAWN);
      npc.spawn(npc.getStoredLocation());
    }
  }

  /*
   * (非 Javadoc)
   * 
   * @see lbn.npc.CustomNpcInterface#onNPCLeftClickEvent(net.citizensnpcs.api.event.NPCLeftClickEvent)
   */
  @Override
  public void onNPCLeftClickEvent(NPCLeftClickEvent e) {
    Player p = e.getClicker();

    // クエスト終了の村人ならここでクエストを終了させる
    boolean existTouchQuest = false;
    PlayerQuestSession session = PlayerQuestSessionManager.getQuestSession(p);
    Collection<Quest> doingQuestList = session.getDoingQuestList();
    for (Quest quest : doingQuestList) {
      // もし終了の村人がこの村人でないなら無視
      if (!getId().equals(quest.getEndVillagerId())) {
        continue;
      }

      // アイテムを持ってくるクエストなら処理を行う
      if (TakeItemQuest.isTakeItemQuest(quest)) {
        ((TakeItemQuest) quest).onTouchVillager(p, e.getNPC().getEntity(), session);
        existTouchQuest = true;
      }

      // もし処理を全て終わらせているなら完了にする
      if (session.getProcessingStatus(quest) == QuestProcessingStatus.PROCESS_END) {
        QuestManager.complateQuest(quest, p, false);
        existTouchQuest = true;
      }

    }

    // 村人タッチのクエストが存在したらここで終了
    if (existTouchQuest) { return; }

    switch (JavaUtil.getNull(data.getType(), VillagerType.NORMAL)) {
      case NORMAL:
        QuestSelectorViewer.openSelector(this, p);
        break;
      case SHOP:
        BuyerShopSelector.onOpen(p, NpcManager.getId(e.getNPC()));
        break;
      case BLACKSMITH:
        StrengthMenu.open(p, this);
        break;
      case REINC:
        ReincarnationFactor.openReincarnationInv(p);
        break;
      case MAGIC_ORE:
        MagicStoneTrade.open(p);
        break;
      default:
        break;
    }
  }

  @Override
  public String getName() {
    return data.getName();
  }

  @Override
  public void onNPCDamageEvent(NPCDamageEvent e) {
    // e.setCancelled(true);
  }

  public Location getLocation() {
    if (npc == null) { return data.getLocation(); }
    return npc.getStoredLocation();
  }

  public VillagerData getVillagerData() {
    return data;
  }

  @Override
  public String getId() {
    return data.getId();
  }

  @Override
  public NPC getNpc() {
    return npc;
  }

}
