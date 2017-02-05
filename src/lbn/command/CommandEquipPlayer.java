package lbn.command;

import java.util.ArrayList;
import java.util.List;

import lbn.api.LevelType;
import lbn.api.player.TheLowPlayer;
import lbn.api.player.TheLowPlayerManager;
import lbn.common.other.DungeonData;
import lbn.dungeon.contents.item.armor.BeneCommonAromor;
import lbn.dungeon.contents.item.armor.CommonArmor;
import lbn.dungeon.contents.item.magic.normalItems.NormalMagicItem;
import lbn.dungeon.contents.item.shootbow.NormalBowWrapper;
import lbn.dungeon.contents.item.sword.NormalSwordWrapper;
import lbn.dungeon.contents.item.sword.special.SpecialSwordIceAspect;
import lbn.dungeon.contents.slotStone.OneMobDamageUpSlot;
import lbn.dungeon.contents.slotStone.level1.FireAspect1;
import lbn.dungeon.contents.slotStone.level2.FireAspect2;
import lbn.dungeon.contents.slotStone.level3.HealSlotStone;
import lbn.dungeon.contents.slotStone.level4.CombatLightningStone;
import lbn.dungeon.contents.slotStone.level4.HealSlotStone2;
import lbn.item.attackitem.AttackItemStack;
import lbn.item.strength.StrengthOperator;
import lbn.util.LivingEntityUtil;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;
import org.bukkit.util.StringUtil;

import com.google.common.collect.ImmutableList;

public class CommandEquipPlayer implements CommandExecutor, TabCompleter{

	@Override
	public boolean onCommand(CommandSender paramCommandSender,
			Command paramCommand, String paramString,
			String[] paramArrayOfString) {

		if (paramArrayOfString.length == 0) {
			return false;
		}

		Player p = (Player) paramCommandSender;
		p.getInventory().clear();
		switch (paramArrayOfString[0]) {
		case DungeonData.DIFFICULTY_VERY_EASY:
			equipVeryEasy(p);
			updatePlayerLevel(p, 0);
			break;
		case DungeonData.DIFFICULTY_EASY:
			equipEasy(p);
			updatePlayerLevel(p, 5);
			break;
		case DungeonData.DIFFICULTY_NORMAL:
			equipNormal(p);
			updatePlayerLevel(p, 20);
			break;
		case DungeonData.DIFFICULTY_HARD:
			equipHard(p);
			updatePlayerLevel(p, 40);
			break;
		case DungeonData.DIFFICULTY_VERY_HARD:
			equipVeryHard(p);
			updatePlayerLevel(p, 48);
			break;
		case DungeonData.DIFFICULTY_IMPOSSIBLE:
			equipImpossible(p);
			updatePlayerLevel(p, 60);
			break;
		default:
			return false;
		}
		p.updateInventory();
		return true;
	}

	private void equipVeryEasy(Player p) {
		LivingEntityUtil.setEquipment(p, new CommonArmor(Material.LEATHER_HELMET).getItem(),
				new CommonArmor(Material.LEATHER_CHESTPLATE).getItem(),
				new CommonArmor(Material.LEATHER_LEGGINGS).getItem(),
				new CommonArmor(Material.LEATHER_BOOTS).getItem(),
				0f);

		Potion potion = new Potion(PotionType.INSTANT_HEAL);

		PlayerInventory inventory = p.getInventory();
		inventory.addItem(NormalSwordWrapper.getAllNormalItem().get(0).getItem(),
				NormalMagicItem.getAllItem().get(0).getItem(),
				NormalBowWrapper.getAllNormalItem().get(0).getItem(),
				new ItemStack(Material.ARROW, 64),
				potion.toItemStack(1),
				potion.toItemStack(1),
				potion.toItemStack(1),
				potion.toItemStack(1),
				new ItemStack(Material.MELON, 64)
				);


	}

