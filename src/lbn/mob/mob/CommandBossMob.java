package lbn.mob.mob;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import lbn.api.LevelType;
import lbn.api.player.TheLowPlayer;
import lbn.api.player.TheLowPlayerManager;
import lbn.chest.AbstractCustomChest;
import lbn.chest.BossChest;
import lbn.chest.CustomChestManager;
import lbn.chest.SpletSheetChest;
import lbn.common.event.player.PlayerCustomMobSpawnEvent;
import lbn.dungeoncore.LbnRuntimeException;
import lbn.dungeoncore.Main;
import lbn.mob.AbstractMob;
import lbn.mob.LastDamageManager;
import lbn.mob.LastDamageMethodType;
import lbn.mob.MobHolder;
import lbn.mob.mobskill.MobSkillExcuteConditionType;
import lbn.player.status.StatusAddReason;
import lbn.util.LbnRunnable;
import lbn.util.LivingEntityUtil;
import lbn.util.spawn.LbnMobTag;
import lbn.util.spawn.MobSpawnByCommand;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.connorlinfoot.actionbarapi.ActionBarAPI;
import com.google.common.collect.HashBasedTable;

public class CommandBossMob extends CommandableMob implements BossMobable{

	static HashMap<String, LivingEntity> entityList = new HashMap<String, LivingEntity>();

	protected CommandBossMob(LbnMobTag nbtTag, String[] command, String name) {
		super(nbtTag, command, name);
	}

	long spawnTimeMill = -1;

	public static CommandBossMob getInstance(String[] command, String name, CommandSender sender, Location locationByString) {
		LbnMobTag nbtTag = MobSpawnByCommand.getTBTTagByCommand(command, sender);

		if (nbtTag == null) {
			return null;
		}

		if (locationByString == null) {
			return null;
		}

		CommandBossMob commandableMob = new CommandBossMob(nbtTag, command, name);
		commandableMob.chestloc = locationByString;

		AbstractMob<?> mob2 = MobHolder.getMob(name);
		if (mob2 != null && !mob2.isNullMob() && !(mob2 instanceof CommandableMob)) {
			commandableMob.mob = mob2;
		}

		if (locationByString != null) {
			AbstractCustomChest customChest = CustomChestManager.getCustomChest(locationByString);
			if (customChest != null && customChest instanceof SpletSheetChest) {
				((SpletSheetChest)customChest).setBossChestTemplate(true);
			}
		}

		return commandableMob;
	}

	Location chestloc = null;

	private LivingEntity e;

	HashMap<MobSkillExcuteConditionType, BukkitRunnable> runtineMap = new HashMap<MobSkillExcuteConditionType, BukkitRunnable>();

	public void setEntity(LivingEntity e) {
		this.e = e;
		entityList.put(getName(), e);

		//すでにキャンセルされてる可能性もあるのエラーを無視
		try {
			for (BukkitRunnable rutineRun : runtineMap.values()) {
				rutineRun.cancel();
			}
		} catch (Exception ex) {
		}

		//mobskillをスタート
		for (MobSkillExcuteConditionType condtion : Arrays.asList(MobSkillExcuteConditionType.RUNTINE_10SEC, MobSkillExcuteConditionType.RUNTINE_30SEC, MobSkillExcuteConditionType.RUNTINE_60SEC)) {
			RuntineRunnable run = new RuntineRunnable(condtion);
			run.runtask();
			runtineMap.put(condtion, run);
		}
	}

