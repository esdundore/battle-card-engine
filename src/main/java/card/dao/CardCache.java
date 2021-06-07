package card.dao;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import card.enums.MonsterBreed;
import card.enums.MonsterType;
import card.enums.SkillKeyword;
import card.enums.SkillType;
import card.enums.TargetArea;
import card.model.cards.MonsterCard;
import card.model.cards.SkillCard;

@Component
public class CardCache {
	
	private static final String MONSTER_CARDS_PATH = "card/cards/monstercards.txt";
	private static final String SKILL_CARDS_PATH = "card/cards/skillcards.txt";
	
	public static final String DEL = "/";
	public static final String DRAGON_BITE = MonsterBreed.Dragon.name()+DEL+"Bite";
	public static final String DRAGON_TAIL_ATTACK = MonsterBreed.Dragon.name()+DEL+"Tail Attack";
	
	private Map<String,MonsterCard> monsterCards = new HashMap<String,MonsterCard>();
	private Map<String,SkillCard> skillCards = new HashMap<String,SkillCard>();

	/** Loads monster and skill cards into cache at runtime **/
	CardCache() throws Exception {
		BufferedReader bufferedSkillReader = null;
		BufferedReader bufferedMonsterReader = null;
		try {
			
			// Load monster cards into cache
			String currentMonsterLine;
			ClassPathResource monsterPathResource = new ClassPathResource(MONSTER_CARDS_PATH);
	        InputStream monsterStream = monsterPathResource.getInputStream();
			bufferedMonsterReader = new BufferedReader(new InputStreamReader(monsterStream));
			while ((currentMonsterLine = bufferedMonsterReader.readLine()) != null) {
				String[] split = currentMonsterLine.split(",");
				int i = 0;
				String monsterName = split[i++];
				String mainLineage = split[i++];
				String subLineage = split[i++];
				String monsterType = split[i++];
				Integer maxLife = Integer.parseInt(split[i++]);
			
				MonsterCard monsterCard = new MonsterCard();
				monsterCard.setName(monsterName);
				monsterCard.setMainLineage(MonsterBreed.valueOf(mainLineage));
				monsterCard.setSubLineage(MonsterBreed.valueOf(subLineage));
				monsterCard.setMonsterType(MonsterType.valueOf(monsterType));
				monsterCard.setMaxLife(maxLife);
				
				monsterCards.put(monsterCard.getName(), monsterCard);
			}
			
			// Load skill cards into cache
			String currentSkillLine;
			ClassPathResource skillPathResource = new ClassPathResource(SKILL_CARDS_PATH);
	        InputStream skillStream = skillPathResource.getInputStream();
			bufferedSkillReader = new BufferedReader(new InputStreamReader(skillStream));
			while ((currentSkillLine = bufferedSkillReader.readLine()) != null) {
				String[] split = currentSkillLine.split(",");
				int i = 0;
				String monsterName = split[i++];
				String cardName = split[i++];
				String skillType = split[i++];
				Integer gutsCost = Integer.parseInt(split[i++]);
				Integer damage = Integer.parseInt(split[i++]);
				String targetArea = split[i++];
				String skillKeyword = split[i++];
				Integer keyValue = Integer.parseInt(split[i++]);
					
				SkillCard skillCard = new SkillCard();
				skillCard.setName(monsterName + DEL + cardName);
				skillCard.setUserBreed(MonsterBreed.valueOf(monsterName));
				skillCard.setSkillType(SkillType.valueOf(skillType));
				skillCard.setGutsCost(gutsCost);
				skillCard.setBaseGutsCost(gutsCost);
				skillCard.setDamage(damage);
				skillCard.setBaseDamage(damage);
				skillCard.setTargetArea(TargetArea.valueOf(targetArea));
				skillCard.setSkillKeyword(SkillKeyword.valueOf(skillKeyword));
				skillCard.setKeywordValue(keyValue);
			
				skillCards.put(skillCard.getName(), skillCard);
			}

		} finally {
			if (bufferedSkillReader != null) bufferedSkillReader.close();
			if (bufferedMonsterReader != null) bufferedMonsterReader.close();
		}
		
	}
	
	public MonsterCard getMonsterCard(String name) {
		return monsterCards.get(name);
	}
	public Map<String,MonsterCard> getMonsterCards() {
		return monsterCards;
	}
	public SkillCard getSkillCard(String name) {
		return skillCards.get(name);
	}
	public Map<String,SkillCard> getSkillCards() {
		return skillCards;
	}
	
}