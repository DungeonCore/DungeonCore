package lbn.dungeoncore.SpletSheet;

import java.util.Arrays;

import lbn.mob.AIType;
import lbn.mob.AbstractMob;
import lbn.mob.MobHolder;
import lbn.mob.attribute.Attribute;
import lbn.mob.mob.BossMobable;
import lbn.mob.mob.CommandBossMob;
import lbn.mob.mob.CommandableMob;
import lbn.util.ItemStackUtil;
import lbn.util.JavaUtil;
import lbn.util.spawn.LbnMobTag;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

public class MobSheetRunnable extends AbstractSheetRunable{

	public MobSheetRunnable(CommandSender p) {
		super(p);
		query = "name!=\"\"";
	}

	public MobSheetRunnable(CommandSender p, String monsterName) {
		super(p);
		query = "name=" + monsterName.trim();
	}

	String query = null;

	@Override
	protected String getQuery() {
		return query;
	}

	@Override
	public String getSheetName() {
		return "mob";
	}

	@Override
	public String[] getTag() {
		return new String[]{"name", "command", "ignorewater", "dropitem1", "droprate1", "dropitem2", "droprate2",
				"chestlocation", "skill1", "skill2", "skill3", "skill4", "skill5", "money", "exp",
				"swordresistance", "bowresistance", "magicresistance", "redstonelocation", "attribute", "attackpoint", "defencePoint",//21
				"aitype", "reach", "jumpattack", "cps", "sps"
				};
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

			boolean isBoss = false;

			//mobのインスタンス作成
			CommandableMob instance;
			if (isEmpty(row, 7)) {
				instance = CommandableMob.getInstance(command.split(" "), name, sender);
			} else {
				//AbstractChestを取得
				Location locationByString = getLocationByString(row[7]);
				if (locationByString == null) {
					sendMessage("chest locationが不正です：" + name);
					return;
				}
				instance = CommandBossMob.getInstance(command.split(" "), name, sender, locationByString);
				isBoss = true;
			}

			if (instance == null) {
				sendMessage("入力されたsummon commandが不正です。:" + name);
				return;
			}

			//LbnNbtTagを取得
			LbnMobTag nbtTag = instance.getLbnMobTag();
			if (nbtTag == null) {
				sendMessage("入力されたsummon commandが不正です。(1):" + name);
				return;
			}
			//WaterMob化
			nbtTag.setWaterMonster(JavaUtil.getBoolean(row[2], false));
			//AI Type
			nbtTag.setAiType(AIType.fromName(row[22]));
			//腕の長さ
			nbtTag.setAttackReach(JavaUtil.getFloat(row[23], 3f));
			//ジャンプ斬り
			nbtTag.setJumpAttack("ジャンプ斬りする".equals(row[24]));
			//CPS
			nbtTag.setAttackCountPerSec((int)JavaUtil.getDouble(row[25], 1));
			//SPS
			nbtTag.setShotTarm((int)JavaUtil.getDouble(row[26], 1));

			//DROP ITEM の設定
			if (row[3] != null && !row[3].isEmpty()) {
				ItemStack item = ItemStackUtil.getItemStack(row[3]);
				if (item == null) {
					sendMessage("入力されたdrop_item1が不正です。" + name);
					return;
				}
				double dropRate1 = Double.parseDouble(row[4]);
				instance.setDropItem(item, dropRate1);
			}

			if (row[5] != null && !row[5].isEmpty()) {
				ItemStack item = ItemStackUtil.getItemStack(row[5]);
				if (item == null) {
					sendMessage("入力されたdrop_item2が不正です。" + name);
					return;
				}
				double dropRate1 = Double.parseDouble(row[6]);
				instance.setDropItem(item, dropRate1);
			}

			//スキル追加
			if (!isEmpty(row, 8)) {
				instance.addSkill(row[8]);
			}
			if (!isEmpty(row, 9)) {
				instance.addSkill(row[9]);
			}
			if (!isEmpty(row, 10)) {
				instance.addSkill(row[10]);
			}
			if (!isEmpty(row, 11)) {
				instance.addSkill(row[11]);
			}
			if (!isEmpty(row, 12)) {
				instance.addSkill(row[12]);
			}

			if (!isEmpty(row, 13)) {
				instance.setMoney(JavaUtil.getInt(row[13], 10));
			}
			if (!isEmpty(row, 14)) {
				instance.setExp(JavaUtil.getInt(row[14], -1));
			}

			//モブがまだ存在していればそのままセットする
			AbstractMob<?> mob = MobHolder.getMob(name);
			if (mob instanceof BossMobable && isBoss) {
				//もしEntityが存在していれば
				LivingEntity entity = ((BossMobable)mob).getEntity();
				if (entity != null) {
					((CommandBossMob)instance).setEntity(entity);
				}
			}

			instance.setSwordRegistance(JavaUtil.getDouble(row[15], 0));
			instance.setBowRegistance(JavaUtil.getDouble(row[16], 0));
			instance.setMagicRegistance(JavaUtil.getDouble(row[17], 0));

			Location locationByString = getLocationByString(row[18]);
			if (!isEmpty(row, 18) && locationByString == null) {
				sendMessage("redstonelocationが不正です");
			}
			instance.setRedstoneLocation(locationByString);

			//属性を設定
			instance.setAttribute(Attribute.getAttribute(row[19]));

			instance.setAttackPoint(JavaUtil.getDouble(row[20], 1));
			instance.setDefencePoint(JavaUtil.getDouble(row[21], 1));

			MobHolder.registMob(instance);
		} catch (NumberFormatException e) {
			sendMessage("入力されたDropItemRateが不正です。" + row[0]);
		} catch (Exception e) {
			e.printStackTrace();
			sendMessage("エラーが発生しました。モンスターを登録出来ませんでした:"+ row[0]);
		}
	}

	protected boolean isEmpty(String[] row, int i) {
		return row[i] == null || row[i].isEmpty();
	}
}
