package card.rules;

import card.enums.MonsterStatus;
import card.enums.SkillKeyword;
import card.model.game.Expression;

public class MultiRule implements KeywordRule {
	
    @Override
    public boolean applyEffects(Expression expression) {
        boolean evalResult = false;
        if (expression.getSkillKeyword() == SkillKeyword.MULTI) {
            evalResult = true;
            expression.getAttacker().addStatusDuration(MonsterStatus.MULTI, 1);
        }
        return evalResult;
    }

}
