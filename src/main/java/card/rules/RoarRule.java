package card.rules;

import java.util.ArrayList;

import card.enums.MonsterStatus;
import card.enums.SkillKeyword;
import card.model.game.Expression;
import card.model.game.Monster;

public class RoarRule implements KeywordRule {
	
    @Override
    public boolean applyEffects(Expression expression) {
        boolean evalResult = false;
        if (expression.getSkillKeyword() == SkillKeyword.ROAR) {
            evalResult = true;
            ArrayList<Monster> defenderMonsters = expression.getDefenderArea().getMonsters();
            for (Monster monster : defenderMonsters) {
            	monster.addStatusDuration(MonsterStatus.STUNNED, 1);
            }
        }
        return evalResult;
    }

}
