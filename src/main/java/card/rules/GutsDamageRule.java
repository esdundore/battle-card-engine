package card.rules;

import card.enums.SkillKeyword;
import card.model.game.Breeder;
import card.model.game.Expression;

public class GutsDamageRule implements KeywordRule {
	
    @Override
    public boolean applyEffects(Expression expression) {
        boolean evalResult = false;
        if (expression.getSkillKeyword() == SkillKeyword.GUTS_DMG) {
            evalResult = true;
            if (!expression.getDefenseKeywords().contains(SkillKeyword.PERSISTANCE) && expression.getDamageDealt() > 0) {
            	Breeder defender = expression.getDefenderArea().getBreeder();
            	defender.setGuts(defender.getGuts() - expression.getKeywordValue());
            }
        }
        return evalResult;
    }

}
