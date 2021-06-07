package card.rules;

import java.util.ArrayList;

import card.enums.MonsterStatus;
import card.enums.SkillKeyword;
import card.model.game.Expression;
import card.model.game.Monster;

public class ProvokeRule implements KeywordRule {
	
    @Override
    public boolean applyEffects(Expression expression) {
        boolean evalResult = false;
        if (expression.getSkillKeyword() == SkillKeyword.PROVOKE) {
            evalResult = true;
            Monster attacker = expression.getAttacker();
            ArrayList<Monster> monsters = expression.getAttackerArea().getMonsters();
            for (Monster monster : monsters) {
            	if (monster != attacker) {
                	monster.addStatusDuration(MonsterStatus.UNTARGETABLE, 1);
            	}
            }
            attacker.getStatusDuration().remove(MonsterStatus.UNTARGETABLE);
        }
        return evalResult;
    }

}
