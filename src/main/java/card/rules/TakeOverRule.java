package card.rules;

import card.enums.SkillKeyword;
import card.model.game.Expression;
import card.model.game.Monster;

public class TakeOverRule implements KeywordRule {
	
    @Override
    public boolean applyEffects(Expression expression) {
        boolean evalResult = false;
        if (expression.getSkillKeyword() == SkillKeyword.TAKE_OVER) {
            evalResult = true;
            expression.getAttacker().setCurrentLife(0);
            Monster target = expression.getTargetMonster();
            target.setCurrentLife(target.getCurrentLife() + expression.getKeywordValue());
        }
        return evalResult;
    }

}