	private void equipEasy(Player p) {
		LivingEntityUtil.setEquipment(p, new BeneCommonAromor(Material.LEATHER_HELMET).getItem(),
				new BeneCommonAromor(Material.LEATHER_CHESTPLATE).getItem(),
				new BeneCommonAromor(Material.LEATHER_LEGGINGS).getItem(),
				new BeneCommonAromor(Material.LEATHER_BOOTS).getItem(),
				0f);

		Potion potion = new Potion(PotionType.INSTANT_HEAL);
		potion.setSplash(true);

		ItemStack sword = NormalSwordWrapper.getAllNormalItem().get(0).getItem();
		StrengthOperator.updateLore(sword, 3);
		ItemStack magic = NormalMagicItem.getAllItem().get(0).getItem();
		StrengthOperator.updateLore(magic, 3);
		ItemStack bow = NormalBowWrapper.getAllNormalItem().get(0).getItem();
		StrengthOperator.updateLore(bow, 3);

		PlayerInventory inventory = p.getInventory();
		inventory.addItem(sword,
				magic,
				bow,
				new ItemStack(Material.ARROW, 64),
				new ItemStack(Material.MELON, 64)
			);
		for (int i = 0; i < 20; i++) {
			inventory.addItem(potion.toItemStack(1));
		}
	}

	private void equipNormal(Player p) {
		LivingEntityUtil.setEquipment(p,
				new BeneCommonAromor(Material.GOLD_HELMET).getItem(),
				new BeneCommonAromor(Material.GOLD_CHESTPLATE).getItem(),
				new BeneCommonAromor(Material.GOLD_LEGGINGS).getItem(),
				new BeneCommonAromor(Material.GOLD_BOOTS).getItem(),
				0f);


		ItemStack sword = NormalSwordWrapper.getAllNormalItem().get(2).getItem();
		StrengthOperator.updateLore(sword, 5);
		AttackItemStack instance = AttackItemStack.getInstance(sword);
		instance.addSlot(new FireAspect1());
		sword = instance.getItem();

		ItemStack magic = NormalMagicItem.getAllItem().get(2).getItem();
		AttackItemStack instance1 = AttackItemStack.getInstance(magic);
		StrengthOperator.updateLore(magic, 5);
		instance1.addSlot(OneMobDamageUpSlot.slotList.get(0));
		magic = instance1.getItem();

		ItemStack bow = NormalBowWrapper.getAllNormalItem().get(2).getItem();
		AttackItemStack instance2 = AttackItemStack.getInstance(bow);
		StrengthOperator.updateLore(bow, 5);
		instance2.addSlot(new FireAspect1());
		bow = instance2.getItem();

		PlayerInventory inventory = p.getInventory();
		inventory.addItem(sword,
				magic,
				bow,
				new ItemStack(Material.ARROW, 64),
				new ItemStack(Material.APPLE, 64)
				);

		Potion potion = new Potion(PotionType.INSTANT_HEAL);
		potion.setSplash(true);
		for (int i = 0; i < 10; i++) {
			inventory.addItem(potion.toItemStack(1));
		}

		Potion potion2 = new Potion(PotionType.INSTANT_HEAL, 2);
		potion2.setSplash(true);
		for (int i = 0; i < 10; i++) {
			inventory.addItem(potion2.toItemStack(1));
		}

		ItemStack itemStack = new ItemStack(Material.GOLDEN_APPLE, 2);
		inventory.addItem(itemStack);
	}

