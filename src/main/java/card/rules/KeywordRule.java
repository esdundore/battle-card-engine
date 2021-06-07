package card.rules;

import card.model.game.Expression;

public interface KeywordRule {
    boolean applyEffects(Expression expression);
}
