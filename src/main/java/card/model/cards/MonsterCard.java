package card.model.cards;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MonsterCard extends BattleCard {

	protected String type;
	protected int maxLife;
	protected String mainLineage;
	protected String subLineage;
	
	public String getType() {
		return type;
	}
	@XmlElement
	public void setType(String type) {
		this.type = type;
	}
	public int getMaxLife() {
		return maxLife;
	}
	@XmlElement
	public void setMaxLife(int maxLife) {
		this.maxLife = maxLife;
	}
	public String getMainLineage() {
		return mainLineage;
	}
	@XmlElement
	public void setMainLineage(String mainLineage) {
		this.mainLineage = mainLineage;
	}
	public String getSubLineage() {
		return subLineage;
	}
	@XmlElement
	public void setSubLineage(String subLineage) {
		this.subLineage = subLineage;
	}
	
}
