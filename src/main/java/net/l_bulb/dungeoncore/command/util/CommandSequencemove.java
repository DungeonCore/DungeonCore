package net.l_bulb.dungeoncore.command.util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.util.Vector;

import net.l_bulb.dungeoncore.util.BlockUtil.BlockData;
import net.l_bulb.dungeoncore.util.LbnRunnable;
import net.l_bulb.dungeoncore.util.MinecraftUtil;

import lombok.Data;

public class CommandSequencemove implements CommandExecutor, UsageCommandable {

  @Override
  public String getUsage() {
    return "<command> second count id:data x y z dx dy dz  \n" +
        "second ・・・何秒間に一回動くか  \n" +
        "count ・・・何回動くか  \n" +
        "x y z　・・スタート地点の座標(3×3の中心)  \n" +
        "dx dy dz ・・・動く方向  \n" +
        "id:data  ・・・ブロックIDとデータ値";
  }

  @Override
  public String getDescription() {
    return "3×3の動く足場を作成する";
  }

  @Override
  public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
    RemoveBlockBean bean = createData(arg3, MinecraftUtil.getSenderLocation(arg0).getWorld());
    if (bean == null) { return false; }
    // beanを初期化する
    bean.init();

    new LbnRunnable() {
      boolean isFirst = true;

      @Override
      public void run2() {
        // 一番最初は足場を設置する
        if (isFirst) {
          for (Vector vec : getScaffoldLocation(bean.getBeforeLocationl())) {
            setBlock(vec, bean.getW(), bean.getBlockData());
          }
          // 座標を更新する
          bean.incrementPoint();
          isFirst = false;
          return;
        }

        // 前の座標
        List<Vector> beforeScaffoldLocation1 = getScaffoldLocation(bean.getBeforeLocationl());
        List<Vector> beforeScaffoldLocation2 = new ArrayList<>(beforeScaffoldLocation1);
        // 次の座標
        List<Vector> nowScaffoldLocation = getScaffoldLocation(bean.getNowLocationl());

        // 消える座標
        beforeScaffoldLocation1.removeAll(nowScaffoldLocation);
        // 次に表示される座標
        nowScaffoldLocation.removeAll(beforeScaffoldLocation2);

        // ブロックを消す
        for (Vector vec : beforeScaffoldLocation1) {
          vec.toLocation(bean.getW()).getBlock().setType(Material.AIR);
        }

        // ブロックを設置する
        for (Vector vec : nowScaffoldLocation) {
          World w = bean.getW();
          BlockData blockData = bean.getBlockData();
          // 指定したブロックを設置する
          setBlock(vec, w, blockData);
        }

        // 座標を更新する
        bean.incrementPoint();

        // 終了するなら終了する
        if (bean.isFinish()) {
          cancel();
        }
      }

      /**
       * ブロックを設置する
       *
       * @param vec
       * @param w
       * @param blockData
       */
      @SuppressWarnings("deprecation")
      public void setBlock(Vector vec, World w, BlockData blockData) {
        // ブロックを設置する
        Block block = vec.toLocation(w).getBlock();
        block.setType(blockData.getM());
        block.setData(blockData.getB());
      }
    }.runTaskTimer((long) (bean.getSecond() * 20));

    return true;
  }

  public RemoveBlockBean createData(String[] arg3, World w) {
    try {
      RemoveBlockBean removeBlockBean = new RemoveBlockBean(w);
      removeBlockBean.setSecond(Double.parseDouble(arg3[0]));
      removeBlockBean.setCount(Integer.parseInt(arg3[1]));

      BlockData blockData = MinecraftUtil.getBlockData(arg3[2]);
      if (blockData == null) { return null; }
      removeBlockBean.setBlockData(blockData);
      removeBlockBean.setStartX(Integer.parseInt(arg3[3]));
      removeBlockBean.setStartY(Integer.parseInt(arg3[4]));
      removeBlockBean.setStartZ(Integer.parseInt(arg3[5]));
      removeBlockBean.setDX(Integer.parseInt(arg3[6]));
      removeBlockBean.setDY(Integer.parseInt(arg3[7]));
      removeBlockBean.setDZ(Integer.parseInt(arg3[8]));
      return removeBlockBean;
    } catch (Exception e) {
      return null;
    }
  }

  Vector vecX = new Vector(1, 0, 0);
  Vector vecX2 = new Vector(-1, 0, 0);
  Vector vecZ = new Vector(0, 0, 1);
  Vector vecZ2 = new Vector(0, 0, -1);
  Vector vecCorner1 = new Vector(1, 0, 1);
  Vector vecCorner2 = new Vector(1, 0, -1);
  Vector vecCorner3 = new Vector(-1, 0, 1);
  Vector vecCorner4 = new Vector(-1, 0, -1);

  /**
   * 足場の座標を取得する
   *
   * @param center
   * @return
   */
  public List<Vector> getScaffoldLocation(Location center) {
    ArrayList<Vector> vecList = new ArrayList<>();
    Vector vector = new Vector(center.getBlockX(), center.getBlockY(), center.getBlockZ());

    vecList.add(vector.clone());
    vecList.add(vector.clone().add(vecX));
    vecList.add(vector.clone().add(vecX2));
    vecList.add(vector.clone().add(vecZ));
    vecList.add(vector.clone().add(vecZ2));
    vecList.add(vector.clone().add(vecCorner1));
    vecList.add(vector.clone().add(vecCorner2));
    vecList.add(vector.clone().add(vecCorner3));
    vecList.add(vector.clone().add(vecCorner4));

    return vecList;
  }
}

@Data
class RemoveBlockBean {
  private World w;

  public RemoveBlockBean(World w) {
    this.w = w;
  }

  // 何秒に一回動くか
  private double second;
  // 何回動くか
  private int count;
  // 開始座標
  private int startX;
  private int startY;
  private int startZ;
  // 移動方向の座標
  private double dX;
  private double dY;
  private double dZ;

  BlockData blockData;

  /**
   * 初期化処理
   */
  public void init() {
    // 合計
    double sum = dX + dY + dZ;
    if (sum != 0) {
      // 正規化する
      dX = dX / sum;
      dY = dY / sum;
      dZ = dZ / sum;
    }

    // スタート地点を登録する
    nowLocationl = new Location(w, startX, startY, startZ);
  }

  /**
   * 座標を更新する
   */
  public void incrementPoint() {
    // 座標を記録する
    beforeLocationl.setX(nowLocationl.getX());
    beforeLocationl.setY(nowLocationl.getY());
    beforeLocationl.setZ(nowLocationl.getZ());

    // 座標を追加する
    nowLocationl.add(dX, dY, dZ);

    // 座標を追加する
    nowLocationl.add(dX, dY, dZ);

    count--;
  }

  /**
   * 指定した回数実行されているならTRUE
   *
   * @return
   */
  public boolean isFinish() {
    return count <= 0;
  }

  /**
   * 現在の座標を取得
   *
   * @return
   */
  Location nowLocationl;

  /**
   * 1つ前の座標を取得
   *
   * @return
   */
  Location beforeLocationl = new Location(w, 0, 0, 0);
}
