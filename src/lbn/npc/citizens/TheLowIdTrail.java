package lbn.npc.citizens;

import net.citizensnpcs.api.exception.NPCLoadException;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.util.DataKey;

public class TheLowIdTrail extends Trait{

	private static final String THE_LOW_NPC_ID = "TheLowNpcId";

	public TheLowIdTrail() {
		super("THE_LOW_NPC_ID");
	}

	String id = null;

	@Override
	public void load(DataKey key) throws NPCLoadException {
		id = key.getString(THE_LOW_NPC_ID);
	}

	@Override
	public void save(DataKey key) {
		key.setString(THE_LOW_NPC_ID, id);
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public static TheLowIdTrail fromId(String id) {
		TheLowIdTrail theLowIdTrail = new TheLowIdTrail();
		theLowIdTrail.setId(id);
		return theLowIdTrail;
	}

}
