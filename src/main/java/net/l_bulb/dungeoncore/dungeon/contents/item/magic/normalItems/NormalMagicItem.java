package net.l_bulb.dungeoncore.dungeon.contents.item.magic.normalItems;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.common.dropingEntity.DamagedFallingBlockForPlayer;
import net.l_bulb.dungeoncore.common.particle.ParticleData;
import net.l_bulb.dungeoncore.common.particle.ParticleType;
import net.l_bulb.dungeoncore.dungeon.contents.item.magic.normalItems.magicExcutor.FallingBlockMagicExcutor;
import net.l_bulb.dungeoncore.dungeon.contents.item.magic.normalItems.magicExcutor.SummonMagicExcutor;
import net.l_bulb.dungeoncore.dungeon.contents.mob.zombie.NormalSummonZombie;
import net.l_bulb.dungeoncore.dungeon.contents.strength_template.NormalWeaponStrengthTemplate;
import net.l_bulb.dungeoncore.dungeon.contents.strength_template.StrengthTemplate;
import net.l_bulb.dungeoncore.item.ItemInterface;
import net.l_bulb.dungeoncore.item.customItem.attackitem.old.MagicItemOld;
import net.l_bulb.dungeoncore.item.itemInterface.MagicExcuteable;
import net.l_bulb.dungeoncore.item.system.strength.StrengthOperator;
import net.l_bulb.dungeoncore.mob.AbstractMob;

public class NormalMagicItem extends MagicItemOld {

  public static List<ItemInterface> getAllItem() {
    ArrayList<ItemInterface> arrayList = new ArrayList<ItemInterface>();
    arrayList
        .add(new NormalMagicItem("始まりの杖", 0, Material.STICK, new BlockData(Material.WOOD, (byte) 1), new SoundData(Sound.ZOMBIE_WOODBREAK, 1, 3)));
    arrayList.add(new NormalMagicItem("アイスジャベリン", 10, Material.WOOD_HOE, new BlockData(Material.ICE), new SoundData(Sound.GLASS, 1, (float) 0.1)));
    arrayList.add(new NormalMagicItem("セイクリッド", 20, Material.STONE_HOE, new BlockData(Material.IRON_BLOCK),
        new SoundData(Sound.IRONGOLEM_HIT, 1, (float) 0.7)));
    arrayList.add(new NormalMagicItem("ジャベラス", 30, Material.IRON_HOE, new BlockData(Material.STAINED_CLAY, (byte) 11),
        new SoundData(Sound.ZOMBIE_METAL, 1, 1)));
    arrayList.add(new NormalMagicItem("ルーク", 40, Material.IRON_HOE, new BlockData(Material.STAINED_CLAY, (byte) 14),
        new SoundData(Sound.GHAST_FIREBALL, 1, 1)));
    arrayList
        .add(new NormalMagicItem("グレス", 50, Material.DIAMOND_HOE, new BlockData(Material.GOLD_BLOCK), new SoundData(Sound.GLASS, 1, (float) 0.1)));
    arrayList.add(new NormalMagicItem("ジュピター", 60, Material.DIAMOND_HOE, new BlockData(Material.DIAMOND_BLOCK),
        new SoundData(Sound.GHAST_FIREBALL, 1, 1)));
    arrayList.add(new NormalMagicItem("エルシオン", 70, Material.DIAMOND_HOE, new BlockData(Material.OBSIDIAN, (byte) 11),
        new SoundData(Sound.ZOMBIE_METAL, 1, (float) 0.1)));
    arrayList.add(new NormalMagicItem("セルシオ", 80, Material.DIAMOND_HOE, new BlockData(Material.OBSIDIAN, (byte) 11),
        new SoundData(Sound.ZOMBIE_METAL, 1, (float) 0.1)));
    return arrayList;
  }

  protected NormalMagicItem(String name,
      int availableLevel, Material m, BlockData blockdata, SoundData onHitSound) {
    this.id = "normalmagic_" + availableLevel;
    this.name = name;
    this.availableLevel = availableLevel;
    this.blockdata = blockdata;
    this.onHitSound = onHitSound;
    this.m = m;
  }

  Material m;
  String id;
  String name;
  int availableLevel;
  BlockData blockdata;
  SoundData onHitSound;

  @Override
  public String getId() {
    return "level" + getAvailableLevel() + "magicItem";
  }

  @Override
  public StrengthTemplate getStrengthTemplate() {
    return new NormalWeaponStrengthTemplate(getAvailableLevel(), getMaxStrengthCount());
  }

  @Override
  protected int getBaseBuyPrice() {
    return 100;
  }

  protected AbstractMob<Zombie> getSummonZombie(ItemStack item) {
    return new NormalSummonZombie(getAvailableLevel(), StrengthOperator.getLevel(item));
  }

  @Override
  protected MagicExcuteable getRightClickMagic(ItemStack item) {
    return new SummonMagicExcutor(getAvailableLevel(), item, getId() + "_rc") {
      @Override
      protected AbstractMob<Zombie> getSummonMob() {
        return getSummonZombie(item);
      }
    };
  }

  @Override
  protected MagicExcuteable getLeftClickMagic(ItemStack item) {
    return new FallingBlockMagicExcutor(item, getId() + "_lc") {
      @Override
      protected DamagedFallingBlockForPlayer getDamagedFallingBlock(Player p, PlayerInteractEvent e) {
        DamagedFallingBlockForPlayer fallingBlock = new DamagedFallingBlockForPlayer(p, ferFallingBlockType(), item,
            getAttackItemDamage(StrengthOperator.getLevel(item)), getFallingBlockData()) {
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
            playSoundWhenFaillingBlockHit(target);
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

  protected void playSoundWhenFaillingBlockHit(Entity target) {
    target.getWorld().playSound(target.getLocation(), onHitSound.s, onHitSound.vol, onHitSound.pitch);
  }

  protected byte getFallingBlockData() {
    return blockdata.data;
  }

  protected Material ferFallingBlockType() {
    return blockdata.m;
  }

  @Override
  public String getItemName() {
    return name;
  }

  @Override
  public int getAvailableLevel() {
    return availableLevel;
  }

  @Override
  protected String[] getStrengthDetail2(int level) {
    return null;
  }

  @Override
  protected Material getMaterial() {
    return m;
  }

  @Override
  public String[] getDetail() {
    return new String[] { "左クリックで遠距離攻撃" };
  }
}

class BlockData {
  protected BlockData(Material m) {
    this.m = m;
    this.data = 0;
  }

  protected BlockData(Material m, byte data) {
    this.m = m;
    this.data = data;
  }

  Material m;
  byte data;
}

class SoundData {
  protected SoundData(Sound s, float vol, float pitch) {
    this.s = s;
    this.vol = vol;
    this.pitch = pitch;
  }

  Sound s;
  float vol;
  float pitch;
}
