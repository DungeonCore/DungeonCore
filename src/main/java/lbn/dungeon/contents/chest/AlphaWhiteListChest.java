package lbn.dungeon.contents.chest;

import java.util.HashSet;
import java.util.UUID;

import lbn.chest.WhiteListChest;
import lbn.util.Message;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

public class AlphaWhiteListChest extends WhiteListChest {

  public AlphaWhiteListChest() {// 134 66 843
    super(new Location(Bukkit.getWorld("world"), 134, 66, 843), new Location(Bukkit.getWorld("world"), 134, 66, 843),
        1, new Location(Bukkit.getWorld("world"), 127, 42, 834).setDirection(new Vector(1, 0, 0)), 400, 400, 10, true);
  }

  static HashSet<UUID> hashSet = new HashSet<UUID>();
  static {
    hashSet.add(UUID.fromString("01ee905e-a45b-3017-b985-136d7e2ac473"));
    hashSet.add(UUID.fromString("0467c9c1-89e6-4a8a-9a5a-231b5b8e300c"));
    hashSet.add(UUID.fromString("05ff6ca8-5cf8-42d8-9fbd-5d398408524b"));
    hashSet.add(UUID.fromString("069fccae-362f-4f8f-bbb1-de8fd6633547"));
    hashSet.add(UUID.fromString("0771f771-8a9f-4840-8834-e8cc8265f1dc"));
    hashSet.add(UUID.fromString("07763a30-0da9-4a86-ace1-fbf4e897db25"));
    hashSet.add(UUID.fromString("087f46fe-6cde-4c25-8063-4bda77186b74"));
    hashSet.add(UUID.fromString("08e0e3fc-c943-4b8d-b50b-34d0201ae478"));
    hashSet.add(UUID.fromString("0bf81ad3-1bc9-4e47-bf88-c1f596dd8a06"));
    hashSet.add(UUID.fromString("0e03a1fc-5cf6-4ff4-9f03-d81df144481b"));
    hashSet.add(UUID.fromString("0e48162c-9373-3e08-951e-740c0afbd77d"));
    hashSet.add(UUID.fromString("12b9440f-2b2d-4641-8b23-db4090fb477b"));
    hashSet.add(UUID.fromString("12d5ac79-9822-447b-b9db-9fc71dc30a2b"));
    hashSet.add(UUID.fromString("16bcc725-e32e-469c-94f1-86949dba4e18"));
    hashSet.add(UUID.fromString("199798ad-2966-47ba-932d-c0a735144cf1"));
    hashSet.add(UUID.fromString("1a92849b-285e-42fd-81ae-f77e62d39d85"));
    hashSet.add(UUID.fromString("1c2a1e8b-6b4d-44a5-9221-6fa0320348d1"));
    hashSet.add(UUID.fromString("1c2acef6-7f06-48cf-8812-98a76ecd3927"));
    hashSet.add(UUID.fromString("1c958ed3-53c9-460e-b36e-5d60ffd6d202"));
    hashSet.add(UUID.fromString("1cca0aa1-7198-45db-9698-2c66950ae036"));
    hashSet.add(UUID.fromString("1f203ea0-a727-407f-a3bd-f8b11e6509c1"));
    hashSet.add(UUID.fromString("20ca8ca6-16d1-4d9a-8b5a-83abb046a47e"));
    hashSet.add(UUID.fromString("225626f5-1992-4e6b-a2b6-10480d049226"));
    hashSet.add(UUID.fromString("2397f8c4-47d8-36ba-b533-48c659e2fe99"));
    hashSet.add(UUID.fromString("245b7e9f-5b47-427b-9f43-c5acf4d45d47"));
    hashSet.add(UUID.fromString("25c8ec28-c1ea-3177-a714-6967898a8d4c"));
    hashSet.add(UUID.fromString("29c07979-4b7a-410f-8e19-7c54fb9f7302"));
    hashSet.add(UUID.fromString("2c3b0053-c810-4b3b-816c-0a8daa914010"));
    hashSet.add(UUID.fromString("2c686da7-bc09-45b2-be37-6aee188f07af"));
    hashSet.add(UUID.fromString("2f751a35-f820-405a-8faf-e0c647b2ea55"));
    hashSet.add(UUID.fromString("305fc040-a514-4012-804d-e5da106323de"));
    hashSet.add(UUID.fromString("34df875b-a792-4b98-a48b-2dbad9f9659e"));
    hashSet.add(UUID.fromString("36c17f53-f2d7-382f-9580-554d1aef9ffc"));
    hashSet.add(UUID.fromString("3b0cd5e8-04cc-4dc0-a72d-71a01f7de3f0"));
    hashSet.add(UUID.fromString("3fad4436-564d-461b-a53f-8478a4e83b3a"));
    hashSet.add(UUID.fromString("4086b2f8-27d4-4c90-8ad8-53a791f7a558"));
    hashSet.add(UUID.fromString("409bd8d7-80c8-4612-b23e-0959c4d2b13d"));
    hashSet.add(UUID.fromString("41ba8ba5-e980-4cfd-80b1-550102238d7c"));
    hashSet.add(UUID.fromString("44d731e9-5392-4f09-9266-65c62732f8a0"));
    hashSet.add(UUID.fromString("4a436dd2-6205-4118-ae68-d994d8dd6aa0"));
    hashSet.add(UUID.fromString("4aa397bf-4900-4315-90b2-3eb05b81e820"));
    hashSet.add(UUID.fromString("4ac2f646-26a4-39db-a7d5-a1ef74c03d95"));
    hashSet.add(UUID.fromString("50f06a16-3380-4c85-b202-d44c2122a9e2"));
    hashSet.add(UUID.fromString("53745c64-a5f0-44ab-824d-496ae4763a78"));
    hashSet.add(UUID.fromString("5381357b-8361-47d8-a8a4-fb0000ad01f9"));
    hashSet.add(UUID.fromString("54681f82-072c-4642-87c7-aa14f42cde4b"));
    hashSet.add(UUID.fromString("559acf95-bb41-437a-8e53-bbf1c7ba296b"));
    hashSet.add(UUID.fromString("5632dbba-cfe6-35c3-9130-c2027ea37c8c"));
    hashSet.add(UUID.fromString("580ba7c3-e59d-4e88-b566-952abf20fd13"));
    hashSet.add(UUID.fromString("58bb363c-ae7e-4af5-bce4-cf5b5708c949"));
    hashSet.add(UUID.fromString("59cbba52-78f8-4e1f-94a4-b517bfabf1b8"));
    hashSet.add(UUID.fromString("5bad2518-ac40-4357-a8da-33adaa70fa59"));
    hashSet.add(UUID.fromString("5c8f76e6-3342-43b8-80b3-4d0ca51e72b0"));
    hashSet.add(UUID.fromString("5c9281b2-ce46-41ba-a9e8-469b57848e6e"));
    hashSet.add(UUID.fromString("5d9e9f18-f2f0-4d2f-b942-90b7049039c6"));
    hashSet.add(UUID.fromString("5e0933cb-88a7-4648-8fe6-c07293cfeb35"));
    hashSet.add(UUID.fromString("5ea58cea-3616-44b6-80f9-46764d40fb52"));
    hashSet.add(UUID.fromString("5ec8256f-60b1-47f2-842e-aefcf84937c7"));
    hashSet.add(UUID.fromString("60c8aa86-5a0f-421c-8d9c-f5c7ae4ccccd"));
    hashSet.add(UUID.fromString("60fb460e-381a-40de-92e7-a95e38763f57"));
    hashSet.add(UUID.fromString("67d7d0a0-2e5a-498c-b74b-ea72e0b10b3d"));
    hashSet.add(UUID.fromString("6acca04d-328e-48cd-979f-b036b78ea920"));
    hashSet.add(UUID.fromString("6c49ef8a-94c9-45b0-8670-a02bc9bc2197"));
    hashSet.add(UUID.fromString("6f248bbc-e935-4423-a480-8bbd232b3283"));
    hashSet.add(UUID.fromString("7408834c-91e4-44fe-8b8a-b4f844466f6c"));
    hashSet.add(UUID.fromString("74a37ee1-4da4-4c96-81f5-ba057f51ba89"));
    hashSet.add(UUID.fromString("750c3e9c-e6b6-4001-976d-6e8b3548359a"));
    hashSet.add(UUID.fromString("7532df68-a598-4da4-b782-889852ef8f18"));
    hashSet.add(UUID.fromString("754bb621-c255-4bc5-9839-2345aaacffd6"));
    hashSet.add(UUID.fromString("7916936d-7556-4ad5-997f-8bd0725e95af"));
    hashSet.add(UUID.fromString("793f3f52-d8f1-308d-a057-0403eba2b71f"));
    hashSet.add(UUID.fromString("7a2ade96-5c81-45b4-94c6-a2ef4849653c"));
    hashSet.add(UUID.fromString("7a44a801-652f-40fa-a65c-863c882935f9"));
    hashSet.add(UUID.fromString("7ce4ec11-8925-44cb-969b-32c5db9360fc"));
    hashSet.add(UUID.fromString("7f08258d-52dd-4c73-b1f3-90c0fb7508d2"));
    hashSet.add(UUID.fromString("7f747cce-c77e-47d6-b9a5-eb4d66c625ba"));
    hashSet.add(UUID.fromString("833d9b30-bb9d-429e-a171-a8e16c04b1d2"));
    hashSet.add(UUID.fromString("86cecb2f-f7ec-4aa8-8cf3-ee02c3967a3d"));
    hashSet.add(UUID.fromString("88e8e3dd-cc6b-4a04-abea-cf65fc5a21fe"));
    hashSet.add(UUID.fromString("89ab7747-f3de-47bb-ac03-78877b2f0116"));
    hashSet.add(UUID.fromString("8adaabde-5869-45ee-a94c-b43ecd6920e1"));
    hashSet.add(UUID.fromString("8b5bbf17-eecd-4627-a675-8633546517d6"));
    hashSet.add(UUID.fromString("8bbe25d7-f92d-42e6-84d8-d2cf9d3d205d"));
    hashSet.add(UUID.fromString("8c2a7f7f-8600-439b-9d07-5fba0a1aaaca"));
    hashSet.add(UUID.fromString("8d67d18d-e147-467f-ae57-7e4a210136c8"));
    hashSet.add(UUID.fromString("8e0c5f37-a424-46f8-980c-cfeaebc2796a"));
    hashSet.add(UUID.fromString("8f18a45f-2fd7-4be7-a813-072304c818c0"));
    hashSet.add(UUID.fromString("908516aa-b27d-487f-8e80-67bb126a7392"));
    hashSet.add(UUID.fromString("91006b74-1011-46e6-9ba0-d790102979f9"));
    hashSet.add(UUID.fromString("91f7c7a1-4ac3-4e33-9dd9-661ef3615c53"));
    hashSet.add(UUID.fromString("92692f76-7b11-4946-9ad9-2ec77424036c"));
    hashSet.add(UUID.fromString("92785bba-8a53-492f-aaf5-74c3435c72e4"));
    hashSet.add(UUID.fromString("947bc18a-252d-4fbf-9917-01a6a50b782c"));
    hashSet.add(UUID.fromString("98ffb06d-7559-42df-bf10-04f3b7a1ae31"));
    hashSet.add(UUID.fromString("9a8913a8-1f89-4294-8edd-89a8fa3dc531"));
    hashSet.add(UUID.fromString("9bfae87b-a7db-4a14-92cc-c10ef209b279"));
    hashSet.add(UUID.fromString("9cb9acdd-1e56-4a2e-9e3b-a81692b985e1"));
    hashSet.add(UUID.fromString("9dac206c-d1e8-474c-a696-56f9e50f5bc4"));
    hashSet.add(UUID.fromString("9f87a41f-f658-41c5-80d2-8819b28978a9"));
    hashSet.add(UUID.fromString("a1082690-83f1-3725-b816-f51cb2df5d93"));
    hashSet.add(UUID.fromString("a2bca04e-43a0-4135-9748-ab4db83e02ce"));
    hashSet.add(UUID.fromString("a3c8acd5-20e9-4461-997e-2c130f82c02f"));
    hashSet.add(UUID.fromString("a404055f-dd8b-45da-9ee2-266b1ec14ced"));
    hashSet.add(UUID.fromString("a46ad75f-a579-44ab-8151-4d185536acdf"));
    hashSet.add(UUID.fromString("a6bfa6e4-4f2b-4c97-a716-c6a0b3aa7383"));
    hashSet.add(UUID.fromString("a74cb1c0-0713-4ac2-88af-41f1a4677e2e"));
    hashSet.add(UUID.fromString("a83ab3f4-6cea-4a88-ae18-7c71240f6495"));
    hashSet.add(UUID.fromString("a90b6496-c647-46b2-a284-ad3292f12b9a"));
    hashSet.add(UUID.fromString("aac025f2-ad32-4e88-8bc1-ff4851fade7f"));
    hashSet.add(UUID.fromString("ab6a5bc9-2f92-41d3-b7e2-bc49438357cf"));
    hashSet.add(UUID.fromString("afd08b5c-1d15-404e-9ae1-1b90aceb28fe"));
    hashSet.add(UUID.fromString("b071651a-ac97-4ce9-8d87-37b2029d32d1"));
    hashSet.add(UUID.fromString("b077c7ab-6b73-4079-8a96-0409e9a54034"));
    hashSet.add(UUID.fromString("b08a029f-a132-47e2-9b3f-9f9342bab1be"));
    hashSet.add(UUID.fromString("b11f1d16-551b-429a-8db2-26c02cd7eb4c"));
    hashSet.add(UUID.fromString("b1560aac-8fa1-376e-88ee-d091be16878b"));
    hashSet.add(UUID.fromString("b35dfc48-7eee-48ca-bfac-3df9b8b744da"));
    hashSet.add(UUID.fromString("b6477803-4614-468f-8acd-a24546d3dcb1"));
    hashSet.add(UUID.fromString("b6a42871-e5a7-441c-af62-62907d347e9f"));
    hashSet.add(UUID.fromString("b6bb2a8d-06d9-4d0b-a59b-9e73d5ef0a13"));
    hashSet.add(UUID.fromString("b7da28a4-65a2-4009-92c0-8f7a579f959d"));
    hashSet.add(UUID.fromString("b7eb0a17-bc80-4d7e-8ae9-382d8d7cdae0"));
    hashSet.add(UUID.fromString("b8f0fb46-4e9c-4452-908c-fa5c6d1558c8"));
    hashSet.add(UUID.fromString("bd7231ca-e480-458e-b9d5-16980267abf7"));
    hashSet.add(UUID.fromString("bf80636b-e409-3d3e-9feb-15745acad231"));
    hashSet.add(UUID.fromString("bf98ead3-8d35-4fd7-9b3d-f0168d29806a"));
    hashSet.add(UUID.fromString("bf9a5f8d-bdbe-4707-b651-c3fdea8426dc"));
    hashSet.add(UUID.fromString("c3d36dcd-78f1-4b6f-8a71-cbf47e61bcc1"));
    hashSet.add(UUID.fromString("c5445889-399e-43c1-9ad8-674c5cc7827b"));
    hashSet.add(UUID.fromString("c5e3f15a-ad3d-4de8-aad1-8dc150344a8e"));
    hashSet.add(UUID.fromString("c64baee7-0ec0-4467-b403-eff1d12f09ae"));
    hashSet.add(UUID.fromString("c6640f97-8a48-3557-bc46-f73ae698fff2"));
    hashSet.add(UUID.fromString("c9809b0d-9c06-413b-add5-e3577556adff"));
    hashSet.add(UUID.fromString("c9b33d69-e07e-4422-8f04-2f1eeeae00ea"));
    hashSet.add(UUID.fromString("ce81e5e6-e2f4-4533-bb18-e4eef2674d7b"));
    hashSet.add(UUID.fromString("d038bace-7a8c-4d43-a72b-87416b167f4f"));
    hashSet.add(UUID.fromString("d34cae00-2437-4581-9fc4-cba58c7d4c8f"));
    hashSet.add(UUID.fromString("d49011ef-b58f-413a-be38-73c1fca8230f"));
    hashSet.add(UUID.fromString("d55bad84-7808-4a8f-869f-4e6331384b6f"));
    hashSet.add(UUID.fromString("d55ca5d0-8e24-494b-9994-dfccc3d673d9"));
    hashSet.add(UUID.fromString("d5635ee7-c44d-3a11-b7eb-e82de830148c"));
    hashSet.add(UUID.fromString("d5aa162a-954a-3faa-8a8c-a9775682486d"));
    hashSet.add(UUID.fromString("d6efd7ba-5a10-4e45-bf31-676ebdd6b8e9"));
    hashSet.add(UUID.fromString("d70d2b2f-0208-4b52-a3d4-c431241fff82"));
    hashSet.add(UUID.fromString("d78cbe88-46b0-4e5b-a1f3-6511e77cbb81"));
    hashSet.add(UUID.fromString("dc6f22eb-758e-46cd-9943-5b2065074db3"));
    hashSet.add(UUID.fromString("dd254cf4-5ea1-4424-946f-fd777fb5d8bc"));
    hashSet.add(UUID.fromString("dd28fef7-0d41-43c9-b108-6dc75ac54e71"));
    hashSet.add(UUID.fromString("e224d5f2-cc9c-4f99-bdcb-c06e32b1df33"));
    hashSet.add(UUID.fromString("e22a33c0-9150-4ff2-a62b-6dfa356e0a4e"));
    hashSet.add(UUID.fromString("e31109a7-b7c4-44db-bee0-15aa100ae04c"));
    hashSet.add(UUID.fromString("e4c21742-9a32-494c-802f-9ef41317826d"));
    hashSet.add(UUID.fromString("e4d03de3-2d83-43a5-828d-ad40dbd647bf"));
    hashSet.add(UUID.fromString("e77e9abd-9139-4138-833e-f3c18360bef5"));
    hashSet.add(UUID.fromString("ea2bba3a-33a1-4670-9c47-25dcd075f04a"));
    hashSet.add(UUID.fromString("eb66b92b-04ec-4f9d-9d8e-10359e6b7fcb"));
    hashSet.add(UUID.fromString("ed235a42-9e6f-31e6-a692-8204724b97c5"));
    hashSet.add(UUID.fromString("efad14a5-5bc0-4bdd-a20e-803df644c0a2"));
    hashSet.add(UUID.fromString("efc73349-a92e-4c43-a79d-c566a2bdb46c"));
    hashSet.add(UUID.fromString("f05f9324-5e2d-41bc-8f23-eadf68dd8316"));
    hashSet.add(UUID.fromString("f12136c9-3172-4af5-9418-0c9e875640f3"));
    hashSet.add(UUID.fromString("f1748633-bb0c-4eec-9269-3da85bb5e3d3"));
    hashSet.add(UUID.fromString("f341fe1d-21ba-4439-b1c4-f496029bd6f6"));
    hashSet.add(UUID.fromString("f4866c45-2574-48d1-a438-0a533ff87097"));
    hashSet.add(UUID.fromString("f54b9168-ab1e-4e9f-9b24-8d7e009474db"));
    hashSet.add(UUID.fromString("f592543f-dbf0-4448-a5e5-5503e75b2844"));
    hashSet.add(UUID.fromString("f86bb599-c6db-4ce2-a94f-22d4e753ff9d"));
    hashSet.add(UUID.fromString("f917b686-4e5f-437e-a550-6cf43fc826db"));
    hashSet.add(UUID.fromString("f985e04f-2412-4dcd-9878-178a2e690614"));
    hashSet.add(UUID.fromString("f9b7a7d8-497e-4fa8-97f6-78aa644fe820"));
    hashSet.add(UUID.fromString("fcde3ad3-3e3a-494a-a87d-c3b455727083"));
    hashSet.add(UUID.fromString("fec1cec0-7f6a-440a-8438-f8561ae05417"));
    hashSet.add(UUID.fromString("fffd0f6f-e6eb-49ad-81df-2a24d1638d68"));
  }

  @Override
  public HashSet<UUID> getHashSet() {
    return hashSet;
  }

  @Override
  public boolean canOpen(Player p, Block block, PlayerInteractEvent e) {
    boolean canOpen = super.canOpen(p, block, e);
    if (!canOpen) {
      Message.sendMessage(p, "α版に参加した人だけこのチェストを開けられます");
    }
    return canOpen;
  }
}
