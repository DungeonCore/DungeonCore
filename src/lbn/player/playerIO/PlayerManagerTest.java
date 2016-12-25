package lbn.player.playerIO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.bukkit.craftbukkit.libs.com.google.gson.Gson;
import org.bukkit.util.Vector;

import com.google.common.reflect.TypeToken;

import lbn.chest.wireless.PersonalChestData;
import lbn.util.DungeonLogger;
import lbn.util.InOutputUtil;

public class PlayerManagerTest {
  public static void main(String[] args) throws IOException {
    HashMap<String, String> hashSet = new HashMap<>();
    
    File file = new File("F:\\Users\\Ken\\minecraft\\マルチ_1.7.10 - 1.8\\plugins\\DungeonCore\\data\\chest");
    
    for (File path : file.listFiles()) {
      ArrayList<String> readFile = readFile(path.getAbsolutePath());
      // ファイルが存在しない時は何もしない
      if (readFile.isEmpty()) {
        continue;
      }
      StringBuilder sb = new StringBuilder();
      for (String string : readFile) {
        sb.append(string);
      }
      
      Gson gson = new Gson();
      Type collectionType = new TypeToken<Set<PersonalChestData>>() {
        private static final long serialVersionUID = 2199382832408849845L;
      }.getType();
      gson.fromJson(sb.toString(), collectionType);
      
      Set<PersonalChestData> fromJson = gson.fromJson(sb.toString(), collectionType);
      for (PersonalChestData personalChestData : fromJson) {
        Vector vector = new Vector(personalChestData.x, personalChestData.y, personalChestData.z);
        hashSet.containsKey(vector.toString());
        hashSet.put(vector.toString(), path.getAbsolutePath());
      }
    }
  }
  
  public static ArrayList<String> readFile(String filePath) throws IOException {
    File file = new File(filePath);
    
    try (BufferedReader br = new BufferedReader(new FileReader(file));) {
      ArrayList<String> list = new ArrayList<String>();
      String line = null;
      while ((line = br.readLine()) != null) {
        list.add(line);
      }
      return list;
    } catch (FileNotFoundException e) {
      DungeonLogger.info(filePath + "ファイルが存在しないので読み込みを無視します");
    } catch (IOException e) {
      throw e;
    }
    return new ArrayList<>();
  }
  
  public static void readPlayerIoData() throws IOException {
    // String saveType = "A";
    File file = new File("F:\\Users\\Ken\\minecraft\\マルチ_1.7.10 - 1.8\\plugins\\DungeonCore\\data\\player_data");
    
    for (String path : file.list()) {
      PlayerIOData fromJson;
      ArrayList<String> readFile = InOutputUtil.readFile(path);
      
      if (readFile.isEmpty()) {
        return;
      }
      
      String json = StringUtils.join(readFile.iterator(), "");
      Gson gson = new Gson();
      fromJson = gson.fromJson(json, PlayerIOData.class);
      
      if (fromJson == null) {
        return;
      }
    }
  }
}
