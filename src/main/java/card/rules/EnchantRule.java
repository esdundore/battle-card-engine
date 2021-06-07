package card.rules;

import card.enums.MonsterStatus;
import card.enums.SkillKeyword;
import card.model.game.Expression;

public class EnchantRule implements KeywordRule {
	
    @Override
    public boolean applyEffects(Expression expression) {
        boolean evalResult = false;
        if (expression.getSkillKeyword() == SkillKeyword.ENCHANT) {
            evalResult = true;
            expression.getAttacker().addStatusDuration(MonsterStatus.UNTARGETABLE, 1);
        }
        return evalResult;
    }

}
