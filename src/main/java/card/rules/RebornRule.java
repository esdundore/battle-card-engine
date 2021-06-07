package card.rules;

import card.enums.SkillKeyword;
import card.model.game.Expression;
import card.model.game.Monster;
import card.model.game.PlayerArea;

public class RebornRule implements KeywordRule {
	
    @Override
    public boolean applyEffects(Expression expression) {
        boolean evalResult = false;
        if (expression.getSkillKeyword() == SkillKeyword.REBORN) {
            evalResult = true;
            PlayerArea attackerArea = expression.getAttackerArea();
            Monster targetMonster = attackerArea.getMonsters().get(expression.getAttack().getTarget());
            targetMonster.setCurrentLife(targetMonster.getMaxLife());
        }
        return evalResult;
    }

}
