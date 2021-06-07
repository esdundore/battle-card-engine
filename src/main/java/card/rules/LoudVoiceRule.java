package card.rules;

import card.enums.SkillKeyword;
import card.model.cards.SkillCard;
import card.model.game.Expression;
import card.model.game.PlayerArea;
import card.util.CardUtil;

public class LoudVoiceRule implements KeywordRule {
	
    @Override
    public boolean applyEffects(Expression expression) {
        boolean evalResult = false;
        if (expression.getSkillKeyword() == SkillKeyword.LOUD_VOICE) {
            evalResult = true;
            PlayerArea defenderArea = expression.getDefenderArea();
            Integer handIndex = -1;
            for (SkillCard skillCard : defenderArea.getHand()) {
            	handIndex++;
            	if (null != skillCard) {
            		CardUtil.discardFromHand(defenderArea.getHand(), handIndex, defenderArea.getDiscards());
            		break;
            	}
            }
        }
        return evalResult;
    }

}
