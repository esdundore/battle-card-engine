package card.rules;

import card.enums.SkillKeyword;
import card.model.game.Expression;

public class NutsOilRule implements KeywordRule {
	
    @Override
    public boolean applyEffects(Expression expression) {
        boolean evalResult = false;
        if (expression.getSkillKeyword() == SkillKeyword.NUTS_OIL) {
            evalResult = true;
            expression.getTargetMonster().setCanAttack(true);
        }
        return evalResult;
    }

}
