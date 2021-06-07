package card.rules;

import card.enums.SkillKeyword;
import card.model.game.Expression;
import card.model.game.PlayerArea;
import card.util.CardUtil;

public class BusyTimeRule implements KeywordRule {
	
    @Override
    public boolean applyEffects(Expression expression) {
        boolean evalResult = false;
        if (expression.getSkillKeyword() == SkillKeyword.BUSY_TIME) {
            evalResult = true;
            PlayerArea attackerArea = expression.getAttackerArea();
            for (int i = 0; i < attackerArea.getBreeder().getGuts(); i++) {
            	CardUtil.discardFromDeck(attackerArea.getDiscards(), attackerArea.getDeck().getSkillCards());
            }
			CardUtil.shuffle(attackerArea.getDeck().getSkillCards());
        }
        return evalResult;
    }

}
