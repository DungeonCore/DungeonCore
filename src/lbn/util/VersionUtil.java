package lbn.util;

import lbn.mob.customEntity1_7.CustomVillager;

import org.bukkit.craftbukkit.v1_8_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.potion.PotionEffectType;

public class VersionUtil {
	public static boolean isCustomVillager(Entity entity) {
		return (((CraftEntity)entity).getHandle() instanceof CustomVillager);
	}

//	public static void setOldPotionEffectType() {
//		OldPotionEffectType[] oldPotionEffectTypeList = {new OldPotionEffectType(PotionEffectType.SPEED, "SPEED"),
//				new OldPotionEffectType(PotionEffectType.SLOW, "SLOW"),
//				new OldPotionEffectType(PotionEffectType.FAST_DIGGING, "FAST_DIGGING"),
//				new OldPotionEffectType(PotionEffectType.SLOW_DIGGING, "SLOW_DIGGING"),
//				new OldPotionEffectType(PotionEffectType.INCREASE_DAMAGE, "INCREASE_DAMAGE"),
//				new OldPotionEffectType(PotionEffectType.HEAL, "HEAL"),
//				new OldPotionEffectType(PotionEffectType.HARM, "HARM"),
//				new OldPotionEffectType(PotionEffectType.JUMP, "JUMP"),
//				new OldPotionEffectType(PotionEffectType.CONFUSION, "CONFUSION"),
//				new OldPotionEffectType(PotionEffectType.REGENERATION, "REGENERATION"),
//				new OldPotionEffectType(PotionEffectType.DAMAGE_RESISTANCE, "DAMAGE_RESISTANCE"),
//				new OldPotionEffectType(PotionEffectType.FIRE_RESISTANCE, "FIRE_RESISTANCE"),
//				new OldPotionEffectType(PotionEffectType.WATER_BREATHING, "WATER_BREATHING"),
//				new OldPotionEffectType(PotionEffectType.INVISIBILITY, "INVISIBILITY"),
//				new OldPotionEffectType(PotionEffectType.BLINDNESS, "BLINDNESS"),
//				new OldPotionEffectType(PotionEffectType.NIGHT_VISION, "NIGHT_VISION"),
//				new OldPotionEffectType(PotionEffectType.HUNGER, "HUNGER"),
//				new OldPotionEffectType(PotionEffectType.WEAKNESS, "WEAKNESS"),
//				new OldPotionEffectType(PotionEffectType.POISON, "POISON"),
//				new OldPotionEffectType(PotionEffectType.WITHER, "WITHER"),
//				new OldPotionEffectType(PotionEffectType.HEALTH_BOOST, "HEALTH_BOOST"),
//				new OldPotionEffectType(PotionEffectType.ABSORPTION, "ABSORPTION"),
//				new OldPotionEffectType(PotionEffectType.SATURATION, "SATURATION")};
//
//		try {
//			Field f1 = PotionEffectType.class.getDeclaredField( "byName" );
//			f1.setAccessible( true );
//			@SuppressWarnings("unchecked")
//			Map<String, PotionEffectType> byName = (Map<String, PotionEffectType>) f1.get(null);
//
//			for (OldPotionEffectType oldPotionEffectType : oldPotionEffectTypeList) {
//				byName.put(oldPotionEffectType.getName(), oldPotionEffectType.getType());
//			}
//
//			Field f2 = PotionEffectType.class.getDeclaredField( "byName" );
//			f2.setAccessible( true );
//			@SuppressWarnings("unchecked")
//			Map<String, PotionEffectType> byNames = (Map<String, PotionEffectType>) f2.get(null);
//		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
//			e.printStackTrace();
//		}
//	}

	static class OldPotionEffectType {
		protected OldPotionEffectType(PotionEffectType type, String name) {
			this.type = type;
			this.name = name;
		}

		String name;
		PotionEffectType type;

		public String getName() {
			return name;
		}

		public PotionEffectType getType() {
			return type;
		}



	}
}
