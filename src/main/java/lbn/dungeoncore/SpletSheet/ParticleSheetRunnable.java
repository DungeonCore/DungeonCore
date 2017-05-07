package lbn.dungeoncore.SpletSheet;

import java.util.Arrays;
import java.util.concurrent.Future;

import net.minecraft.server.v1_8_R1.EnumParticle;

import org.bukkit.command.CommandSender;

import lbn.common.particle.CircleParticleData;
import lbn.common.particle.ParticleData;
import lbn.common.particle.ParticleManager;
import lbn.common.particle.SpringParticleData;
import lbn.util.JavaUtil;

public class ParticleSheetRunnable extends AbstractSheetRunable {

  public ParticleSheetRunnable(CommandSender p) {
    super(p);
  }

  @Override
  protected String getQuery() {
    return null;
  }

  @Override
  public String getSheetName() {
    return "particle";
  }

  @Override
  public String[] getTag() {
    return new String[] { "id", "particle", "shape", "data", "amount", "dx", "dy", "dz" };
  }

  @Override
  public void onCallbackFunction(Future<String[][]> submit) throws Exception {
    ParticleManager.clear();
    super.onCallbackFunction(submit);
  }

  @Override
  protected void excuteOnerow(String[] row) {
    String id = row[0];
    if (id == null || id.isEmpty()) {
      sendMessage("idが存在しません。" + Arrays.toString(row));
      return;
    }

    String particle = row[1];
    EnumParticle enumParticle = null;
    try {
      enumParticle = EnumParticle.valueOf(particle);
    } catch (Exception e) {}

    if (enumParticle == null) {
      sendMessage("particleが不正です:" + enumParticle);
      return;
    }

    ParticleData particleData = getParticleData(row[2], enumParticle);
    if (particleData == null) {
      sendMessage("shapeが不正です:" + row[2]);
      return;
    }

    double data = JavaUtil.getDouble(row[3], 0);
    int amount = JavaUtil.getInt(row[4], -1);

    double dx = JavaUtil.getDouble(row[5], -1);
    double dy = JavaUtil.getDouble(row[6], -1);
    double dz = JavaUtil.getDouble(row[7], -1);

    particleData.setLastArgument(data);

    if (amount != -1) {
      particleData.setAmount(amount);
    }

    if (dx != -1 && dy != -1 && dz != -1) {
      particleData.setDispersion(dx, dy, dz);
    }

    ParticleManager.regist(id, particleData);
  }

  protected ParticleData getParticleData(String shape, EnumParticle type) {
    ParticleData data;
    if ("円".equals(shape)) {
      data = new CircleParticleData(new ParticleData(type, 1), 3);
    } else if ("バネ状".equals(shape)) {
      data = new SpringParticleData(new ParticleData(type, 3), 3, 6, 1.5, 10);
    } else {
      data = new ParticleData(type, 30);
    }
    return data;
  }
}
