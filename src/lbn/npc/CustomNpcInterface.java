package lbn.npc;

import net.citizensnpcs.api.event.NPCDamageEvent;
import net.citizensnpcs.api.event.NPCLeftClickEvent;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.event.NPCSpawnEvent;

import org.bukkit.entity.EntityType;

public interface CustomNpcInterface {

	/**
	 * 右クリック時の処理
	 * @param e
	 */
	public abstract void onNPCRightClickEvent(NPCRightClickEvent e);

	/**
	 * reload時にも呼ばれる。もしすでに同じNPCが存在した場合、それらを削除する
	 * @param e
	 */
	public abstract void onSpawn(NPCSpawnEvent e);

	public abstract EntityType getEntityType();

	/**
	 * 左クリック時の処理
	 * @param e
	 */
	public abstract void onNPCLeftClickEvent(NPCLeftClickEvent e);

	public abstract void onNPCDamageEvent(NPCDamageEvent e);

	public abstract String getId();

	public String getName();

}