package lbn.item.slot;

public enum SlotLevel {
	UNUSE("", 0, 0, (byte)0),
	TUTORIAL("", 100, 0, (byte)7),
	LEVEL1("★", 80, 100, (byte)10),
	LEVEL2("★ ★", 70, 300, (byte)9),
	LEVEL3("★ ★ ★", 60, 500, (byte)5),
	LEVEL4("★ ★ ★ ★", 50, 700, (byte)11),
	LEVEL5("★ ★ ★ ★ ★", 40, 1000, (byte)14),
	LEGENT("LEGENDARY", 100, 10000, (byte)1),
	ADD_EMPTY(null, 50, 1000, (byte)0),
	ADD_EMPTY2(null, 25, 1000, (byte)0),
	REMOVE_UNAVAILABLE(null, 100, 200, (byte)0),
	REMOVE_UNAVAILABLE2(null, 75, 200, (byte)0)
	;

	String star;
	int sucessPer;
	int price;
	byte data;

	private SlotLevel(String star, int sucessPer, int price, byte data) {
		this.star = star;
		this.sucessPer = sucessPer;
		this.price = price;
		this.data = data;
	}

	public String getStar() {
		return star;
	}

	public int getSucessPer() {
		return sucessPer;
	}

	public int getPrice() {
		return price;
	}

	public byte getData() {
		return data;
	}
}
