package lbn.util.spawn;

import lbn.dungeoncore.LbnRuntimeException;
import lbn.mob.customEntity1_7.CustomEnderDragon;
import lbn.mob.customEntity1_7.CustomEnderman;
import lbn.mob.customEntity1_7.CustomGiant;
import lbn.mob.customEntity1_7.CustomPig;
import lbn.mob.customEntity1_7.CustomPigZombie;
import lbn.mob.customEntity1_7.CustomSkeleton;
import lbn.mob.customEntity1_7.CustomSpider;
import lbn.mob.customEntity1_7.CustomWitch;
import lbn.mob.customEntity1_7.CustomZombie;
import lbn.util.DungeonLogger;
import net.minecraft.server.v1_8_R1.CommandAbstract;
import net.minecraft.server.v1_8_R1.CommandException;
import net.minecraft.server.v1_8_R1.CommandSummon;
import net.minecraft.server.v1_8_R1.Entity;
import net.minecraft.server.v1_8_R1.EntityTypes;
import net.minecraft.server.v1_8_R1.IChatBaseComponent;
import net.minecraft.server.v1_8_R1.MojangsonParser;
import net.minecraft.server.v1_8_R1.NBTBase;
import net.minecraft.server.v1_8_R1.NBTTagCompound;
import net.minecraft.server.v1_8_R1.WorldServer;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.craftbukkit.v1_8_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;


public class MobSpawnByCommand {
	public static void spawn(Player p, String[] comand) throws CommandException {
		new CommandSummon().execute(((CraftPlayer) p).getHandle(), comand);
	}

