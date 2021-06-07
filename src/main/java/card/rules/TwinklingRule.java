package card.rules;

import card.enums.SkillKeyword;
import card.model.game.Breeder;
import card.model.game.Expression;
import card.model.game.PlayerArea;
import card.util.CardUtil;

public class TwinklingRule implements KeywordRule {
	
    @Override
    public boolean applyEffects(Expression expression) {
        boolean evalResult = false;
        if (expression.getSkillKeyword() == SkillKeyword.TWINKLING) {
            evalResult = true;
            PlayerArea attackerArea = expression.getAttackerArea();
            Breeder attacker = attackerArea.getBreeder();
			while (!attackerArea.getDeck().getSkillCards().isEmpty()) {
				CardUtil.discardFromDeck(attackerArea.getDeck().getSkillCards(), attackerArea.getDiscards());
				attacker.setGuts(attacker.getGuts() + 1);
			}
        }
        return evalResult;
    }

}
