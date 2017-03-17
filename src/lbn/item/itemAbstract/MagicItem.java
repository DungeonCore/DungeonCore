package lbn.item.itemAbstract;

import lbn.common.cooltime.CooltimeManager;
import lbn.common.other.ItemStackData;
import lbn.common.sound.SoundData;
import lbn.dungeon.contents.item.magic.normalItems.magicExcutor.FallingBlockMagicExcutor;
import lbn.item.SpreadSheetItem.SpreadSheetAttackItem;
import lbn.item.attackitem.SpreadSheetWeaponData;
import lbn.item.attackitem.weaponSkill.WeaponSkillExecutor;
import lbn.item.itemInterface.LeftClickItemable;
import lbn.item.itemInterface.MagicExcuteable;
import lbn.item.itemInterface.RightClickItemable;
import lbn.item.strength.old.StrengthOperator;
import lbn.player.ItemType;
import lbn.player.customplayer.MagicPointManager;
import lbn.util.Message;
import lbn.util.dropingEntity.DamagedFallingBlockForPlayer;
import lbn.util.particle.ParticleData;
import lbn.util.particle.ParticleType;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;

public class MagicItem extends SpreadSheetAttackItem implements RightClickItemable, LeftClickItemable{

	public MagicItem(SpreadSheetWeaponData data) {
		super(data);
		soundData = MagicAttackData.getSoundData(getAvailableLevel());
		itemStackData = MagicAttackData.getItemStackData(getAvailableLevel());
	}

	@Override
	final public void excuteOnLeftClick(PlayerInteractEvent e) {
		//レベルなどを確認する
		e.setCancelled(true);
		Player player = e.getPlayer();
		ItemStack item = player.getItemInHand();
		if (!isAvilable(player)) {
			sendNotAvailableMessage(player);
			return;
		}
		//魔法を実行
		excuteMagic(e, player, item, getLeftClickMagic(item));
	}

	@Override
	final public void excuteOnRightClick(PlayerInteractEvent e) {
		super.excuteOnRightClick(e);
		e.setCancelled(true);
		Player player = e.getPlayer();

		if (!isAvilable(player)) {
			e.setCancelled(true);
			return;
		}
		//スキルを発動
		WeaponSkillExecutor.executeWeaponSkillOnClick(e, this);
	}

	/**
	 * 指定された魔法を発動する
	 * @param e
	 * @param player
	 * @param item
	 * @param magic
	 */
	protected void excuteMagic(PlayerInteractEvent e, Player player, ItemStack item, MagicExcuteable magic) {
		//魔法が存在しないなら何もしない
		if (magic == null) {
			return;
		}
		//クールタイムを確認
		CooltimeManager cooltime = new CooltimeManager(e, magic);
		//クールタイム中ならメッセージを表示
		if (!cooltime.canUse()) {
			if (magic.isShowMessageIfUnderCooltime()) {
				cooltime.sendCooltimeMessage(player);
			}
			return;
		}
		//マジックポイントを確認し足りなければメッセージを表示
		if (!hasMagicPoint(player, magic.getNeedMagicPoint())) {
			Message.sendMessage(player, "マジックポイントが不足しています。");
			return;
		}
		//魔法を発動
		magic.excuteMagic(player, e);
		//クールタイムをつける
		cooltime.setCoolTime();
		//マジックポイントを消費する
		MagicPointManager.consumeMagicPoint(player, magic.getNeedMagicPoint());

		PlayerItemDamageEvent event = new PlayerItemDamageEvent(player, item, 1);
		Bukkit.getServer().getPluginManager().callEvent(event);
		if (event.isCancelled()) {
			int damage = event.getDamage();
			item.setDurability((short) (item.getDurability() + damage));
		}

	}

	/**
	 * 必要のマジックポイントを持っているか
	 * @param p
	 * @param needMagicPoint
	 * @return
	 */
	protected boolean hasMagicPoint(Player p, int needMagicPoint) {
		int nowMagicPoint = MagicPointManager.getNowMagicPoint(p);
		if (needMagicPoint > nowMagicPoint) {
			Message.sendMessage(p, Message.MP_SHORTAGE);
			return false;
		}
		return true;
	}

	@Override
	public ItemType getAttackType() {
		return ItemType.MAGIC;
	}

	@Override
	public double getMaterialDamage() {
		return 0;
	}

