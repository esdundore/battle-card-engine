package card.rules;

import card.enums.SkillKeyword;
import card.model.game.Expression;
import card.model.game.PlayerArea;
import card.util.CardUtil;

public class ComputingRule implements KeywordRule {
	
    @Override
    public boolean applyEffects(Expression expression) {
        boolean evalResult = false;
        if (expression.getSkillKeyword() == SkillKeyword.COMPUTING) {
            evalResult = true;
            PlayerArea attackerArea = expression.getAttackerArea();
            CardUtil.discardFromHand(attackerArea.getHand(), expression.getAttack().getTarget(), attackerArea.getDiscards());
			CardUtil.draw(attackerArea.getDeck().getSkillCards(), attackerArea.getHand(), 2);
        }
        return evalResult;
    }

}
