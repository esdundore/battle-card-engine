package card.rules;

import card.enums.SkillKeyword;
import card.model.game.Breeder;
import card.model.game.Expression;
import card.model.game.Monster;
import card.model.game.PlayerArea;
import card.util.CardUtil;

public class GoodLuckRule implements KeywordRule {
	
    @Override
    public boolean applyEffects(Expression expression) {
        boolean evalResult = false;
        if (expression.getSkillKeyword() == SkillKeyword.GOOD_LUCK) {
            evalResult = true;
            Monster attackMonster = expression.getAttacker();
            PlayerArea attackerArea = expression.getAttackerArea();
            Breeder attacker = attackerArea.getBreeder();
			for (int i = 0; i < attackMonster.getCurrentLife(); i++) {
				CardUtil.discardFromDeck(attackerArea.getDeck().getSkillCards(), attackerArea.getDiscards());
				attacker.setGuts(attacker.getGuts() + 1);
			}
			attackMonster.setCurrentLife(0);
        }
        return evalResult;
    }

}
