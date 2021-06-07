package card.rules;

import card.enums.SkillKeyword;
import card.model.game.Breeder;
import card.model.game.Expression;

public class FakeCryRule implements KeywordRule {
	
    @Override
    public boolean applyEffects(Expression expression) {
        boolean evalResult = false;
        if (expression.getSkillKeyword() == SkillKeyword.FAKE_CRY) {
            evalResult = true;
            Breeder defender = expression.getDefenderArea().getBreeder();
            defender.setGuts(defender.getGuts() - 1);
        }
        return evalResult;
    }

}
