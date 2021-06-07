package card.rules;

import card.enums.SkillKeyword;
import card.model.game.Breeder;
import card.model.game.Expression;
import card.model.game.PlayerArea;
import card.util.CardUtil;

public class WildRushRule implements KeywordRule {
	
    @Override
    public boolean applyEffects(Expression expression) {
        boolean evalResult = false;
        if (expression.getSkillKeyword() == SkillKeyword.WILD_RUSH) {
            evalResult = true;
            PlayerArea attackerArea = expression.getAttackerArea();
            Breeder attacker = attackerArea.getBreeder();
			for (int i = 0; i < 5; i++) {
				CardUtil.discardFromDeck(attackerArea.getDeck().getSkillCards(), attackerArea.getDiscards());
				attacker.setGuts(attacker.getGuts() + 1);
			}
        }
        return evalResult;
    }

}
