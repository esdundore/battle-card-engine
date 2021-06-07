package card.model.view;

import card.model.cards.Deck;

public class DeckView {

	public Integer deckSize;
	public String deckName;
	public String deckAvatar;
	
	public DeckView() {}
	public DeckView(Deck deck) {
		deckSize = deck.getSkillCards().size();
		deckName = deck.getDeckName();
		deckAvatar = deck.getDeckAvatar();
	}
	
	public Integer getDeckSize() {
		return deckSize;
	}
	public void setDeckSize(Integer deckSize) {
		this.deckSize = deckSize;
	}
	public String getDeckName() {
		return deckName;
	}
	public void setDeckName(String deckName) {
		this.deckName = deckName;
	}
	public String getDeckAvatar() {
		return deckAvatar;
	}
	public void setDeckAvatar(String deckAvatar) {
		this.deckAvatar = deckAvatar;
	}
	
	
}
