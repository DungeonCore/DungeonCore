package lbn.command.notUsually.command;

import java.util.HashMap;

import lbn.command.notUsually.AbstractVanillaCommand;
import net.minecraft.server.v1_8_R1.ChatMessage;
import net.minecraft.server.v1_8_R1.CommandEffect;
import net.minecraft.server.v1_8_R1.CommandException;
import net.minecraft.server.v1_8_R1.EntityLiving;
import net.minecraft.server.v1_8_R1.ExceptionInvalidNumber;
import net.minecraft.server.v1_8_R1.ExceptionUsage;
import net.minecraft.server.v1_8_R1.ICommandListener;
import net.minecraft.server.v1_8_R1.MobEffect;
import net.minecraft.server.v1_8_R1.MobEffectList;

public class CustomEffectCommand extends AbstractVanillaCommand{

	public CustomEffectCommand() {
		super(new CommandEffect());
	}

	@Override
	protected void execute2(ICommandListener paramICommandListener,
			String[] paramArrayOfString) throws CommandException{
		if (paramArrayOfString.length < 2) {
			throw new ExceptionUsage("commands.effect.usage", new Object[0]);
		}

		//1.7の方の名前を使う
		String effectName = getEffectName(paramArrayOfString[1]);
		paramArrayOfString[1] = effectName;

		EntityLiving localEntityLiving = (EntityLiving) a(
				paramICommandListener, paramArrayOfString[0],
				EntityLiving.class);
		if (paramArrayOfString[1].equals("clear")) {
			if (localEntityLiving.getEffects().isEmpty()) {
				throw new CommandException(
						"commands.effect.failure.notActive.all",
						new Object[] { localEntityLiving.getName() });
			}
			localEntityLiving.removeAllEffects();
			a(paramICommandListener, this,
					"commands.effect.success.removed.all",
					new Object[] { localEntityLiving.getName() });
			return;
		}
		int i;
		try {
			i = a(paramArrayOfString[1], 1);
		} catch (Exception localExceptionInvalidNumber) {
			MobEffectList localMobEffectList1 = MobEffectList
					.b(paramArrayOfString[1]);
			if (localMobEffectList1 == null) {
				throw localExceptionInvalidNumber;
			}
			i = localMobEffectList1.id;
		}
		int j = 600;
		int k = 30;
		int m = 0;
		if ((i < 0) || (i >= MobEffectList.byId.length)
				|| (MobEffectList.byId[i] == null)) {
			throw new ExceptionInvalidNumber("commands.effect.notFound",
					new Object[] { Integer.valueOf(i) });
		}
		MobEffectList localMobEffectList2 = MobEffectList.byId[i];
		if (paramArrayOfString.length >= 3) {
			k = a(paramArrayOfString[2], 0, 1000000);
			if (localMobEffectList2.isInstant()) {
				j = k;
			} else {
				j = k * 20;
			}
		} else if (localMobEffectList2.isInstant()) {
			j = 1;
		}
		if (paramArrayOfString.length >= 4) {
			m = a(paramArrayOfString[3], 0, 255);
		}
		boolean bool = true;
		if ((paramArrayOfString.length >= 5)
				&& ("true".equalsIgnoreCase(paramArrayOfString[4]))) {
			bool = false;
		}
		if (k > 0) {
			MobEffect localMobEffect = new MobEffect(i, j, m, false, bool);
			localEntityLiving.addEffect(localMobEffect);
			a(paramICommandListener, this, "commands.effect.success",
					new Object[] {
							new ChatMessage(localMobEffect.g(), new Object[0]),
							Integer.valueOf(i), Integer.valueOf(m),
							localEntityLiving.getName(), Integer.valueOf(k) });
			return;
		}
		if (localEntityLiving.hasEffect(i)) {
			localEntityLiving.removeEffect(i);
			a(paramICommandListener,
					this,
					"commands.effect.success.removed",
					new Object[] {
							new ChatMessage(localMobEffectList2.a(),
									new Object[0]), localEntityLiving.getName() });
		} else {
			throw new CommandException(
					"commands.effect.failure.notActive",
					new Object[] {
							new ChatMessage(localMobEffectList2.a(),
									new Object[0]), localEntityLiving.getName() });
		}
	}

	static HashMap<String, String> nameMap = new HashMap<String, String>();
	static {
		nameMap.put("SPEED", "minecraft:speed");
		nameMap.put("SLOW", "minecraft:slowness");
		nameMap.put("FAST_DIGGING", "minecraft:haste");
		nameMap.put("SLOW_DIGGING", "minecraft:mining_fatigue");
		nameMap.put("INCREASE_DAMAGE", "minecraft:strength");
		nameMap.put("HEAL", "minecraft:instant_health");
		nameMap.put("HARM", "minecraft:instant_damage");
		nameMap.put("JUMP", "minecraft:jump_boost");
		nameMap.put("CONFUSION", "minecraft:nausea");
		nameMap.put("REGENERATION", "minecraft:regeneration");
		nameMap.put("DAMAGE_RESISTANCE", "minecraft:resistance");
		nameMap.put("FIRE_RESISTANCE", "minecraft:fire_resistance");
		nameMap.put("WATER_BREATHING", "minecraft:water_breathing");
		nameMap.put("INVISIBILITY", "minecraft:invisibility");
		nameMap.put("BLINDNESS", "minecraft:blindness");
		nameMap.put("NIGHT_VISION", "minecraft:night_vision");
		nameMap.put("HUNGER", "minecraft:hunger");
		nameMap.put("WEAKNESS", "minecraft:weakness");
		nameMap.put("POISON", "minecraft:poison");
		nameMap.put("WITHER", "minecraft:wither");
		nameMap.put("HEALTH_BOOST", "minecraft:health_boost");
		nameMap.put("ABSORPTION", "minecraft:absorption");
		nameMap.put("SATURATION", "minecraft:saturation");
	}

	private String getEffectName(String effect) {
		if (nameMap.containsKey(effect.toUpperCase())) {
			return nameMap.get(effect.toUpperCase());
		}
		return effect;
	}

}
