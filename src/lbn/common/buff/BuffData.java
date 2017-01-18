package lbn.common.buff;

import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BuffData {

	private String id;
	private PotionEffectType effect;
	private int second;
	private int level;

	public BuffData(String id, PotionEffectType effect, int second, int level) {
		this.id = id;
		this.effect = effect;
		this.second = second;
		this.level = level;
	}

	public String getId() {
		return id;
	}

	public PotionEffectType getPotionEffectType() {
		return effect;
	}

	public int getSecond() {
		return second;
	}

	public int getLevel() {
		return level;
	}

	public void addBuff(LivingEntity e) {
		e.addPotionEffect(new PotionEffect(effect, second, level), false);
	}

}
