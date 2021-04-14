package card.model.view;

import java.util.ArrayList;
import java.util.stream.Collectors;

import card.model.game.SkillArea;
import card.model.requests.PlayersRequest;

public class SkillAreaView extends PlayersRequest {
	
	public Integer attackId;
	public String targetArea;
	public ArrayList<Integer> damages;
	public ArrayList<SkillView> attacks;
	public ArrayList<SkillView> defenses;

	public SkillAreaView() { }
	public SkillAreaView(SkillArea skillArea) {
		player1 = skillArea.getPlayer1();
		player2 = skillArea.getPlayer2();
		attackId = skillArea.attackId;
		targetArea = skillArea.getTargetArea().name();
		damages = skillArea.getDamages();
		attacks = skillArea.getAttacks().stream().map(skill -> new SkillView(skill))
				.collect(Collectors.toCollection(ArrayList::new));
		defenses = skillArea.getDefenses().stream().map(skill -> new SkillView(skill))
				.collect(Collectors.toCollection(ArrayList::new));
	}
	
	public Integer getAttackId() {
		return attackId;
	}
	public void setAttackId(Integer attackId) {
		this.attackId = attackId;
	}
	public String getTargetArea() {
		return targetArea;
	}
	public void setTargetArea(String targetArea) {
		this.targetArea = targetArea;
	}
	public ArrayList<Integer> getDamages() {
		return damages;
	}
	public void setDamages(ArrayList<Integer> damages) {
		this.damages = damages;
	}
	public ArrayList<SkillView> getAttacks() {
		return attacks;
	}
	public void setAttacks(ArrayList<SkillView> attacks) {
		this.attacks = attacks;
	}
	public ArrayList<SkillView> getDefenses() {
		return defenses;
	}
	public void setDefenses(ArrayList<SkillView> defenses) {
		this.defenses = defenses;
	}
	
}