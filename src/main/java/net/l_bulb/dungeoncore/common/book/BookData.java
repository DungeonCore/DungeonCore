package net.l_bulb.dungeoncore.common.book;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import lombok.Getter;
import net.l_bulb.dungeoncore.util.ItemStackUtil;

public class BookData {
  public BookData(String command, String id) {
    this.id = id;

    if (isNull(command)) { return; }
    item = ItemStackUtil.getItemStackByCommand(command);

    // 本のコマンドがあるならそれを追加する
    if (item.getItemMeta() instanceof BookMeta) {
      // 本の情報を取得
      BookMeta itemMeta = (BookMeta) item.getItemMeta();
      // タイトルを表示
      title = itemMeta.getTitle();
      // 作者
      auther = itemMeta.getAuthor();
      // 内容
      contents.addAll(itemMeta.getPages());
    }
  }

  protected boolean isNull(String command) {
    return command == null || command.isEmpty();
  }

  @Getter
  String id;
  @Getter
  String title;
  @Getter
  String auther;

  ItemStack item = null;

  ArrayList<String> contents = new ArrayList<>();

  public ItemStack getItem() {
    return item;
  }

  public void setTitile(String title) {
    if (isNull(title)) { return; }
    this.title = title;
  }

  public void setAuther(String auther) {
    if (isNull(auther)) { return; }
    this.auther = auther;
  }

  public String[] getContents() {
    return contents.toArray(new String[0]);
  }

  int index = 0;

  public void addContents(String page) {
    if (isNull(page)) {
      index++;
      return;
    }

    page = page.replace("　", "");

    if (page.length() < 150) {
      updateRange();
      contents.set(index, page);
      index++;
    } else {
      while (page.length() > 150) {
        String onePage = page.substring(0, 150);
        updateRange();
        contents.set(index, onePage);
        index++;

        page = page.replace(onePage, "");
      }
      updateRange();
      contents.set(index, page);
      index++;
    }
  }

  private void updateRange() {
    while (index >= contents.size()) {
      contents.add("");
    }
  }

  ItemStack cacheBookItem = null;

  /**
   * 本のアイテムへ変換
   *
   * @return
   */
  public ItemStack toBookItem() {
    if (cacheBookItem == null) {
      cacheBookItem = new ItemStack(Material.WRITTEN_BOOK);
      // アイテムの情報を書きかる
      BookMeta itemMeta = (BookMeta) cacheBookItem.getItemMeta();
      itemMeta.setAuthor(getAuther());
      itemMeta.setTitle(getTitle());
      itemMeta.setPages(getContents());
      cacheBookItem.setItemMeta(itemMeta);
    }

    return cacheBookItem;
  }
}
