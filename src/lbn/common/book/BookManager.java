package lbn.common.book;

import java.util.HashMap;
import java.util.Set;

import lbn.command.SpletSheetCommand;
import lbn.item.ItemInterface;
import lbn.item.ItemManager;
import lbn.item.customItem.itemAbstract.BookItem;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_8_R1.ChatComponentText;
import net.minecraft.server.v1_8_R1.ChatComponentUtils;
import net.minecraft.server.v1_8_R1.ChatSerializer;
import net.minecraft.server.v1_8_R1.EntityHuman;
import net.minecraft.server.v1_8_R1.EntityPlayer;
import net.minecraft.server.v1_8_R1.IChatBaseComponent;
import net.minecraft.server.v1_8_R1.ItemWrittenBook;
import net.minecraft.server.v1_8_R1.NBTTagCompound;
import net.minecraft.server.v1_8_R1.NBTTagList;
import net.minecraft.server.v1_8_R1.NBTTagString;
import net.minecraft.server.v1_8_R1.PacketPlayOutSetSlot;
import net.minecraft.server.v1_8_R1.Slot;

import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BookManager {
	static HashMap<String, BookData> bookMap = new HashMap<String, BookData>();

	public static void reloadSpletSheet(CommandSender command) {
		bookMap.clear();
		SpletSheetCommand.reloadSheet(command, "book");
		SpletSheetCommand.reloadSheet(command, "book2");
	}

	public static Set<String> getNames() {
		return bookMap.keySet();
	}

	/**
	 * 本の情報を登録する
	 *
	 * @param data
	 */
	public static void regist(BookData data) {
		bookMap.put(data.getId(), data);
		BookItem bookItem = new BookItem(data);
		ItemManager.registItem(bookItem);
	}

	public static ItemInterface getItem(String id) {
		ItemInterface customItemById = ItemManager.getCustomItemById(id);
		return customItemById;
	}

	/**
	 * 本を開く
	 * 
	 * @param p
	 * @param id
	 */
	public static void opneBook(Player p, String id) {
		BookData bookData = bookMap.get(id);
		if (bookData != null) {
			openBook(bookData.toBookItem(), p);

			p.sendMessage(ChatColor.GOLD + "[BOOK] " + ChatColor.GREEN + bookData.getTitile() + "の本を開きました");
		}
	}

	public static void openBook(ItemStack book, Player p) {
		EntityPlayer ePlayer = ((CraftPlayer) p).getHandle();
		net.minecraft.server.v1_8_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(book);

		// 本で無いなら無視
		if (nmsStack.getItem() instanceof ItemWrittenBook) {
			ItemStack itemInHand2 = p.getItemInHand();
			// 一瞬だけ本に持ち帰る
			changeBook(nmsStack, ePlayer);
			// 本を開ける
			ePlayer.openBook(nmsStack);
			// アイテムを戻す
			p.setItemInHand(itemInHand2);
			p.updateInventory();
		}
	}

	public static void changeBook(net.minecraft.server.v1_8_R1.ItemStack itemstack, EntityHuman entityhuman) {
		if (itemstack != null && itemstack.getTag() != null) {
			NBTTagCompound nbttagcompound = itemstack.getTag();

			// 本を開いているにセットする
			nbttagcompound.setBoolean("resolved", true);
			if (ItemWrittenBook.b(nbttagcompound)) {
				NBTTagList nbttaglist = nbttagcompound.getList("pages", 8);

				for (int i = 0; i < nbttaglist.size(); ++i) {
					String s = nbttaglist.getString(i);

					Object object;

					try {
						IChatBaseComponent ichatbasecomponent = ChatSerializer.a(s);

						object = ChatComponentUtils.filterForDisplay(entityhuman, ichatbasecomponent, entityhuman);
					} catch (Exception exception) {
						object = new ChatComponentText(s);
					}

					nbttaglist.a(i, new NBTTagString(ChatSerializer.a((IChatBaseComponent) object)));
				}

				nbttagcompound.set("pages", nbttaglist);
				Slot slot = entityhuman.activeContainer.getSlot(entityhuman.inventory,
						entityhuman.inventory.itemInHandIndex);
				((EntityPlayer) entityhuman).playerConnection
						.sendPacket(new PacketPlayOutSetSlot(0, slot.rawSlotIndex, itemstack));

			}
		}

	}
}
