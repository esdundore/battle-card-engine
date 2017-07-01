package card.dao;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

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
			ClassPathResource skillPathResource = new ClassPathResource("card/cards/skillcards.txt");
	        InputStream skillStream = skillPathResource.getInputStream();
			bufferedSkillReader = new BufferedReader(new InputStreamReader(skillStream));
			while ((currentLine = bufferedSkillReader.readLine()) != null) {
				String[] split = currentLine.split(",");
				int i = 0;
				String monsterName = split[i++];
				String cardName = split[i++];
				String cardType = split[i++];
				int gutsCost = Integer.parseInt(split[i++]);
				int damage = Integer.parseInt(split[i++]);
				String targeting = split[i++];
				String keyword = split[i++];
				int keyValue = Integer.parseInt(split[i++]);
					
				SkillCard skillCard = new SkillCard();
				skillCard.setId(monsterName + "_" + cardName);
				skillCard.setUserId(monsterName);
				skillCard.setType(cardType);
				skillCard.setGutsCost(gutsCost);
				skillCard.setDamage(damage);
				skillCard.setTargeting(targeting);
				skillCard.setKeyword(keyword);
				skillCard.setKeyValue(keyValue);
			
				battleCards.put(skillCard.getId(), skillCard);
			}
		
			// load monster cards to cache
			ClassPathResource monsterPathResource = new ClassPathResource("card/cards/monstercards.txt");
	        InputStream monsterStream = monsterPathResource.getInputStream();
			bufferedMonsterReader = new BufferedReader(new InputStreamReader(monsterStream));
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
