package card.rules;

import card.enums.SkillKeyword;
import card.model.game.Expression;
import card.model.game.PlayerArea;
import card.util.CardUtil;

public class LastTrumpRule implements KeywordRule {
	
    @Override
    public boolean applyEffects(Expression expression) {
        boolean evalResult = false;
        if (expression.getSkillKeyword() == SkillKeyword.LAST_TRUMP) {
            evalResult = true;
            PlayerArea attackerArea = expression.getAttackerArea();
            Integer targetIndex = expression.getAttack().getTarget();
            CardUtil.discardFromHand(attackerArea.getDeck().getSkillCards(), targetIndex, attackerArea.getDeck().getSkillCards());
            CardUtil.shuffle(attackerArea.getDeck().getSkillCards());
			CardUtil.draw(attackerArea.getDeck().getSkillCards(), attackerArea.getHand());
        }
        return evalResult;
    }

}
