package card.rules;

import card.enums.MonsterStatus;
import card.enums.SkillKeyword;
import card.model.game.Expression;

public class DollRule implements KeywordRule {
	
    @Override
    public boolean applyEffects(Expression expression) {
        boolean evalResult = false;
        if (expression.getSkillKeyword() == SkillKeyword.DOLL) {
            evalResult = true;
            expression.getAttacker().addStatusDuration(MonsterStatus.INVULNERABLE, 1);
        }
        return evalResult;
    }

}
