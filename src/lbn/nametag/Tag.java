package lbn.nametag;

public class Tag {

	static TagType tagtype;

	public static enum TagType {
		OWNER,
		CO_OWNER,
		DEVELOPER,
		JR_DEVELOPER,
		ADMIN,
		JR_ADMIN,
		MODERATOR,
		JR_MODERATOR,
		SENIOR_BUILDER,
		BUILDER,
		JR_BUILDER,
		MANAGER,
		HELPER,
		TRAIAL,
	}

	public static TagType getTagType() {
		return tagtype;
	}
}
