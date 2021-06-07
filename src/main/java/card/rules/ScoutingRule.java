package card.rules;

import card.enums.MonsterStatus;
import card.enums.SkillKeyword;
import card.model.game.Expression;

public class ScoutingRule implements KeywordRule {
	
    @Override
    public boolean applyEffects(Expression expression) {
        boolean evalResult = false;
        if (expression.getSkillKeyword() == SkillKeyword.SCOUTING) {
            evalResult = true;
            expression.getAttackerArea().getBreeder().addStatusDuration(MonsterStatus.SCOUTING, 1);
        }
        return evalResult;
    }

}
