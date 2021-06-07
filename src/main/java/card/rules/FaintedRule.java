package card.rules;

import card.enums.SkillKeyword;
import card.model.game.Expression;

public class FaintedRule implements KeywordRule {
	
    @Override
    public boolean applyEffects(Expression expression) {
        boolean evalResult = false;
        if (expression.getSkillKeyword() == SkillKeyword.FAINTED) {
            evalResult = true;
            expression.getDefenderArea().getBreeder().setGuts(0);
            expression.getAttackerArea().getBreeder().setGuts(0);
        }
        return evalResult;
    }

}
