package card.rules;

import card.enums.MonsterStatus;
import card.enums.SkillKeyword;
import card.model.game.Expression;

public class RootoneRule implements KeywordRule {
	
    @Override
    public boolean applyEffects(Expression expression) {
        boolean evalResult = false;
        if (expression.getSkillKeyword() == SkillKeyword.ROOTONE) {
            evalResult = true;
            expression.getAttacker().addStatusDuration(MonsterStatus.FOCUSPOWx2, 1);
        }
        return evalResult;
    }

}
