package net.l_bulb.dungeoncore.util;

import java.util.Random;

//正規分布グラフ参考　http://keisan.casio.jp/exec/system/1161228881
public class NormalDistributionRandomGenerator {
  private Random random;
  private double mu;
  private double sigma;
  private boolean useCosine;

  /**
   * 正規分布に基づいた乱数を生成する
   *
   * @param mu 平均
   * @param sigma 標準偏差
   */
  public NormalDistributionRandomGenerator(double mu, double sigma) {
    random = new Random(System.currentTimeMillis());

    this.mu = mu;
    this.sigma = sigma;
    this.useCosine = true;
  }

  /**
   * 正規分布に基づいた乱数を取得する
   *
   * @return
   */
  public double next() {
    double x = random.nextDouble();
    double y = random.nextDouble();
    double result = 0.0;

    if (useCosine) {
      result = Math.sqrt(-2.0 * Math.log(x)) * Math.cos(2.0 * Math.PI * y);
    } else {
      result = Math.sqrt(-2.0 * Math.log(x)) * Math.sin(2.0 * Math.PI * y);
    }
    return result * sigma + mu;
  }

  // // テスト用メソッド
  // public static void main(String[] args) {
  // NormalDistributionRandomGenerator mathUtil = new NormalDistributionRandomGenerator(0.9, 0.13);
  // IntStream.range(0, 1000).asDoubleStream().map(d -> mathUtil.next()).sorted().forEach(System.out::println);
  // }
}
