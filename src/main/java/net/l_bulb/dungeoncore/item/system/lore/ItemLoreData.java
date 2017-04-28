package net.l_bulb.dungeoncore.item.system.lore;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.util.ItemStackUtil;

public class ItemLoreData {
  ItemStack item;

  ArrayList<String> beforeDetail = new ArrayList<>();

  ArrayList<String> afterDetail = new ArrayList<>();

  TreeMap<String, ItemLoreToken> loreMap = null;

  public ItemLoreData() {
    loreMap = new TreeMap<String, ItemLoreToken>(new ComparatorImplemention());
  }

  // テスト用
  // public static void main(String[] args) {
  // String[] aa = new String[]{"§8id:pvp2", "", "§a機能性能§ctitle", " §e最大強化 ： §6+10", " §e使用可能 ： §6弓レベル0以上", " §eスキルレベル ： §660レベル", " §e耐久値 ： §6120", "
  // §e使用可能 ： §6弓レベル0以上", " §eスキルレベル ： §660レベル", "§e耐久値 ： §6120", "", "", "§a強化性能§ctitle", " §eADD:ダメージ §6+1.8", "", "§aSLOT §b最大1個", "§f ■
  // 空のスロット§0id:empty", ""};
  // List<String> asList = java.util.Arrays.asList(aa);
  // new ItemLoreData(asList);
  // }

  public ItemLoreData(ItemStack stack) {
    this(stack, null);
  }

  public ItemLoreData(ItemStack stack, Comparator<String> comparator) {
    this(ItemStackUtil.getLore(stack), comparator);
  }

  public ItemLoreData(List<String> lore, Comparator<String> comparator) {
    if (comparator == null) {
      loreMap = new TreeMap<String, ItemLoreToken>(new ComparatorImplemention());
    } else {
      loreMap = new TreeMap<String, ItemLoreToken>(comparator);
    }

    // テスト用
    // lbn.util.DungeonLogger.debug("String[] aa = new String[]{\"" + lore.toString().replace("\"", "\\\"").replace("[", "").replace("]",
    // "").replace(", ", "\", \"") + "\"};");

    LorePoint point = LorePoint.BEFORE;

    ItemLoreToken nowToken = null;

    for (String line : lore) {
      switch (point) {
        case BEFORE:
          // タイトルでない場合は値を保持しておく
          if (!ItemLoreToken.isTitle(line)) {
            beforeDetail.add(line);
            break;
          }
          // タイトルであった場合はLoreTokenとする
          point = LorePoint.LORE;
        case LORE:
          if (nowToken == null) {
            // タイトルだった場合はLoreTokenを生成する
            if (ItemLoreToken.isTitle(line)) {
              nowToken = createLoreTokenInstance(line);
              // タイトルでなければ次に進む
              break;
            } else if (line.equals("")) {
              // 空白の時はミスかもしれないので何もしない
              break;
            } else {
              point = LorePoint.AFTER;
            }
          } else {
            // 空白の場合はLoreToken終了とする
            if (line.equals("")) {
              loreMap.put(nowToken.getTitle(), nowToken);
              nowToken = null;
            } else {
              nowToken.addLoreAsOriginal(line);
            }
            break;
          }
        case AFTER:
          afterDetail.add(line);
        default:
          break;
      }

    }
    // 一番最後が空文字ならそれを削除する
    if (beforeDetail.size() != 0) {
      if (beforeDetail.get(beforeDetail.size() - 1).equals("")) {
        beforeDetail.remove(beforeDetail.size() - 1);
      }
    }
    // 一番最後が空文字ならそれを削除する
    if (afterDetail.size() != 0) {
      if (afterDetail.get(afterDetail.size() - 1).equals("")) {
        afterDetail.remove(afterDetail.size() - 1);
      }
    }

  }

  public ItemLoreData(List<String> lore) {
    this(lore, null);
  }

  /**
   * タイトルからLoreTokenを生成する
   * 
   * @param line
   * @return
   */
  public static ItemLoreToken createLoreTokenInstance(String line) {
    if (line.contains(ItemLoreToken.TITLE_SLOT)) { return new ItemLoreSlotToken(line); }
    return new ItemLoreToken(line, false);
  }

  public void addBefore(String line) {
    beforeDetail.add(line);
  }

  public void addBefore(List<String> line) {
    beforeDetail.addAll(line);
  }

  public void setBefore(List<String> line) {
    beforeDetail = new ArrayList<String>(line);
  }

  public void addAfter(String line) {
    afterDetail.add(line);
  }

  public void addAfter(List<String> line) {
    afterDetail.addAll(line);
  }

  public void setAfter(List<String> line) {
    afterDetail = new ArrayList<String>(line);
  }

  /**
   * 指定したタイトルのLoreを削除する
   * 
   * @param title
   */
  public void removeLore(String title) {
    loreMap.remove(title);
  }

  /**
   * 指定したLoreTokenを取得する。もし存在しない場合は新規作成して生成する
   * 
   * @param title
   * @return
   */
  public ItemLoreToken getLoreToken(String title) {
    return loreMap.getOrDefault(title, createLoreTokenInstance(title));
  }

  /**
   * Loreを取得する
   * 
   * @param item
   */
  public List<String> getLore() {
    List<String> lore = new ArrayList<String>();
    lore.addAll(beforeDetail);
    for (Entry<String, ItemLoreToken> entry : loreMap.entrySet()) {
      lore.addAll(entry.getValue().getLoreWithTitle());
    }
    lore.add("");
    lore.addAll(afterDetail);

    return lore;
  }

  /**
   * Loreを追加する
   * 
   * @param loreToken
   */
  public void addLore(ItemLoreToken loreToken) {
    loreMap.put(loreToken.getTitle(), loreToken);
  }

  enum LorePoint {
    BEFORE, LORE, AFTER;
  }

  class ComparatorImplemention implements Comparator<String> {
    @Override
    public int compare(String o1, String o2) {
      int index1 = getIndex(o1);
      int index2 = getIndex(o2);

      if (index1 == index2) { return o1.compareTo(o2); }
      return index1 - index2;
    }
  }

  public int getIndex(String value) {
    if (value.contains(ItemLoreToken.TITLE_STANDARD)) {
      return 1;
    } else if (value.contains(ItemLoreToken.TITLE_STRENGTH)) {
      return 2;
    } else if (value.contains(ItemLoreToken.TITLE_SLOT)) { return 20; }
    return 10;
  }
}
