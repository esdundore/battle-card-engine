package card.model.view;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.stream.Collectors;

import card.dao.CardCache;
import card.enums.MonsterBreed;
import card.model.cards.SkillCard;
import card.model.game.Monster;
import card.model.game.PlayerArea;

public class PlayerAreaView {
	
	public DeckView deck;
	public ArrayList<SkillCardView> hand;
	public LinkedList<String> discards;
	public ArrayList<MonsterView> monsters = new ArrayList<>();
	public BreederView breeder = new BreederView();
	
	public PlayerAreaView() { }
	public PlayerAreaView(PlayerArea playerArea, boolean showCards, String playerName) {
		deck = new DeckView(playerArea.getDeck());
		hand = new ArrayList<SkillCardView>();
		for (SkillCard card : playerArea.getHand()) {
			if (null == card) {
				hand.add(null);
			}
			else if (showCards) {
				hand.add(new SkillCardView(card));
			}
			else {
				hand.add(new SkillCardView(MonsterBreed.Breeder + CardCache.DEL + "Hidden", card.getId(),  1, 1));
			}
		}
		for (Monster monster : playerArea.getMonsters()) {
			 monsters.add(new MonsterView(monster));
		}
		discards = playerArea.getDiscards().stream()
				.map(SkillCard::getName)
				.collect(Collectors.toCollection(LinkedList::new));
		breeder = new BreederView(playerArea.getBreeder());
	}
	
	public ArrayList<SkillCardView> getHand() {
		return hand;
	}
	public void setHand(ArrayList<SkillCardView> hand) {
		this.hand = hand;
	}
	public LinkedList<String> getDiscards() {
		return discards;
	}
	public void setDiscards(LinkedList<String> discards) {
		this.discards = discards;
	}
	public ArrayList<MonsterView> getMonsters() {
		return monsters;
	}
	public void setMonsters(ArrayList<MonsterView> monsters) {
		this.monsters = monsters;
	}
	
}
