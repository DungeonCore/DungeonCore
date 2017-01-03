package lbn.mob;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import lbn.common.event.player.PlayerCustomMobSpawnEvent;
import lbn.dungeoncore.Main;
import lbn.item.ItemInterface;
import lbn.item.ItemManager;
import lbn.mob.attribute.Attribute;
import lbn.mob.attribute.AttributeNormal;
import lbn.mob.mob.BossMobable;
import lbn.mob.mob.SummonMobable;
import lbn.mob.mob.abstractmob.villager.AbstractVillager;
import lbn.player.AttackType;
import lbn.player.status.IStatusManager;
import lbn.player.status.StatusAddReason;
import lbn.quest.QuestProcessingStatus;
import lbn.quest.quest.PickItemQuest;
import lbn.quest.questData.PlayerQuestSession;
import lbn.quest.questData.PlayerQuestSessionManager;
import lbn.util.JavaUtil;
import lbn.util.Message;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class AbstractMob<T extends Entity> {
	protected Random rnd = new Random();

	boolean isBoss = this instanceof BossMobable;
	public boolean isBoss() {
		return isBoss;
	}


	TheLowMobType type = TheLowMobType.NORMAL;
	{
		if (this instanceof SummonMobable) {
			type = TheLowMobType.SUMMON;
		} else if (this instanceof AbstractVillager) {
			type = TheLowMobType.VILLAGER;
		}
	}

	public TheLowMobType getTheLowMobType() {
		return type;
	}

	abstract public String getName();

	abstract public void onSpawn(PlayerCustomMobSpawnEvent e);

	public void onAttackBefore(LivingEntity mob, LivingEntity target, EntityDamageByEntityEvent e) {
		//属性適用
		getAttribute().onAttack(mob, target, e);
	}

	public boolean isRiding() {
		return false;
	}

	abstract public void onAttack(LivingEntity mob, LivingEntity target, EntityDamageByEntityEvent e);

	abstract public void onDamage(LivingEntity mob, Entity damager, EntityDamageByEntityEvent e);

	Attribute attribute = new AttributeNormal();
	public Attribute getAttribute() {
		return attribute;
	}

	public int getDropGalions() {
		return 10;
	}

	public void onDamageBefore(LivingEntity mob, Entity damager, EntityDamageByEntityEvent e) {
		//属性適用
		getAttribute().onDamage(mob, damager, e);

		if (getTheLowMobType() == TheLowMobType.VILLAGER) {
			((AbstractVillager)this).isExecuteOnDamage = true;
			return;
		}

		//ボスモンスターの時はダメージ計算の補正を行わない
		if (isBoss()) {
			return;
		}

		if (getTheLowMobType() == TheLowMobType.SUMMON) {
			return;
		}

		Player player = LastDamageManager.getLastDamagePlayer(mob);
		AttackType type = LastDamageManager.getLastDamageAttackType(mob);
		if (player == null) {
//			new RuntimeException("Last Damage player is null: "+ getName()).printStackTrace();
			return;
		}

		double damagePer = getDamagePer(player, mob, type);
		e.setDamage(e.getDamage() * damagePer);

		//クエリの時だけダメージを表示させる
		if (player.getGameMode() == GameMode.CREATIVE) {
			new BukkitRunnable() {
				double health = ((Damageable)mob).getHealth();
				@Override
				public void run() {
					Message.sendMessage(player, "{0}:{1}ダメージ！！(もとのダメージの倍率:{2})", type, JavaUtil.round(health - ((Damageable)mob).getHealth(), 2), JavaUtil.round(damagePer, 2));
				}
			}.runTaskLater(Main.plugin, 2);
		}
	}

	protected double getDamagePer(Player p, LivingEntity mob, AttackType type) {
		double editLevel = 0;
		if (type.isDamageCaluculate()) {
			EntityDamageEvent cause = mob.getLastDamageCause();
			//この場合は攻撃者がSummonMobとするのでダメージ倍率は考慮しない
			if (cause != null && type == AttackType.MAGIC && cause.getCause() == DamageCause.ENTITY_ATTACK) {
				return 1.0;
			}
			int attackLevel = type.getManager().getLevel(p);
			//他のレベルは影響しないようにする
//			int mainLevel = MainStatusManager.getInstance().getLevel(p);
			editLevel = attackLevel;
			//無いとは思うが念のため
//			if (editLevel < 0) {
//				new RuntimeException(Message.getMessage("edit level is minus. : mainLevel{0} , {1}{2} ", mainLevel, type.getManager().getManagerName(), attackLevel)).printStackTrace();
//				editLevel = MagicStatusManager.getInstance().getLevel(p);
//			}
		}

		return type.getDamagePer((int)editLevel);
	}

	abstract public void onOtherDamage(EntityDamageEvent e);

	public void onDeath(EntityDeathEvent e) {
		if (isRiding()) {
			List<Entity> nearbyEntities = e.getEntity().getNearbyEntities(1, 1, 1);
			for (Entity entity : nearbyEntities) {
				if (entity.getType().isAlive()) {
					if (((LivingEntity)entity).getPassenger() != null && ((LivingEntity)entity).getPassenger().isDead()) {
						entity.remove();
					}
				}
			}
		}
		onDeathPrivate(e);
	}

	abstract public void onDeathPrivate(EntityDeathEvent e);

	public void onShotbow(EntityShootBowEvent e) {
	}

	public void onInteractEntity(PlayerInteractEntityEvent e) {

	}

	 public void onTeleport(EntityTeleportEvent e) {
		 if (e.getEntityType() == EntityType.ENDERMAN) {
			 e.setCancelled(true);
		 }
	 }

	 public void onProjectileHitEntity(LivingEntity mob, LivingEntity target, EntityDamageByEntityEvent e) {
	}

	public boolean isNullMob() {
		return false;
	}

	public void onProjectileHit(ProjectileHitEvent e) {
	}

	public void onTarget(EntityTargetLivingEntityEvent event) {
		//多分いらないのでコメントアウト
//		if (event.getTarget().getType() != EntityType.PLAYER) {
//			List<Entity> nearbyEntities = event.getEntity().getNearbyEntities(50, 10, 50);
//			for (Entity entity : nearbyEntities) {
//				if (entity.getType() == EntityType.PLAYER) {
//					event.setTarget(entity);
//					return;
//				}
//			}
//		}
	}

	final public T spawn(Location loc) {
		T entity = spawnPrivate(loc);
		if (entity == null) {
			return null;
		}

		if ((getName() != null || !getName().isEmpty()) && entity.getType().isAlive()) {
			((LivingEntity)entity).setCustomName(getAttribute().getPrefix() + getName());
		}

		//ボスと召喚モブの時は名前をずっと表示する
		if ((isBoss() || SummonPlayerManager.isSummonMob(entity)) && entity.getType().isAlive()) {
			((LivingEntity)entity).setCustomNameVisible(true);
		}

		if (entity.getType().isAlive()) {
			//eventを発火させる
			PlayerCustomMobSpawnEvent event = new PlayerCustomMobSpawnEvent((LivingEntity) entity);
			Bukkit.getServer().getPluginManager().callEvent(event);
		}

		return entity;
	}

	@SuppressWarnings("unchecked")
	protected T spawnPrivate(Location loc) {
		T spawnCreature = (T)loc.getWorld().spawnEntity(loc, getEntityType());
		return spawnCreature;
	}

	abstract public EntityType getEntityType();

	public boolean isThisMob(LivingEntity e) {
		if (e.getCustomName() == null) {
			if (getName() == null) {
				return true;
			}
		} else {
			return e.getCustomName().equalsIgnoreCase(getName());
		}

		return false;
	}

	public List<ItemStack> getDropItem(Player lastDamagePlayer) {
		ArrayList<ItemStack> arrayList = new ArrayList<ItemStack>();

		if (isNormalDropChance(lastDamagePlayer)) {
			ItemStack[] normalItem = getNormalItem(lastDamagePlayer);
			if (normalItem != null) {
				arrayList.addAll(Arrays.asList(normalItem));
			}
		}
		if (isRareDropChance(lastDamagePlayer)) {
			ItemStack[] rarelItem = getRareItem(lastDamagePlayer);
			if (rarelItem != null) {
				arrayList.addAll(Arrays.asList(rarelItem));
			}
		}

		PlayerQuestSession questSession = null;

		//DROPするのがクエストアイテムの場合、クエスト進行中でないからドロップさせない
		Iterator<ItemStack> iterator = arrayList.iterator();
		label1:
		while (iterator.hasNext()) {
			//quest sessionインスタンスを作成
			if (questSession == null) {
				questSession = PlayerQuestSessionManager.getQuestSession(lastDamagePlayer.getPlayer());
			}

			ItemStack next = iterator.next();
			ItemInterface customItem = ItemManager.getCustomItem(next);
			//カスタムアイテムでないなら何もしない
			if (customItem == null) {
				continue;
			}

			//クエストアイテムでないなら何もしない
			if (!customItem.isQuestItem()) {
				continue;
			}

			//クエスト実行中か調べる
			Set<PickItemQuest> quest = PickItemQuest.getQuest(customItem);
			for (PickItemQuest pickItemQuest : quest) {
				//1つでもクエストが実行中なら許可する
				if (questSession.getProcessingStatus(pickItemQuest) == QuestProcessingStatus.PROCESSING) {
					continue label1;
				}
			}
			//一つも関連クエストが実行されていないなら許可しない
			iterator.remove();
		}
		return arrayList;
	}

	protected ItemStack[] getNormalItem(Player lastDamagePlayer) {
		return null;
	}

	protected boolean isNormalDropChance(Player lastDamagePlayer) {
		return rnd.nextInt(20) == 0;
	}


	protected ItemStack[] getRareItem(Player lastDamagePlayer) {
		return null;
	}

	protected boolean isRareDropChance(Player lastDamagePlayer) {
		return rnd.nextInt(100) == 0;
	}

	public void updateName(boolean islater) {
	}

	@Override
	public boolean equals(Object obj) {
		if (obj  != null && obj instanceof AbstractMob<?>) {
			return ((AbstractMob<?>)obj).getName().equals(getName()) && ((AbstractMob<?>)obj).getEntityType() == getEntityType();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return getName().hashCode();
	}

	public int getExp(AttackType type) {
		return -1;
	}

	public void onDisablePlugin(PluginDisableEvent e) {

	}

	public void addExp(LivingEntity entity, AttackType type, Player p) {
		IStatusManager instance = type.getManager();
		if (instance == null) {
			return;
		}

		//コウモリの場合は経験値を加算しない
		if (entity.getType() == EntityType.BAT) {
			return;
		}

		//EXPを加算する
		AbstractMob<?> mob = MobHolder.getMob(entity);
		int exp = getExp(type);
		double health = ((Damageable)entity).getMaxHealth();

		//体力の1.3倍
		if (mob.isBoss()) {
			exp = (int) (health * 1.3);
		} else {
			//9~16まで体力に合わせて経験値を決める
			if (exp == -1) {
				if (health < 20) {
					exp = 9;
				} else if (health > 40) {
					exp = 16;
				} else {
					exp = (int) ((health - 20.0) * 6.0 / 20 + 10);
				}
			}
		}
		instance.addExp(p, exp, StatusAddReason.monster_drop);
	}
}
