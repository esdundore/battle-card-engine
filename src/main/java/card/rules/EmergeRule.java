package card.rules;

import java.util.ArrayList;

import card.enums.SkillKeyword;
import card.model.game.Expression;
import card.model.game.Monster;

public class EmergeRule implements KeywordRule {
	
    @Override
    public boolean applyEffects(Expression expression) {
        boolean evalResult = false;
        if (expression.getSkillKeyword() == SkillKeyword.EMERGE) {
            evalResult = true;
            ArrayList<Monster> monsters = expression.getAttackerArea().getMonsters();
            Integer monsterIndex = -1;
            for (Monster monster : monsters) {
            	monsterIndex++;
            	if (expression.getAttacker() == monster) {
            		break;
            	}
            }
            Integer target = expression.getAttack().getTarget();
			Monster emergedMonster = expression.getAttackerArea().getSubMonsters().get(target);
			emergedMonster.setCurrentLife(emergedMonster.getMaxLife());
            monsters.set(monsterIndex, emergedMonster);
        }
        return evalResult;
    }

}
