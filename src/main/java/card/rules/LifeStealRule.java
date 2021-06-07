package card.rules;

import card.enums.SkillKeyword;
import card.model.game.Expression;
import card.model.game.Monster;

public class LifeStealRule implements KeywordRule {
	
    @Override
    public boolean applyEffects(Expression expression) {
        boolean evalResult = false;
        if (expression.getSkillKeyword() == SkillKeyword.LIFE_STEAL) {
            evalResult = true;
            Monster attacker = expression.getAttacker();
            attacker.setCurrentLife(attacker.getCurrentLife() + expression.getDamageDealt());
        }
        return evalResult;
    }

}
