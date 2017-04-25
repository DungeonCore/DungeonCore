package lbn.nametag;



public class Tag {
	public TagType tagtype;

	public enum TagType {
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

	public TagType getTagType() {
		return this.tagtype;
	}

	public void setName(TagType tag) {
		this.tagtype = tag;
	}


}
