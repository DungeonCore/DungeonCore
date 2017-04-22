package lbn.dungeoncore.SpletSheet;

import org.bukkit.command.CommandSender;

public class Book2SheetRunnable extends BookSheetRunnable {

	public Book2SheetRunnable(CommandSender sender) {
		super(sender);
	}

	@Override
	public String getSheetName() {
		return "book2";
	}

}
