package card.model.game;

import java.util.ArrayList;
import java.util.stream.Collectors;

import card.enums.SkillKeyword;
import card.enums.SkillType;
import card.enums.TargetArea;
import card.model.cards.SkillCard;

public class Expression {
    private GameState gameState;
    private PlayerArea attackerArea;
    private PlayerArea defenderArea;
    private ActiveSkill attack;
    private Integer damageDealt;

    public Expression(GameState gameState, PlayerArea attackerArea, PlayerArea defenderArea, ActiveSkill attack, Integer damageDealt) {
    	this.gameState = gameState;
    	this.attackerArea = attackerArea;
    	this.defenderArea = defenderArea;
    	this.attack = attack;
    }

	public GameState getGameState() {
		return gameState;
	}
	public void setGameState(GameState gameState) {
		this.gameState = gameState;
	}
	public PlayerArea getAttackerArea() {
		return attackerArea;
	}
	public void setAttackerArea(PlayerArea attackerArea) {
		this.attackerArea = attackerArea;
	}
	public PlayerArea getDefenderArea() {
		return defenderArea;
	}
	public void setDefenderArea(PlayerArea defenderArea) {
		this.defenderArea = defenderArea;
	}
	public ActiveSkill getAttack() {
		return attack;
	}
	public void setAttack(ActiveSkill attack) {
		this.attack = attack;
	}
	public Integer getDamageDealt() {
		return damageDealt;
	}
	public void setDamageDealt(Integer damageDealt) {
		this.damageDealt = damageDealt;
	}
	
	public ArrayList<SkillKeyword> getDefenseKeywords() {
		return gameState.getSkillArea().getDefenses().stream()
				.map(ActiveSkill::getCard)
				.map(SkillCard::getSkillKeyword)
				.collect(Collectors.toCollection(ArrayList::new));
	}
	public ArrayList<SkillType> getDefenseTypes() {
		return gameState.getSkillArea().getDefenses().stream()
				.map(ActiveSkill::getCard)
				.map(SkillCard::getSkillType)
				.collect(Collectors.toCollection(ArrayList::new));
	}
	public Monster getAttacker() {
		return attack.getUser();
	}
	public Monster getTargetMonster() {
		TargetArea targetArea = gameState.getSkillArea().getTargetArea();
		if (TargetArea.ALLY == targetArea) {
			return attackerArea.getMonsters().get(attack.getTarget());
		}
		else {
			return defenderArea.getMonsters().get(attack.getTarget());
		}
	}
	public SkillKeyword getSkillKeyword() {
		return attack.getCard().getSkillKeyword();
	}
	public Integer getKeywordValue() {
		return attack.getCard().getKeywordValue();
	}


	
}
