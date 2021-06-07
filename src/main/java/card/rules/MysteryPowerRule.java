package card.rules;

import java.util.Collections;

import card.enums.SkillKeyword;
import card.model.game.Expression;
import card.model.game.PlayerArea;
import card.util.CardUtil;

public class MysteryPowerRule implements KeywordRule {
	
    @Override
    public boolean applyEffects(Expression expression) {
        boolean evalResult = false;
        if (expression.getSkillKeyword() == SkillKeyword.MYSTERY_POWER) {
            evalResult = true;
            PlayerArea attackerArea = expression.getAttackerArea();
            Integer discardIndex = expression.getAttack().getTarget();
            CardUtil.putOnBottom(attackerArea.getDiscards(), discardIndex, attackerArea.getDeck().getSkillCards());
            attackerArea.getDiscards().removeAll(Collections.singleton(null));
        }
        return evalResult;
    }

}
