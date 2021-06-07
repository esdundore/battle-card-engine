package card.rules;

import card.enums.MonsterStatus;
import card.enums.SkillKeyword;
import card.model.game.Expression;

public class GritRule implements KeywordRule {
	
    @Override
    public boolean applyEffects(Expression expression) {
        boolean evalResult = false;
        if (expression.getSkillKeyword() == SkillKeyword.GRIT) {
            evalResult = true;
            expression.getAttacker().addStatusDuration(MonsterStatus.GRIT, 99);
        }
        return evalResult;
    }

}