	public static org.bukkit.entity.Entity spawn(Location loc, String[] paramArrayOfString) {
		// summonがあったら取り除く
		if (paramArrayOfString[0].equalsIgnoreCase("summon") || paramArrayOfString[0].equalsIgnoreCase("/summon")) {
			String[] newParam = new String[paramArrayOfString.length - 1];
			System.arraycopy(paramArrayOfString, 1, newParam, 0, paramArrayOfString.length - 1);
			paramArrayOfString = newParam;
		}

		try {
			String str = paramArrayOfString[0];
			NBTTagCompound localNBTTagCompound1 = new NBTTagCompound();
			IChatBaseComponent localObject1;
			if (paramArrayOfString.length >= 5) {
				localObject1 = CommandAbstract.a(null, paramArrayOfString, 4);
				NBTBase localNBTBase = MojangsonParser.parse(((IChatBaseComponent) localObject1).c());
				if ((localNBTBase instanceof NBTTagCompound)) {
					localNBTTagCompound1 = (NBTTagCompound) localNBTBase;
				} else {
					a("Not a valid tag");
					return null;
				}
			}
			localNBTTagCompound1.setString("id", str);

			//NBTTag generic.attackDamageをとらないので一時的にコメントアウト
//			//NBTTag generic.attackDamageをとる
//			NBTTagList nbtBase = (NBTTagList) localNBTTagCompound1.get("Attributes");
//			if (nbtBase != null) {
//				for (int i = 0; i < nbtBase.size(); i++) {
//					NBTBase g = nbtBase.g(i);
//					if (g.toString().contains("generic.attackDamage")) {
//						nbtBase.a(i);
//						continue;
//					}
//				}
//			}

			WorldServer world = ((CraftWorld) loc.getWorld()).getHandle();
			Entity localObject1_1 = getEntity(localNBTTagCompound1, world);
			if (localObject1_1 != null) {
				// spawn
				((Entity) localObject1_1).setPositionRotation(loc.getX(), loc.getY(), loc.getZ(),
						((Entity) localObject1_1).yaw,
						((Entity) localObject1_1).pitch);
				world.addEntity((Entity) localObject1_1);

				Entity localObject2 = localObject1_1;
				NBTTagCompound localNBTTagCompound2 = localNBTTagCompound1;
				while ((localObject2 != null)
						&& (localNBTTagCompound2.hasKeyOfType("Riding", 10))) {
					Entity localEntity = EntityTypes.a(
							localNBTTagCompound2.getCompound("Riding"),
							world);
					if (localEntity != null) {
						// spawn
						localEntity.setPositionRotation(loc.getX(), loc.getY(), loc.getZ(),
								localEntity.yaw, localEntity.pitch);
						world.addEntity(localEntity);
						localObject2.mount(localEntity);
					}
					localObject2 = localEntity;
					localNBTTagCompound2 = localNBTTagCompound2
							.getCompound("Riding");
				}
				return localObject1_1.getBukkitEntity();
			}
			a("entityの生成に失敗しました:" + localNBTTagCompound1.getString("id"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	protected static Entity getEntity(NBTTagCompound paramNBTTagCompound,
			WorldServer world) {
		Entity entity = EntityTypes.a(paramNBTTagCompound, world);
		if (entity == null) {
			throw new LbnRuntimeException("entity is null:" + paramNBTTagCompound.getString("id"));
		}
		EntityType type = entity.getBukkitEntity().getType();

		boolean updateFlg = true;
		switch (type) {
			case ENDER_DRAGON:
				entity = new CustomEnderDragon(world);
				break;
			case ENDERMAN:
				entity = new CustomEnderman(world);
				break;
			case PIG:
				entity = new CustomPig(world);
				break;
			case SKELETON:
				entity = new CustomSkeleton(world);
				break;
			case SPIDER:
				entity = new CustomSpider(world);
				break;
			case WITCH:
				entity = new CustomWitch(world);
				break;
			case ZOMBIE:
				entity = new CustomZombie(world);
				break;
			case PIG_ZOMBIE:
				entity = new CustomPigZombie(world);
				break;
			case GIANT:
				entity = new CustomGiant(world);
				break;
			default:
				updateFlg = false;
				break;
		}

		if (updateFlg) {
			entity.f(paramNBTTagCompound);
		}

		return entity;
	}

	public static LbnNBTTag getTBTTagByCommand(String[] paramArrayOfString, CommandSender sender) {
		// summonがあったら取り除く
		if (paramArrayOfString[0].equalsIgnoreCase("summon") || paramArrayOfString[0].equalsIgnoreCase("/summon")) {
			String[] newParam = new String[paramArrayOfString.length - 1];
			System.arraycopy(paramArrayOfString, 1, newParam, 0, paramArrayOfString.length - 1);
			paramArrayOfString = newParam;
		}

		String str = paramArrayOfString[0];
		NBTTagCompound localNBTTagCompound1 = new NBTTagCompound();
		try {
			IChatBaseComponent localObject1;
			if (paramArrayOfString.length >= 5) {
				localObject1 = CommandAbstract.a(null, paramArrayOfString, 4);
				String c = ((IChatBaseComponent) localObject1).c();
				NBTBase localNBTBase = MojangsonParser.parse(c);
				if ((localNBTBase instanceof NBTTagCompound)) {
					localNBTTagCompound1 = (NBTTagCompound) localNBTBase;
				} else {
					a("Not a valid tag", sender);
					return null;
				}
			}
			localNBTTagCompound1.setString("id", str);

			boolean isRiding = false;

			WorldServer world = ((CraftWorld) Bukkit.getWorlds().get(0)).getHandle();
			Entity localObject1_1 = getEntity(localNBTTagCompound1, world);
			if (localObject1_1 != null) {
				Entity localObject2 = localObject1_1;
				NBTTagCompound localNBTTagCompound2 = localNBTTagCompound1;
				while ((localObject2 != null)
						&& (localNBTTagCompound2.hasKeyOfType("Riding", 10))) {
					isRiding = true;
					Entity localEntity = EntityTypes.a(
							localNBTTagCompound2.getCompound("Riding"),
							world);
					localObject2 = localEntity;
					localNBTTagCompound2 = localNBTTagCompound2
							.getCompound("Riding");
				}

				LbnNBTTag lbnNBTTag = new LbnNBTTag(localObject1_1.getBukkitEntity());
				lbnNBTTag.setRiding(isRiding);
				return lbnNBTTag;
			}
		} catch (Exception e) {
		}
		a("entityの生成に失敗しました:" + localNBTTagCompound1.getString("id"), sender);
		return null;
	}

	// 使わないのでコメントアウト
	// public static org.bukkit.entity.Entity getEntityByCommand(String[]
	// paramArrayOfString) {
	// //summonがあったら取り除く
	// if (paramArrayOfString[0].equalsIgnoreCase("summon")) {
	// String[] newParam = new String[paramArrayOfString.length - 1];
	// System.arraycopy(paramArrayOfString, 1, newParam, 1,
	// paramArrayOfString.length - 1);
	// paramArrayOfString = newParam;
	// }
	//
	// String str = paramArrayOfString[0];
	// NBTTagCompound localNBTTagCompound1 = new NBTTagCompound();
	// int i = 0;
	// IChatBaseComponent localObject1;
	// if (paramArrayOfString.length >= 5) {
	// localObject1 = CommandAbstract.a(null, paramArrayOfString, 4);
	// NBTBase localNBTBase = MojangsonParser.parse(((IChatBaseComponent)
	// localObject1).c());
	// if ((localNBTBase instanceof NBTTagCompound)) {
	// localNBTTagCompound1 = (NBTTagCompound) localNBTBase;
	// i = 1;
	// } else {
	// a("Not a valid tag" );
	// return null;
	// }
	// }
	// localNBTTagCompound1.setString("id", str);
	//
	// WorldServer world = ((CraftWorld)Bukkit.getWorlds().get(0)).getHandle();
	// Entity localObject1_1 = EntityTypes.a(localNBTTagCompound1, world);
	// if (localObject1_1 != null) {
	// //spawn
	//// ((Entity) localObject1_1).setPositionRotation(d1, d2, d3,
	//// ((Entity) localObject1_1).yaw,
	//// ((Entity) localObject1_1).pitch);
	// if (i == 0) {
	// if ((localObject1_1 instanceof EntityInsentient)) {
	// ((EntityInsentient) localObject1_1).prepare(null);
	// }
	// }
	//// localWorld.addEntity((Entity) localObject1_1);
	//
	// Entity localObject2 = localObject1_1;
	// NBTTagCompound localNBTTagCompound2 = localNBTTagCompound1;
	// while ((localObject2 != null)
	// && (localNBTTagCompound2.hasKeyOfType("Riding", 10))) {
	// Entity localEntity = EntityTypes.a(
	// localNBTTagCompound2.getCompound("Riding"),
	// world);
	// if (localEntity != null) {
	// //spawn
	//// localEntity.setPositionRotation(d1, d2, d3,
	//// localEntity.yaw, localEntity.pitch);
	//// localWorld.addEntity(localEntity);
	//// localObject2.mount(localEntity);
	// }
	// localObject2 = localEntity;
	// localNBTTagCompound2 = localNBTTagCompound2
	// .getCompound("Riding");
	// }
	// a("summon 成功");
	// return localObject1_1.getBukkitEntity();
	// }
	// return null;
	// }

	private static void a(String string) {
		DungeonLogger.development(string);
	}

	private static void a(String string, CommandSender sender) {
		if (!(sender instanceof ConsoleCommandSender)) {
			sender.sendMessage(string);
		}
	}
}
