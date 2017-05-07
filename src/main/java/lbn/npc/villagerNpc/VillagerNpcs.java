package lbn.npc.villagerNpc;

public class VillagerNpcs {
  /**
   * IDから名前を取得。もし存在しない場合はIDをそのまま返す
   * 
   * @param id
   * @return
   */
  public static String getVillagerName(String id) {
    VillagerNpc villagerNpcById = VillagerNpcManager.getVillagerNpcById(id);
    if (villagerNpcById != null) { return villagerNpcById.getName(); }
    return id;
  }
}
