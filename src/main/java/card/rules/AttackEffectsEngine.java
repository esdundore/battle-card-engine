package card.rules;

import java.util.ArrayList;
import java.util.List;

import card.model.game.Expression;

public class AttackEffectsEngine {
    private static List<KeywordRule> rules = new ArrayList<>();

    static {
    	rules.add(new BerserkRule());
    	rules.add(new BusyTimeRule());
    	rules.add(new CocoonRule());
    	rules.add(new ComputingRule());
    	rules.add(new DodgeProgramRule());
    	rules.add(new DollRule());
    	rules.add(new EmergeRule());
    	rules.add(new EnchantRule());
    	rules.add(new FaintedRule());
    	rules.add(new FakeCryRule());
    	rules.add(new GlareRule());
    	rules.add(new GoodLuckRule());
    	rules.add(new GritRule());
        rules.add(new GutsDamageRule());
        rules.add(new InYourFaceRule());
        rules.add(new LastManRule());
        rules.add(new LastTrumpRule());
        rules.add(new LifeStealRule());
        rules.add(new LoudVoiceRule());
        rules.add(new MeditateRule());
        rules.add(new MultiRule());
        rules.add(new MysteryPowerRule());
        rules.add(new NutsOilRule());
        rules.add(new PORule());
        rules.add(new PowderRule());
        rules.add(new ProvokeRule());
        rules.add(new RebornRule());
        rules.add(new RechargeRule());
        rules.add(new RefreshRule());
        rules.add(new RestoreRule());
        rules.add(new RiddlerRule());
        rules.add(new RoarRule());
        rules.add(new RootoneRule());
        rules.add(new ScoutingRule());
        rules.add(new SilentStanceRule());
        rules.add(new SingingCatRule());
        rules.add(new StunRule());
        rules.add(new TacticsRule());
        rules.add(new TakeOverRule());
        rules.add(new ThrowAwayRule());
        rules.add(new TransformRule());
        rules.add(new TwinklingRule());
        rules.add(new WildRushRule());
        rules.add(new YodelRule());
    }

    public void applyEffects(Expression expression) {
        rules.stream().filter(r -> r.applyEffects(expression)).findFirst();
    }
    
}
