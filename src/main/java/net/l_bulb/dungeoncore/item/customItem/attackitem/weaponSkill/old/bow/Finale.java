package net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.old.bow;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.l_bulb.dungeoncore.common.cooltime.Cooltimable;
import net.l_bulb.dungeoncore.common.cooltime.CooltimeManager;
import net.l_bulb.dungeoncore.common.other.Stun;
import net.l_bulb.dungeoncore.common.particle.ParticleData;
import net.l_bulb.dungeoncore.common.particle.ParticleType;
import net.l_bulb.dungeoncore.common.projectile.ProjectileInterface;
import net.l_bulb.dungeoncore.common.projectile.ProjectileManager;
import net.l_bulb.dungeoncore.dungeoncore.Main;
import net.l_bulb.dungeoncore.item.customItem.attackitem.AbstractAttackItem;
import net.l_bulb.dungeoncore.item.customItem.attackitem.weaponSkill.old.WeaponSkillWithMultiClick;

public class Finale extends WeaponSkillWithMultiClick implements ProjectileInterface {

  private static final String THELOW_WEAPONSKILL_FINALE_COUNT = "THELOW_WEAPONSKILL_FINALE_COUNT";

  @Override
  public void onProjectileLaunchEvent(ProjectileLaunchEvent e, ItemStack item) {

  }

  static ParticleData particleData = new ParticleData(ParticleType.reddust, 20);
  static {
    particleData.setDispersion(1, 1, 1);
    particleData.setLastArgument(2);
  }

  @Override
  public void onProjectileDamage(EntityDamageByEntityEvent e, ItemStack item, LivingEntity owner, LivingEntity target) {
    int count = -1;
    List<MetadataValue> metadata = e.getDamager().getMetadata(THELOW_WEAPONSKILL_FINALE_COUNT);
    if (!metadata.isEmpty()) {
      MetadataValue metadataValue = metadata.get(0);
      count = metadataValue.asInt();
    }

    switch (count) {
      case 1:
      case 2:
      case 3:
        target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, (int) (20 * getData(0)), 1));
        e.setDamage(e.getDamage() * getData(1));
        break;
      case 4:
        Stun.addStun(target, (int) (20 * 1.5));
        e.setDamage(e.getDamage() * getData(2));
        break;
      default:
        break;
    }
  }

  @Override
  public String getId() {
    return "skill13";
  }

  @Override
  public void onProjectileHit(ProjectileHitEvent event, ItemStack item) {

  }

  // 何発目の発射かを記録するためのMap
  HashMap<Player, Integer> countMap = new HashMap<>();

  @Override
  protected boolean onClick2(Player p, ItemStack item, AbstractAttackItem customItem) {

    // cooltimeを調べる
    CooltimeManager cooltimeManager = new CooltimeManager(p, new CooltimeImplemention(), item);
    if (cooltimeManager.canUse()) {
      // 矢を発射する
      Arrow launchProjectile = p.launchProjectile(Arrow.class, p.getLocation().getDirection().multiply(2));

      // 何発目の矢なのかを取得
      int count = 0;
      if (countMap.containsKey(p)) {
        count = countMap.get(p);
      }

      // カウントアップする
      count++;
      launchProjectile.setMetadata(THELOW_WEAPONSKILL_FINALE_COUNT, new FixedMetadataValue(Main.plugin, count));
      ProjectileManager.onLaunchProjectile(launchProjectile, this, item);

      // 音をだす
      p.playSound(p.getLocation(), Sound.FIREWORK_BLAST2, 1, (float) 0.1);

      // 何発目の矢なのかを記録する
      countMap.put(p, count);

      // クールタイムを開始する
      cooltimeManager.setCoolTime();

      // 4回目以上発射した場合は終了する
      return count >= 4;
    } else {
      // クールタイムがまだある場合は何もしない
      return false;
    }

  }

  @Override
  public double getTimeLimit() {
    return 30;
  }

  @Override
  protected void runWaitParticleData(Location loc, int i) {
    if (i % 3 == 0) {
      particleData.run(loc);
    }
  }

  @Override
  protected void startSkill(Player p, ItemStack item) {
    p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, (int) (getTimeLimit() * 20), 100), true);
    countMap.remove(p);
  }

  @Override
  protected void endSkill(Player p, ItemStack item) {
    p.removePotionEffect(PotionEffectType.SLOW);
    countMap.remove(p);
  }

  class CooltimeImplemention implements Cooltimable {
    @Override
    public int getCooltimeTick(ItemStack item) {
      return 20 * 1;
    }

    @Override
    public String getId() {
      return "weapon_skill_finale_shot_cooltime";
    }
  }
}
