package main.util;

import java.util.ArrayList;
import java.util.UUID;

public class SizeLockedSet extends ArrayList<UUID>{
	private static final long serialVersionUID = 7976327007357107817L;

	int maxSize = 20000;

	public void setSize(int size) {
		this.maxSize = size;
	}

	@Override
	public boolean add(UUID e) {
		if (contains(e)) {
			remove(e);
		}
		while (size() > maxSize) {
			if (size() < 10) {
				DungeonLog.println("ERROR ANNI KIT: list size is too small @ SizeLockedSet.java");
				break;
			}
			remove(0);
		}
		return super.add(e);
	}

	@Override
	public void add(int index, UUID element) {
		if (contains(element)) {
			remove(element);
		}
		super.add(index, element);
	}
}
