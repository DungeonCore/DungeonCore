package main.quest.quest;

import java.util.List;

import org.bukkit.entity.Player;

import main.common.event.quest.ComplateQuestEvent;
import main.common.event.quest.DestructionQuestEvent;
import main.common.event.quest.StartQuestEvent;
import main.quest.Quest;

public class NullQuest implements Quest{

	String id;

	public NullQuest(String id) {
		this.id = id;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getName() {
		return id;
	}

	@Override
	public String getQuestDetail() {
		return "クエストが存在しません";
	}

	@Override
	public void onStart(StartQuestEvent e) {

	}

	@Override
	public void onComplate(ComplateQuestEvent e) {

	}

	@Override
	public void onDistruction(DestructionQuestEvent e) {

	}

	@Override
	public List<Quest> getBeforeQuest() {
		return null;
	}

	@Override
	public boolean isMainQuest() {
		return false;
	}

	@Override
	public boolean isStartOverlap() {
		return false;
	}

	@Override
	public String getCurrentInfo(Player p) {
		return null;
	}

	@Override
	public void playCompleteSound(Player p) {

	}

	@Override
	public void playDistructionSound(Player p) {

	}

	@Override
	public void playStartSound(Player p) {

	}

	@Override
	public boolean isDoing(Player p) {
		return false;
	}

	@Override
	public boolean canDestory() {
		return false;
	}

	@Override
	public String[] getTalk1() {
		return null;
	}

	@Override
	public String[] getTalk2() {
		return null;
	}

	@Override
	public void giveRewardItem(Player p) {
	}
}