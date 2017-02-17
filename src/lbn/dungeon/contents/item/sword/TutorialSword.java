package lbn.dungeon.contents.item.sword;

import lbn.dungeon.contents.strength_template.StrengthTemplate;
import lbn.dungeon.contents.strength_template.TutorialTemplate;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class TutorialSword extends LevelSword{

	@Override
	public String getItemName() {
		return "チュートリアルソード";
	}

	@Override
	public String getId() {
		return "tutorial sword";
	}

	@Override
	public int getAvailableLevel() {
		return 0;
	}

	@Override
	public StrengthTemplate getStrengthTemplate() {
		return new TutorialTemplate();
	}

	@Override
	protected void excuteOnMeleeAttack2(ItemStack item, LivingEntity owner,
			LivingEntity target, EntityDamageByEntityEvent e) {
	}

	@Override
	protected void excuteOnRightClick2(PlayerInteractEvent e) {
	}

	@Override
	protected String[] getStrengthDetail2(int level) {
		return null;
	}

	@Override
	protected Material getMaterial() {
		return Material.WOOD_SWORD;
	}

	@Override
	public String[] getDetail() {
		return new String[]{"チュートリアル用の剣です"};
	}

	@Override
	public int getBuyPrice(ItemStack item) {
		return 5;
	}

}
