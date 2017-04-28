package net.l_bulb.dungeoncore.nametag;

public class Tag {
  public TagType tagtype;

  public enum TagType {
    OWNER, CO_OWNER, DEVELOPER, MINI_DEVELOPER, ADMIN, MINI_ADMIN, MODERATOR, MINI_MODERATOR, SENIOR_BUILDER, BUILDER, MINI_BUILDER, MANAGER, HELPER, TRAIAL,
  }

  public TagType getTagType() {
    return tagtype;
  }

  public void setName(TagType tag) {
    tagtype = tag;
  }

}
