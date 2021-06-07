package card.rules;

import card.enums.MonsterStatus;
import card.enums.SkillKeyword;
import card.model.game.Expression;

public class SingingCatRule implements KeywordRule {
	
    @Override
    public boolean applyEffects(Expression expression) {
        boolean evalResult = false;
        if (expression.getSkillKeyword() == SkillKeyword.SINGING_CAT) {
            evalResult = true;
            expression.getAttackerArea().getBreeder().addStatusDuration(MonsterStatus.BERSERK, 1);
        }
        return evalResult;
    }

}