	private void equipHard(Player p) {
		LivingEntityUtil.setEquipment(p,
				new BeneCommonAromor(Material.CHAINMAIL_BOOTS).getItem(),
				new BeneCommonAromor(Material.CHAINMAIL_CHESTPLATE).getItem(),
				new BeneCommonAromor(Material.CHAINMAIL_LEGGINGS).getItem(),
				new BeneCommonAromor(Material.CHAINMAIL_BOOTS).getItem(),
				0f);

		Potion potion = new Potion(PotionType.INSTANT_HEAL, 2);
		potion.setSplash(true);

		ItemStack sword = NormalSwordWrapper.getAllNormalItem().get(3).getItem();
		StrengthOperator.updateLore(sword, 6);
		AttackItemStack instance = AttackItemStack.getInstance(sword);
		instance.addSlot(new FireAspect2());
		instance.addSlot(new HealSlotStone());
		instance.addSlot(OneMobDamageUpSlot.slotList.get(1));
		instance.updateItem();
		sword = instance.getItem();

		ItemStack magic = NormalMagicItem.getAllItem().get(3).getItem();
		AttackItemStack instance2 = AttackItemStack.getInstance(magic);
		StrengthOperator.updateLore(magic, 6);
		instance2.addSlot(OneMobDamageUpSlot.slotList.get(1));
		instance2.addSlot(OneMobDamageUpSlot.slotList.get(5));
		instance2.updateItem();
		magic = instance2.getItem();

		ItemStack bow = NormalBowWrapper.getAllNormalItem().get(3).getItem();
		AttackItemStack instance3 = AttackItemStack.getInstance(bow);
		StrengthOperator.updateLore(bow, 6);
		instance3.addSlot(new FireAspect1());
		instance3.updateItem();
		bow = instance3.getItem();

		PlayerInventory inventory = p.getInventory();
		inventory.addItem(sword,
				magic,
				bow,
				new ItemStack(Material.ARROW, 64),
				new ItemStack(Material.BREAD, 64)
				);
		for (int i = 0; i < 20; i++) {
			inventory.addItem(potion.toItemStack(1));
		}

		ItemStack itemStack = new ItemStack(Material.GOLDEN_APPLE, 5);
		inventory.addItem(itemStack);
	}

	private void equipVeryHard(Player p) {
		LivingEntityUtil.setEquipment(p,
				new BeneCommonAromor(Material.IRON_HELMET).getItem(),
				new BeneCommonAromor(Material.IRON_CHESTPLATE).getItem(),
				new BeneCommonAromor(Material.IRON_LEGGINGS).getItem(),
				new BeneCommonAromor(Material.IRON_BOOTS).getItem(),
				0f);

		Potion potion = new Potion(PotionType.INSTANT_HEAL, 2);
		potion.setSplash(true);

		ItemStack sword = new SpecialSwordIceAspect(48).getItem();
		StrengthOperator.updateLore(sword, 10);
		AttackItemStack instance = AttackItemStack.getInstance(sword);
		instance.addSlot(new FireAspect2());
		instance.addSlot(new HealSlotStone2());
		instance.addSlot(OneMobDamageUpSlot.slotList.get(2));
		instance.addSlot(OneMobDamageUpSlot.slotList.get(7));
		instance.updateItem();
		sword = instance.getItem();

		ItemStack magic = NormalMagicItem.getAllItem().get(4).getItem();
		AttackItemStack instance2 = AttackItemStack.getInstance(magic);
		StrengthOperator.updateLore(magic, 10);
		instance2.addSlot(OneMobDamageUpSlot.slotList.get(2));
		instance2.addSlot(OneMobDamageUpSlot.slotList.get(7));
		instance2.addSlot(new CombatLightningStone());
		instance2.updateItem();
		magic = instance2.getItem();

		ItemStack bow = NormalBowWrapper.getAllNormalItem().get(4).getItem();
		AttackItemStack instance3 = AttackItemStack.getInstance(bow);
		StrengthOperator.updateLore(bow, 10);
		instance3.addSlot(new FireAspect1());
		instance3.updateItem();
		bow = instance3.getItem();

		PlayerInventory inventory = p.getInventory();
		inventory.addItem(sword,
				magic,
				bow,
				new ItemStack(Material.ARROW, 64),
				new ItemStack(Material.BREAD, 64)
				);
		for (int i = 0; i < 27; i++) {
			inventory.addItem(potion.toItemStack(1));
		}

		Potion potionS = new Potion(PotionType.SPEED, 1);
		inventory.addItem(potionS.toItemStack(1));

		ItemStack itemStack = new ItemStack(Material.GOLDEN_APPLE, 10);
		inventory.addItem(itemStack);
	}

