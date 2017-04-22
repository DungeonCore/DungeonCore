package lbn.dungeoncore.SpletSheet;

import java.util.Arrays;

import lbn.mob.AbstractMob;
import lbn.mob.MobHolder;
import lbn.mob.MobSpawnerFromCommand;
import lbn.mob.customMob.BossMobable;
import lbn.mob.customMob.LbnMobTag;
import lbn.mob.customMob.LbnMobTag2;
import lbn.mob.customMob.SpreadSheetMob;
import lbn.mob.customMob.SpreadSheetMob2;
import lbn.util.ItemStackUtil;
import lbn.util.JavaUtil;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

public class Mob1SheetRunnable extends AbstractSheetRunable {

	public Mob1SheetRunnable(CommandSender p) {
		super(p);
		query = "name!=\"\"";
	}

	public Mob1SheetRunnable(CommandSender p, String monsterName) {
		super(p);
		query = "name=" + monsterName.trim();
	}

	@Override
	public boolean hasSecoundSheet() {
		return true;
	}

	String query = null;

	@Override
	protected String getQuery() {
		return query;
	}

	@Override
	public String getSheetName() {
		return "mob1";
	}

	@Override
	public String[] getTag() {
		return new String[] { "name", "command", "level", "attack", "hp", "money", "exp", "dropitem1", "droprate1",
				"dropitem2", "droprate2", "dropitem3", "droprate3", "dropitem4", "droprate4", "mobskill1", "mobskill2",
				"mobskill3" };
	}

	@Override
	protected int startRow() {
		return 1;
	}

	@Override
	protected void excuteOnerow(String[] row) {
		try {
			String name = row[0];
			if (name == null || name.isEmpty()) {
				sendMessage("名前が登録されていません。:" + Arrays.toString(row));
				return;
			}
			String command = row[1];
			if (command == null) {
				sendMessage("Summonコマンドが記入されていません：" + name);
				return;
			}

			LbnMobTag lbnTag = MobSpawnerFromCommand.getNBTTagByCommand(command.split(" "), sender);
			if (lbnTag == null) {
				sendMessage("Summonコマンドが不正です：" + name);
				return;
			}

			LbnMobTag2 mobData = new LbnMobTag2(lbnTag.getType());

			mobData.setRiding(lbnTag.isRiding());

			mobData.setLevel(JavaUtil.getInt(row[2], -1));

			mobData.setAttack(JavaUtil.getDouble(row[3], -1));

			mobData.setHp(JavaUtil.getDouble(row[4], -1));

			SpreadSheetMob2 instance = new SpreadSheetMob2(mobData, command.split(" "), name);

			// DROP ITEM の設定
			setDropItem(row[7], row[8], instance);
			setDropItem(row[9], row[10], instance);
			setDropItem(row[11], row[12], instance);
			setDropItem(row[13], row[14], instance);

			// モブがまだ存在していればそのままセットする
			AbstractMob<?> mob = MobHolder.getMob(name);
			if (mob instanceof BossMobable) {
				// もしEntityが存在していれば
				LivingEntity entity = ((BossMobable) mob).getEntity();
				if (entity != null) {
					((BossMobable) instance).setEntity(entity);
				}
			}

			MobHolder.registMob(instance);
		} catch (NumberFormatException e) {
			sendMessage("入力されたDropItemRateが不正です。" + row[0]);
		} catch (Exception e) {
			e.printStackTrace();
			sendMessage("エラーが発生しました。モンスターを登録出来ませんでした:" + row[0]);
		}
	}

	protected boolean isEmpty(String[] row, int i) {
		return row[i] == null || row[i].isEmpty();
	}

	/**
	 * Set
	 * 
	 * @param itemId
	 * @param parcent
	 * @param instance
	 */
	public void setDropItem(String itemId, String parcent, SpreadSheetMob instance) {
		// DROP ITEM の設定
		if (itemId != null && !itemId.isEmpty()) {
			ItemStack item = ItemStackUtil.getItemStack(itemId);
			if (item != null) {
				double dropRate1 = Double.parseDouble(parcent);
				instance.setDropItem(item, dropRate1);
			} else {
				sendMessage("入力されたdrop_itemが不正です。" + itemId);
			}
		}
	}
}
