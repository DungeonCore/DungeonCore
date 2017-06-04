package net.l_bulb.dungeoncore.util;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;
import java.util.function.Supplier;

import org.bukkit.Location;

public class JavaUtil {
  public static Set<Class<?>> getInterface(Class<?> clazz) {
    return new InterfaceGetter(clazz).getInterfaceList();
  }

  private static Random rnd = new Random();

  public static boolean isRandomTrue(int percent) {
    if (rnd.nextInt(100) < percent) { return true; }
    return false;
  }

  public static double round(double val, int digits) {
    double pow = Math.pow(10, digits);
    return Math.round(pow * val) / pow;
  }

  public static int getInt(String data, int other) {
    try {
      return Integer.parseInt(data.trim());
    } catch (Exception e) {
      return other;
    }
  }

  public static float getFloat(String data, float other) {
    try {
      return Float.parseFloat(data);
    } catch (Exception e) {
      return other;
    }
  }

  public static short getShort(String data, short other) {
    try {
      return Short.parseShort(data);
    } catch (Exception e) {
      return other;
    }
  }

  public static boolean getBoolean(String data, boolean other) {
    if ("true".equalsIgnoreCase(data)) {
      return true;
    } else if ("false".equalsIgnoreCase(data)) { return false; }
    return other;
  }

  public static double getDouble(String deta, double other) {
    try {
      return Double.parseDouble(deta);
    } catch (Exception e) {
      return other;
    }
  }

  static TimeZone timeZone = TimeZone.getTimeZone("Asia/Tokyo");

  public static long getJapanTimeInMillis() {
    Calendar cal1 = Calendar.getInstance(timeZone);
    long timeInMillis = cal1.getTimeInMillis();
    return timeInMillis;
  }

  @SuppressWarnings("unchecked")
  public static <T> T getField(Class<?> clazz, String fieldName, Object targetInstance) {
    try {
      Field field = clazz.getDeclaredField(fieldName);
      field.setAccessible(true);
      return (T) field.get(targetInstance);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Privateなメソッドに値を挿入する
   *
   * @param target_object
   * @param field_name
   * @param value
   * @throws Exception
   */
  public static void setPrivateField(Object target_object, String field_name, Object value) {
    try {
      Class<?> c = target_object.getClass();
      Field fld = c.getDeclaredField(field_name);
      fld.setAccessible(true);
      fld.set(target_object, value);
    } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
      e.printStackTrace();
    }
  }

  /**
   * 指定された座標間の距離の2乗を返す
   *
   * @param loc
   * @param x
   * @param y
   * @param z
   * @return
   */
  public static double getDistanceSquared(Location loc, double x, double y, double z) {
    return (loc.getX() - x) * (loc.getX() - x) + (loc.getY() - y) * (loc.getY() - y) + (loc.getZ() - z) * (loc.getZ() - z);
  }

  /**
   * 指定された座標間の距離の2乗を返す
   *
   * @param loc
   * @param x
   * @param y
   * @param z
   * @return
   */
  public static double getDistanceSquared(double x, double y, double z, double x1, double y1, double z1) {
    return x * x1 + y * y1 + z * z1;
  }

  /**
   * NullならnullValueを返す。nullじゃないならそのままvalueを返す
   *
   * @param value
   * @param nullValue
   * @return
   */
  public static <T> T getNull(T value, T nullValue) {
    if (value == null) { return nullValue; }
    return value;
  }

  /**
   * 指定した場所から指定した半径内のランダムな場所を取得する
   *
   * @param add
   * @param radius
   * @return
   */
  public static Location getRandomLocationInCircle(Location add, int radius) {
    int rndAngle = rnd.nextInt(360);
    double rndRadius = rnd.nextInt(radius * 10) / 10.0;

    return new Location(add.getWorld(), add.getX() + Math.sin(Math.toRadians(rndAngle)) * rndRadius, add.getY(),
        add.getZ() + Math.cos(Math.toRadians(rndAngle)) * rndRadius);
  }

  /**
   * 指定されたSupplierから値を取得する。もしエラーが起きればdefaultValueから取得する
   *
   * @param supplier
   * @param defaultValue
   * @return
   */
  public static <T> T getValueOrDefaultWhenThrow(Supplier<T> supplier, T defaultValue) {
    try {
      return supplier.get();
    } catch (Exception e) {
      return defaultValue;
    }
  }
}

class InterfaceGetter {
  private Set<Class<?>> interfaceList = new HashSet<>();

  private Class<?> clazz;

  public InterfaceGetter(Class<?> clazz) {
    this.clazz = clazz;
  }

  private void search(Class<?> clazz) {
    for (Class<?> inter : clazz.getInterfaces()) {
      // すでに探索済みなら何もしない
      if (interfaceList.contains(inter)) {
        continue;
      }
      interfaceList.add(inter);
      search(inter);
    }
    Class<?> superclass = clazz.getSuperclass();
    if (superclass == null) { return; }
    search(superclass);
  }

  public Set<Class<?>> getInterfaceList() {
    search(this.clazz);
    return this.interfaceList;
  }
}
