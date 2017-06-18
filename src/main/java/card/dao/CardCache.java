package card.dao;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

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
		JAXBContext battleCardContext = JAXBContext.newInstance(BattleCard.class);
		JAXBContext skillCardContext = JAXBContext.newInstance(SkillCard.class);
		JAXBContext monsterCardContext = JAXBContext.newInstance(MonsterCard.class);
		
		File[] directories = ResourceUtils.getFile("classpath:card/cards").listFiles(File::isDirectory);
		ArrayList<File> files = new ArrayList<File>();
		for (File directory : directories) {
			files.addAll(new ArrayList<File>(Arrays.asList(directory.listFiles())));
		}
		for (File file : files) {
			try {
				Unmarshaller jaxbUnmarshaller = skillCardContext.createUnmarshaller();
				SkillCard skillCard = (SkillCard) jaxbUnmarshaller.unmarshal(file);
				battleCards.put(skillCard.getId(), skillCard);
			} catch (Exception e1) {
				try {
					Unmarshaller jaxbUnmarshaller = monsterCardContext.createUnmarshaller();
					MonsterCard monsterCard = (MonsterCard) jaxbUnmarshaller.unmarshal(file);
					battleCards.put(monsterCard.getId(), monsterCard);
				} catch (Exception e2) {
					Unmarshaller jaxbUnmarshaller = battleCardContext.createUnmarshaller();
					BattleCard battleCard = (BattleCard) jaxbUnmarshaller.unmarshal(file);
					battleCards.put(battleCard.getId(), battleCard);
				}
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
