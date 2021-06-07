package card.rules;

import card.enums.MonsterStatus;
import card.enums.SkillKeyword;
import card.model.game.Expression;

public class CocoonRule implements KeywordRule {
	
    @Override
    public boolean applyEffects(Expression expression) {
        boolean evalResult = false;
        if (expression.getSkillKeyword() == SkillKeyword.COCOON) {
            evalResult = true;
            expression.getAttacker().addStatusDuration(MonsterStatus.COCOON, 99);
        }
        return evalResult;
    }

}
