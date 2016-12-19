package main.lbn.SpletSheet;

import java.util.Arrays;
import java.util.concurrent.Future;

import main.command.CommandGiveItem;
import main.item.ItemInterface;
import main.item.ItemManager;
import main.item.SpreadSheetItem.SpreadSheetKeyCommandBlockExecuteItem;
import main.item.SpreadSheetItem.SpreadSheetKeyTpItem;
import main.item.SpreadSheetItem.SpreadSheetOtherItem;
import main.item.SpreadSheetItem.SpreadSheetQuestItem;
import main.util.ItemStackUtil;
import main.util.JavaUtil;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

public class ItemSheetRunnable extends AbstractSheetRunable{

	public ItemSheetRunnable(CommandSender p) {
		super(p);
	}

	public ItemSheetRunnable(CommandSender p, String itemId) {
		super(p);
		query = "id=" + itemId;
	}

	String query = "id!=\"\"";

	@Override
	protected String getQuery() {
		return query;
	}

	@Override
	String getSheetName() {
		return "item";
	}

	@Override
	public String[] getTag() {
		return new String[]{"id", "name", "command", "type", "data", "price", "dungeonname", "dungeonlocation"};
	}

	@Override
	protected void excuteOnerow(String[] row) {
		try {
			String id = row[0];
			if (id == null || id.isEmpty()) {
				sendMessage("idは必須です。");
				return;
			}
			String name = row[1];
			if (name == null || name.isEmpty()) {
				sendMessage("nameは必須です。");
				return;
			}

			String command = row[2];
			ItemStack itemByCommand = ItemStackUtil.getItemStackByCommand(command);
			if (itemByCommand == null || itemByCommand.getType() == Material.AIR) {
				sendMessage("commandが不正です。");
			}

			String data = row[4];
			int price = JavaUtil.getInt(row[5], 0);

			String dungeonName = row[6];

			Location dungeonLoc = getLocationByString(row[7]);

			//アイテムを生成
			ItemInterface item;
			if (row[3].startsWith("1.")) {
				if (dungeonLoc == null) {
					sendMessage("使用する看板の座標が不正です");
					return;
				}
				item = new SpreadSheetKeyCommandBlockExecuteItem(name, id, price, command, dungeonName, dungeonLoc);
			} else if (row[3].startsWith("2.")) {
				if (dungeonLoc == null) {
					sendMessage("使用する看板の座標が不正です");
					return;
				}
				Location tpLoc = getLocationByString(data);
				if (tpLoc == null) {
					sendMessage("TP先の座標が不正です。(dataで指定してください)");
					return;
				}
				item = new SpreadSheetKeyTpItem(name, id, price, command, dungeonName, dungeonLoc, tpLoc);
			} else if (row[3].startsWith("3.")) {
				item = new SpreadSheetQuestItem(name, id, price, command);
			} else {
				item = new SpreadSheetOtherItem(name, id, price, command);
			}

			ItemManager.registItem(item);

		} catch (Exception e) {
			e.printStackTrace();
			sendMessage("エラーが発生したため登録出来ませんでした。:" + Arrays.toString(row));
		}
	}

	@Override
	protected int startRow() {
		return super.startRow();
	}

	@Override
	public void onCallbackFunction(Future<String[][]> submit) throws Exception {
		super.onCallbackFunction(submit);

		CommandGiveItem.initFlg = true;
	}
}