	private void equipImpossible(Player p) {
		LivingEntityUtil.setEquipment(p,
				new BeneCommonAromor(Material.DIAMOND_HELMET).getItem(),
				new BeneCommonAromor(Material.IRON_CHESTPLATE).getItem(),
				new BeneCommonAromor(Material.IRON_LEGGINGS).getItem(),
				new BeneCommonAromor(Material.DIAMOND_BOOTS).getItem(),
				0f);

		Potion potion = new Potion(PotionType.INSTANT_HEAL, 2);
		potion.setSplash(true);

		ItemStack sword = new SpecialSwordIceAspect(60).getItem();
		StrengthOperator.updateLore(sword, 5);
		AttackItemStack instance = AttackItemStack.getInstance(sword);
		instance.addSlot(new FireAspect2());
		instance.addSlot(new HealSlotStone2());
		instance.addSlot(OneMobDamageUpSlot.slotList.get(2));
		instance.addSlot(OneMobDamageUpSlot.slotList.get(7));
		instance.updateItem();
		sword = instance.getItem();

		ItemStack magic = NormalMagicItem.getAllItem().get(6).getItem();
		AttackItemStack instance2 = AttackItemStack.getInstance(magic);
		StrengthOperator.updateLore(magic, 5);
		instance2.addSlot(OneMobDamageUpSlot.slotList.get(2));
		instance2.addSlot(OneMobDamageUpSlot.slotList.get(7));
		instance2.addSlot(new CombatLightningStone());
		instance2.updateItem();
		magic = instance2.getItem();

		ItemStack bow = NormalBowWrapper.getAllNormalItem().get(6).getItem();
		AttackItemStack instance3 = AttackItemStack.getInstance(bow);
		StrengthOperator.updateLore(bow, 5);
		instance3.addSlot(new FireAspect1());
		instance3.updateItem();
		bow = instance3.getItem();

		PlayerInventory inventory = p.getInventory();
		inventory.addItem(sword,
				magic,
				bow,
				new ItemStack(Material.ARROW, 64),
				new ItemStack(Material.BREAD, 64)
				);
		for (int i = 0; i < 27; i++) {
			inventory.addItem(potion.toItemStack(1));
		}

		Potion potionS = new Potion(PotionType.SPEED, 1);
		inventory.addItem(potionS.toItemStack(1));
		inventory.addItem(potionS.toItemStack(1));

		ItemStack itemStack = new ItemStack(Material.GOLDEN_APPLE, 15);
		inventory.addItem(itemStack);
	}

	protected void updatePlayerLevel(Player p, int level) {
		TheLowPlayer theLowPlayer = TheLowPlayerManager.getTheLowPlayer(p);
		if (theLowPlayer != null) {
			theLowPlayer.setLevel(LevelType.MAIN, level);
		}
	}

	static ArrayList<String> difficulties = new ArrayList<>();
	static{
		difficulties.add(DungeonData.DIFFICULTY_VERY_EASY);
		difficulties.add(DungeonData.DIFFICULTY_EASY);
		difficulties.add(DungeonData.DIFFICULTY_NORMAL);
		difficulties.add(DungeonData.DIFFICULTY_HARD);
		difficulties.add(DungeonData.DIFFICULTY_VERY_HARD);
		difficulties.add(DungeonData.DIFFICULTY_IMPOSSIBLE);

	}

	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		if (arg3.length == 1) {
			return (List<String>)StringUtil.copyPartialMatches(arg3[0], difficulties, new ArrayList<String>(difficulties.size()));
		}
		return ImmutableList.of();
	}

}
