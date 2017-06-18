package card.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import card.model.cards.BattleCard;
import card.model.cards.MonsterCard;
import card.model.cards.SkillCard;

/**
* Class run on engine startup to load all battle cards into a cache
 */
@Component("cardCache")
public class CardCache {
	
	private Map<String,BattleCard> battleCards = new HashMap<String,BattleCard>();
	
	CardCache() throws Exception {
		String currentLine;
		BufferedReader bufferedSkillReader = null;
		BufferedReader bufferedMonsterReader = null;
		try {
			// load skill cards to cache
			File skillFile = ResourceUtils.getFile("classpath:card/cards/skillcards.txt");
			bufferedSkillReader = new BufferedReader(new FileReader(skillFile));
			while ((currentLine = bufferedSkillReader.readLine()) != null) {
				String[] split = currentLine.split(",");
				String monsterName = split[0];
				String cardName = split[1];
				String cardType = split[2];
				int gutsCost = Integer.parseInt(split[3]);
				int damage = Integer.parseInt(split[4]);
			
				SkillCard skillCard = new SkillCard();
				skillCard.setId(monsterName + "_" + cardName);
				skillCard.setUserId(monsterName);
				skillCard.setType(cardType);
				skillCard.setGutsCost(gutsCost);
				skillCard.setDamage(damage);
			
				battleCards.put(skillCard.getId(), skillCard);
			}
		
			// load monster cards to cache
			File monsterFile = ResourceUtils.getFile("classpath:card/cards/monstercards.txt");
			bufferedMonsterReader = new BufferedReader(new FileReader(monsterFile));
			while ((currentLine = bufferedMonsterReader.readLine()) != null) {
				String[] split = currentLine.split(",");
				String monsterName = split[0];
				String mainLineage = split[1];
				String subLineage = split[2];
				String monsterType = split[3];
				int maxLife = Integer.parseInt(split[4]);
			
				MonsterCard monsterCard = new MonsterCard();
				monsterCard.setId(monsterName);
				monsterCard.setMainLineage(mainLineage);
				monsterCard.setSubLineage(subLineage);
				monsterCard.setType(monsterType);
				monsterCard.setMaxLife(maxLife);
				
				battleCards.put(monsterCard.getId(), monsterCard);
			}
		} finally {
			if (bufferedSkillReader != null) {
				bufferedSkillReader.close();
			}
			if (bufferedMonsterReader != null) {
				bufferedMonsterReader.close();
			}
		}
		
	}
	
	public BattleCard getCard(String id) {
		return battleCards.get(id);
	}
	
	public Map<String,BattleCard> getCards() {
		return battleCards;
	}
	
}
