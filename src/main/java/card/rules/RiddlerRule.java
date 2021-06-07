package card.rules;

import card.enums.SkillKeyword;
import card.model.cards.SkillCard;
import card.model.game.Expression;
import card.model.game.PlayerArea;
import card.util.CardUtil;

public class RiddlerRule implements KeywordRule {
	
    @Override
    public boolean applyEffects(Expression expression) {
        boolean evalResult = false;
        if (expression.getSkillKeyword() == SkillKeyword.RIDDLER) {
            evalResult = true;
            PlayerArea defenderArea = expression.getDefenderArea();
            Integer handIndex = -1;
            for (SkillCard skillCard : defenderArea.getHand()) {
            	handIndex++;
            	if (null != skillCard) {
            		CardUtil.putOnBottom(defenderArea.getHand(), handIndex, defenderArea.getDiscards());
            	}
            }
            CardUtil.draw(defenderArea.getDeck().getSkillCards(), defenderArea.getHand(), 4);
        }
        return evalResult;
    }

}
