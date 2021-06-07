package card.rules;

import card.enums.SkillKeyword;
import card.model.game.Expression;
import card.model.game.Monster;

public class RestoreRule implements KeywordRule {
	
    @Override
    public boolean applyEffects(Expression expression) {
        boolean evalResult = false;
        if (expression.getSkillKeyword() == SkillKeyword.RESTORE) {
            evalResult = true;
            Monster target = expression.getTargetMonster();
            target.setCurrentLife(target.getCurrentLife() + expression.getKeywordValue());
        }
        return evalResult;
    }

}
