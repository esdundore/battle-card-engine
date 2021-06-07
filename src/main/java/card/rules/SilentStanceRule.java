package card.rules;

import card.enums.MonsterStatus;
import card.enums.SkillKeyword;
import card.model.game.Expression;

public class SilentStanceRule implements KeywordRule {
	
    @Override
    public boolean applyEffects(Expression expression) {
        boolean evalResult = false;
        if (expression.getSkillKeyword() == SkillKeyword.SILENT_STANCE) {
            evalResult = true;
            expression.getAttacker().addStatusDuration(MonsterStatus.FOCUSINTx2, 1);
        }
        return evalResult;
    }

}