	SoundData soundData;
	ItemStackData itemStackData;

	protected MagicExcuteable getLeftClickMagic(ItemStack item) {
		return new FallingBlockMagicExcutor(item, getId() + "_lc") {
			@Override
			protected DamagedFallingBlockForPlayer getDamagedFallingBlock(Player p, PlayerInteractEvent e) {
				DamagedFallingBlockForPlayer fallingBlock = new DamagedFallingBlockForPlayer(p, itemStackData.getMaterial(), item, getAttackItemDamage(StrengthOperator.getLevel(item)), itemStackData.getData()){
					ParticleData particleData = new ParticleData(ParticleType.snowshovel, 40).setDispersion(0.8, 0.8, 0.8);

					ParticleData particleData2 = new ParticleData(ParticleType.crit, 40);
					@Override
					public void tickRutine(int count) {
						if (count % 2 == 0) {
							particleData2.run(spawnEntity.getLocation());
						}
					}

					@Override
					public void removedRutine(Entity spawnEntity) {
						particleData.run(spawnEntity.getLocation());
					}

					@Override
					public void onHitDamagedEntity(Entity target) {
						soundData.playSoundAllPlayer(target.getLocation());
					}

					@Override
					public void startEntityRutine(Player p) {
						p.getWorld().playSound(p.getLocation(), Sound.WITHER_SHOOT, (float) 0.2, (float) 0.1);
					}
				};
				return fallingBlock;
			}
		};
	}
}

class MagicAttackData {
	static SoundData lv0Sound = new SoundData(Sound.ZOMBIE_WOODBREAK, 1, 3);
	static SoundData lv10Sound = new SoundData(Sound.GLASS, 1, (float) 0.1);
	static SoundData lv20Sound = new SoundData(Sound.IRONGOLEM_HIT, 1, (float) 0.7);
	static 	SoundData lv30Sound = new SoundData(Sound.ZOMBIE_METAL, 1, (float) 1);
	static SoundData lv40Sound = new SoundData(Sound.GHAST_FIREBALL, 1, (float) 1);
	static 	SoundData lv50Sound = new SoundData(Sound.GLASS, 1, (float) 0.1);
	static 	SoundData lv60Sound = new SoundData(Sound.GHAST_FIREBALL, 1, (float) 1);
	static 	SoundData lv70Sound = new SoundData(Sound.ZOMBIE_METAL, 1, (float) 0.1);
	static 	SoundData lv80Sound = new SoundData(Sound.ZOMBIE_METAL, 1, (float) 0.1);

	public static SoundData getSoundData(int level) {
		switch ((int)(level / 10)) {
		case 0:
			return lv0Sound;
		case 1:
			return lv10Sound;
		case 2:
			return lv20Sound;
		case 3:
			return lv30Sound;
		case 4:
			return lv40Sound;
		case 5:
			return lv50Sound;
		case 6:
			return lv60Sound;
		case 7:
			return lv70Sound;
		case 8:
			return lv80Sound;
		default:
			break;
		}
		return lv0Sound;
	}

	static ItemStackData lv00Block = new ItemStackData(Material.WOOD, 1);
	static ItemStackData lv10Block = new ItemStackData(Material.ICE);
	static ItemStackData lv20Block = new ItemStackData(Material.IRON_BLOCK);
	static ItemStackData lv30Block = new ItemStackData(Material.STAINED_CLAY, 11);
	static ItemStackData lv40Block = new ItemStackData(Material.STAINED_CLAY, 14);
	static ItemStackData lv50Block = new ItemStackData(Material.GOLD_BLOCK);
	static ItemStackData lv60Block = new ItemStackData(Material.DIAMOND_BLOCK);
	static ItemStackData lv70Block = new ItemStackData(Material.OBSIDIAN);
	static ItemStackData lv80Block = new ItemStackData(Material.OBSIDIAN);

	public static ItemStackData getItemStackData(int level) {
		switch ((int)(level / 10)) {
		case 0:
			return lv00Block;
		case 1:
			return lv10Block;
		case 2:
			return lv20Block;
		case 3:
			return lv30Block;
		case 4:
			return lv40Block;
		case 5:
			return lv50Block;
		case 6:
			return lv60Block;
		case 7:
			return lv70Block;
		case 8:
			return lv80Block;
		default:
			break;
		}
		return lv00Block;
	}
}
