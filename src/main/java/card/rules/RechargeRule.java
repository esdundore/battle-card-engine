package card.rules;

import card.enums.MonsterStatus;
import card.enums.SkillKeyword;
import card.model.game.Expression;

public class RechargeRule implements KeywordRule {
	
    @Override
    public boolean applyEffects(Expression expression) {
        boolean evalResult = false;
        if (expression.getSkillKeyword() == SkillKeyword.RECHARGE) {
            evalResult = true;
            expression.getAttacker().addStatusDuration(MonsterStatus.POWx2, 1);
        }
        return evalResult;
    }

}
