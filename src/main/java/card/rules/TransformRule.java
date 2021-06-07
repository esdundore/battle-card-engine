package card.rules;

import card.enums.MonsterType;
import card.enums.SkillKeyword;
import card.model.game.Expression;
import card.model.game.Monster;
import card.model.game.PlayerArea;
import card.util.CardUtil;

public class TransformRule implements KeywordRule {
	
    @Override
    public boolean applyEffects(Expression expression) {
        boolean evalResult = false;
        if (expression.getSkillKeyword() == SkillKeyword.TRANSFORM) {
            evalResult = true;
            Monster attacker = expression.getAttacker();
            MonsterType attackerType = attacker.getMonsterType();
            attacker.setMonsterType(attackerType == MonsterType.GRD ? MonsterType.AIR : MonsterType.GRD);
            PlayerArea attackerArea = expression.getAttackerArea();
            CardUtil.draw(attackerArea.getDeck().getSkillCards(), attackerArea.getHand());
        }
        return evalResult;
    }

}
