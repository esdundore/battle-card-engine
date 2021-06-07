package card.rules;

import card.enums.SkillKeyword;
import card.model.game.Expression;
import card.model.game.PlayerArea;
import card.util.CardUtil;

public class TacticsRule implements KeywordRule {
	
    @Override
    public boolean applyEffects(Expression expression) {
        boolean evalResult = false;
        if (expression.getSkillKeyword() == SkillKeyword.TACTICS) {
            evalResult = true;
            PlayerArea attackerArea = expression.getAttackerArea();
            Integer discardIndex = expression.getAttack().getTarget();
            CardUtil.discardFromHand(attackerArea.getDiscards(), discardIndex, attackerArea.getDeck().getSkillCards());
            CardUtil.draw(attackerArea.getDeck().getSkillCards(), attackerArea.getHand());
        }
        return evalResult;
    }

}
