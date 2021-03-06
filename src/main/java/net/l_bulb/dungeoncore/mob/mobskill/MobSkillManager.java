package net.l_bulb.dungeoncore.mob.mobskill;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import net.l_bulb.dungeoncore.dungeoncore.SpletSheet.MobSkillSheetRunnable;
import net.l_bulb.dungeoncore.dungeoncore.SpletSheet.SpletSheetExecutor;

public class MobSkillManager {
  static HashMap<String, MobSkillInterface> skillMap = new HashMap<>();

  public static void registSkill(MobSkillInterface skill) {
    skillMap.put(skill.getName(), skill);
  }

  public static MobSkillInterface fromId(String id) {
    return skillMap.get(id);
  }

  public static Set<MobSkillInterface> getSkill(Set<String> nameList, MobSkillExcuteConditionType timing) {
    HashSet<MobSkillInterface> skillSet = new HashSet<>();
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
