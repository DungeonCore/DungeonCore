package lbn.dungeon.contents.item.sword.special;

import java.util.Collection;
import java.util.HashSet;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import lbn.item.ItemInterface;
import lbn.mob.attribute.Attribute;
import lbn.mob.attribute.AttributeTree;


public class AttributeLeafSword extends AbstractSpecialSword{
	protected AttributeLeafSword(Material m, int availableLevel,  int maxStrength) {
		super(m, availableLevel);
		this.maxStrength = maxStrength;
	}

	public AttributeLeafSword() {
	}

	static HashSet<ItemInterface> hashSet = new HashSet<ItemInterface>();
	static {
		hashSet.add(new AttributeLeafSword(Material.IRON_SWORD, 20, 11));
		hashSet.add(new AttributeLeafSword(Material.IRON_SWORD, 35, 11));
		hashSet.add(new AttributeLeafSword(Material.DIAMOND_SWORD, 50, 11));
		hashSet.add(new AttributeLeafSword(Material.DIAMOND_SWORD, 65, 11));
		hashSet.add(new AttributeLeafSword(Material.DIAMOND_SWORD, 78, 12));
	}

	int maxStrength;

	@Override
	public String getSpecialName() {
		return "Leaf Sword";
	}

	@Override
	public int getMaxStrengthCount() {
		return maxStrength;
	}

	@Override
	public Collection<ItemInterface> getAllItem() {
		return hashSet;
	}

	@Override
	public int getRank() {
		return 5;
	}

	@Override
	protected double getSpecialDamagePercent() {
		return 1.2;
	}

	@Override
	protected String getBaseId() {
		return "leafSword";
	}

	@Override
	public int getMaxSlotCount() {
		return 5;
	}

	@Override
	public int getDefaultSlotCount() {
		return 3;
	}

	@Override
	protected void excuteOnMeleeAttack2(ItemStack item, LivingEntity owner,
			LivingEntity target, EntityDamageByEntityEvent e) {

	}

	@Override
	protected void excuteOnRightClick2(PlayerInteractEvent e) {

	}

	@Override
	protected String[] getDetail() {
		return new String[]{"森の力が宿りし剣"};
	}

	@Override
	public Attribute getAttribute() {
		return new AttributeTree();
	}

}
