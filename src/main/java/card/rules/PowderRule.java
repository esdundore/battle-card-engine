package card.rules;

import card.enums.SkillKeyword;
import card.model.game.Expression;
import card.model.game.Monster;
import card.model.game.PlayerArea;
import card.util.CardUtil;

public class PowderRule implements KeywordRule {
	
    @Override
    public boolean applyEffects(Expression expression) {
        boolean evalResult = false;
        if (expression.getSkillKeyword() == SkillKeyword.POWDER) {
            evalResult = true;
            PlayerArea attackerArea = expression.getAttackerArea();
            Monster target = expression.getTargetMonster();
			target.setCurrentLife(target.getCurrentLife() - 1);
			CardUtil.draw(attackerArea.getDeck().getSkillCards(), attackerArea.getHand());
        }
        return evalResult;
    }

}
