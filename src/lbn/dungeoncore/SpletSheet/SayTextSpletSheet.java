package lbn.dungeoncore.SpletSheet;

import org.bukkit.command.CommandSender;

import lbn.common.text.SayText;

public class SayTextSpletSheet extends AbstractSheetRunable{

	public SayTextSpletSheet(CommandSender p) {
		this(p, null);
	}

	String query = null;

	public SayTextSpletSheet(CommandSender p, String targetId) {
		super(p);
		if (targetId != null) {
			query = "id=" + targetId;
		}
	}

	@Override
	protected String getQuery() {
		return query;
	}

	@Override
	String getSheetName() {
		return "sayText";
	}

	@Override
	public String[] getTag() {
		return new String[]{"id", "color", "text_jp"};
	}

	@Override
	protected void excuteOnerow(String[] row) {
		SayText.registSayText(row[0], row[1], row[2]);
	}
}
