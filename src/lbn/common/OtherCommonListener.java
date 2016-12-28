package lbn.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockCanBuildEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import lbn.command.CommandChest;
import lbn.command.CommandGiveItem;
import lbn.command.util.CommandSpecialSign;
import lbn.command.util.SimplySetSpawnPointCommand;
import lbn.common.menu.MenuSelectorManager;
import lbn.common.other.BookshelfCommandRunner;
import lbn.common.other.DungeonList;
import lbn.common.other.GetItemSign;
import lbn.common.other.InHandItemClickSign;
import lbn.common.other.SoulBound;
import lbn.common.other.Stun;
import lbn.dungeoncore.Main;
import lbn.mobspawn.point.MobSpawnerPointManager;
import lbn.util.ItemStackUtil;

public class OtherCommonListener implements Listener{
	@EventHandler
	public void StopDrops(PlayerDropItemEvent event) {
		SoulBound.onDrops(event);
	}

	@EventHandler
	public void onCraftSoulBound(PrepareItemCraftEvent e) {
		SoulBound.onCraftSoulBound(e);
	}

	@EventHandler
	public void onDeathPlayer(PlayerDeathEvent e) {
		for (ItemStack s : new ArrayList<>(e.getDrops())) {
			if (ItemStackUtil.isSoulBound(s)) {
				e.getDrops().remove(s);
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void StopClicking(InventoryClickEvent event) {
		//soulboundの処理
		SoulBound.onInventoryClick(event);
	}

	/**
	 * 高レベルエンチャントを付与する
	 * @param event
	 */
	@EventHandler
	public void testClicking(InventoryClickEvent event) {
		final Inventory inv = event.getView().getTopInventory();
		if (inv == null || inv.getType() != InventoryType.ANVIL) {
			return;
		}
		new BukkitRunnable() {
			@Override
			public void run() {
				//エンチャントを行うアイテム
				ItemStack material = inv.getItem(0);
				//エンチャントブック
				ItemStack enchant = inv.getItem(1);
				//エンチャント後のアイテム
				ItemStack product = inv.getItem(2);

				if (needExtraEnchant(enchant, material, product)) {
					EnchantmentStorageMeta itemMeta = (EnchantmentStorageMeta) enchant.getItemMeta();
					Map<Enchantment, Integer> storedEnchants = itemMeta.getStoredEnchants();
					product.addUnsafeEnchantments(storedEnchants);
				}
			}
		}.runTaskLater(Main.plugin, 1);
	}

	private boolean needExtraEnchant(ItemStack enchant, ItemStack material, ItemStack product) {
		if (material == null || enchant == null || product == null) {
			return false;
		}
		if (enchant.getType() != Material.ENCHANTED_BOOK) {
			return false;
		}
		EnchantmentStorageMeta itemMeta = (EnchantmentStorageMeta) enchant.getItemMeta();
		if (!ItemStackUtil.isUnsafeEnchant(itemMeta)) {
			return false;
		}

		return true;
	}

	@EventHandler
	public void shootbow(EntityShootBowEvent e) {
		Stun.onShootbow(e);
	}

	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e) {
		Stun.onDamage(e);
	}

	@EventHandler
	public void onClick(PlayerInteractEvent event) {
		//stun処理
		Stun.onClick(event);

		if (event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.LAPIS_BLOCK) {
			DungeonList.sendDungeonInfo(event.getClickedBlock().getLocation(), event.getPlayer());
		}
	}

	static boolean notItemFlg = false;

	@EventHandler
	public void onBlockCanBuildEvent(BlockCanBuildEvent event) {
		if (!event.isBuildable() && event.getMaterial() == Material.GRAVEL
				&& (event.getBlock().getType() == Material.REDSTONE_TORCH_ON || event.getBlock().getType() == Material.REDSTONE_TORCH_OFF)) {
			notItemFlg = true;
		} else {
			notItemFlg = false;
		}
	}

	@EventHandler
	public void onItemSpawnEvent(ItemSpawnEvent e) {
		try {
			if (e.getEntity().getItemStack().getType() == Material.GRAVEL) {
				if (notItemFlg) {
					e.getEntity().remove();
				}
			}
		} catch (NullPointerException ex) {
			ex.printStackTrace();
		} finally {
			notItemFlg = false;
		}
	}

	@EventHandler
	public void onEntityChangeBlockEvent(EntityChangeBlockEvent event) {
		if (event.getEntity().getType() == EntityType.FALLING_BLOCK
				&& ((FallingBlock) event.getEntity()).getMaterial() == Material.GRAVEL
				&& event.getTo() == Material.AIR) {
			Block block = event.getBlock().getLocation().add(0, 1, 0).getBlock();
			if (block.getType() == Material.STONE_PLATE) {
				block.setType(Material.AIR);
			}
		}
	}


	public static int[] randomNumber = new int[100];
	static {
		Random rnd = new Random();
		for (int i = 0; i < randomNumber.length; i++) {
			randomNumber[i] = rnd.nextInt(16);
		}
	}
	static int i = 0;

	static HashMap<HumanEntity, Integer> foodlevel = new HashMap<HumanEntity, Integer>();

	@EventHandler
	public void onChangeFoodLevel(FoodLevelChangeEvent e) {
		//1/16の確率で腹減りを起こさせる
		HumanEntity entity = e.getEntity();

		Integer level = foodlevel.get(entity);
		if (level == null || level <= e.getFoodLevel()) {
			foodlevel.put(e.getEntity(), e.getFoodLevel());
			return;
		}

		if (!entity.hasPotionEffect(PotionEffectType.HUNGER)) {
			if (i >= randomNumber.length) {
				i = 0;
			}
			if (randomNumber[i] != 1) {
				e.setCancelled(true);
			}
			i++;
		}

		foodlevel.put(e.getEntity(), e.getFoodLevel());
	}

	@EventHandler
	public void onClick2(PlayerInteractEvent e) {
		if (e.getPlayer().getGameMode() == GameMode.CREATIVE && e.getClickedBlock() != null) {
			if (CommandSpecialSign.isWhiteSign(e)) {
				CommandSpecialSign.signClickMap.put(e.getPlayer(), e.getClickedBlock().getLocation());
			} else if (e.getClickedBlock().getType() == Material.CHEST) {
				CommandChest.chestClickMap.put(e.getPlayer(), e.getClickedBlock().getLocation());
			}
		}

		//空白の看板に書き込む
		if (InHandItemClickSign.onWriteSign(e)) {
			return;
		}

		//鍵を持ったままクリックする看板
		InHandItemClickSign tpSign = new InHandItemClickSign(e);
		if (tpSign.isSuccess()) {
			tpSign.doClick(e);
			e.setCancelled(true);
			return;
		}

		//何も持たない状態でクリックしアイテムを取得する看板
		GetItemSign getItemSign = new GetItemSign(e);
		if (getItemSign.isSuccess()) {
			getItemSign.doClick(e);
			e.setCancelled(true);
			return;
		}

		BookshelfCommandRunner bookshelfCommandRunner = new BookshelfCommandRunner(e);
		if (bookshelfCommandRunner.canDo()) {
			bookshelfCommandRunner.doCommand();
		}
	}

	@EventHandler
	public void onChunkLoad(ChunkLoadEvent e) {
//		VillagerChunkManager.onLoadChank(e.getChunk());
	}

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		MenuSelectorManager.onSelect(e);

		CommandGiveItem.onClick(e);
	}

	@EventHandler
	public void onDrag(InventoryDragEvent e) {
		MenuSelectorManager.onSelect(e);

		CommandGiveItem.onClick(e);
	}

	@EventHandler
	public void ProjectileHitEventBySpawnSet(ProjectileHitEvent e) {
		SimplySetSpawnPointCommand.ProjectileHitEvent(e);
	}

	@EventHandler
	public void PlayerInteractEventBySpawnSet(PlayerInteractEvent e) {
		SimplySetSpawnPointCommand.onClick(e);
	}

	@EventHandler
	public void onBrakeSponge(BlockBreakEvent e) {
		MobSpawnerPointManager.onBrakeSponge(e);
	}

}
