package card.rules;

import card.enums.MonsterStatus;
import card.enums.SkillKeyword;
import card.model.game.Expression;

public class YodelRule implements KeywordRule {
	
    @Override
    public boolean applyEffects(Expression expression) {
        boolean evalResult = false;
        if (expression.getSkillKeyword() == SkillKeyword.YODEL) {
            evalResult = true;
            expression.getAttackerArea().getBreeder().addStatusDuration(MonsterStatus.EXTRA_TURN, 1);
        }
        return evalResult;
    }

}
