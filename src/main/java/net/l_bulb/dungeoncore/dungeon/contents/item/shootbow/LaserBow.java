package net.l_bulb.dungeoncore.dungeon.contents.item.shootbow;

//今後使う可能性があるためコメントアウト
public class LaserBow
// implements Cooltimable
{
  // @Override
  // public String getItemName() {
  // return "LASER BOW";
  // }
  //
  // @Override
  // public double getAttackItemDamage(int strengthLevel) {
  // return getMaxAttackDamage() / 0.8;
  // }
  //
  // final static Vector zeroVec = new Vector(0, 0, 0);
  //
  // @Override
  // public void excuteOnShootBow2(EntityShootBowEvent e) {
  // final Entity projectile = e.getProjectile();
  // final Vector velocity = projectile.getVelocity();
  //
  // new BukkitRunnable() {
  // int count = 0;
  //
  // @Override
  // public void run() {
  // count++;
  // if (count >= 20 * 60) {
  // cancel();
  // }
  // if (!projectile.isValid() || projectile.isOnGround() || projectile.getVelocity().equals(zeroVec)) {
  // cancel();
  // }
  // projectile.setVelocity(velocity);
  // }
  // }.runTaskTimer(Main.plugin, 0, 1);
  // }
  //
  // @Override
  // public void onProjectileHit(ProjectileHitEvent e, ItemStack item) {
  // Projectile entity = e.getEntity();
  //
  // if (!entity.hasMetadata("bow_date_lbn_doungeon_laserbow_attack")) {
  // Location location = entity.getLocation();
  // if (!(entity.getShooter() instanceof Entity)) { return; }
  // Entity shooter = (Entity) entity.getShooter();
  // final Location shooterLoc = shooter.getLocation();
  //
  // // 爆発させる
  // if (LivingEntityUtil.isFriendship(shooter)) {
  // new NoPlayerDamageExplotionForAttackType(location, 3, (LivingEntity) shooter, LastDamageMethodType.BOW).runExplosion();
  // } else {
  // new NotMonsterDamageExplosion(location, 3).runExplosion();
  // }
  //
  // // 距離が近い時はパーティクルを出す
  // if (location.distance(shooterLoc) < 100) {
  // // 軌道にパーティクルを追加する
  // final StraightParticleData particleData = new StraightParticleData(new ParticleData(ParticleType.reddust, 10), location);
  // particleData.run(shooterLoc);
  // }
  // }
  //
  // // 矢を削除する
  // entity.remove();
  // }
  //
  // @Override
  // public String[] getDetail() {
  // return new String[] {
  // "一直線に飛んで、着地地点で",
  // "爆発します"
  // };
  // }
  //
  // @Override
  // protected void excuteOnLeftClick2(PlayerInteractEvent e) {
  // CooltimeManager cooltimeManager = new CooltimeManager(e, this);
  // if (!cooltimeManager.canUse()) {
  // cooltimeManager.sendCooltimeMessage(e.getPlayer());
  // return;
  // }
  // cooltimeManager.setCoolTime();
  //
  // Player player = e.getPlayer();
  // Location add = player.getLocation().add(0, 7, 0);
  //
  // new LaserBowRunner(add, player, this, e.getItem()).runTaskTimer(1);
  // }
  //
  // @Override
  // public int getAvailableLevel() {
  // return 60;
  // }
  //
  // @Override
  // protected String[] getStrengthDetail2(int level) {
  // return null;
  // }
  //
  // @Override
  // public int getCooltimeTick(ItemStack item) {
  // return 20 * 40;
  // }
  //
  // @Override
  // public String getId() {
  // return "laser bow";
  // }
  //
  // @Override
  // public StrengthTemplate getStrengthTemplate() {
  // return new WeaponStrengthTemplate();
  // }
  //
  // @Override
  // protected int getBaseBuyPrice() {
  // return 5000;
  // }
  //
  // @Override
  // public void onProjectileLaunchEvent(ProjectileLaunchEvent e, ItemStack item) {}
  //
  // }
  //
  // class LaserBowRunner extends LbnRunnable {
  // public LaserBowRunner(Location add, Player player,
  // LaserBow bow, ItemStack item) {
  // this.add = add;
  // this.player = player;
  // this.bow = bow;
  // this.item = item;
  // }
  //
  // CircleParticleData circleParticleData = new CircleParticleData(new ParticleData(ParticleType.instantSpell, 10), 3);
  //
  // ParticleData data = new ParticleData(ParticleType.lava, 3);
  //
  // Random rnd = new Random();
  //
  // Location add;
  // Player player;
  // LaserBow bow;
  // ItemStack item;
  //
  // @Override
  // public void run2() {
  // // 0.5秒に一度パーティクルを発生
  // if (getAgeTick() % 20 == 0) {
  // circleParticleData.run(add);
  // }
  // int rndAngle = rnd.nextInt(360);
  // double rndRadius = rnd.nextInt(30) / 10.0;
  //
  // Location add2 = new Location(add.getWorld(), add.getX() + Math.sin(Math.toRadians(rndAngle)) * rndRadius, add.getY(),
  // add.getZ() + Math.cos(Math.toRadians(rndAngle)) * rndRadius);
  // data.run(add2);
  // Arrow spawnArrow = add.getWorld().spawnArrow(add2, new Vector(0, -1, 0), (float) 1.2, 3);
  // spawnArrow.setShooter(player);
  // spawnArrow.setBounce(false);
  //
  // // 矢の情報を付与する
  // ProjectileManager.onLaunchProjectile(spawnArrow, bow, item);
  //
  // // 弓のアイテム情報をつける
  // spawnArrow.setMetadata("bow_date_lbn_doungeon_laserbow_attack", new FixedMetadataValue(Main.plugin, "1"));
  //
  // add.getWorld().playSound(add2, Sound.BLAZE_HIT, 1, 1);
  //
  // new BukkitRunnable() {
  // @Override
  // public void run() {
  // StraightParticleData particleData = new StraightParticleData(new ParticleData(ParticleType.reddust, 10), add2);
  // particleData.run(spawnArrow);
  // }
  // }.runTaskLater(Main.plugin, 10);
  //
  // if (isElapsedSecond(8)) {
  // cancel();
  // }
  // }

}
