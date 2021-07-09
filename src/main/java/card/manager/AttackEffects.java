package card.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import card.enums.MonsterStatus;
import card.enums.MonsterType;
import card.enums.SkillKeyword;
import card.enums.SkillType;
import card.model.cards.SkillCard;
import card.model.game.ActiveSkill;
import card.model.game.GameState;
import card.model.game.Monster;
import card.model.game.PlayerArea;
import card.model.requests.PlayersRequest;
import card.util.CardUtil;

@Component
public class AttackEffects {
	
	public void applyAttackEffects(PlayersRequest playersRequest, GameState gameState, ActiveSkill attackSkill, Integer damageDealt) {
		SkillKeyword keyword = attackSkill.getCard().getSkillKeyword();
		Monster attacker = gameState.getSkillArea().getAttackMonster();
		PlayerArea attackerArea = gameState.getPlayerArea(playersRequest.getPlayer2());
		PlayerArea defenderArea = gameState.getPlayerArea(playersRequest.getPlayer1());
		Collection<SkillKeyword> defenseKeywords = gameState.getSkillArea().getDefenses().stream()
				.map(ActiveSkill::getCard).map(SkillCard::getSkillKeyword)
				.collect(Collectors.toCollection(ArrayList::new));
		
        if (SkillKeyword.BERSERK == keyword) {
        	attacker.addStatusDuration(MonsterStatus.BERSERK, 1);
        }
        else if (SkillKeyword.BUSY_TIME == keyword) {
            for (int i = 0; i < attackerArea.getBreeder().getGuts(); i++) {
            	try {
            		CardUtil.discardFromDeck(attackerArea.getDiscards(), attackerArea.getDeck().getSkillCards());
            	} catch (NoSuchElementException nsee) { }
            }
			CardUtil.shuffle(attackerArea.getDeck().getSkillCards());
        }
        else if (SkillKeyword.COCOON == keyword) {
        	attacker.addStatusDuration(MonsterStatus.COCOON, 999);
        }
        else if (SkillKeyword.COMPUTING == keyword) {
            CardUtil.discardFromHand(attackerArea.getHand(), attackSkill.getTarget(), attackerArea.getDiscards());
            try {
            	CardUtil.draw(attackerArea.getDeck().getSkillCards(), attackerArea.getHand(), 2);
            } catch (NoSuchElementException nsee) { }
        }
        else if (SkillKeyword.DODGE_PROGRAM == keyword) {
            Integer discardIndex = attackSkill.getTarget();
            CardUtil.discardFromHand(attackerArea.getDiscards(), discardIndex, attackerArea.getDeck().getSkillCards());
            try {
            	CardUtil.draw(attackerArea.getDeck().getSkillCards(), attackerArea.getHand());
            } catch (NoSuchElementException nsee) { }
        }
        else if (SkillKeyword.DOLL == keyword) {
        	attacker.addStatusDuration(MonsterStatus.INVULNERABLE, 2);
        }
        else if (SkillKeyword.EMERGE == keyword) {
        	ArrayList<Monster> monsters = attackerArea.getMonsters();
            Integer monsterIndex = -1;
            for (Monster monster : monsters) {
            	monsterIndex++;
            	if (attacker == monster) {
            		break;
            	}
            }
            Integer target = attackSkill.getTarget();
			Monster emergedMonster = attackerArea.getSubMonsters().get(target);
			emergedMonster.setCurrentLife(emergedMonster.getMaxLife());
            monsters.set(monsterIndex, emergedMonster);
        }
        else if (SkillKeyword.ENCHANT == keyword) {
        	attacker.addStatusDuration(MonsterStatus.UNTARGETABLE, 2);
        }
        else if (SkillKeyword.FAINTED == keyword) {
        	if (!defenseKeywords.contains(SkillKeyword.PERSISTANCE) 
        			&& !defenderArea.getBreeder().getStatusDuration().containsKey(MonsterStatus.MEDITATING)) {
        		defenderArea.getBreeder().setGuts(0);
        		attackerArea.getBreeder().setGuts(0);
        	}
        }
        else if (SkillKeyword.FAKE_CRY == keyword) {
        	if (!defenseKeywords.contains(SkillKeyword.PERSISTANCE)
        			&& !defenderArea.getBreeder().getStatusDuration().containsKey(MonsterStatus.MEDITATING)) {
        		defenderArea.getBreeder().setGuts(defenderArea.getBreeder().getGuts() - 1);
        	}
        }
        else if (SkillKeyword.GLARE == keyword) {
            ArrayList<Monster> defenderMonsters = defenderArea.getMonsters();
            for (Monster monster : defenderMonsters) {
            	monster.addStatusDuration(MonsterStatus.NODODGE, 1);
            }
        }
        else if (SkillKeyword.GOOD_LUCK == keyword) {
			for (int i = 0; i < attacker.getCurrentLife(); i++) {
				try {
					CardUtil.discardFromDeck(attackerArea.getDeck().getSkillCards(), attackerArea.getDiscards());
					attackerArea.getBreeder().setGuts(attackerArea.getBreeder().getGuts() + 1);
				} catch (NoSuchElementException nsee) { }
			}
			attacker.setCurrentLife(0);
        }
        else if (SkillKeyword.GUTS_DMG == keyword) {
            if ((!defenseKeywords.contains(SkillKeyword.PERSISTANCE) 
            		&& !defenderArea.getBreeder().getStatusDuration().containsKey(MonsterStatus.MEDITATING)) 
            		&& damageDealt > 0) {
            	defenderArea.getBreeder().setGuts(defenderArea.getBreeder().getGuts() - attackSkill.getCard().getKeywordValue());
            }
        }
        else if (SkillKeyword.IN_YOUR_FACE == keyword) {
            for (Monster monster : attackerArea.getMonsters()) {
            	if (monster != attacker) {
            		monster.addStatusDuration(MonsterStatus.INVULNERABLE, 2);
                	monster.addStatusDuration(MonsterStatus.UNTARGETABLE, 2);
            	}
            }
            attacker.getStatusDuration().remove(MonsterStatus.UNTARGETABLE);
            attacker.addStatusDuration(MonsterStatus.TAUNTING, 2);
        }
        else if (SkillKeyword.LAST_MAN == keyword) {
        	attacker.addStatusDuration(MonsterStatus.BERSERK, 1);
        }
        else if (SkillKeyword.LAST_TRUMP == keyword) {
            Integer targetIndex = attackSkill.getTarget();
            CardUtil.discardFromHand(attackerArea.getHand(), targetIndex, attackerArea.getDeck().getSkillCards());
            CardUtil.shuffle(attackerArea.getDeck().getSkillCards());
            try {
            	CardUtil.draw(attackerArea.getDeck().getSkillCards(), attackerArea.getHand());
            } catch (NoSuchElementException nsee) { }
        }
        else if (SkillKeyword.LIFE_STEAL == keyword) {
            attacker.setCurrentLife(attacker.getCurrentLife() + damageDealt);
        }
        else if (SkillKeyword.LOUD_VOICE == keyword) {
            Integer handIndex = -1;
            for (SkillCard skillCard : defenderArea.getHand()) {
            	handIndex++;
            	if (null != skillCard) {
            		CardUtil.discardFromHand(defenderArea.getHand(), handIndex, defenderArea.getDiscards());
            		break;
            	}
            }
        }
        else if (SkillKeyword.MEDITATE == keyword) {
        	attackerArea.getBreeder().addStatusDuration(MonsterStatus.MEDITATING, 2);
        }
        else if (SkillKeyword.MULTI == keyword) {
        	attacker.addStatusDuration(MonsterStatus.MULTI, 1);
        }
        else if (SkillKeyword.MYSTERY_POWER == keyword) {
        	Integer discardIndex = attackSkill.getTarget();
            CardUtil.putOnBottom(attackerArea.getDiscards(), discardIndex, attackerArea.getDeck().getSkillCards());
            attackerArea.getDiscards().removeAll(Collections.singleton(null));
        }
        else if (SkillKeyword.NUTS_OIL == keyword) {
        	attackerArea.getMonsters().get(attackSkill.getTarget()).setCanAttack(true);
        }
        else if (SkillKeyword.PO == keyword) {
            attacker.addStatusDuration(MonsterStatus.NOBLOCK, 2);
			attacker.addStatusDuration(MonsterStatus.NODODGE, 2);
			attacker.addStatusDuration(MonsterStatus.DMGx2, 3);
        }
        else if (SkillKeyword.POWDER == keyword) {
        	Monster target = attackerArea.getMonsters().get(attackSkill.getTarget());
			target.setCurrentLife(target.getCurrentLife() - 1);
			try {
				CardUtil.draw(attackerArea.getDeck().getSkillCards(), attackerArea.getHand());
			} catch (NoSuchElementException nsee) { }
        }
        else if (SkillKeyword.PROVOKE == keyword) {
            ArrayList<Monster> monsters = attackerArea.getMonsters();
            for (Monster monster : monsters) {
            	if (monster != attacker) {
                	monster.addStatusDuration(MonsterStatus.UNTARGETABLE, 2);
            	}
            }
            attacker.getStatusDuration().remove(MonsterStatus.UNTARGETABLE);
            attacker.addStatusDuration(MonsterStatus.TAUNTING, 2);
        }
        else if (SkillKeyword.REBORN == keyword) {
        	Monster targetMonster = attackerArea.getMonsters().get(attackSkill.getTarget());
            targetMonster.setCurrentLife(targetMonster.getMaxLife());
            targetMonster.setCanAttack(false);
        }
        else if (SkillKeyword.RECHARGE == keyword) {
        	attacker.addStatusDuration(MonsterStatus.POWx2, 3);
        }
        else if (SkillKeyword.REFRESH == keyword) {
        	Integer cardsDiscarded = 0;
            Integer handIndex = -1;
            for (SkillCard skillCard : attackerArea.getHand()) {
            	handIndex++;
            	if (null != skillCard) {
            		CardUtil.discardFromHand(attackerArea.getHand(), handIndex, attackerArea.getDiscards());
            		cardsDiscarded++;
            	}
            }
            try {
            	CardUtil.draw(attackerArea.getDeck().getSkillCards(), attackerArea.getHand(), cardsDiscarded);
            } catch (NoSuchElementException nsee) { }
        }
        else if (SkillKeyword.RESTORE == keyword) {
        	Monster target = attackerArea.getMonsters().get(attackSkill.getTarget());
            target.setCurrentLife(target.getCurrentLife() + attackSkill.getCard().getKeywordValue());
        }
        else if (SkillKeyword.RIDDLER == keyword) {
        	Integer handIndex = -1;
            for (SkillCard skillCard : defenderArea.getHand()) {
            	handIndex++;
            	if (null != skillCard) {
            		CardUtil.putOnBottom(defenderArea.getHand(), handIndex, defenderArea.getDiscards());
            	}
            }
            try {
            	CardUtil.draw(defenderArea.getDeck().getSkillCards(), defenderArea.getHand(), 4);
            } catch (NoSuchElementException nsee) { }
        }
        else if (SkillKeyword.ROAR == keyword) {
            for (Monster monster : defenderArea.getMonsters()) {
            	monster.addStatusDuration(MonsterStatus.STUNNED, 2);
            }
        }
        else if (SkillKeyword.ROOTONE == keyword) {
        	attacker.addStatusDuration(MonsterStatus.FOCUSPOWx2, 2);
        }
        else if (SkillKeyword.SCOUTING == keyword) {
        	attackerArea.getBreeder().addStatusDuration(MonsterStatus.SCOUTING, 1);
        }
        else if (SkillKeyword.SILENT_STANCE == keyword) {
        	attacker.addStatusDuration(MonsterStatus.FOCUSINTx2, 2);
        }
        else if (SkillKeyword.SINGING_CAT == keyword) {
        	attackerArea.getBreeder().addStatusDuration(MonsterStatus.BERSERK, 1);
        }
        else if (SkillKeyword.STUN == keyword) {
        	Collection<SkillType> defenseTypes = gameState.getSkillArea().getDefenses().stream()
    				.map(ActiveSkill::getCard)
    				.map(SkillCard::getSkillType)
    				.collect(Collectors.toCollection(ArrayList::new));
        	if (!defenseTypes.contains(SkillType.DGE)) {
            	Monster targetMonster = defenderArea.getMonsters().get(attackSkill.getTarget());
            	targetMonster.addStatusDuration(MonsterStatus.STUNNED, 2);
            }
        }
        else if (SkillKeyword.TACTICS == keyword) {
        	Integer discardIndex = attackSkill.getTarget();
            CardUtil.discardFromHand(attackerArea.getDiscards(), discardIndex, attackerArea.getDeck().getSkillCards());
            try {
            	CardUtil.draw(attackerArea.getDeck().getSkillCards(), attackerArea.getHand());
            } catch (NoSuchElementException nsee) { }
        }
        else if (SkillKeyword.TAKE_OVER == keyword) {
        	attacker.setCurrentLife(0);
            Monster target = attackerArea.getMonsters().get(attackSkill.getTarget());
            target.setCurrentLife(target.getMaxLife());
        }
        else if (SkillKeyword.THROW_AWAY == keyword) {
            ArrayList<Monster> defenderMonsters = defenderArea.getMonsters();
            for (Monster monster : defenderMonsters) {
            	monster.addStatusDuration(MonsterStatus.AIRBORNE, 1);
            }
            attacker.setCanAttack(true);
        }
        else if (SkillKeyword.TRANSFORM == keyword) {
            MonsterType attackerType = attacker.getMonsterType();
            attacker.setMonsterType(attackerType == MonsterType.GRD ? MonsterType.AIR : MonsterType.GRD);
            try {
            	CardUtil.draw(attackerArea.getDeck().getSkillCards(), attackerArea.getHand());
            } catch (NoSuchElementException nsee) { }
        }
        else if (SkillKeyword.TWINKLING == keyword) {
			while (!attackerArea.getDeck().getSkillCards().isEmpty()) {
				CardUtil.discardFromDeck(attackerArea.getDeck().getSkillCards(), attackerArea.getDiscards());
				attackerArea.getBreeder().setGuts(attackerArea.getBreeder().getGuts() + 1);
			}
        }
        else if (SkillKeyword.WILD_RUSH == keyword) {
        	attackerArea.setAttacksThisTurn(-1);
			for (int i = 0; i < 5; i++) {
				try {
					CardUtil.discardFromDeck(attackerArea.getDeck().getSkillCards(), attackerArea.getDiscards());
					attackerArea.getBreeder().setGuts(attackerArea.getBreeder().getGuts() + 1);
				} catch (NoSuchElementException nsee) { }
			}
        }
        else if (SkillKeyword.YODEL == keyword) {
        	attackerArea.getBreeder().addStatusDuration(MonsterStatus.EXTRA_TURN, 1);
        }

	}
	
}