	@Override
	public void onSpawn(PlayerCustomMobSpawnEvent e) {
		super.onSpawn(e);
		setEntity(e.getEntity());

		spawnTimeMill = System.currentTimeMillis();

		//前のが動いていた場合はキャンセルする
		for (BukkitRunnable val : runtineMap.values()) {
			if (val == null) {
				continue;
			}
			//念のため
			try {
				val.cancel();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		//mobskillをスタート
		for (MobSkillExcuteConditionType condtion : Arrays.asList(MobSkillExcuteConditionType.RUNTINE_10SEC, MobSkillExcuteConditionType.RUNTINE_30SEC, MobSkillExcuteConditionType.RUNTINE_60SEC)) {
			RuntineRunnable run = new RuntineRunnable(condtion);
			run.runtask();
			runtineMap.put(condtion, run);
		}
	}

	public Location chestLocation() {
		return chestloc;
	}

	@Override
	public BossChest getBossChest() {
		AbstractCustomChest customChest = CustomChestManager.getCustomChest(chestloc.getBlock().getLocation());
		if (customChest != null ||customChest instanceof SpletSheetChest) {
			return new BossChest((SpletSheetChest) customChest);
		} else {
			return null;
		}
	}

	@Override
	protected Entity spawnPrivate(Location loc) {
		Entity spawnPrivate = super.spawnPrivate(loc);
//		String eInfo = null;
//		if (e != null) {
//			eInfo = e + "(isDead:" + e.isDead() + ")";
//		}
//
//		String eList = null;
//		LivingEntity livingEntity = entityList.get(getName());
//		if (livingEntity != null) {
//			eList = livingEntity + "(isDead:" + livingEntity.isDead() + ")";
//		}

//		DungeonLog.printDevelopln(getEntity() + " will be spawn!!(" + getName() + ") entity:" + eInfo + ", entityList:" + eList);
		return spawnPrivate;
	}

	@Override
	public LivingEntity getEntity() {
		if (e == null) {
			return entityList.get(getName());
		}
		return e;
	}

	private HashMap<TheLowPlayer, Long> combatPlayerSet = new HashMap<TheLowPlayer, Long>();

	private HashBasedTable<TheLowPlayer, LevelType, Double> combatDamagePlayerMap = HashBasedTable.create();

	public void onDamage(LivingEntity mob, Entity damager, EntityDamageByEntityEvent e) {
		super.onDamage(mob, damager, e);

		//最後に攻撃したPlayerと攻撃方法を取得
		Player player = LastDamageManager.getLastDamagePlayer(mob);
		if (player == null) {
			return;
		}
		TheLowPlayer p = TheLowPlayerManager.getTheLowPlayer(player);
		LastDamageMethodType lastDamageType = LastDamageManager.getLastDamageAttackType(mob);

		//攻撃方法を対応するステータスのTypeに変換
		LevelType type = lastDamageType.getLevelType();

		//攻撃者がいる　または　攻撃方法に対応するステータスが存在するならダメージを記録する
		if (p != null && type != null) {
			combatPlayerSet.put(p, System.currentTimeMillis());

			//ダメージを記録
			double doubleNowDamage = 0;
			if (combatDamagePlayerMap.contains(p, type)) {
				doubleNowDamage = combatDamagePlayerMap.get(p, type);
			}
			combatDamagePlayerMap.put(p, type, doubleNowDamage + e.getDamage());
		}

		//ボスモブとして認識されていないなら再セットする
		if ((this.e == null || !this.e.isValid()) &&mob != this.e) {
			setEntity(mob);
		}
	}

	@Override
	public void onAttack(LivingEntity mob, LivingEntity target,
			EntityDamageByEntityEvent e) {
		super.onAttack(mob, target, e);

		//ボスモブとして認識されていないなら再セットする
		if ((this.e == null || !this.e.isValid()) &&mob != this.e) {
			setEntity(mob);
		}
	}

	@Override
	public Set<TheLowPlayer> getCombatPlayer() {
		Iterator<Entry<TheLowPlayer, Long>> iterator = combatPlayerSet.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<TheLowPlayer, Long> entry = iterator.next();
			//Combatした時間よりも後に死んでいたらCombatを認めない
			if (entry.getKey().getLastDeathTimeMillis() > entry.getValue()) {
				iterator.remove();
			}
		}
		return combatPlayerSet.keySet();
	}

	class RuntineRunnable extends BukkitRunnable {
		MobSkillExcuteConditionType condtion;
		int term;
		public RuntineRunnable(MobSkillExcuteConditionType condtion) {
			this.term = condtion.getTerm();
			this.condtion = condtion;

			if (term <= 0) {
				throw new LbnRuntimeException("mob skill term is invalid!! :" + term);
			}
		}

		@Override
		public void run() {
			if (getEntity() == null || !getEntity().isValid()) {
				cancel();
				runtineMap.remove(condtion);
				return;
			}
			//ランダムで一番近くのプレイヤー
			for (LivingEntity entity : LivingEntityUtil.getNearFrendly(getEntity(), 30, 20, 30)) {
				//combatプレイやーでないなら無視
				if (entity.getType() == EntityType.PLAYER && TheLowPlayerManager.isLoaded((Player) entity)) {
					if (!combatPlayerSet.containsKey(TheLowPlayerManager.getTheLowPlayer((Player) entity))) {
						continue;
					}
				}
				executeMobSkill(getEntity(), entity, condtion);
				return;
			}
		}

		public void runtask() {
			runTaskTimer(Main.plugin, rnd.nextInt(term * 20), term * 20);
		}
	}

	@Override
	public void onDeathPrivate(EntityDeathEvent e) {
		super.onDeathPrivate(e);

		entityList.remove(getName());

		if (Main.plugin.getServer().getPluginManager().isPluginEnabled("ActionBarAPI")) {
			for (TheLowPlayer p : combatPlayerSet.keySet()) {
				if (p.isOnline()) {
					ActionBarAPI.sendActionBar(p.getOnlinePlayer(), "");
				}
			}
		}

		//数チック後に削除する
		new LbnRunnable() {
			@Override
			public void run2() {
				//コンバットプレイヤーをクリア
				combatPlayerSet.clear();
				combatDamagePlayerMap.clear();
			}
		}.runTaskLater(Main.plugin, 2);
	}

	@EventHandler
	public void updateName(boolean islater) {
		if (isNullMob()) {
			return;
		}
		if (this instanceof BossMobable) {
			new UpdateNameLater().runTaskLater(Main.plugin, 1);
		}
	}

	@Override
	public void onTarget(EntityTargetLivingEntityEvent event) {
		super.onTarget(event);
	}

	class UpdateNameLater extends BukkitRunnable {
		@SuppressWarnings("unchecked")
		@Override
		public void run() {
			LivingEntity entity = getEntity();
			if (entity != null) {
				double maxHealth = ((Damageable)entity).getMaxHealth();
				double nowHealth = ((Damageable)entity).getHealth();
				entity.setCustomName(StringUtils.join(getName(), ChatColor.RED , " [" , (int)nowHealth , "/" , (int)maxHealth, "]"));

				if (Main.plugin.getServer().getPluginManager().isPluginEnabled("ActionBarAPI")) {
					for (TheLowPlayer p : combatPlayerSet.keySet()) {
						if (p == null) {
							continue;
						}
						if (p.isOnline()) {
							if (nowHealth <= 0) {
								ActionBarAPI.sendActionBar(p.getOnlinePlayer(), "");
							} else {
								ActionBarAPI.sendActionBar(p.getOnlinePlayer(), getName() + ChatColor.RED + " [" + (int)nowHealth + "/" + (int)maxHealth+ "]");
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void addExp(LivingEntity entity, LastDamageMethodType type, TheLowPlayer p) {
		int exp = getExp(type);
		if (exp == -1) {
			exp = (int) (((Damageable)entity).getMaxHealth() * 1.3);
		}

		//totalダメージを取得
		double totalDamage = 0;
		for (Double damage : combatDamagePlayerMap.values()) {
			if (damage != null) {
				totalDamage += damage.doubleValue();
			}
		}
		//経験値を分配する
		for (Entry<TheLowPlayer, Map<LevelType, Double>> entry : combatDamagePlayerMap.rowMap().entrySet()) {
			TheLowPlayer theLowPlayer = entry.getKey();
			for (Entry<LevelType, Double> typeEntry : entry.getValue().entrySet()) {
				if (typeEntry.getKey() != null) {
					theLowPlayer.addExp(typeEntry.getKey(), (int) (exp * typeEntry.getValue() / totalDamage), StatusAddReason.monster_drop);
				}
			}
		}
	}
}
