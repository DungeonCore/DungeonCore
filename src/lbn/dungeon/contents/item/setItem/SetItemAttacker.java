package lbn.dungeon.contents.item.setItem;

import java.util.Arrays;
import java.util.List;

import lbn.item.setItem.AbstractCommonSetItem;
import lbn.item.setItem.SetItemParts;
import lbn.item.setItem.SetItemPartsType;
import lbn.player.appendix.PlayerAppendixManager;
import lbn.player.appendix.appendixObject.SetItemAppendix;
import lbn.util.particle.ParticleData;
import lbn.util.particle.ParticleType;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SetItemAttacker extends AbstractCommonSetItem{
	public SetItemAttacker() {
		appendix.setMaxHealth(14.0);
		appendix.setAttack(6);
//		appendix.setAttackDefence(10);
	}

	private static ParticleData particleData = new ParticleData(ParticleType.flame, 20);

	@Override
	public void doRutine(Player p, ItemStack[] itemStacks) {
		particleData.run(p.getLocation().add(0, 1, 0));
	}


	private SetItemAppendix appendix =  new SetItemAppendix(SetItemPartsType.SLOT2);

	@Override
	public void startJob(Player p, ItemStack ...item) {
		PlayerAppendixManager.addAppendix(p, appendix);
	}

	@Override
	public void endJob(Player p) {
		PlayerAppendixManager.removeAppendix(p, appendix);
	}


	@Override
	public List<String> getLore() {
		return Arrays.asList("4箇所装備することで、以下の効果を得る", "   ・最大体力14上昇", "   ・攻撃威力4上昇", "   ・防御力上昇");
	}

	@Override
	public String getName() {
		return "ATTACKER SET";
	}

	@Override
	protected List<SetItemParts> getAllItemParts() {
		SetItemParts helmet = new SetItemParts(this, Material.DIAMOND_HELMET, SetItemPartsType.HELMET);
		SetItemParts chestplate = new SetItemParts(this, Material.IRON_CHESTPLATE, SetItemPartsType.CHEST_PLATE);
		SetItemParts legginse = new SetItemParts(this, Material.DIAMOND_LEGGINGS, SetItemPartsType.LEGGINSE);
		SetItemParts boots = new SetItemParts(this, Material.DIAMOND_BOOTS, SetItemPartsType.BOOTS);
		return Arrays.asList(helmet, chestplate, legginse, boots);
	}

}
