package main.mob.mobskill;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import main.lbn.SpletSheet.MobSkillSheetRunnable;
import main.lbn.SpletSheet.SpletSheetExecutor;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class MobSkillManager {
	static HashMap<String, MobSkillInterface> skillMap = new HashMap<String, MobSkillInterface>();

	public static void registSkill(MobSkillInterface skill) {
		skillMap.put(skill.getName(), skill);
	}

	public static Set<MobSkillInterface> getSkill(Set<String> nameList, MobSkillExcuteConditionType timing) {
		HashSet<MobSkillInterface> skillSet = new HashSet<MobSkillInterface>();
		for (String string : nameList) {
			if (skillMap.containsKey(string)) {
				MobSkillInterface skill = skillMap.get(string);
				if (skill.getCondtion() == timing) {
					skillSet.add(skill);
				}
			}
		}
		return skillSet;
	}

	public static void clear() {
		skillMap.clear();
	}

	public static void reloadDataByCommand(CommandSender send) {
		MobSkillSheetRunnable sheetRunnable = new MobSkillSheetRunnable(send);
		if (sheetRunnable.isTransaction()) {
			send.sendMessage("他の人が実行中です。終わってから実行してください。");
			return;
		}
		clear();
		SpletSheetExecutor.onExecute(sheetRunnable);
	}

	public static void reloadDataBySystem() {
		MobSkillSheetRunnable sheetRunnable = new MobSkillSheetRunnable(Bukkit.getConsoleSender());
		clear();
		SpletSheetExecutor.onExecute(sheetRunnable);
	}
}
