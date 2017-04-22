package lbn.command.notUsually.command;

import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.Material;

import lbn.command.notUsually.AbstractVanillaCommand;
import net.minecraft.server.v1_8_R1.Block;
import net.minecraft.server.v1_8_R1.BlockPosition;
import net.minecraft.server.v1_8_R1.Blocks;
import net.minecraft.server.v1_8_R1.CommandAbstract;
import net.minecraft.server.v1_8_R1.CommandException;
import net.minecraft.server.v1_8_R1.CommandSetBlock;
import net.minecraft.server.v1_8_R1.EnumCommandResult;
import net.minecraft.server.v1_8_R1.ExceptionUsage;
import net.minecraft.server.v1_8_R1.IBlockData;
import net.minecraft.server.v1_8_R1.ICommandListener;
import net.minecraft.server.v1_8_R1.IInventory;
import net.minecraft.server.v1_8_R1.MojangsonParser;
import net.minecraft.server.v1_8_R1.NBTTagCompound;
import net.minecraft.server.v1_8_R1.TileEntity;
import net.minecraft.server.v1_8_R1.World;

public class CustomSetBlockCommand extends AbstractVanillaCommand {

	public CustomSetBlockCommand() {
		super(new CommandSetBlock());
	}

	@Override
	protected void execute2(ICommandListener paramICommandListener, String[] paramArrayOfString)
			throws CommandException {
		if (paramArrayOfString.length < 4) {
			throw new ExceptionUsage("commands.setblock.usage", new Object[0]);
		}
		paramICommandListener.a(EnumCommandResult.AFFECTED_BLOCKS, 0);

		try {
			if (NumberUtils.isNumber(paramArrayOfString[3])) {
				@SuppressWarnings("deprecation")
				Material material = Material.getMaterial(Integer.parseInt(paramArrayOfString[3]));
				if (material != null) {
					paramArrayOfString[3] = "minecraft:" + material.toString().toLowerCase();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		BlockPosition localBlockPosition = a(paramICommandListener, paramArrayOfString, 0, false);
		Block localBlock = CommandAbstract.g(paramICommandListener, paramArrayOfString[3]);

		int i = 0;
		if (paramArrayOfString.length >= 5) {
			i = a(paramArrayOfString[4], 0, 15);
		}
		World localWorld = paramICommandListener.getWorld();
		if (!localWorld.isLoaded(localBlockPosition)) {
			throw new CommandException("commands.setblock.outOfWorld", new Object[0]);
		}
		NBTTagCompound localNBTTagCompound = new NBTTagCompound();
		int j = 0;
		if ((paramArrayOfString.length >= 7) && (localBlock.isTileEntity())) {
			Object localObject = a(paramICommandListener, paramArrayOfString, 6).c();
			localNBTTagCompound = MojangsonParser.parse((String) localObject);
			j = 1;
		}
		if (paramArrayOfString.length >= 6) {
			if (paramArrayOfString[5].equals("destroy")) {
				localWorld.setAir(localBlockPosition, true);
				if (localBlock == Blocks.AIR) {
					a(paramICommandListener, this, "commands.setblock.success", new Object[0]);
				}
			} else if ((paramArrayOfString[5].equals("keep")) && (!localWorld.isEmpty(localBlockPosition))) {
				throw new CommandException("commands.setblock.noChange", new Object[0]);
			}
		}
		Object localObject = localWorld.getTileEntity(localBlockPosition);
		if (localObject != null) {
			if ((localObject instanceof IInventory)) {
				((IInventory) localObject).l();
			}
			localWorld.setTypeAndData(localBlockPosition, Blocks.AIR.getBlockData(), localBlock == Blocks.AIR ? 2 : 4);
		}
		IBlockData localIBlockData = localBlock.fromLegacyData(i);
		if (!localWorld.setTypeAndData(localBlockPosition, localIBlockData, 2)) {
			throw new CommandException("commands.setblock.noChange", new Object[0]);
		}
		if (j != 0) {
			TileEntity localTileEntity = localWorld.getTileEntity(localBlockPosition);
			if (localTileEntity != null) {
				localNBTTagCompound.setInt("x", localBlockPosition.getX());
				localNBTTagCompound.setInt("y", localBlockPosition.getY());
				localNBTTagCompound.setInt("z", localBlockPosition.getZ());

				localTileEntity.a(localNBTTagCompound);
			}
		}
		localWorld.update(localBlockPosition, localIBlockData.getBlock());
		paramICommandListener.a(EnumCommandResult.AFFECTED_BLOCKS, 1);
		a(paramICommandListener, this, "commands.setblock.success", new Object[0]);
	}

}
