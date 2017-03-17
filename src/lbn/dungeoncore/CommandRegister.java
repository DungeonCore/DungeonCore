package lbn.dungeoncore;

import lbn.command.CommandAnnounce;
import lbn.command.CommandAttention;
import lbn.command.CommandBook;
import lbn.command.CommandChest;
import lbn.command.CommandEquipPlayer;
import lbn.command.CommandExecuteLockByTimeCommand;
import lbn.command.CommandExtraMob;
import lbn.command.CommandGiveItem;
import lbn.command.CommandGiveSetItem;
import lbn.command.CommandItem;
import lbn.command.CommandMobSkill;
import lbn.command.CommandQuestOperate;
import lbn.command.CommandStatusView;
import lbn.command.CommandTpOtherWorld;
import lbn.command.CommandViewInfo;
import lbn.command.JumpCommand;
import lbn.command.MobCommand;
import lbn.command.MobSkillExecuteCommand;
import lbn.command.MoneyCommand;
import lbn.command.OpenCommand;
import lbn.command.PlayerStatusCommand;
import lbn.command.QuestCommand;
import lbn.command.SetDungeonCommand;
import lbn.command.SetSpawnPointCommand;
import lbn.command.ShopCommand;
import lbn.command.SpletSheetCommand;
import lbn.command.StrengthItemCommand;
import lbn.command.TpCutCommand;
import lbn.command.VillagerCommand;
import lbn.command.notUsually.command.CustomEffectCommand;
import lbn.command.notUsually.command.CustomKillCommand;
import lbn.command.notUsually.command.CustomSetBlockCommand;
import lbn.command.notUsually.command.ExecuteMobSkillCommand;
import lbn.command.util.ChangeBiomeCommand;
import lbn.command.util.CommandSavePlayer;
import lbn.command.util.CommandSpecialSign;
import lbn.command.util.DelayCommand;
import lbn.command.util.LoopCommand;
import lbn.command.util.RandomExecuteCommand;
import lbn.command.util.RemoveMobCommand;
import lbn.command.util.SearchPathCommand;
import lbn.command.util.SequenceCommand;
import lbn.command.util.SequenceSetBlockCommand;
import lbn.command.util.SetRedStoneBlockCommand;
import lbn.command.util.ShopItemCommand;
import lbn.command.util.SimplySetSpawnPointCommand;
import lbn.command.util.SoundPlayCommand;
import lbn.command.util.TimerExcuteCommand;
import lbn.command.util.TmCommand;
import lbn.command.util.ToggleSetBlockCommand;
import lbn.command.util.UsageCommandable;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;

public class CommandRegister {
	public static void registCommand() {
		registCommand(new CommandAnnounce(), "announce");
		registCommand(new CommandAttention(), "attention");
		registCommand(new CommandGiveItem(), "getItem");
		registCommand(new CommandGiveSetItem(), "getSetItem");
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
		registCommand(new ChangeBiomeCommand(), "change_biome");
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
		registCommand(new MobSkillExecuteCommand(), "mobskillexcute");
		registCommand(new SpletSheetCommand(), "sheet_reload");
		registCommand(new SetRedStoneBlockCommand(), "setredstone");
		registCommand(new LoopCommand(), "loop");
		registCommand(new RandomExecuteCommand(), "randomExec");
		registCommand(new JumpCommand(), "jumpboost");
		registCommand(new OpenCommand(), "open");
		registCommand(new CommandBook(), "book");

		new ExecuteMobSkillCommand().regist();
		new CustomKillCommand().regist();
		new CustomEffectCommand().regist();
		new CustomSetBlockCommand().regist();
	}

	private  static void registCommand(CommandExecutor instance, String name) {
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
