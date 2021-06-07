package card.rules;

import card.enums.SkillKeyword;
import card.model.cards.SkillCard;
import card.model.game.Expression;
import card.model.game.PlayerArea;
import card.util.CardUtil;

public class RefreshRule implements KeywordRule {
	
    @Override
    public boolean applyEffects(Expression expression) {
        boolean evalResult = false;
        if (expression.getSkillKeyword() == SkillKeyword.REFRESH) {
            evalResult = true;
            PlayerArea attackerArea = expression.getAttackerArea();
            Integer cardsDiscarded = 0;
            Integer handIndex = -1;
            for (SkillCard skillCard : attackerArea.getHand()) {
            	handIndex++;
            	if (null != skillCard) {
            		CardUtil.discardFromHand(attackerArea.getHand(), handIndex, attackerArea.getDiscards());
            		cardsDiscarded++;
            	}
            }
            CardUtil.draw(attackerArea.getDeck().getSkillCards(), attackerArea.getHand(), cardsDiscarded);
        }
        return evalResult;
    }

}
