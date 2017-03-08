package lbn.npc;

public class VillagerNpcs {
	/**
	 * IDから名前を取得。もし存在しない場合はIDをそのまま返す
	 * @param id
	 * @return
	 */
	public static String getVillagerName(String id) {
		VillagerNpc villagerNpcById = NpcManager.getVillagerNpcById(id);
		if (villagerNpcById != null) {
			return villagerNpcById.getName();
		}
		return id;
	}
}
