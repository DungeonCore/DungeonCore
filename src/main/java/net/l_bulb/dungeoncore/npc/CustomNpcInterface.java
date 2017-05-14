package net.l_bulb.dungeoncore.npc;

import org.bukkit.entity.EntityType;

import net.citizensnpcs.api.event.NPCDamageEvent;
import net.citizensnpcs.api.event.NPCLeftClickEvent;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;

public interface CustomNpcInterface {

  /**
   * 右クリック時の処理
   * 
   * @param e
   */
  public abstract void onNPCRightClickEvent(NPCRightClickEvent e);

  public abstract EntityType getEntityType();

  /**
   * 左クリック時の処理
   * 
   * @param e
   */
  public abstract void onNPCLeftClickEvent(NPCLeftClickEvent e);

  public abstract void onNPCDamageEvent(NPCDamageEvent e);

  public abstract String getId();

  public String getName();

  /**
   * NPCを取得する
   * 
   * @return
   */
  public NPC getNpc();

  /**
   * NPCをセットする
   * 
   * @param npc
   */
  public void setNpc(NPC npc);

}