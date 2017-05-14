package net.l_bulb.dungeoncore.nametag;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Tag {
  private TagType tagtype;
  private String target;

  public enum TagType {
    OWNER, CO_OWNER, DEVELOPER, MINI_DEVELOPER, ADMIN, MINI_ADMIN, MODERATOR, MINI_MODERATOR, SENIOR_BUILDER, BUILDER, MINI_BUILDER, MANAGER, HELPER, TRAIAL,
  }

}
