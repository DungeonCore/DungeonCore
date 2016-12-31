package lbn.dungeoncore.SpletSheet;

import lbn.quest.Quest;
import lbn.quest.quest.QuestBuilder;

import org.bukkit.command.CommandSender;

public class QuestSheetRunnable extends AbstractSheetRunable{

	public QuestSheetRunnable(CommandSender p) {
		super(p);
	}

	@Override
	protected String getQuery() {
		return null;
	}

	@Override
	String getSheetName() {
		return null;
	}

	@Override
	public String[] getTag() {
		//								0		1				2			3			4			5			6			7			8					9						10					11					12				13				14				15			16				17					18					19
		return new String[]{"id", "name", "detail", "type", "date1", "date2", "talk1", "talk2", "titleFlg", "rewarditem", "rewardmoney", "swordexp", "bowexp", "magicexp", "premise", "chain", "overlap", "candelete", "cooltime", "availablelevel"};
	}

	@Override
	protected void excuteOnerow(String[] row) {
		QuestBuilder questBuilder = new QuestBuilder(row);
		Quest quest = questBuilder.getQuest();
	}

}
