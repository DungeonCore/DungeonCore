package net.l_bulb.dungeoncore.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.bukkit.Bukkit;

import net.l_bulb.dungeoncore.dungeoncore.Main;

import lombok.NonNull;
import lombok.val;

public class InOutputUtil {

  public final static String dataFolder = Main.dataFolder + File.separator + "data" + File.separator;

  /**
   * シリアライズ可能なデータをシリアライズし、対象のファイルに書き出します。
   *
   * @param serializable シリアライズ可能データ
   * @param fileName 書き出し先のファイル
   * @return シリアライズに成功したかどうか。成功した場合はtrue、それ以外の場合はfalse。
   */
  public static boolean serializeObject(@NonNull Serializable serializable, @NonNull String fileName) {
    // ファイルに保存する
    File file = new File(dataFolder + fileName);
    file.getParentFile().mkdirs();

    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
      oos.writeObject(serializable);
      oos.flush();
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  /**
   * 指定されたファイルを、ZIP圧縮 (Deflateアルゴリズム) して保存します。
   *
   * @param filePath 圧縮する対象のファイル。
   * @param directory 圧縮されたデータの保存先ディレクトリ
   * @return 処理に成功したかどうか。成功した場合はtrue、それ以外の場合はfalse。
   */
  public static boolean compressDirectory(@NonNull String filePath, @NonNull String directory) {
    val baseFile = new File(filePath);
    val file = new File(directory);

    try {
      baseFile.getParentFile().mkdirs();
      baseFile.createNewFile();
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }

    try (ZipOutputStream outZip = new ZipOutputStream(new FileOutputStream(baseFile))) {
      archive(outZip, baseFile, file);
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  // FIXME: Javadocを修正する。
  /**
   * ディレクトリ圧縮のための再帰処理
   *
   * @param outZip 圧縮と書き出しで使用される、ZipOutputStreamのインスタンス。
   * @param baseFile 保存先ファイル
   * @param targetDirectory 圧縮したいディレクトリ？ファイル？
   */
  private static void archive(@NonNull ZipOutputStream outZip, @NonNull File baseFile, @NonNull File targetDirectory) {
    if (!targetDirectory.isDirectory()) { return; }

    File[] files = targetDirectory.listFiles();
    for (File f : files) {
      if (f.isDirectory()) {
        archive(outZip, baseFile, f);
      } else {
        if (!f.getAbsoluteFile().equals(baseFile)) {
          // 圧縮処理
          archive(outZip,
              baseFile,
              f,
              f.getAbsolutePath()
                  .replace(baseFile.getParent(), "")
                  .substring(1),
              "Shift_JIS");
        }
      }
    }
  }

  /**
   * 圧縮処理
   *
   * @param outZip ZipOutputStream
   * @param baseFile File 保存先ファイル
   * @param targetFile File 圧縮したいファイル
   * @parma entryName 保存ファイル名
   * @param enc 文字コード
   */
  private static boolean archive(ZipOutputStream outZip, File baseFile, File targetFile, String entryName, String enc) {

    // 圧縮レベル設定
    outZip.setLevel(Deflater.BEST_SPEED);

    // 圧縮対象ファイルの入力ストリームを作成
    // (注: 正常にリソースが解放されない可能性を除去。二重バッファリングはNG！)
    try (FileInputStream in = new FileInputStream(targetFile)) {
      // ZIPエントリ作成
      outZip.putNextEntry(new ZipEntry(entryName));

      // 圧縮ファイルをZIPファイルに出力
      int readSize = 0;
      byte buffer[] = new byte[1024]; // 読み込みバッファ
      while ((readSize = in.read(buffer, 0, buffer.length)) != -1) {
        outZip.write(buffer, 0, readSize);
      }

      // ZIPエントリクローズ
      outZip.closeEntry();
    } catch (Exception e) {
      // ZIP圧縮失敗
      Bukkit.getLogger().log(Level.SEVERE, "ZIP圧縮 (Deflate) に失敗しました。", e);
      return false;
    }
    return true;
  }

  public static boolean write(String str, String fileName) {
    return write(str, fileName, dataFolder);
  }

  public static boolean write(Collection<String> strings, String fileName) {
    // ファイルに保存する
    File file = new File(dataFolder + fileName);
    file.getParentFile().mkdirs();

    try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
      for (String string : strings) {
        bw.write(string);
        bw.write("\n");
      }
      bw.flush();
      return true;
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
  }

  public static boolean write(String str, String fileName, String dir) {
    // ファイルに保存する
    File file = new File(dir + fileName);
    file.getParentFile().mkdirs();

    try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
      bw.write(str);
      bw.flush();
      return true;
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * データをデシリアライズする
   *
   * @param fileName
   * @return
   */
  public static Object deserializeObject(String fileName) {
    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(dataFolder + fileName)))) {
      return ois.readObject();
    } catch (FileNotFoundException e) {
      DungeonLogger.error(fileName + "ファイルが存在しないので読み込みを無視します");
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * ファイルを読み込む
   *
   * @param fileName
   * @return
   * @throws IOException
   */
  public static ArrayList<String> readFile(String fileName) throws IOException {
    File file = new File(dataFolder + fileName);
    if (!file.isFile()) {
      DungeonLogger.error(fileName + "ファイルが存在しないので読み込みを無視します");
      return new ArrayList<>();
    }
    List<String> result = Files.readAllLines(file.toPath());
    return result instanceof ArrayList ? (ArrayList<String>) result : new ArrayList<>(result);
  }
}
