package card.rules;

import card.enums.MonsterStatus;
import card.enums.SkillKeyword;
import card.enums.SkillType;
import card.model.game.Expression;

public class StunRule implements KeywordRule {
	
    @Override
    public boolean applyEffects(Expression expression) {
        boolean evalResult = false;
        if (expression.getSkillKeyword() == SkillKeyword.STUN) {
            evalResult = true;
            if (!expression.getDefenseTypes().contains(SkillType.DGE)) {
				expression.getTargetMonster().addStatusDuration(MonsterStatus.STUNNED, 1);
            }
        }
        return evalResult;
    }

}
