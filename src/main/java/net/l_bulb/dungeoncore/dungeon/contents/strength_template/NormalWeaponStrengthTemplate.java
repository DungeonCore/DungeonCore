package net.l_bulb.dungeoncore.dungeon.contents.strength_template;

import org.bukkit.inventory.ItemStack;

public class NormalWeaponStrengthTemplate extends AbstractSelectMaxMinStrengthTemplate {
  int available;

  public NormalWeaponStrengthTemplate(int available, int maxStrengthCount) {
    super(maxStrengthCount);
    this.available = available;
  }

  @Override
  public ItemStack getStrengthMaterials(int level) {
    return null;
  }

  @Override
  public int getStrengthGalions(int level) {
    return 50 * level + 50;
  }

  @Override
  protected double getSuccessRateLevel0() {
    return 100 - getAvailableLevel() / 10;
  }

  /**
   * 武器の使用可能レベル
   *
   * @return
   */
  public int getAvailableLevel() {
    return available;
  }

  @Override
  protected double getSuccessRateLevelMax() {
    double a = -0.007;
    return a * getAvailableLevel() * getAvailableLevel() - (80 * a + 3.0 / 4.0) * getAvailableLevel() + 70;
  }

}
