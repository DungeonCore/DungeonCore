package lbn.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import lbn.dungeoncore.Main;

public class InOutputUtil {
	public final static String dataFolder = Main.dataFolder + File.separator + "data" + File.separator;

	/**
	 * データをSerializableする
	 *
	 * @param serializable
	 * @param fileName
	 * @return
	 */
	public static boolean outputStream(Serializable serializable, String fileName) {
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
	 * ZIPにして保存する
	 *
	 * @param filePath
	 * @param directory
	 * @return
	 */
	public static boolean compressDirectory(String filePath, String directory) {
		File baseFile = new File(filePath);
		File file = new File(directory);
		try {
			baseFile.getParentFile().mkdirs();
			baseFile.createNewFile();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		try (ZipOutputStream outZip = new ZipOutputStream(new FileOutputStream(baseFile));) {
			archive(outZip, baseFile, file);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * ディレクトリ圧縮のための再帰処理
	 *
	 * @param outZip
	 *            ZipOutputStream
	 * @param baseFile
	 *            File 保存先ファイル
	 * @param file
	 *            File 圧縮したいファイル
	 */
	private static void archive(ZipOutputStream outZip, File baseFile, File targetFile) {
		if (targetFile.isDirectory()) {
			File[] files = targetFile.listFiles();
			for (File f : files) {
				if (f.isDirectory()) {
					archive(outZip, baseFile, f);
				} else {
					if (!f.getAbsoluteFile().equals(baseFile)) {
						// 圧縮処理
						archive(outZip, baseFile, f, f.getAbsolutePath().replace(baseFile.getParent(), "").substring(1),
								"Shift_JIS");
					}
				}
			}
		}
	}

	/**
	 * 圧縮処理
	 *
	 * @param outZip
	 *            ZipOutputStream
	 * @param baseFile
	 *            File 保存先ファイル
	 * @param targetFile
	 *            File 圧縮したいファイル
	 * @parma entryName 保存ファイル名
	 * @param enc
	 *            文字コード
	 */
	private static boolean archive(ZipOutputStream outZip, File baseFile, File targetFile, String entryName,
			String enc) {
		// 圧縮レベル設定
		outZip.setLevel(5);

		// // 文字コードを指定
		// outZip.setEncoding(enc);
		try {
			// ZIPエントリ作成
			outZip.putNextEntry(new ZipEntry(entryName));

			// 圧縮ファイル読み込みストリーム取得
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(targetFile));

			// 圧縮ファイルをZIPファイルに出力
			int readSize = 0;
			byte buffer[] = new byte[1024]; // 読み込みバッファ
			while ((readSize = in.read(buffer, 0, buffer.length)) != -1) {
				outZip.write(buffer, 0, readSize);
			}
			// クローズ処理
			in.close();
			// ZIPエントリクローズ
			outZip.closeEntry();
		} catch (Exception e) {
			// ZIP圧縮失敗
			return false;
		}
		return true;
	}

	public static boolean write(String str, String fileName) {
		return write(str, fileName, dataFolder);
	}

	public static boolean write(Collection<String> strList, String fileName) {
		// ファイルに保存する
		File file = new File(dataFolder + fileName);
		file.getParentFile().mkdirs();

		try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
			for (String string : strList) {
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
	 * Serializableデータを読み込む
	 *
	 * @param fileName
	 * @return
	 */
	public static Object inputStream(String fileName) {
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

		try (BufferedReader br = new BufferedReader(new FileReader(file));) {
			ArrayList<String> list = new ArrayList<String>();
			String line = null;
			while ((line = br.readLine()) != null) {
				list.add(line);
			}
			return list;
		} catch (FileNotFoundException e) {
			DungeonLogger.error(fileName + "ファイルが存在しないので読み込みを無視します");
		} catch (IOException e) {
			throw e;
		}
		return new ArrayList<>();
	}
}
