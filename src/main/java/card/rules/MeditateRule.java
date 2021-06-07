package card.rules;

import card.enums.MonsterStatus;
import card.enums.SkillKeyword;
import card.model.game.Expression;

public class MeditateRule implements KeywordRule {
	
    @Override
    public boolean applyEffects(Expression expression) {
        boolean evalResult = false;
        if (expression.getSkillKeyword() == SkillKeyword.MEDITATE) {
            evalResult = true;
            expression.getAttackerArea().getBreeder().addStatusDuration(MonsterStatus.MEDITATING, 1);
        }
        return evalResult;
    }

}
