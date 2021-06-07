package card.rules;

import java.util.ArrayList;

import card.enums.MonsterStatus;
import card.enums.SkillKeyword;
import card.model.game.Expression;
import card.model.game.Monster;

public class ThrowAwayRule implements KeywordRule {
	
    @Override
    public boolean applyEffects(Expression expression) {
        boolean evalResult = false;
        if (expression.getSkillKeyword() == SkillKeyword.GLARE) {
            evalResult = true;
            ArrayList<Monster> defenderMonsters = expression.getDefenderArea().getMonsters();
            for (Monster monster : defenderMonsters) {
            	monster.addStatusDuration(MonsterStatus.AIRBORNE, 1);
            }
            expression.getAttacker().setCanAttack(true);
        }
        return evalResult;
    }

}
