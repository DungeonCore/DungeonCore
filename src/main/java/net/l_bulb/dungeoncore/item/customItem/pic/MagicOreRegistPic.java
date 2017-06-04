package net.l_bulb.dungeoncore.item.customItem.pic;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import net.l_bulb.dungeoncore.dungeoncore.SpletSheet.AbstractComplexSheetRunable;
import net.l_bulb.dungeoncore.dungeoncore.SpletSheet.MagicStoneOreSheetRunnable;
import net.l_bulb.dungeoncore.dungeoncore.SpletSheet.SpletSheetExecutor;
import net.l_bulb.dungeoncore.item.customItem.AbstractItem;
import net.l_bulb.dungeoncore.item.itemInterface.BreakBlockItemable;
import net.l_bulb.dungeoncore.player.PlayerChecker;
import net.l_bulb.dungeoncore.player.magicstoneOre.MagicStoneFactor;
import net.l_bulb.dungeoncore.player.magicstoneOre.MagicStoneOreType;

public class MagicOreRegistPic extends AbstractItem implements BreakBlockItemable {

  @Override
  public String getItemName() {
    return "登録用ピッケル";
  }

  @Override
  public String getId() {
    return "regist_pic";
  }

  @Override
  public int getBuyPrice(ItemStack item) {
    // TODO 自動生成されたメソッド・スタブ
    return 0;
  }

  @Override
  protected Material getMaterial() {
    return Material.DIAMOND_PICKAXE;
  }

  @Override
  public String[] getDetail() {
    return new String[] { "このアイテムで壊した鉱石は登録されます。" };
  }

  @Override
  public void onBlockBreakEvent(BlockBreakEvent e, ItemStack useItem) {
    // 一般Playerなら何もしない
    Player player = e.getPlayer();
    if (PlayerChecker.isNormalPlayer(player)) { return; }
    e.setCancelled(true);

    // 壊したブロックの場所
    Location location = e.getBlock().getLocation();

    // すでに登録されているか確認
    MagicStoneOreType magicStoneByLocation = MagicStoneFactor.getMagicStoneByLocation(location);
    if (magicStoneByLocation != null) {
      player.sendMessage("この場所にはすでに鉱石が登録されています。スプレットシートから変更してください。");
      MagicStoneFactor.sendOreSchedulerInfo(player, location);
      return;
    }

    // 鉱石を確認
    Material type = e.getBlock().getType();
    MagicStoneOreType fromMaterial = MagicStoneOreType.FromMaterial(type);
    if (fromMaterial == null) {
      player.sendMessage("このブロックは魔法鉱石として登録できません。:" + type);
      return;
    }

    // スプレットシートに登録する
    MagicStoneOreSheetRunnable runnable = new MagicStoneOreSheetRunnable(player);
    HashMap<String, Object> hashMap = new HashMap<>();
    hashMap.put("type", fromMaterial.getJpName());
    hashMap.put("location", AbstractComplexSheetRunable.getLocationString(location));
    runnable.addData(hashMap);
    SpletSheetExecutor.onExecute(runnable);

    // 鉱石を登録する
    MagicStoneFactor.regist(location, MagicStoneOreType.FromMaterial(e.getBlock().getType()));
    // ブロックを再配置する
    MagicStoneFactor.relocationOre(location);
  }

}
