package net.l_bulb.dungeoncore.dungeoncore;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;

import net.l_bulb.dungeoncore.command.CommandAnnounce;
import net.l_bulb.dungeoncore.command.CommandAttention;
import net.l_bulb.dungeoncore.command.CommandBook;
import net.l_bulb.dungeoncore.command.CommandChest;
import net.l_bulb.dungeoncore.command.CommandDropItem;
import net.l_bulb.dungeoncore.command.CommandEquipPlayer;
import net.l_bulb.dungeoncore.command.CommandExecuteLockByTimeCommand;
import net.l_bulb.dungeoncore.command.CommandExtraMob;
import net.l_bulb.dungeoncore.command.CommandGetItem;
import net.l_bulb.dungeoncore.command.CommandItem;
import net.l_bulb.dungeoncore.command.CommandMobSkill;
import net.l_bulb.dungeoncore.command.CommandNpcSpawn;
import net.l_bulb.dungeoncore.command.CommandQuestOperate;
import net.l_bulb.dungeoncore.command.CommandStatusView;
import net.l_bulb.dungeoncore.command.CommandTag;
import net.l_bulb.dungeoncore.command.CommandTpOtherWorld;
import net.l_bulb.dungeoncore.command.CommandViewInfo;
import net.l_bulb.dungeoncore.command.JumpCommand;
import net.l_bulb.dungeoncore.command.MobCommand;
import net.l_bulb.dungeoncore.command.MoneyCommand;
import net.l_bulb.dungeoncore.command.OpenCommand;
import net.l_bulb.dungeoncore.command.PlayerStatusCommand;
import net.l_bulb.dungeoncore.command.QuestCommand;
import net.l_bulb.dungeoncore.command.SetDungeonCommand;
import net.l_bulb.dungeoncore.command.SetSpawnPointCommand;
import net.l_bulb.dungeoncore.command.ShopCommand;
import net.l_bulb.dungeoncore.command.SpletSheetCommand;
import net.l_bulb.dungeoncore.command.StrengthItemCommand;
import net.l_bulb.dungeoncore.command.TpCutCommand;
import net.l_bulb.dungeoncore.command.VillagerCommand;
import net.l_bulb.dungeoncore.command.notUsually.command.CustomEffectCommand;
import net.l_bulb.dungeoncore.command.notUsually.command.CustomKillCommand;
import net.l_bulb.dungeoncore.command.notUsually.command.CustomSetBlockCommand;
import net.l_bulb.dungeoncore.command.notUsually.command.ExecuteMobSkillCommand;
import net.l_bulb.dungeoncore.command.util.CommandSavePlayer;
import net.l_bulb.dungeoncore.command.util.CommandSequencemove;
import net.l_bulb.dungeoncore.command.util.CommandSpecialSign;
import net.l_bulb.dungeoncore.command.util.DelayCommand;
import net.l_bulb.dungeoncore.command.util.LoopCommand;
import net.l_bulb.dungeoncore.command.util.RandomExecuteCommand;
import net.l_bulb.dungeoncore.command.util.RemoveMobCommand;
import net.l_bulb.dungeoncore.command.util.SearchPathCommand;
import net.l_bulb.dungeoncore.command.util.SequenceCommand;
import net.l_bulb.dungeoncore.command.util.SequenceSetBlockCommand;
import net.l_bulb.dungeoncore.command.util.SetRedStoneBlockCommand;
import net.l_bulb.dungeoncore.command.util.ShopItemCommand;
import net.l_bulb.dungeoncore.command.util.SimplySetSpawnPointCommand;
import net.l_bulb.dungeoncore.command.util.SoundPlayCommand;
import net.l_bulb.dungeoncore.command.util.TimerExcuteCommand;
import net.l_bulb.dungeoncore.command.util.TmCommand;
import net.l_bulb.dungeoncore.command.util.ToggleSetBlockCommand;
import net.l_bulb.dungeoncore.command.util.UsageCommandable;

public class CommandRegister {
  public static void registCommand() {
    registCommand(new CommandAnnounce(), "announce");
    registCommand(new CommandAttention(), "attention");
    registCommand(new CommandGetItem(), "getItem");
    registCommand(new CommandExtraMob(), "spawnmob");
    registCommand(new CommandViewInfo(), "viewinfo");
    registCommand(new CommandStatusView(), "statusview");
    registCommand(new PlayerStatusCommand(), "statusCommand");
    registCommand(new SetSpawnPointCommand(), "set_spawn_point");
    registCommand(new StrengthItemCommand(), "strength_item");
    registCommand(new CommandSpecialSign(), "set_special_sign");
    registCommand(new DelayCommand(), "DELAYCOMMAND");
    registCommand(new SequenceCommand(), "SEQUENCECOMMAND");
    registCommand(new SequenceSetBlockCommand(), "SEQUENCE_SETBLOCK");
    registCommand(new SoundPlayCommand(), "soundPlay");
    registCommand(new TimerExcuteCommand(), "timer_command");
    registCommand(new CommandQuestOperate(), "quest_operate");
    registCommand(new QuestCommand(), "quest");
    registCommand(new TmCommand(), "tm");
    registCommand(new MoneyCommand(), "galion");
    registCommand(new ShopCommand(), "shop");
    registCommand(new VillagerCommand(), "villager");
    registCommand(new CommandChest(), "chest");
    registCommand(new RemoveMobCommand(), "removemob");
    registCommand(new MobCommand(), "mob");
    registCommand(new SimplySetSpawnPointCommand(), "setSpawn");
    registCommand(new CommandMobSkill(), "mobskill");
    registCommand(new CommandItem(), "item");
    registCommand(new ShopItemCommand(), "shopitem");
    registCommand(new CommandSavePlayer(), "saveplayer");
    registCommand(new SearchPathCommand(), "searchPath");
    registCommand(new SetDungeonCommand(), "setdungeon");
    registCommand(new TpCutCommand(), "tpcut");
    registCommand(new CommandEquipPlayer(), "equip");
    registCommand(new CommandExecuteLockByTimeCommand(), "timelock");
    registCommand(new ToggleSetBlockCommand(), "toggleSetblock");
    registCommand(new CommandTpOtherWorld(), "tpworld");
    registCommand(new SpletSheetCommand(), "sheet_reload");
    registCommand(new SetRedStoneBlockCommand(), "setredstone");
    registCommand(new LoopCommand(), "loop");
    registCommand(new RandomExecuteCommand(), "randomExec");
    registCommand(new JumpCommand(), "jumpboost");
    registCommand(new OpenCommand(), "open");
    registCommand(new CommandBook(), "book");
    registCommand(new CommandNpcSpawn(), "spawnNpc");
    registCommand(new CommandTag(), "tag");
    registCommand(new CommandSequencemove(), "sequencemove");
    registCommand(new CommandDropItem(), "dropItem");

    new ExecuteMobSkillCommand().regist();
    new CustomKillCommand().regist();
    new CustomEffectCommand().regist();
    new CustomSetBlockCommand().regist();
  }

  private static void registCommand(CommandExecutor instance, String name) {
    PluginCommand command = Main.plugin.getCommand(name);
    command.setExecutor(instance);
    if (instance instanceof TabCompleter) {
      command.setTabCompleter((TabCompleter) instance);
    }
    if (instance instanceof UsageCommandable) {
      command.setUsage(((UsageCommandable) instance).getUsage());
      command.setDescription(((UsageCommandable) instance).getDescription());
    }
  }
}
