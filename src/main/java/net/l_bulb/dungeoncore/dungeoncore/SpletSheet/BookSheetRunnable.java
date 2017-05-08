package net.l_bulb.dungeoncore.dungeoncore.SpletSheet;

import net.l_bulb.dungeoncore.common.book.BookData;
import net.l_bulb.dungeoncore.common.book.BookManager;

import org.bukkit.command.CommandSender;

public class BookSheetRunnable extends AbstractSheetRunable {

  public BookSheetRunnable(CommandSender sender) {
    super(sender);
  }

  @Override
  protected String getQuery() {
    return null;
  }

  @Override
  public String getSheetName() {
    return "book";
  }

  @Override
  public String[] getTag() {
    return new String[] { "id", "title", "auther", "command", "page1", "page2", "page3", "page4", "page5", "page6", "page7", "page8", "page9",
        "page10" };
  }

  @Override
  protected void excuteOnerow(String[] row) {
    String id = row[0];
    String command = row[3];

    if (id == null || id.isEmpty()) {
      sendMessage("idが不正です");
    }

    BookData bookData = new BookData(command, id);
    bookData.setTitile(row[1]);
    bookData.setAuther(row[2]);

    for (int i = 0; i < 10; i++) {
      bookData.addContents(row[4 + i]);
    }
    BookManager.regist(bookData);
  }

}
