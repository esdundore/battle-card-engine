package card.rules;

import card.enums.MonsterStatus;
import card.enums.SkillKeyword;
import card.model.game.Expression;
import card.model.game.Monster;

public class PORule implements KeywordRule {
	
    @Override
    public boolean applyEffects(Expression expression) {
        boolean evalResult = false;
        if (expression.getSkillKeyword() == SkillKeyword.PO) {
            evalResult = true;
            Monster attacker = expression.getAttacker();
            attacker.addStatusDuration(MonsterStatus.NOBLOCK, 1);
			attacker.addStatusDuration(MonsterStatus.NODODGE, 1);
			attacker.addStatusDuration(MonsterStatus.DMGx2, 1);
        }
        return evalResult;
    }

}
